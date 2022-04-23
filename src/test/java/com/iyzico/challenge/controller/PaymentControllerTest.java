package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.exception.GeneralException;
import com.iyzico.challenge.exception.GlobalExceptionHandler;
import com.iyzico.challenge.exception.IyzicoErrorCode;
import com.iyzico.challenge.service.PaymentRequest;
import com.iyzico.challenge.service.PaymentService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaymentControllerTest {
	private PaymentService paymentService;
	private MockMvc mockMvc;
	private ObjectMapper objectMapper;
	private static String baseUri = "/v1/payments";

	@Before
	public void setUp() {
		paymentService = mock(PaymentService.class);
		objectMapper = new ObjectMapper();
		PaymentController paymentController = new PaymentController(paymentService);
		mockMvc = MockMvcBuilders.standaloneSetup(paymentController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.setControllerAdvice(new GlobalExceptionHandler()).build();
	}

	@Test
	public void whenDoPayment_thenReturnSucceed() throws Exception {
		when(paymentService.purchase(anyLong(), anyInt())).thenReturn(CompletableFuture.completedFuture("success"));
		PaymentRequest request = new PaymentRequest();
		request.setProductId(1L);
		request.setQuantity(1);
		mockMvc.perform(MockMvcRequestBuilders.post(baseUri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	public void whenDoPayment_thenThrowInvalidStockNumberError() throws Exception {
		when(paymentService.purchase(anyLong(), anyInt()))
				.thenThrow(new GeneralException(IyzicoErrorCode.INVALID_STOCK_NUMBER_ERROR));
		PaymentRequest request = new PaymentRequest();
		request.setProductId(1L);
		request.setQuantity(1);
		mockMvc.perform(MockMvcRequestBuilders.post(baseUri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().is(400))
				.andReturn();
	}

	@Test
	public void whenDoPayment_thenThrowNotAvailableStockError() throws Exception {
		when(paymentService.purchase(anyLong(), anyInt()))
				.thenThrow(new GeneralException(IyzicoErrorCode.NOT_AVAILABLE_STOCK_ERROR));
		PaymentRequest request = new PaymentRequest();
		request.setProductId(1L);
		request.setQuantity(1);
		mockMvc.perform(MockMvcRequestBuilders.post(baseUri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().is(400))
				.andReturn();
	}

	@Test
	public void whenDoPayment_thenThrowPurchaseError() throws Exception {
		when(paymentService.purchase(anyLong(), anyInt()))
				.thenThrow(new GeneralException(IyzicoErrorCode.PURCHASE_ERROR));
		PaymentRequest request = new PaymentRequest();
		request.setProductId(1L);
		request.setQuantity(1);
		mockMvc.perform(MockMvcRequestBuilders.post(baseUri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().is(500))
				.andReturn();
	}
}