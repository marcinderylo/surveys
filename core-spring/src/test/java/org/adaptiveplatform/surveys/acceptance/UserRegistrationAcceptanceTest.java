package org.adaptiveplatform.surveys.acceptance;

import org.adaptiveplatform.surveys.ContainerEnabledTest;
import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.application.UserDao;
import org.adaptiveplatform.surveys.application.UserFacade;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.security.EmailAddressAlreadyRegisteredException;
import org.adaptiveplatform.surveys.exception.security.NotAllowedToViewUserDetailsException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Rafał Jamróz
 */
public class UserRegistrationAcceptanceTest extends ContainerEnabledTest {

    @Resource
    private UserFacade userFacade;

    @Resource
    private UserDao userDao;

    @Resource
    private AuthenticationService authenticationService;

    @Test
    public void shouldAllowRegisteringAndLoggingIn() throws Exception {
        // when
        userFacade.registerUser(new RegisterAccountCommand("user", "s3cr3t", "bob@adapt.pl"));
        // then
        logIn("bob@adapt.pl", "s3cr3t");
        assertIsAuthenticatedAs("bob@adapt.pl");
    }

    @Test
    public void ensureCannotRegisterTwoAccountsWithTheSameEmail() throws Exception {
        // when
        userFacade.registerUser(new RegisterAccountCommand("user2", "s3cr3t", "alice@adapt.pl"));
        try {
            userFacade.registerUser(new RegisterAccountCommand("user1", "secret", "alice@adapt.pl"));
            fail("exception should have been thrown");
        } catch (EmailAddressAlreadyRegisteredException ex) {
            // then - exception shoud have been thrown
        }
    }

    @Test
    public void shouldUserGetHisOwnDetails() throws Exception {
        // given
        userFacade.registerUser(new RegisterAccountCommand("user2", "s3cr3t", "alice@adapt.pl"));
        // when
        logIn("alice@adapt.pl", "s3cr3t");
        UserDto user = userDao.getByEmail("alice@adapt.pl");
        // then
        assertEquals(user.getName(), "user2");
        assertEquals(user.getEmail(), "alice@adapt.pl");
    }

    @Test(expected = NotAllowedToViewUserDetailsException.class)
    public void ensureUserCannotGetOtherUsersDetails() throws Exception {
        // given
        userFacade.registerUser(new RegisterAccountCommand("alice", "s3cr3t", "alice@adapt.pl"));
        userFacade.registerUser(new RegisterAccountCommand("bob", "s3cr3t", "bob@adapt.pl"));
        // when
        logIn("alice@adapt.pl", "s3cr3t");
        userDao.getByEmail("bob@adapt.pl");
        // then - exception should have been thrown
    }

    private void assertIsAuthenticatedAs(String email) {
        assertEquals(SecurityContextHolder.getContext().getAuthentication().getName(), email);
    }

    private void logIn(String email, String password) {
        authenticationService.login(email, password);
    }

    @Before
    public void notLoggedIn() {
        authenticationService.logout();
    }
}
