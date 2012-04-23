package org.adaptiveplatform.surveys.acceptance;

import com.google.common.collect.Iterables;
import org.adaptiveplatform.surveys.ContainerEnabledTest;
import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.application.UserDao;
import org.adaptiveplatform.surveys.application.UserFacade;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.dto.UserQuery;
import org.adaptiveplatform.surveys.exception.security.CantRevokeOwnAdminRights;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.student;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author Rafał Jamróz
 */
public class UserManagementAcceptanceTest extends ContainerEnabledTest {

    private static final String ADMIN_EMAIL = "adaptserver@gmail.com";
    private static final String ADMIN_PASSWORD = "adapt2010";

    @Resource
    private CoreFixtureBuilder fixture;

    @Resource
    private UserFacade userFacade;

    @Resource
    private UserDao userDao;

    @Resource
    private AuthenticationService authenticationService;

    @Test
    public void shouldAdminGetOtherUsersDetails() throws Exception {
        // given
        userFacade.registerUser(new RegisterAccountCommand("user2", "s3cr3t", "alice@adapt.pl"));
        // when
        authenticationService.login(ADMIN_EMAIL, ADMIN_PASSWORD);
        UserDto user = userDao.getByEmail("alice@adapt.pl");
        // then
        assertEquals(user.getName(), "user2");
        assertEquals(user.getEmail(), "alice@adapt.pl");
    }

    @Test
    public void shouldAdminBeAbleToQueryByName() throws Exception {
        // given
        fixture.createUser(student("alice@adapt.pl"));
        fixture.createUser(student("bob@adapt.pl"));
        // when
        authenticationService.login(ADMIN_EMAIL, ADMIN_PASSWORD);
        UserQuery userQuery = new UserQuery();
        userQuery.setNameContains("ali");
        List<UserDto> results = userDao.query(userQuery);
        assertEquals(results.size(), 1);
        assertEquals(Iterables.getOnlyElement(results).getEmail(), "alice@adapt.pl");
    }

    @Test
    public void shouldAssignRoles() throws Exception {
        // given
        userFacade.registerUser(new RegisterAccountCommand("annon", "s3cr3t", "anonym@anonym.com"));
        // when
        authenticationService.login(ADMIN_EMAIL, ADMIN_PASSWORD);
        userFacade.setUserRoles("anonym@anonym.com", Collections.singleton(Role.EVALUATOR));
        // then
        final UserDto user = userDao.getByEmail("anonym@anonym.com");
        assertThat(user.getRoles()).containsOnly(Role.USER, Role.EVALUATOR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldAssignOnlyKnownRoles() throws Exception {
        // given
        userFacade.registerUser(new RegisterAccountCommand("annon", "s3cr3t", "anonym@anonym.com"));
        // when
        authenticationService.login(ADMIN_EMAIL, ADMIN_PASSWORD);
        userFacade.setUserRoles("anonym@anonym.com", Collections.singleton("ROLE_NONEXISTING"));
        // then - exception should have been thrown
    }

    @Test(expected = CantRevokeOwnAdminRights.class)
    public void cantRemoveAdminRoleFromSelf() throws Exception {
        // when
        authenticationService.login(ADMIN_EMAIL, ADMIN_PASSWORD);
        userFacade.setUserRoles(ADMIN_EMAIL, Collections.singleton(Role.USER));
        // then - exception should have been thrown
    }

    @Before
    public void notLoggedIn() {
        authenticationService.logout();
    }
}
