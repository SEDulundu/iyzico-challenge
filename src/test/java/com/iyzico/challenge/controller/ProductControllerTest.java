package com.iyzico.challenge.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.TestUtils;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.GeneralException;
import com.iyzico.challenge.exception.GlobalExceptionHandler;
import com.iyzico.challenge.exception.IyzicoErrorCode;
import com.iyzico.challenge.service.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {
	private ProductService productService;
	private MockMvc mockMvc;
	private ObjectMapper objectMapper;
	private static String baseUri = "/v1/products";

	@Before
	public void setUp() {
		productService = mock(ProductService.class);
		objectMapper = new ObjectMapper();
		ProductController productController = new ProductController(productService);
		mockMvc = MockMvcBuilders.standaloneSetup(productController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.setControllerAdvice(new GlobalExceptionHandler()).build();
	}

	@Test
	public void whenGetAllProducts_thenReturnAllProducts() throws Exception {
		when(productService.listAllProducts()).thenReturn(TestUtils.createProductList());
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUri)
									.contentType(MediaType.APPLICATION_JSON))
									.andExpect(status().isOk())
									.andReturn();
		List<Product> products = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<List<Product>>() {
				});
		Assert.assertNotNull(products);
		Assert.assertEquals(2, products.size());
		verify(productService, times(1)).listAllProducts();
	}

	@Test
	public void whenGetProductById_thenReturnProduct() throws Exception {
		when(productService.findProductById(anyLong())).thenReturn(TestUtils.createProduct());
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUri + "/{id}", "1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		Product product = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<Product>() {
				});
		Assert.assertNotNull(product);
		verify(productService, times(1)).findProductById(anyLong());
	}

	@Test
	public void whenCreateProduct_thenReturnProduct() throws Exception {
		when(productService.createProduct(any(Product.class))).thenReturn(TestUtils.createProduct());
		Product productCreationRequest = TestUtils.createProduct();
		productCreationRequest.setId(null);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productCreationRequest)))
				.andExpect(status().isCreated())
				.andReturn();
		Product product = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<Product>() {
				});
		Assert.assertNotNull(product);
		Assert.assertEquals(productCreationRequest.getName(), product.getName());
		Assert.assertEquals(productCreationRequest.getStock(), product.getStock());
		Assert.assertEquals(productCreationRequest.getDescription(), product.getDescription());
		Assert.assertEquals(productCreationRequest.getPrice(), product.getPrice());
		verify(productService, times(1)).createProduct(any(Product.class));
	}

	@Test
	public void whenUpdateProduct_thenReturnProduct() throws Exception {
		Product productCreationRequest = Product.builder()
				.name("table")
				.description("desc")
				.price(new BigDecimal(1200))
				.stock(12).build();
		when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(productCreationRequest);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(baseUri + "/{id}", "1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productCreationRequest)))
				.andExpect(status().isOk())
				.andReturn();
		Product product = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<Product>() {
				});
		Assert.assertNotNull(product);
		Assert.assertEquals(productCreationRequest.getName(), product.getName());
		Assert.assertEquals(productCreationRequest.getStock(), product.getStock());
		Assert.assertEquals(productCreationRequest.getDescription(), product.getDescription());
		Assert.assertEquals(productCreationRequest.getPrice(), product.getPrice());
		verify(productService, times(1)).updateProduct(anyLong(), any(Product.class));
	}

	@Test
	public void whenDeleteProduct_thenReturnSucceed() throws Exception {
		doNothing().when(productService).deleteProduct(anyLong());
		mockMvc.perform(MockMvcRequestBuilders.delete(baseUri + "/{id}", "1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		verify(productService, times(1)).deleteProduct(anyLong());
	}

	@Test
	public void whenDoPayment_thenThrowProductNotFound() throws Exception {
		when(productService.findProductById(anyLong())).thenThrow(new GeneralException(IyzicoErrorCode.PRODUCT_NOT_FOUND_ERROR));
		mockMvc.perform(MockMvcRequestBuilders.get(baseUri + "/{id}", "1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(404))
				.andReturn();
	}
}