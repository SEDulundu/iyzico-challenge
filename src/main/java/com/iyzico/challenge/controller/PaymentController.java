package com.iyzico.challenge.controller;

import com.iyzico.challenge.exception.GeneralException;
import com.iyzico.challenge.service.PaymentRequest;
import com.iyzico.challenge.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
	@Autowired
	private final PaymentService paymentService;

	@PostMapping
	public ResponseEntity doPayment(@Valid @RequestBody PaymentRequest request) throws GeneralException {

		paymentService.purchase(request.getProductId(), request.getQuantity()).join();

		return ResponseEntity.ok().build();
	}
}