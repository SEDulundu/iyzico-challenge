package com.iyzico.challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum IyzicoErrorCode {

	PRODUCT_NOT_FOUND_ERROR(10000, "Product not found", HttpStatus.NOT_FOUND),
	INVALID_STOCK_NUMBER_ERROR(10001, "Invalid input stock number", HttpStatus.BAD_REQUEST),
	NOT_AVAILABLE_STOCK_ERROR(10002, "Not available stock", HttpStatus.BAD_REQUEST),
	INTERNAL_SERVER_ERROR(10003, "Iyzico internal server eror", HttpStatus.INTERNAL_SERVER_ERROR),
	PURCHASE_ERROR(10004, "Purchase error when do payment", HttpStatus.INTERNAL_SERVER_ERROR),
	CONCURRENT_TRANSACTION_ERROR(10005, "Concurrent Transaction Error Optimistic Lock", HttpStatus.INTERNAL_SERVER_ERROR);

	private int errorCode;
	private String errorMessage;
	private HttpStatus httpStatus;

}