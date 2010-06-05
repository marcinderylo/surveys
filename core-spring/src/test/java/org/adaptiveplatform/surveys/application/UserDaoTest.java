package org.adaptiveplatform.surveys.application;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.domain.UserPrivilege;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.security.NotAllowedToViewUserDetailsException;
import org.adaptiveplatform.surveys.service.AuthenticationServiceMock;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "classpath:/testConfigurationContext.xml")
public class UserDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Resource(name = "sessionFactory")
    private SessionFactory sf;
    @Resource
    private UserDao dao;
    @Resource
    private AuthenticationServiceMock authentication;
    private Long existingUsersId;
    private String existingUsersEmail;
    private Long bobId;

    /**
     * Executes before each method but in the same transaction as that method.
     */
    @BeforeMethod
    public void beforeMethod() throws Exception {
        Session session = sf.getCurrentSession();
        UserAccount existingAccount = createUserAccount("alice", "haslo",
                "email@aa.com", UserPrivilege.ADMINISTRATOR);
        session.persist(existingAccount);
        existingUsersId = existingAccount.getId();
        existingUsersEmail = existingAccount.getEmail();
        UserAccount account = createUserAccount("bob", "haslo", "ema123@bb.com",
                UserPrivilege.USER);
        session.persist(account);
        bobId = account.getId();
        session.persist(createUserAccount("mr blue", "haslo", "blue@blue.com",
                UserPrivilege.USER,
                UserPrivilege.ADMINISTRATOR));

    }

    @AfterMethod
    public void afterMethod() throws Exception {
        authentication.logout();
    }

    @Test
    public void shouldAdminGetOtherUsersById() throws Exception {
        authentication.authenticate(bobId, "bob", Role.TEACHER);
        UserDto dto = dao.getUser(existingUsersId);
        assertEquals(dto.getEmail(), existingUsersEmail);
    }

    @Test(expectedExceptions = {NotAllowedToViewUserDetailsException.class})
    public void cantGetOtherUsersInfoWithoutAdminRights() throws Exception {
        authentication.authenticate(bobId, "bob", Role.USER);
        dao.getUser(existingUsersId);
    }

    @Test
    public void shouldGetOwnDetailsWithoutAdminRights() throws Exception {
        authentication.authenticate(bobId, "bob", Role.USER);
        UserDto dto = dao.getUser(bobId);
        assertNotNull(dto);
    }

    @Test
    public void shouldAdminGetByQuery() throws Exception {
        authentication.authenticate(bobId, "bob", Role.ADMINISTRATOR);
        List<UserDto> query = dao.query(null);
        assertEquals(query.size(), 3);
    }

    private UserAccount createUserAccount(String name, String password,
            String email, UserPrivilege... privileges) {
        UserAccount user = new UserAccount(password, email);
        user.setName(name);
        for (UserPrivilege privilege : privileges) {
            user.getPrivileges().add(privilege);
        }
        return user;
    }
}
