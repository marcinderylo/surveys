package org.adaptiveplatform.surveys.service.internal;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.FilledSurvey;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.service.SurveyRepository;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Marcin Derylo
 */
@Repository("surveyRepository")
public class HibernateSurveyRepository implements SurveyRepository {

    @Resource(name = "sessionFactory")
    private SessionFactory sf;

    @Override
    public FilledSurvey get(Long surveyId) {
        return (FilledSurvey) sf.getCurrentSession().get(FilledSurvey.class,
                surveyId);
    }

    @Override
    public void persist(FilledSurvey survey) {
        sf.getCurrentSession().persist(survey);
    }

    @Override
    public List<FilledSurvey> list(Long templateId, Long groupId) {
        final String hqlQuery = "SELECT s FROM FilledSurvey s JOIN s.template t "
                + "WHERE t.surveyTemplate.id = :templateId "
                + "AND t.studentGroup.id = :groupId";
        final Query query = sf.getCurrentSession().createQuery(hqlQuery);
        query.setParameter("templateId", templateId);
        query.setParameter("groupId", groupId);
        return query.list();
    }

    @Override
    public FilledSurvey get(Long templateId, Long groupId, UserDto user) {
        final String hqlQuery = "SELECT s FROM FilledSurvey s JOIN s.template t "
                + "WHERE t.surveyTemplate.id = :templateId "
                + "AND t.studentGroup.id = :groupId AND s.user.id = :userId";
        final Query query = sf.getCurrentSession().createQuery(hqlQuery);
        query.setParameter("templateId", templateId);
        query.setParameter("groupId", groupId);
        query.setParameter("userId", user.getId());
        return (FilledSurvey) query.uniqueResult();
    }

    @Override
    public List<FilledSurvey> list(Long templateId) {
        final String hqlQuery = "SELECT s FROM FilledSurvey s "
                + "WHERE s.template.surveyTemplate.id = :templateId ";
        final Query query = sf.getCurrentSession().createQuery(hqlQuery);
        query.setParameter("templateId", templateId);
        return query.list();
    }
}
