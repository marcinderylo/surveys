package org.adaptiveplatform.surveys.domain;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

/**
 *
 * @author Marcin Derylo
 */
@Service
public class StudentGroupTestFixtureBuilder {

	@Resource
	private SessionFactory sf;
	private Set<Long> groupIds = new HashSet<Long>();
	
	public StudentGroup createGroup(StudentGroupBuilder builder) {
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StudentGroup group = builder.build();
		Long groupId = (Long) session.save(group);
		tx.commit();
		session.close();
		groupIds.add(groupId);
		return group;
	}

	public void cleanUp() {
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
                
		for (Long id : groupIds) {
			Object group = session.load(StudentGroup.class, id);
			session.delete(group);
		}
		tx.commit();
		session.close();
		groupIds.clear();
	}

}
