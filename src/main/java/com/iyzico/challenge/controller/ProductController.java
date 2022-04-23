package com.iyzico.challenge.controller;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.GeneralException;
import com.iyzico.challenge.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {
	@Autowired
	private final ProductService productService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Product> getAllProducts() {
		return productService.listAllProducts();
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Product getProductById(@PathVariable(value = "id") Long productId) throws Exception {
		return productService.findProductById(productId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Product createProduct(@Valid @RequestBody Product product) {
		return productService.createProduct(product);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Product updateProduct(@PathVariable(name = "id") Long productId, @Valid @RequestBody Product product) throws GeneralException {
		return productService.updateProduct(productId, product);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteProduct(@PathVariable(name = "id") Long productId) throws GeneralException {
		productService.deleteProduct(productId);
		return ResponseEntity.ok().build();
	}
}