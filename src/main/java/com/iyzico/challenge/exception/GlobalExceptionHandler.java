package com.iyzico.challenge.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public final ResponseEntity<Exception> handleException(Exception e) {
		log.error("ErrorMessage: " + e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
	}

	@ExceptionHandler(value = GeneralException.class)
	public final ResponseEntity<IyzicoErrorMessage> handleGeneralException(GeneralException g) {
		log.error("ErrorMessage: " + g.getIyzicoErrorCode().getErrorMessage());
		IyzicoErrorMessage message = IyzicoErrorMessage.builder().errorCode(g.getIyzicoErrorCode().name())
				.errorMessage(g.getIyzicoErrorCode().getErrorMessage()).build();
		return ResponseEntity.status(g.getIyzicoErrorCode().getHttpStatus()).body(message);
	}
}
