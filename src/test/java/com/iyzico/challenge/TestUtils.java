package com.iyzico.challenge;

import com.iyzico.challenge.entity.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TestUtils {

	public static Product createProduct() {
		return Product.builder()
				.id(1L)
				.name("TV")
				.description("Smart TV")
				.price(new BigDecimal(17599))
				.stock(123).build();
	}

	public static List<Product> createProductList() {
		Product p1 = Product.builder()
				.id(1L)
				.name("TV")
				.description("Smart TV")
				.price(new BigDecimal(17599))
				.stock(123).build();
		Product p2 = Product.builder()
				.id(2L)
				.name("Laptop")
				.description("Gaming")
				.price(new BigDecimal(24000))
				.stock(500).build();
		return Arrays.asList(p1, p2);
	}

}
