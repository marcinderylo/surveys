package org.adaptiveplatform.surveys.domain;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Service;

/**
 * @author Rafał Jamróz
 * @deprecated use {@link CoreFixtureBuilder}
 */
@Service
public class CoreTestFixtureBuilder {

	@Resource
	private SessionFactory sf;

	List<UserAccount> users = new ArrayList<UserAccount>();
	List<Long> usersIds = new ArrayList<Long>();

	public UserAccount createUser(UserAccountBuilder builder) {
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		UserAccount user = builder.build();
		usersIds.add((Long) session.save(user));
		tx.commit();
		session.close();
		return user;
	}

	public void removeUser(Long id) {
		usersIds.add(id);
	}

	public void cleanUp() {
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		for (Long id : usersIds) {
			Object user = session.load(UserAccount.class, id);
			session.delete(user);
		}
		tx.commit();
		session.close();
		usersIds.clear();
	}
}
