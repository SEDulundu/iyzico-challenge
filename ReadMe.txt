Merhabalar,

Test coverage ve test sonuçları yine ektedir.
test_coverage.png
test_result.png

----------------------------------------------------------------

hsqldb-2.4.0 kullanıldı.

ilk gelen dosyada datasourceUrl : jdbc:hsqldb:hsql:/localhost:9001/db bu şekildeydi.
şu şekilde düzenleyerek çalıştım: jdbc:hsqldb:hsql://localhost:9001/db
aynı şekilde test db sindede / ekledim. Aksi taktirde hata alıyordu.

serverları bu sekilde çalıştırdım.
db
PS C:\Users\Emre\Desktop\hsqldb-2.4.0\hsqldb\data> java -classpath ../lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:hsqldb/db --dbname.0 db

testdb
PS C:\Users\Emre\Desktop\hsqldb-2.4.0\hsqldb\data> java -classpath ../lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:hsqldb/db/test --dbname.0 test

username: SA
password:

olacak şekilde çalıştım. size gönderirken eski haline getirip gönderdim username ve passwordu.

Ayrıca create-db.sql içerisinede product için create scripti ekledim.

----------------------------------------------------------------

Optimistic lock ile çözümde test çalıştırıldığında

2022-04-23 14:16:10.655  WARN 10512 --- [pool-1-thread-1] c.i.c.service.PaymentServiceImpl         : Somebody has already updated the stock for product: 1 in concurrent transaction. Will try again...
CONCURRENT_TRANSACTION_ERROR

şeklinde log basılmakta.

-----------------------------------------------------------------

Burdaki kod block 1.sorudaki tek instance çalışan bir uygulama için java tarafındaki çözümüdür.
multiple instance ta bu çözüm yeterli olmaz.

/*
public class ProductLockGenerator {
	private static final ConcurrentMap<Long, ReentrantLock> locks = new ConcurrentHashMap<>();

	public static ReentrantLock getLocker(Long productId) {
		if (!locks.containsKey(productId)) {
			locks.put(productId, new ReentrantLock());
		}
		return locks.get(productId);
	}
}

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private final ProductService productService;
	@Autowired
	private final PaymentServiceClients paymentServiceClients;

    @Override
    public CompletableFuture<String> purchase(Long productId, int quantity) throws GeneralException {
        if (quantity < 1) {
            throw new GeneralException(IyzicoErrorCode.INVALID_STOCK_NUMBER_ERROR);
        }
        Product product = productService.findProductById(productId);

        updateStock(product, quantity);
        BigDecimal price = product.getPrice().multiply(new BigDecimal(quantity));
        try {
            log.info("Payment Call executed");
            return paymentServiceClients.call(price);
        } catch (Exception e) {
            log.error("Purchase error product: {}, price: {} error : {}", product, price, e.getMessage());
            updateStock(product, product.getStock());
            throw new GeneralException(IyzicoErrorCode.PURCHASE_ERROR);
        }
    }

    private void updateStock(Product product, int quantity) throws GeneralException {
        ReentrantLock locker = ProductLockGenerator.getLocker(product.getId());
        locker.lock();
        try {
            if (quantity > 0 && product.getStock() < quantity) {
                throw new GeneralException(IyzicoErrorCode.NOT_AVAILABLE_STOCK_ERROR);
            }

            int oldStock = product.getStock();
            int newStock = oldStock - quantity;
            product.setStock(newStock);
            productService.updateProduct(product.getId(), product);

            log.info("Updated Product Stock ProductID: {}, OldStock: {} NewStock: {}",
                    product.getId(), oldStock, newStock);
        } finally {
            locker.unlock();
        }
    }
}
*/