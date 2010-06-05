package org.adaptiveplatform.surveys.service.internal;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.Research;
import org.adaptiveplatform.surveys.exception.NoSuchResearchException;
import org.adaptiveplatform.surveys.service.ResearchRepository;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Marcin Deryło
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class HibernateResearchRepository implements ResearchRepository {

    private SessionFactory sessionFactory;

    @Override
    public Research getExisting(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        final Research research = (Research) session.get(Research.class, id);
        if (research == null) {
            throw new NoSuchResearchException(id);
        }
        return research;
    }

    @Override
    public void persist(Research research) {
        sessionFactory.getCurrentSession().persist(research);
    }

    @Resource
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
