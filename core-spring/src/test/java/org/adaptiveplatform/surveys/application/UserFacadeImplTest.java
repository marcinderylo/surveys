package org.adaptiveplatform.surveys.application;

import static org.adaptiveplatform.surveys.domain.UserAccountBuilder.user;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.CoreTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.dto.UserQuery;
import org.adaptiveplatform.surveys.exception.security.CantRevokeOwnAdminRights;
import org.adaptiveplatform.surveys.service.AuthenticationServiceMock;
import org.adaptiveplatform.surveys.exception.security.EmailAddressAlreadyRegisteredException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:/testConfigurationContext.xml")
public class UserFacadeImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private UserFacade userFacade;
    @Resource
    private UserDao userDao;
    // TODO move to CoreTestFixtureBuilder
    @Resource
    private AuthenticationServiceMock authentication;
    @Resource
    private CoreTestFixtureBuilder fixture;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        fixture.createUser(user().withEmail("john@doe.com"));
        fixture.createUser(user().withEmail("anonym@anonym.com"));
        fixture.createUser(user().withEmail("keyser@soze.com"));
        UserAccount user = fixture.createUser(user().withEmail("bob@builder.com").inRoles(
                Role.ADMINISTRATOR));
        authentication.authenticate(user.getId(), "bob@builder.com", Role.ADMINISTRATOR);
    }

    @AfterMethod
    public void afterMethod() throws Exception {
        authentication.logout();
        fixture.cleanUp();
    }

    private UserQuery queryByName(String name) {
        UserQuery userQuery = new UserQuery();
        userQuery.setNameContains(name);
        return userQuery;
    }

    @Test
    public void shouldLoadAccounts() throws Exception {
        List<UserDto> result = userDao.query(queryByName("anon"));
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getEmail(), "anonym@anonym.com");
    }

    @Test(expectedExceptions = {EmailAddressAlreadyRegisteredException.class})
    public void shouldNotAllowDuplicateEmails() throws Exception {
        // given
        final String existingEmail = "john@doe.com";
        RegisterAccountCommand command = new RegisterAccountCommand("pat", "the postman",
                existingEmail);
        // when
        userFacade.registerUser(command);
        // then
        fail("Exception should have been thrown");
    }

    @Test
    public void shouldRegisterAccount() throws Exception {
        RegisterAccountCommand command = new RegisterAccountCommand("homer", "simpson", "h@s.com");

        Long id = userFacade.registerUser(command);
        UserDto found = userDao.getUser(id);
        assertEquals(found.getName(), "homer");
        assertEquals(found.getEmail(), "h@s.com");

        // cleanup
        fixture.removeUser(id);
    }

    @Test
    public void shouldAssignRoles() throws Exception {
        // when
        userFacade.setUserRoles("anonym@anonym.com", Collections.singleton(Role.EVALUATOR));

        // then
        final UserDto user = userDao.getByEmail("anonym@anonym.com");
        assertEquals(user.getRoles(), Collections.singleton(Role.EVALUATOR));
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void shouldAssignOnlyKnownRoles() throws Exception {
        // when
        userFacade.setUserRoles("anonym@anonym.com", Collections.singleton("ROLE_NONEXISTING"));
        // then
        fail("exception should have been thrown");
    }

    @Test(expectedExceptions = {CantRevokeOwnAdminRights.class})
    public void cantRemoveAdminRoleFromSelf() throws Exception {
        // when
        userFacade.setUserRoles("bob@builder.com", Collections.singleton(Role.USER));
        // then
        fail("exception should have been thrown");
    }
}
