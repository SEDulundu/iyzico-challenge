package com.iyzico.challenge.service;

import com.iyzico.challenge.TestUtils;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.GeneralException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAsync
public class PaymentServiceIntTest {
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private ProductService productService;

	@Test
	public void purchase() throws InterruptedException, GeneralException {
		Product product = TestUtils.createProduct();
		product.setId(null);
		product.setStock(1);
		Product createdProduct = productService.createProduct(product);

		final ExecutorService executor = Executors.newFixedThreadPool(2);

		for (int i = 0; i < 2; ++i) {
			executor.execute(() -> {
				try {
					paymentService.purchase(createdProduct.getId(), 1);
				} catch (GeneralException generalException) {
					System.err.println(generalException.getIyzicoErrorCode());
				}
			});
		}

		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);

		Product product1 = productService.findProductById(createdProduct.getId());
		Assert.assertNotNull(product1);
		Assert.assertEquals(0, product1.getStock());
	}

	@After
	public void clean() throws GeneralException {
		List<Product> productList = productService.listAllProducts();
		for (Product product : productList) {
			productService.deleteProduct(product.getId());
		}
	}
}
