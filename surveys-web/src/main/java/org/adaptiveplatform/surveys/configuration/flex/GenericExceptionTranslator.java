package org.adaptiveplatform.surveys.configuration.flex;

import org.springframework.flex.core.ExceptionTranslator;

import flex.messaging.MessageException;

public class GenericExceptionTranslator implements ExceptionTranslator {

	private final String errorCode;

	public GenericExceptionTranslator(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public boolean handles(Class<?> clazz) {
		return true;
	}

	@Override
	public MessageException translate(Throwable t) {
		MessageException message = new MessageException();
		message.setCode(errorCode);
		message.setMessage(t.getMessage());
		return message;

	}
}
