package com.iyzico.challenge.exception;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IyzicoErrorMessage {
	private String errorCode;
	private String errorMessage;
}
