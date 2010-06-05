package org.adaptiveplatform.surveys.service.internal;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.SurveyPublication;
import org.adaptiveplatform.surveys.exception.NoSuchPublishedSurveyTemplateException;
import org.adaptiveplatform.surveys.service.SurveyPublicationRepository;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Marcin Deryło
 */
@Repository
public class HibernateSurveyPublicationRepository implements
        SurveyPublicationRepository {

    @Resource
    private SessionFactory sf;

    @Override
    public SurveyPublication get(Long publicationId) {
        return (SurveyPublication) sf.getCurrentSession().
                get(SurveyPublication.class, publicationId);
    }

    @Override
    public void persist(SurveyPublication newPublication) {
        sf.getCurrentSession().persist(newPublication);
    }

    @Override
    public List<SurveyPublication> getByGroup(Long groupId) {
        Query q = sf.getCurrentSession().createQuery("FROM SurveyPublication sp "
                + "WHERE sp.studentGroup.id = :groupId");
        q.setParameter("groupId", groupId);
        return q.list();
    }

    @Override
    public List<SurveyPublication> listPublications(Long templateId) {
        return sf.getCurrentSession().createCriteria(SurveyPublication.class).
                add(Restrictions.eq("surveyTemplate.id", templateId)).
                list();
    }

    @Override
    public void remove(SurveyPublication publication) {
        sf.getCurrentSession().delete(publication);
    }

    @Override
    public SurveyPublication getExisting(Long publishedSurveyTemplateId) {
        SurveyPublication publication = get(publishedSurveyTemplateId);
        if (publication == null) {
            throw new NoSuchPublishedSurveyTemplateException(
                    publishedSurveyTemplateId);
        }
        return publication;
    }
}
