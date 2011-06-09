package org.adaptiveplatform.surveys.service.internal;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.NoSuchSurveyTemplateException;
import org.adaptiveplatform.surveys.service.SurveyTemplateRepository;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateSurveyTemplateRepository implements SurveyTemplateRepository {

    @Resource(name = "sessionFactory")
    private SessionFactory sf;

    @Override
    public SurveyTemplate get(Long templateId) {
        return (SurveyTemplate) sf.getCurrentSession().get(SurveyTemplate.class, templateId);
    }

    @Override
    public void persist(SurveyTemplate template) {
        sf.getCurrentSession().persist(template);
    }

    @Override
    public void delete(SurveyTemplate template) {
        sf.getCurrentSession().delete(template);
    }

    @Override
    public boolean exists(UserDto creator, String name) {
        Criteria crit = sf.getCurrentSession().createCriteria(SurveyTemplate.class);
        crit.add(Restrictions.eq("title", name)).add(Restrictions.eq("owner.id", creator.getId()));
        crit.setProjection(Projections.countDistinct("id"));

        Long count = (Long) crit.uniqueResult();
        return count > 0;
    }

    @Override
    public SurveyTemplate getExisting(Long surveyTemplateId) {
        SurveyTemplate template = get(surveyTemplateId);
        if (template == null) {
            throw new NoSuchSurveyTemplateException(surveyTemplateId);
        }
        return template;
    }
}
