package org.adaptiveplatform.surveys;

import org.adaptiveplatform.surveys.application.UserFacade;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;


public class ValidationTest extends ContainerEnabledTest {

    @Autowired
    private UserFacade userFacade;

    @Test(expected = ConstraintViolationException.class)
    public void shouldValidationFail() throws Exception {
        userFacade.registerUser(null);
    }
}
