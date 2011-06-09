package org.adaptiveplatform.surveys.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.security.NotAllowedToRegisterUserException;
import org.adaptiveplatform.surveys.service.UserAccountFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserRegistrationTest {

    public static final Long CREATED_USER_ID = 1L;
    public static final String MAIL = "foo@bar.com";
    public static final String NAME = "Foo";
    public static final String PASSWORD = "Bar";
    @InjectMocks
    private UserFacade facade;
    @Mock
    private AuthenticationService authentication;
    @Mock
    private UserAccountFactory factory;
    @Mock
    private UserAccount createdAccount;

    @Before
    public void setUp() {
        facade = new UserFacadeImpl();
        MockitoAnnotations.initMocks(this);
        when(createdAccount.getId()).thenReturn(CREATED_USER_ID);
        when(factory.registerNewAccount(NAME, PASSWORD, MAIL)).thenReturn(createdAccount);
    }

    @Test
    public void shouldAllowRegistrationByAnonymousUser() throws Exception {
        // given
        when(authentication.isAuthenticated()).thenReturn(false);
        // when
        final Long id = facade.registerUser(new RegisterAccountCommand(NAME, PASSWORD, MAIL));
        // then
        assertEquals(CREATED_USER_ID, id);
    }

    @Test
    public void shouldAllowRegistrationByAdministrator() throws Exception {
        // given
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getCurrentUser()).thenReturn(userWithAdminRights());
        // when
        final Long id = facade.registerUser(new RegisterAccountCommand(NAME, PASSWORD, MAIL));
        // then
        assertEquals(id, CREATED_USER_ID);
    }

    @Test
    public void shouldNotAllowRegistrationByUserWithoutAdminRights() throws Exception {
        // given
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getCurrentUser()).thenReturn(userWithoutAdminRights());
        // when
        try {
            facade.registerUser(new RegisterAccountCommand(NAME, PASSWORD, MAIL));
            // expect exception
            fail("An exception should have been thrown");
        } catch (NotAllowedToRegisterUserException e) {
            // just as expected;
        }
    }

    private UserDto userWithAdminRights() {
        final UserDto admin = new UserDto();
        admin.getRoles().add(Role.ADMINISTRATOR);
        return admin;
    }

    private UserDto userWithoutAdminRights() {
        return new UserDto();
    }
}
