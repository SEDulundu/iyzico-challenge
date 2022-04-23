package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.GeneralException;
import com.iyzico.challenge.exception.IyzicoErrorCode;
import com.iyzico.challenge.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	private final ProductRepository productRepository;

	@Override
	public Product findProductById(Long productId) throws GeneralException {
		return productRepository.findById(productId).orElseThrow(() -> new GeneralException(IyzicoErrorCode.PRODUCT_NOT_FOUND_ERROR));
	}

	@Override
	public List<Product> listAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public Product createProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Product updateProduct(Long productId, Product product) throws GeneralException {
		Product oldProduct = this.findProductById(productId);
		oldProduct.setName(product.getName());
		oldProduct.setDescription(product.getDescription());
		oldProduct.setPrice(product.getPrice());
		oldProduct.setStock(product.getStock());
		log.info("Update Process Old: {} , New: {}", oldProduct, product);
		return productRepository.save(oldProduct);
	}

	@Override
	public void deleteProduct(Long productId) throws GeneralException {
		Product product = this.findProductById(productId);
		productRepository.delete(product);
	}

	@Override
	public Product updateProductStock(Long productId, int stock) throws GeneralException {
		Product product = this.findProductById(productId);
		product.setStock(stock);
		return productRepository.save(product);
	}
}
