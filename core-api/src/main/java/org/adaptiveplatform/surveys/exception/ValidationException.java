package org.adaptiveplatform.surveys.exception;

public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public static final String VALIDATION_EXCEPTION_CODE = "ValidationException";

	private String message;

	public ValidationException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String getCode() {
		return VALIDATION_EXCEPTION_CODE;

	}
}
