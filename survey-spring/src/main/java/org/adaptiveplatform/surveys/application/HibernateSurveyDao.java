package org.adaptiveplatform.surveys.application;

import com.google.common.collect.Lists;
import static org.adaptiveplatform.surveys.utils.Collections42.asLongs;

import java.util.Collection;
import java.util.Collections;
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
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("surveyDao")
@RemotingDestination
@Transactional(propagation = Propagation.SUPPORTS)
public class HibernateSurveyDao implements SurveyDao {

	@Resource(name = "sessionFactory")
	private SessionFactory sf;
	@Resource
	private AuthenticationService authentication;

	@Secured({ Role.USER, Role.EVALUATOR })
	@Override
	public FilledSurveyDto getSurvey(Long surveyId) {
		FilledSurveyDto survey = (FilledSurveyDto) sf.getCurrentSession().getNamedQuery(FilledSurveyDto.Query.GET_SURVEY).setParameter("surveyId", surveyId)
				.setParameter("userId", authentication.getCurrentUser().getId()).uniqueResult();
		if (survey == null) {
			throw new FilledSurveyDoesNotExistException(surveyId);
		}

		return survey;
	}

	// TODO this is just a temporary unsecure fix
	// @Secured(Role.EVALUATOR)
	@Override
	@SuppressWarnings("unchecked")
	public List<FilledSurveyDto> querySurveys(FilledSurveyQuery query) {
		Criteria criteria = sf.getCurrentSession().createCriteria(FilledSurveyDto.class);
		// TODO add test for reading filled survey for specific publication
		if (query.getTemplateId() != null) {
			criteria.add(Restrictions.eq("surveyTemplateId", query.getTemplateId()));
		}
		if (query.getGroupId() != null) {
			criteria.add(Restrictions.eq("groupId", query.getGroupId()));
		}
		criteria.add(Restrictions.isNotNull("submitDate"));
		restrictFilledSurveysSearchWithKeywordIfAny(criteria, query.getKeyword());

		criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		return criteria.list();
	}

	private void restrictFilledSurveysSearchWithKeywordIfAny(Criteria criteria, String keyword) {
		if (StringUtils.hasText(keyword)) {
			final Disjunction keywordDisjunction = Restrictions.disjunction();
			keywordDisjunction.add(Restrictions.ilike("groupName", keyword, MatchMode.ANYWHERE));
			keywordDisjunction.add(Restrictions.ilike("surveyTemplateName", keyword, MatchMode.ANYWHERE));
			criteria.add(keywordDisjunction);
		}
	}

	@Override
	@Secured({ Role.USER, Role.EVALUATOR })
	@SuppressWarnings("unchecked")
	public List<PublishedSurveyTemplateDto> queryPublishedTemplates(PublishedSurveyTemplateQuery query) {
		GroupRoleEnum role = query.getRunAs();
		// FIXME change to validaton exception?
		Validate.notNull(role, "Must specify a role to read published templates with");
		switch (role) {
		case STUDENT:
			return queryFillablePublishedTemplates(query);
		case EVALUATOR:
			return queryCreatedPublishedTemplates(query);
		default:
			// FIXME change to validaton exception
			throw new IllegalArgumentException("Unsupported role " + "for reading published templates: " + role);
		}

	}

	@Secured({ Role.USER })
	private List<PublishedSurveyTemplateDto> queryFillablePublishedTemplates(PublishedSurveyTemplateQuery query) {
		final Session session = sf.getCurrentSession();

		Query q = null;
		if (thereAreGroupsSpecifiedInQuery(query)) {
			q = session.getNamedQuery(PublishedSurveyTemplateDto.Query.GET_SURVEY_TEMPLATE_IDS_IN_GROUPS);
			q.setParameterList("groupIds", asLongs(query.getGroupIds()));
		} else {
			q = session.getNamedQuery(PublishedSurveyTemplateDto.Query.GET_SURVEY_TEMPLATE_IDS);
		}
		q.setParameter("user", authentication.getCurrentUser());
		List<Long> templateIds = q.list();

		List<PublishedSurveyTemplateDto> templates = Lists.newArrayList();
		if (templateIds.size() > 0) {
			Criteria dtoCriteria = session.createCriteria(PublishedSurveyTemplateDto.class);
			dtoCriteria.add(Restrictions.in("id", templateIds));
			restrictFillableSurveysSearchWithKeywordIfAny(dtoCriteria, query.getKeyword());
			templates.addAll(dtoCriteria.list());
			templates = calculatePublishedSurveyStatus(templates);
		}
		return templates;
	}

	private boolean querySpecifiesKeyword(PublishedSurveyTemplateQuery query) {
		return StringUtils.hasText(query.getKeyword());
	}

	private boolean thereAreGroupsSpecifiedInQuery(PublishedSurveyTemplateQuery query) {
		return query.getGroupIds() != null && !query.getGroupIds().isEmpty();
	}

