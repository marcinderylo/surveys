package org.adaptiveplatform.surveys.acceptance;

import static org.testng.Assert.*;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.application.UserDao;
import org.adaptiveplatform.surveys.application.UserFacade;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.dto.UserQuery;
import org.adaptiveplatform.surveys.exception.security.CantRevokeOwnAdminRights;
import org.adaptiveplatform.surveys.exception.security.EmailAddressAlreadyRegisteredException;
import org.adaptiveplatform.surveys.exception.security.NotAllowedToViewUserDetailsException;
import org.adaptiveplatform.surveys.test.HsqlSavableDataSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.google.inject.internal.Iterables;

/**
 * @author Rafał Jamróz
 */
@ContextConfiguration("classpath:/acceptanceTestContext.xml")
public class CoreAcceptanceTest extends AbstractTestNGSpringContextTests {

	private static final String ADMIN_EMAIL = "adaptserver@gmail.com";
	private static final String ADMIN_PASSWORD = "adapt2010";

	@Resource
	private UserFacade userFacade;

	@Resource
	private UserDao userDao;

	@Test
	public void shouldAllowRegisteringAndLoggingIn() throws Exception {
		// given
		notLoggedIn();
		// when
		userFacade.registerUser(new RegisterAccountCommand("user", "s3cr3t", "bob@adapt.pl"));
		// then
		logIn("bob@adapt.pl", "s3cr3t");
		assertIsAuthenticatedAs("bob@adapt.pl");
	}

	@Test
	public void ensureCannotRegisterTwoAccountsWithTheSameEmail() throws Exception {
		// given
		notLoggedIn();
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
	public void shouldAdminGetOtherUsersDetails() throws Exception {
		// given
		notLoggedIn();
		userFacade.registerUser(new RegisterAccountCommand("user2", "s3cr3t", "alice@adapt.pl"));
		// when
		logIn(ADMIN_EMAIL, ADMIN_PASSWORD);
		UserDto user = userDao.getByEmail("alice@adapt.pl");
		// then
		assertEquals(user.getName(), "user2");
		assertEquals(user.getEmail(), "alice@adapt.pl");
	}

	@Test
	public void shouldUserGetHisOwnDetails() throws Exception {
		// given
		notLoggedIn();
		userFacade.registerUser(new RegisterAccountCommand("user2", "s3cr3t", "alice@adapt.pl"));
		// when
		logIn("alice@adapt.pl", "s3cr3t");
		UserDto user = userDao.getByEmail("alice@adapt.pl");
		// then
		assertEquals(user.getName(), "user2");
		assertEquals(user.getEmail(), "alice@adapt.pl");
	}

	@Test(expectedExceptions = NotAllowedToViewUserDetailsException.class)
	public void ensureUserCannotGetOtherUsersDetails() throws Exception {
		// given
		notLoggedIn();
		userFacade.registerUser(new RegisterAccountCommand("alice", "s3cr3t", "alice@adapt.pl"));
		userFacade.registerUser(new RegisterAccountCommand("bob", "s3cr3t", "bob@adapt.pl"));
		// when
		logIn("alice@adapt.pl", "s3cr3t");
		userDao.getByEmail("bob@adapt.pl");
	}

	@Test
	public void shouldAdminBeAbleToQueryByName() throws Exception {
		// given
		notLoggedIn();
		userFacade.registerUser(new RegisterAccountCommand("alice", "s3cr3t", "alice@adapt.pl"));
		userFacade.registerUser(new RegisterAccountCommand("bob", "s3cr3t", "bob@adapt.pl"));
		// when
		logIn(ADMIN_EMAIL, ADMIN_PASSWORD);
		UserQuery userQuery = new UserQuery();
		userQuery.setNameContains("ali");
		List<UserDto> results = userDao.query(userQuery);
		assertEquals(results.size(), 1);
		assertEquals(Iterables.getOnlyElement(results).getEmail(), "alice@adapt.pl");
	}

	@Test
	public void shouldAssignRoles() throws Exception {
		// given
		notLoggedIn();
		userFacade.registerUser(new RegisterAccountCommand("annon", "s3cr3t", "anonym@anonym.com"));
		// when
		logIn(ADMIN_EMAIL, ADMIN_PASSWORD);
		userFacade.setUserRoles("anonym@anonym.com", Collections.singleton(Role.EVALUATOR));

		// then
		final UserDto user = userDao.getByEmail("anonym@anonym.com");
		assertEquals(user.getRoles(), Collections.singleton(Role.EVALUATOR));
	}

	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void shouldAssignOnlyKnownRoles() throws Exception {
		// given
		notLoggedIn();
		userFacade.registerUser(new RegisterAccountCommand("annon", "s3cr3t", "anonym@anonym.com"));
		// when
		logIn(ADMIN_EMAIL, ADMIN_PASSWORD);
		userFacade.setUserRoles("anonym@anonym.com", Collections.singleton("ROLE_NONEXISTING"));
		// then
		fail("exception should have been thrown");
	}

	@Test(expectedExceptions = { CantRevokeOwnAdminRights.class })
	public void cantRemoveAdminRoleFromSelf() throws Exception {
		// when
		logIn(ADMIN_EMAIL, ADMIN_PASSWORD);
		userFacade.setUserRoles(ADMIN_EMAIL, Collections.singleton(Role.USER));
		// then
		fail("exception should have been thrown");
	}

	private void assertIsAuthenticatedAs(String email) {
		assertEquals(SecurityContextHolder.getContext().getAuthentication().getName(), email);
	}

	@Resource
	private AuthenticationService authenticationService;

	private void logIn(String email, String password) {
		authenticationService.login(email, password);
	}

	private void notLoggedIn() {
		SecurityContextHolder.clearContext();
	}

	@Resource
	private HsqlSavableDataSource dataSource;

	@AfterMethod
	public void cleanup() throws Exception {
		dataSource.restoreFromSnapshot();
		notLoggedIn();
	}
}
