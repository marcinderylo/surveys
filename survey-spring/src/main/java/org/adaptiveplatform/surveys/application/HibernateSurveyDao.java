package org.adaptiveplatform.surveys.application;

import static org.adaptiveplatform.surveys.utils.Collections42.asLongs;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.dto.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.FilledSurveyQuery;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateQuery;
import org.adaptiveplatform.surveys.dto.SurveyStatusEnum;
import org.adaptiveplatform.surveys.dto.SurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.SurveyTemplateQuery;
import org.adaptiveplatform.surveys.exception.FilledSurveyDoesNotExistException;
import org.adaptiveplatform.surveys.exception.NoSuchSurveyTemplateException;
import org.apache.commons.lang.Validate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("surveyDao")
@RemotingDestination
@Transactional(propagation = Propagation.SUPPORTS)
public class HibernateSurveyDao implements SurveyDao {

    @Resource(name = "sessionFactory")
    private SessionFactory sf;
    @Resource
    private AuthenticationService authentication;

    @Secured({Role.USER, Role.EVALUATOR})
    @Override
    public FilledSurveyDto getSurvey(Long surveyId) {
        FilledSurveyDto survey = (FilledSurveyDto) sf.getCurrentSession().
                getNamedQuery(
                FilledSurveyDto.Query.GET_SURVEY).setParameter("surveyId",
                surveyId).setParameter("userId",
                authentication.getCurrentUser().getId()).uniqueResult();
        if (survey == null) {
            throw new FilledSurveyDoesNotExistException(surveyId);
        }

        return survey;
    }

    @Secured(Role.EVALUATOR)
    @Override
    @SuppressWarnings("unchecked")
    public List<FilledSurveyDto> querySurveys(FilledSurveyQuery query) {
        Criteria criteria = sf.getCurrentSession().createCriteria(
                FilledSurveyDto.class);
        // TODO add test for reading filled survey for specific publication
        if (query.getTemplateId() != null) {
            criteria.add(Restrictions.eq("surveyTemplateId",
                    query.getTemplateId()));
        }
        if (query.getGroupId() != null) {
            criteria.add(Restrictions.eq("groupId", query.getGroupId()));
        }
        criteria.add(Restrictions.isNotNull("submitDate"));
        // TODO criteria - add restrictions
        criteria.setResultTransformer(
                DistinctRootEntityResultTransformer.INSTANCE);
        return criteria.list();
    }

    @Override
    @Secured({Role.USER, Role.EVALUATOR})
    @SuppressWarnings("unchecked")
    public List<PublishedSurveyTemplateDto> queryPublishedTemplates(
            PublishedSurveyTemplateQuery query) {
        GroupRoleEnum role = query.getRunAs();
        // FIXME change to validaton exception?
        Validate.notNull(role,
                "Must specify a role to read published templates with");
        switch (role) {
            case STUDENT:
                return queryFillablePublishedTemplates(query);
            case EVALUATOR:
                return queryCreatedPublishedTemplates(query);
            default:
                // FIXME change to validaton exception
                throw new IllegalArgumentException("Unsupported role "
                        + "for reading published templates: " + role);
        }

    }

    @Secured({Role.USER})
    private List<PublishedSurveyTemplateDto> queryFillablePublishedTemplates(
            PublishedSurveyTemplateQuery query) {
        final Session session = sf.getCurrentSession();
        Query q = null;
        // TODO refactor to use criteria API
        if (thereAreGroupsSpecifiedInQuery(query)) {
            q =
                    session.getNamedQuery(
                    PublishedSurveyTemplateDto.Query.GET_SURVEY_TEMPLATES_IN_GROUPS);
            q.setParameterList("groupIds", asLongs(query.getGroupIds()));
        } else {
            q = session.getNamedQuery(
                    PublishedSurveyTemplateDto.Query.GET_SURVEY_TEMPLATES);
        }
        q.setParameter("user", authentication.getCurrentUser());

        // TODO criteria - add restrictions
        List<PublishedSurveyTemplateDto> templates = q.list();

        calculatePublishedSurveyStatus(templates);
        return templates;
    }