	private void restrictFillableSurveysSearchWithKeywordIfAny(Criteria criteria, String keyword) {
		if (StringUtils.hasText(keyword)) {
			final Disjunction keywordDisjunction = Restrictions.disjunction();
			keywordDisjunction.add(Restrictions.ilike("groupName", keyword, MatchMode.ANYWHERE));
			keywordDisjunction.add(Restrictions.ilike("name", keyword, MatchMode.ANYWHERE));
			criteria.add(keywordDisjunction);
		}
	}

	@Secured(Role.EVALUATOR)
	private List<PublishedSurveyTemplateDto> queryCreatedPublishedTemplates(PublishedSurveyTemplateQuery query) {
		// TODO optimization
		Criteria criteria = sf.getCurrentSession().createCriteria(SurveyTemplate.class);
		criteria.add(Restrictions.eq("owner", authentication.getCurrentUser()));
		criteria.setProjection(Projections.id());
		List<Long> templateIds = criteria.list();

		Criteria publishedTemplateCriteria = sf.getCurrentSession().createCriteria(PublishedSurveyTemplateDto.class);
		publishedTemplateCriteria.add(Restrictions.in("templateId", templateIds));
		if (thereAreGroupsSpecifiedInQuery(query)) {
			publishedTemplateCriteria.add(Restrictions.in("groupId", asLongs(query.getGroupIds())));
		}
		return publishedTemplateCriteria.list();
	}

	@Override
	@Secured({ Role.EVALUATOR })
	public SurveyTemplateDto getTemplate(Long templateId) {
		final Session session = sf.getCurrentSession();
		final Query q = session.getNamedQuery(SurveyTemplateDto.Query.GET_TEMPLATE_QUERY);
		q.setParameter("templateId", templateId);
		q.setParameter("owner", authentication.getCurrentUser());
		final SurveyTemplateDto template = (SurveyTemplateDto) q.uniqueResult();

		if (template == null) {
			throw new NoSuchSurveyTemplateException(templateId);
		}

		return template;
	}

	@Override
	@Secured({ Role.EVALUATOR })
	public List<SurveyTemplateDto> queryTemplates(SurveyTemplateQuery query) {
		final Session session = sf.getCurrentSession();

		Criteria templateIdCriteria = session.createCriteria(SurveyTemplate.class);
		templateIdCriteria.add(Restrictions.eq("owner", authentication.getCurrentUser()));
		templateIdCriteria.setProjection(Projections.id());
		if (StringUtils.hasText(query.getNameContains())) {
			templateIdCriteria.add(Restrictions.ilike("title", query.getNameContains(), MatchMode.ANYWHERE));
		}
		List<Long> ids = templateIdCriteria.list();

		List<SurveyTemplateDto> templates = Lists.newArrayList();

		if (ids.size() > 0) {
			Criteria dtoCriteria = session.createCriteria(SurveyTemplateDto.class).add(Restrictions.in("id", ids));
			templates.addAll(dtoCriteria.list());
		}

		for (SurveyTemplateDto template : templates) {
			template.setQuestions(Collections.EMPTY_LIST);
		}

		return templates;
	}

	private List<PublishedSurveyTemplateDto> calculatePublishedSurveyStatus(Collection<PublishedSurveyTemplateDto> templates) {
		List<PublishedSurveyTemplateDto> validResults = Lists.newArrayList();
		List<Object[]> filledIds = sf.getCurrentSession()
				.createQuery("SELECT s.publicationId, s.id, s.submitDate " + "FROM FilledSurveyDto s WHERE s.userId = :userId")
				.setParameter("userId", authentication.getCurrentUser().getId()).list();
		for (PublishedSurveyTemplateDto template : templates) {
			boolean isNotSubmittedYet = true;
			boolean isInsidePublicationPeriod = false;
			for (Object[] ids : filledIds) {
				Long publicationId = (Long) ids[0];
				Long filledSurveyId = (Long) ids[1];
				Date submitDate = (Date) ids[2];
				if (template.getId().equals(publicationId)) {
					if (submitDate == null) {
						template.setStatus(SurveyStatusEnum.STARTED);
						template.setFilledSurveyId(filledSurveyId);
					} else {
						template.setStatus(SurveyStatusEnum.SUBMITTED);
						template.setSubmitted(Boolean.TRUE);
						isNotSubmittedYet = false;
					}
				}
			}

			if (template.isFillable(new Date())) {
				isInsidePublicationPeriod = true;
				setStatusIfNoneYet(template, SurveyStatusEnum.PENDING);
			} else {
				setStatusIfNoneYet(template, SurveyStatusEnum.OUTSIDE_PUBLICATION_PERIOD);
			}
			if (isInsidePublicationPeriod && isNotSubmittedYet) {
				validResults.add(template);
			}
		}
		return validResults;
	}

	private void setStatusIfNoneYet(PublishedSurveyTemplateDto template, SurveyStatusEnum status) {
		if (template.getStatus() == null) {
			template.setStatus(status);
		}
	}
}
