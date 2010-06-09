package org.adaptiveplatform.surveys.service.internal;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.StudentGroup;
import org.adaptiveplatform.surveys.exception.NoSuchGroupException;
import org.adaptiveplatform.surveys.service.StudentGroupRepository;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Marcin Deryło
 */
@Repository("studentGroupRepository")
public class HibernateStudentGroupRepository implements StudentGroupRepository {

    @Resource
    private SessionFactory sf;

    @Override
    public StudentGroup get(Long groupId) {
        return (StudentGroup) sf.getCurrentSession().get(
                StudentGroup.class, groupId);
    }

    @Override
    public StudentGroup getExisting(Long groupId) {
        StudentGroup group = get(groupId);
        if (group == null) {
            throw new NoSuchGroupException(groupId);
        }
        return group;
    }

    @Override
    public void persist(StudentGroup group) {
        sf.getCurrentSession().persist(group);
    }

    @Override
    public void remove(Long groupId) {
        sf.getCurrentSession().delete(get(groupId));
    }

    @Override
    public StudentGroup getByName(String trimmedName) {
        Criteria criteria = sf.getCurrentSession().createCriteria(StudentGroup.class);
        criteria.add(Restrictions.eq("name", trimmedName));
        return (StudentGroup) criteria.uniqueResult();
    }
}
