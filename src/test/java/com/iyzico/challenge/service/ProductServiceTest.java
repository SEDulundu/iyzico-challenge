package com.iyzico.challenge.service;

import com.iyzico.challenge.TestUtils;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.GeneralException;
import com.iyzico.challenge.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Before
	public void setUp() {
		productService = new ProductServiceImpl(productRepository);
	}

	@Test
	public void whenCreateProduct_shouldReturnProduct() {
		when(productRepository.save(any(Product.class))).thenReturn(TestUtils.createProduct());
		Product prd = Product.builder().name("TV")
				.description("Smart TV")
				.price(new BigDecimal(17599))
				.stock(123).build();
		Product newProduct = productService.createProduct(prd);
		Assert.assertNotNull(newProduct);
		verify(productRepository, times(1)).save(any(Product.class));
	}

	@Test
	public void whenUpdateProduct_shouldReturnUpdatedProduct() throws GeneralException {
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(TestUtils.createProduct()));
		Product updatedProduct = TestUtils.createProduct();
		updatedProduct.setStock(17555);
		when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
		Product prd = TestUtils.createProduct();
		prd.setId(null);
		prd.setStock(17555);
		Product newProduct = productService.updateProduct(1L, prd);
		Assert.assertNotNull(newProduct);
		Assert.assertEquals(1,1);
		verify(productRepository, times(1)).findById(anyLong());
		verify(productRepository, times(1)).save(any(Product.class));
	}

	@Test
	public void whenDeleteProduct_shoudBeSuccess() throws GeneralException {
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(TestUtils.createProduct()));
		doNothing().when(productRepository).delete(any(Product.class));
		productService.deleteProduct(1L);
		verify(productRepository, times(1)).findById(anyLong());
		verify(productRepository, times(1)).delete(any(Product.class));
	}

	@Test
	public void whenFindProductById_shouldReturnProduct() throws GeneralException {
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(TestUtils.createProduct()));
		Product product = productService.findProductById(1L);
		Assert.assertNotNull(product);
		verify(productRepository, times(1)).findById(anyLong());
	}

	@Test
	public void whenListAllProducts_shouldReturnProducts() {
		when(productRepository.findAll()).thenReturn(TestUtils.createProductList());
		List<Product> productList = productService.listAllProducts();
		Assert.assertNotNull(productList);
		verify(productRepository, times(1)).findAll();
	}

	@Test
	public void whenUpdateStock_shouldReturnProduct() throws GeneralException {
		Product product = TestUtils.createProduct();
		product.setStock(2);
		Product updatedProduct = TestUtils.createProduct();
		updatedProduct.setStock(1);
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
		when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
		Product product1 = productService.updateProductStock(1L, 1);
		Assert.assertNotNull(product1);
		Assert.assertEquals(1, product1.getStock());
		verify(productRepository, times(1)).findById(anyLong());
		verify(productRepository, times(1)).save(any(Product.class));
	}
}