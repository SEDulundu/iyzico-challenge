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

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAsync
public class ProductServiceIntTest {

	@Autowired
	private ProductService productService;

	@Test
	public void createProduct() {
		Product product = TestUtils.createProduct();
		Product product2 = productService.createProduct(product);

		Assert.assertNotNull(product2);
		Assert.assertTrue(product2.getId() != 0);
		Assert.assertEquals(product.getName(), product2.getName());
		Assert.assertEquals(product.getStock(), product2.getStock());
	}

	@Test
	public void updateProduct() throws GeneralException {
		Product product = TestUtils.createProduct();
		product.setId(null);

		Product product2 = productService.createProduct(product);
		product2.setName("Updated name");
		product2.setDescription("updated desc");
		product2.setPrice(new BigDecimal(12300));
		product2.setStock(1907);

		Product updatedProduct = productService.updateProduct(product2.getId(), product2);
		Assert.assertNotNull(updatedProduct);
		Assert.assertEquals(product2.getId(), updatedProduct.getId());
		Assert.assertEquals(product2.getName(), updatedProduct.getName());
		Assert.assertEquals(product2.getStock(), updatedProduct.getStock());
	}

	@Test
	public void deleteProduct() throws GeneralException {
		Product product = TestUtils.createProduct();
		product.setId(null);
		Product product2 = productService.createProduct(product);

		productService.deleteProduct(product2.getId());

		List<Product> products = productService.listAllProducts();
		Assert.assertEquals(products.size(), 0);
	}

	@Test
	public void findProduct() throws GeneralException {
		Product product = TestUtils.createProduct();
		product.setId(null);
		productService.createProduct(product);

		Product existProduct = productService.findProductById(product.getId());
		Assert.assertNotNull(existProduct);
		Assert.assertEquals(product.getId(), existProduct.getId());
		Assert.assertEquals(product.getName(), existProduct.getName());
		Assert.assertEquals(product.getStock(), existProduct.getStock());
	}

	@After
	public void clean() throws GeneralException {
		List<Product> productList = productService.listAllProducts();
		for (Product product : productList) {
			productService.deleteProduct(product.getId());
		}
	}

}
