package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.GeneralException;
import com.iyzico.challenge.exception.IyzicoErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private final ProductService productService;
	@Autowired
	private final PaymentServiceClients paymentServiceClients;

	/**
	 * Bu problemin cozumune iliskin 2 farklı yaklasımım var.
	 * 1. Eger uygulamamiz multiple instance ise burada optimistic lock uygulamak concurrency icin daha dogru olacaktır.
	 *
	 * 2. Eger uygulama tek instance ise java tarafında reentrant lock kullanmak yeterli olacaktır.
	 * aynı anda yalnız tek bir thread resource u kullanabilecektir. Bu cozümüde comment olarak bıraktım.
	 *
	 *
	 * Uguladığım çözüm: optimistic lock (1.madde).
	 * */
	@Override
	public CompletableFuture<String> purchase(Long productId, int quantity) throws GeneralException {
		if (quantity < 1) {
			throw new GeneralException(IyzicoErrorCode.INVALID_STOCK_NUMBER_ERROR);
		}

		Product product = productService.findProductById(productId);
		if (quantity > 0 && product.getStock() < quantity) {
			throw new GeneralException(IyzicoErrorCode.NOT_AVAILABLE_STOCK_ERROR);
		}

		int oldStock = product.getStock();
		int newStock = oldStock - quantity;
		try {
			productService.updateProductStock(product.getId(), newStock);

			log.info("Updated Product Stock ProductID: {}, OldStock: {} NewStock: {}",
					product.getId(), oldStock, newStock);

			BigDecimal price = product.getPrice().multiply(new BigDecimal(quantity));

			log.info("Payment Call executed");
			return paymentServiceClients.call(price);
		} catch (ObjectOptimisticLockingFailureException  o) {
			log.warn("Somebody has already updated the stock for product: {} in concurrent transaction. Will try again...", productId);
			throw new GeneralException(IyzicoErrorCode.CONCURRENT_TRANSACTION_ERROR);
		} catch (Exception e) {
			log.error("Purchase error product: {} error : {}", product, e.getMessage());
			productService.updateProductStock(productId, product.getStock());
			throw new GeneralException(IyzicoErrorCode.PURCHASE_ERROR);
		}
	}
}