package com.iyzico.challenge.service;

import com.iyzico.challenge.exception.GeneralException;

import java.util.concurrent.CompletableFuture;

public interface PaymentService {
	CompletableFuture<String> purchase(Long productId, int quantity) throws GeneralException;
}
