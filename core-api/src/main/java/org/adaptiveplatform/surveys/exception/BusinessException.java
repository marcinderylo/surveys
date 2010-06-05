package org.adaptiveplatform.surveys.exception;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String code;
	private final String message;
	private final List<Object> parameters;

	public BusinessException(String code, String messagePattern, Object... parameters) {
		this.code = code;
		MessageFormat formatter = new MessageFormat(messagePattern, Locale.UK);
		this.message = formatter.format(parameters);
		this.parameters = Collections.unmodifiableList(Arrays.asList(parameters));
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public List<Object> getParameters() {
		return parameters;
	}
}
