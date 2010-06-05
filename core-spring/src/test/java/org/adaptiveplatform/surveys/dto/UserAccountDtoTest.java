package org.adaptiveplatform.surveys.dto;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.domain.UserPrivilege;
import org.hibernate.SessionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;


@ContextConfiguration(locations = "classpath:/testConfigurationContext.xml")
public class UserAccountDtoTest extends AbstractTransactionalTestNGSpringContextTests {

	@Resource(name = "sessionFactory")
	private SessionFactory sf;

	@Test
	public void shouldReadDtoForStoredDomainEntity() throws Exception {
		// given
		UserAccount account = new UserAccount("password2", "2@test.com");
                account.setName("username2");
		account.getPrivileges().add(UserPrivilege.USER);
		Long id = (Long) sf.getCurrentSession().save(account);

		sf.getCurrentSession().flush();
		// when
		UserDto dto = (UserDto) sf.getCurrentSession().get(UserDto.class, id);

		// then
		assertEquals(dto.getId(), id);
		assertEquals(dto.getName(), account.getName());
		assertEquals(dto.getEmail(), account.getEmail());
		assertEquals(dto.getRoles(), Arrays.asList(UserPrivilege.USER.role));

	}
}
