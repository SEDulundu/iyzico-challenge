package com.iyzico.challenge.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest {
	@NotNull
	@Min(1)
	private Long productId;
	@Min(1)
	private int quantity;
}
