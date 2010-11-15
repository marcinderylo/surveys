package org.adaptiveplatform.surveys;

import javax.validation.ConstraintViolationException;

import org.adaptiveplatform.surveys.application.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "classpath:/testConfigurationContext.xml")
public class ValidationTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private UserFacade userFacade;

	@Test(expectedExceptions = ConstraintViolationException.class)
	public void shouldValidationFail() throws Exception {
		userFacade.registerUser(null);
	}
}
