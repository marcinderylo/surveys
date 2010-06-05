package org.adaptiveplatform.surveys.configuration.flex;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.adaptiveplatform.surveys.exception.ValidationException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import flex.messaging.MessageException;

public class ValidationExceptionTranslatorTest {
	private ValidationExceptionTranslator translator;

	@BeforeMethod
	public void beforeMethod() throws Exception {
		translator = new ValidationExceptionTranslator();
	}

	@Test
	public void shouldAcceptValidationExceptions() throws Exception {
		assertTrue(translator.handles(ValidationException.class));
	}

	@Test
	public void shouldNotAcceptExceptions() throws Exception {
		assertFalse(translator.handles(Exception.class));
	}

	@Test
	public void shouldAcceptValidationExceptionDerivatives() throws Exception {
		class ValidationExceptionDerivative extends ValidationException {
			private static final long serialVersionUID = 1L;

			public ValidationExceptionDerivative() {
				super("sample message");
			}
		}
		assertTrue(translator.handles(ValidationExceptionDerivative.class));
	}

	@Test
	public void shouldTranslateSimpleValidationException() throws Exception {
		// given
		ValidationException ve = new ValidationException("msg");
		// when
		MessageException translate = translator.translate(ve);
		// then
		assertEquals(translate.getMessage(), "msg");
	}
}
