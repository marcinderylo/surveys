package org.adaptiveplatform.surveys;

import javax.validation.ConstraintViolationException;

import org.adaptiveplatform.surveys.application.UserFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/testConfigurationContext.xml")
public class ValidationTest {

    @Autowired
    private UserFacade userFacade;

    @Test(expected = ConstraintViolationException.class)
    public void shouldValidationFail() throws Exception {
        userFacade.registerUser(null);
    }
}