    private boolean thereAreGroupsSpecifiedInQuery(
            PublishedSurveyTemplateQuery query) {
        return query.getGroupIds() != null && !query.getGroupIds().isEmpty();
    }

    @Secured(Role.EVALUATOR)
    private List<PublishedSurveyTemplateDto> queryCreatedPublishedTemplates(
            PublishedSurveyTemplateQuery query) {
        // TODO optimization
        Criteria criteria = sf.getCurrentSession().createCriteria(
                SurveyTemplate.class);
        criteria.add(Restrictions.eq("owner", authentication.getCurrentUser()));
        criteria.setProjection(Projections.id());
        List<Long> templateIds = criteria.list();

        Criteria publishedTemplateCriteria = sf.getCurrentSession().
                createCriteria(PublishedSurveyTemplateDto.class);
        publishedTemplateCriteria.add(Restrictions.in("templateId", templateIds));
        if (thereAreGroupsSpecifiedInQuery(query)) {
            publishedTemplateCriteria.add(Restrictions.in("groupId", asLongs(query.
                    getGroupIds())));
        }
        return publishedTemplateCriteria.list();
    }

    @Override
    @Secured({Role.EVALUATOR})
    public SurveyTemplateDto getTemplate(Long templateId) {
        final Session session = sf.getCurrentSession();
        final Query q = session.getNamedQuery(
                SurveyTemplateDto.Query.GET_TEMPLATE_QUERY);
        q.setParameter("templateId", templateId);
        q.setParameter("owner", authentication.getCurrentUser());
        final SurveyTemplateDto template = (SurveyTemplateDto) q.uniqueResult();

        if (template == null) {
            throw new NoSuchSurveyTemplateException(templateId);
        }

        return template;
    }

    @Override
    @Secured({Role.EVALUATOR})
    public List<SurveyTemplateDto> queryTemplates(SurveyTemplateQuery query) {
        final Session session = sf.getCurrentSession();
        final Query q = session.getNamedQuery(
                SurveyTemplateDto.Query.GET_TEMPLATES_QUERY);
        q.setParameter("owner", authentication.getCurrentUser());
        List<SurveyTemplateDto> templates = q.list();

        for (SurveyTemplateDto template : templates) {
            template.setQuestions(null);
        }

        return templates;
    }

    private void calculatePublishedSurveyStatus(
            Collection<PublishedSurveyTemplateDto> templates) {
        List<Object[]> filledIds = sf.getCurrentSession().createQuery(
                "SELECT s.surveyTemplateId, s.groupId, s.id, s.submitDate "
                + "FROM FilledSurveyDto s WHERE s.userId = :userId").
                setParameter("userId",
                authentication.getCurrentUser().getId()).list();
        for (PublishedSurveyTemplateDto template : templates) {
            for (Object[] ids : filledIds) {
                Long templateId = (Long) ids[0];
                Long groupId = (Long) ids[1];
                Long filledSurveyId = (Long) ids[2];
                Date submitDate = (Date) ids[3];
                if (template.getTemplateId().equals(templateId) && template.
                        getGroupId().equals(groupId)) {
                    if (submitDate == null) {
                        template.setStatus(SurveyStatusEnum.STARTED);
                        template.setFilledSurveyId(filledSurveyId);
                    } else {
                        template.setStatus(SurveyStatusEnum.SUBMITTED);
                        template.setSubmitted(Boolean.TRUE);
                    }
                }
            }
            if (template.getStatus() == null) {

                if (template.isFillable(new Date())) {
                    template.setStatus(SurveyStatusEnum.PENDING);
                } else {
                    template.setStatus(
                            SurveyStatusEnum.OUTSIDE_PUBLICATION_PERIOD);
                }
            }
        }
    }
}
