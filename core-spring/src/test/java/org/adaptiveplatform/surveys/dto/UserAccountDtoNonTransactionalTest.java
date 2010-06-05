package org.adaptiveplatform.surveys.dto;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;

import java.util.Arrays;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.domain.UserPrivilege;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * 
 * @author Rafal
 */
@ContextConfiguration(locations = "classpath:/testConfigurationContext.xml")
public class UserAccountDtoNonTransactionalTest extends AbstractTestNGSpringContextTests {

	@Resource(name = "sessionFactory")
	private SessionFactory sf;

	@Test
	public void shouldPersistRolesEntity() throws Exception {
		// given
		UserAccount account = new UserAccount("password", "email@test.com");
                account.setName("username");
		account.getPrivileges().add(UserPrivilege.USER);
		Long id = saveInTransaction(account);

		// when
		Session session = sf.openSession();
		UserAccount dto = (UserAccount) session.get(UserAccount.class, id);
		session.close();

		// then
		assertNotSame(account, dto);
		assertEquals(dto.getId(), id);
		assertEquals(dto.getName(), account.getName());
		assertEquals(dto.getPassword(), account.getPassword());
		assertEquals(dto.getEmail(), account.getEmail());

		// cleaup
		removeUserAccount(id);
	}

	@Test
	public void shouldReadDtoForStoredDomainEntity() throws Exception {
		// given
		UserAccount account = new UserAccount("password2", "2@test.com");
                account.setName("username2");
		account.getPrivileges().add(UserPrivilege.USER);
		Long id = saveInTransaction(account);
		Session session = sf.openSession();

		// when
		UserDto dto = (UserDto) session.get(UserDto.class, id);

		// then
		assertEquals(dto.getId(), id);
		assertEquals(dto.getName(), account.getName());
		assertEquals(dto.getEmail(), account.getEmail());
		assertEquals(dto.getRoles(), Arrays.asList(UserPrivilege.USER.role));

		// cleaup
		session.close();
		removeUserAccount(id);
	}

	private Long saveInTransaction(Object entity) {
		Session s = sf.openSession();
		s.beginTransaction();
		Long id = (Long) s.save(entity);
		s.getTransaction().commit();
		s.close();
		return id;
	}

	private void removeUserAccount(Long id) {
		Session s = sf.openSession();
		s.beginTransaction();
		UserAccount dto = (UserAccount) s.get(UserAccount.class, id);
		s.delete(dto);
		s.getTransaction().commit();
		s.close();
	}
}
