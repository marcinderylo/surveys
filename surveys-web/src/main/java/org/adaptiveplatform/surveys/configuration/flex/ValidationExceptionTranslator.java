package org.adaptiveplatform.surveys.configuration.flex;

import org.adaptiveplatform.surveys.exception.ValidationException;
import org.springframework.flex.core.ExceptionTranslator;

import flex.messaging.MessageException;

/**
 * Translates java validation exceptions to flex exceptions.
 */
public class ValidationExceptionTranslator implements ExceptionTranslator {

	@Override
	public boolean handles(Class<?> clazz) {
		return ValidationException.class.isAssignableFrom(clazz);
	}

	@Override
	public MessageException translate(Throwable t) {
		ValidationException ve = (ValidationException) t;
		MessageException message = new MessageException();
		message.setCode(ve.getCode());
		message.setMessage(ve.getMessage());
		return message;
	}

}
