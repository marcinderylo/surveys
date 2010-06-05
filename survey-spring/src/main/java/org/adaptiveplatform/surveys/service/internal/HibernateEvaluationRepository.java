package org.adaptiveplatform.surveys.service.internal;

import org.adaptiveplatform.surveys.domain.AnswerEvaluation;
import org.adaptiveplatform.surveys.domain.AnsweredQuestion;
import org.adaptiveplatform.surveys.service.EvaluationRepository;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Marcin Deryło
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class HibernateEvaluationRepository implements EvaluationRepository {

    private SessionFactory sf;

    @Autowired
    public HibernateEvaluationRepository(SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public AnswerEvaluation get(Long researchId,
            AnsweredQuestion answeredQuestion) {
        final Criteria criteria =
                sf.getCurrentSession().createCriteria(AnswerEvaluation.class);
        criteria.add(Restrictions.eq("answeredQuestion", answeredQuestion));
        criteria.add(Restrictions.eq("research.id", researchId));
        return (AnswerEvaluation) criteria.uniqueResult();
    }

    @Override
    public void persist(AnswerEvaluation evaluation) {
        sf.getCurrentSession().persist(evaluation);
    }
}
