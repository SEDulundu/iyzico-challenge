package com.iyzico.challenge.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GeneralException extends Exception {

	private final IyzicoErrorCode iyzicoErrorCode;
}
