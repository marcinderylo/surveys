package org.adaptiveplatform.surveys.application;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.dto.ChangePasswordCommand;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.security.CantChangePasswordException;
import org.adaptiveplatform.surveys.service.UserAccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;

public class ChangingPasswordTest {

    public static final String EMAIL = "foo@bar.com";
    public static final String ANOTHER_EMAIL = "baz@bar.com";
    public static final String OLD_PASSWORD = "foo";
    public static final String NOT_THE_OLD_PASSWORD = "foobar";
    @InjectMocks
    private UserFacade facade;
    @Mock
    private AuthenticationService authentication;
    @Mock
    private UserAccountRepository repository;
    @Mock
    private UserAccount user;
    @Mock
    private PasswordEncoder encoder;

    @Before
    public void setUp() {
        facade = new UserFacadeImpl();
        MockitoAnnotations.initMocks(this);
        when(repository.getExisting(anyString())).thenAnswer(returingWantedUser());
        doAnswer(throwBadCredentialsIfOldPasswordDoesntMatch()).when(authentication).checkCredentials(anyString(),
                anyString());
        when(encoder.encodePassword(anyString(), anyObject())).thenAnswer(samePassword());
    }

    @Test
    public void shouldAllowChangingUsersOwnPassword() throws Exception {
        // given
        authenticateWith(EMAIL);
        // when
        facade.changePassword(cmd(EMAIL, OLD_PASSWORD, "bar"));
        // then
        verify(user).setPassword("bar");
    }

    @Test
    public void shouldNotAllowRegularUserToChangeAnotherUsersPassword() throws Exception {
        // given
        authenticateWith(EMAIL);
        // when
        try {
            facade.changePassword(cmd(ANOTHER_EMAIL, OLD_PASSWORD, "bar"));
            // expect exception
            fail("Exception should have been thrown");
        } catch (CantChangePasswordException e) {
            // just as expected
        }
    }

    @Test
    public void shouldRequireValidOldPasswordWhenChangingOwnPassword() throws Exception {
        // given
        authenticateWith(EMAIL);
        // when
        try {
            facade.changePassword(cmd(EMAIL, NOT_THE_OLD_PASSWORD, "bar"));
            // expect exception
            fail("Exception should have been thrown");
        } catch (AuthenticationException e) {
            // just as expected
        }
    }

    @Test
    public void shouldAllowAdminChangingAnotherUserPasswordWithoutOldPassword() throws Exception {
        // given
        authenticateWith(EMAIL, Role.ADMINISTRATOR);
        // when
        facade.changePassword(cmd(ANOTHER_EMAIL, null, "bar"));
        // then
        verify(user).setPassword("bar");
    }

    private void authenticateWith(String email, String... roles) {
        when(authentication.isAuthenticated()).thenReturn(false);
        final UserDto authenticatedUser = new UserDto();
        authenticatedUser.setEmail(email);
        authenticatedUser.setId(Long.valueOf(email.hashCode()));
        authenticatedUser.getRoles().add(Role.USER);
        authenticatedUser.getRoles().addAll(Arrays.asList(roles));
        when(authentication.getCurrentUser()).thenReturn(authenticatedUser);
    }

    private ChangePasswordCommand cmd(String email, String oldPassword, String newPassword) {
        return new ChangePasswordCommand(email, oldPassword, newPassword);
    }

    private Answer<UserAccount> returingWantedUser() {
        return new Answer<UserAccount>() {

            @Override
            public UserAccount answer(InvocationOnMock invocation) throws Throwable {
                String email = (String) invocation.getArguments()[0];
                when(user.getEmail()).thenReturn(email);
                return user;
            }
        };
    }

    private Answer<Void> throwBadCredentialsIfOldPasswordDoesntMatch() {
        return new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String providedPassword = (String) invocation.getArguments()[1];
                if (!OLD_PASSWORD.equals(providedPassword)) {
                    throw new BadCredentialsException("incorrect password");
                }
                return null;
            }
        };
    }

    private Answer<String> samePassword() {
        return new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return (String) invocation.getArguments()[0];
            }
        };
    }
}
