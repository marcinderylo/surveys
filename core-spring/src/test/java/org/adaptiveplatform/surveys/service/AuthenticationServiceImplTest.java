package org.adaptiveplatform.surveys.service;

import static org.testng.Assert.assertFalse;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * 
 * @author Rafal Jamroz
 */
@ContextConfiguration("classpath:/testConfigurationContext.xml")
public class AuthenticationServiceImplTest extends AbstractTransactionalTestNGSpringContextTests {

    @Resource(name = "defaultAuthenticationService")
    private AuthenticationService service;
    @Resource(name = "sessionFactory")
    private SessionFactory sf;
    private static final String existingEmail = "username@user.com";
    private static final String existingPassword = "password";
    private static final String existingName = "Foo Bar";
    private Long existingUserId;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Session session = sf.openSession();
        session.beginTransaction();
        UserAccount user = new UserAccount(existingPassword, existingEmail);
        user.setName(existingName);
        existingUserId = (Long) session.save(user);
        session.getTransaction().commit();
    }

    @AfterMethod
    public void afterMethod() throws Exception {
        Session session = sf.openSession();
        session.beginTransaction();
        session.delete(session.get(UserAccount.class, existingUserId));
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void shouldNotBeAuthenticatedByDefault() throws Exception {
        assertFalse(service.isAuthenticated());
    }
}
