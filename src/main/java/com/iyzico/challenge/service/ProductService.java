package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.GeneralException;

import java.util.List;

public interface ProductService {

	Product createProduct(Product product);

	Product updateProduct(Long productId, Product product) throws GeneralException;

	void deleteProduct(Long productId) throws GeneralException;

	Product findProductById(Long productId) throws GeneralException;

	List<Product> listAllProducts();

	Product updateProductStock(Long productId, int stock) throws GeneralException;
}
