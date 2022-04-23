package com.iyzico.challenge.service;

import com.iyzico.challenge.TestUtils;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.GeneralException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {
	private PaymentService paymentService;

	@Mock
	private ProductService productService;
	@Mock
	private PaymentServiceClients paymentServiceClients;

	@Before
	public void setUp() {
		this.paymentService = new PaymentServiceImpl(productService, paymentServiceClients);
	}

	@Test
	public void whenPurchase_shouldReturnAsyncSuccessResult() throws GeneralException {
		Product product = TestUtils.createProduct();
		product.setStock(2);
		Product updatedProduct = TestUtils.createProduct();
		updatedProduct.setStock(1);
		when(productService.findProductById(anyLong())).thenReturn(product);
		when(productService.updateProductStock(anyLong(), anyInt())).thenReturn(updatedProduct);
		when(paymentServiceClients.call(any(BigDecimal.class))).thenReturn(CompletableFuture.completedFuture("success"));
		paymentService.purchase(1L, 1);
		verify(productService, times(1)).findProductById(anyLong());
		verify(productService, times(1)).updateProductStock(anyLong(), anyInt());
		verify(paymentServiceClients, times(1)).call(any(BigDecimal.class));
	}

	@Test(expected = GeneralException.class)
	public void whenPurchase_thenConcurrentTransactionError() throws Exception {
		Product product = TestUtils.createProduct();
		product.setStock(2);
		Product updatedProduct = TestUtils.createProduct();
		updatedProduct.setStock(1);
		when(productService.findProductById(anyLong())).thenReturn(product);
		when(productService.updateProductStock(anyLong(), anyInt())).thenThrow(ObjectOptimisticLockingFailureException.class);
		paymentService.purchase(1L, 1);
	}

	@Test(expected = GeneralException.class)
	public void whenPurchase_thenThrowPurchaseError() throws Exception {
		Product product = TestUtils.createProduct();
		product.setStock(2);
		Product updatedProduct = TestUtils.createProduct();
		updatedProduct.setStock(1);
		when(productService.findProductById(anyLong())).thenReturn(product);
		when(productService.updateProductStock(anyLong(), anyInt())).thenReturn(updatedProduct, product);
		when(paymentServiceClients.call(any(BigDecimal.class))).thenThrow(RuntimeException.class);
		paymentService.purchase(1L, 1);
	}

	@Test(expected = GeneralException.class)
	public void whenPurchase_thenThrowInvalid_Stock_Number_Error() throws Exception {
		paymentService.purchase(1L, -1);
	}

	@Test(expected = GeneralException.class)
	public void whenPurchase_thenThrowNotAvailableStockError() throws Exception {
		Product product = TestUtils.createProduct();
		product.setStock(1);
		when(productService.findProductById(anyLong())).thenReturn(product);
		paymentService.purchase(1L, 2);
	}
}