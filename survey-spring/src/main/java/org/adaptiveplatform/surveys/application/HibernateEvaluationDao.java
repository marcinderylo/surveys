package org.adaptiveplatform.surveys.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.Research;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.dto.ActivitiesQuery;
import org.adaptiveplatform.surveys.dto.AnswerEvaluationDto;
import org.adaptiveplatform.surveys.dto.EvaluationActivityDto;
import org.adaptiveplatform.surveys.dto.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.ResearchDto;
import org.adaptiveplatform.surveys.dto.ResearchesQuery;
import org.adaptiveplatform.surveys.dto.StudentGroupDto;
import org.adaptiveplatform.surveys.dto.SurveyQuestionDto;
import org.adaptiveplatform.surveys.exception.NoSuchResearchException;
import org.adaptiveplatform.surveys.service.EvaluationActivityDao;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;

@Service("evaluationDao")
@RemotingDestination
@Transactional(propagation = Propagation.REQUIRED)
@Secured({ Role.EVALUATOR })
public class HibernateEvaluationDao implements EvaluationDao {

    @Resource
    private SessionFactory sf;
    @Resource
    private AuthenticationService authenticationService;
    @Resource
    private EvaluationActivityDao activityDao;

    @Override
    public ResearchDto get(Long researchId) {
        ResearchDto research = getExistingResearch(researchId);
        includeSubmittedSurveys(research);
        includeGroups(research);
        includeAssignedTags(research);
        return research;
    }

    private ResearchDto getExistingResearch(Long researchId) throws HibernateException {
        final Session s = sf.getCurrentSession();
        final Query q = s.getNamedQuery(ResearchDto.Queries.GET_RESEARCH);
        q.setParameter("id", researchId);
        q.setParameter("userId", authenticationService.getCurrentUser().getId());
        ResearchDto research = (ResearchDto) q.uniqueResult();
        if (research == null) {
            throw new NoSuchResearchException(researchId);
        }
        return research;
    }

    private void includeSubmittedSurveys(ResearchDto research) {
        final Session s = sf.getCurrentSession();
        List<Long> publishedSurveyIds = s.getNamedQuery(PublishedSurveyTemplateDto.Query.GET_IDS_IN_RESEARCH)
                .setParameter("id", research.getId()).list();
        if (!publishedSurveyIds.isEmpty()) {
            List<FilledSurveyDto> filledSurveys = s.getNamedQuery(FilledSurveyDto.Query.GET_FOR_RESEARCH)
                    .setParameterList("publishedSurveyTemplates", publishedSurveyIds).list();
            research.setSubmittedSurveys(filledSurveys);
        }
    }

    private void includeAssignedTags(ResearchDto research) {
        final Criteria criteria = sf.getCurrentSession().createCriteria(AnswerEvaluationDto.class);
        criteria.add(Restrictions.eq("researchId", research.getId()));
        List<AnswerEvaluationDto> answerEvaluations = criteria.list();
        Map<Long, Set<String>> tags = Maps.newHashMap();
        for (AnswerEvaluationDto answerEvaluationDto : answerEvaluations) {
            tags.put(answerEvaluationDto.getAnsweredQuestionId(), answerEvaluationDto.getTags());
        }
        for (FilledSurveyDto filledSurvey : research.getSubmittedSurveys()) {
            for (SurveyQuestionDto question : filledSurvey.getQuestions()) {
                if (tags.containsKey(question.getId())) {
                    Set<String> questionTags = tags.get(question.getId());
                    question.getTags().addAll(questionTags);
                }
            }
        }
    }

    private void includeGroups(ResearchDto research) {
        List<String> groupNames = sf.getCurrentSession()
                .getNamedQuery(StudentGroupDto.Query.GET_GROUP_NAMES_IN_RESEARCH)
                .setParameter("researchId", research.getId()).list();
        research.getGroups().addAll(groupNames);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ResearchDto> queryResearches(ResearchesQuery query) {
        List<Long> availableResearches = findResearchesOfCurrentUser();

        List<ResearchDto> researches = new ArrayList<ResearchDto>();
        if (!availableResearches.isEmpty()) {
            researches.addAll(findMatchingResearches(availableResearches, query));
        }

        for (ResearchDto researchDto : researches) {
            researchDto.setQuestions(Collections.EMPTY_LIST);
            researchDto.getTemplateDto().setQuestions(Collections.EMPTY_LIST);
            researchDto.setSubmittedSurveys(Collections.EMPTY_LIST);
        }

        return researches;
    }

    private List<ResearchDto> findMatchingResearches(List<Long> usersResearches, ResearchesQuery queryCriteria) {
        final Session session = sf.getCurrentSession();
        final Criteria criteria = session.createCriteria(ResearchDto.class);
        narrowSearch(criteria, usersResearches, queryCriteria);
        return criteria.list();
    }

    private void narrowSearch(final Criteria criteria, List<Long> usersResearches, ResearchesQuery queryCriteria) {
        narrowToUsersResearches(criteria, usersResearches);
        if (areCriteriaSpecified(queryCriteria)) {
            addNameRestrictionsIfSpecified(queryCriteria, criteria);
            addTemplateRestrictionsIfSpecified(queryCriteria, criteria);
        }
    }

    private void narrowToUsersResearches(final Criteria criteria, List<Long> availableResearches) {
        criteria.add(Restrictions.in("id", availableResearches));
    }

    private boolean areCriteriaSpecified(ResearchesQuery query) {
        return query != null;
    }

    private void addTemplateRestrictionsIfSpecified(ResearchesQuery query, final Criteria criteria) {
        final Long templateId = query.getSurveyTemplateId();
        if (templateId != null && templateId != 0) {
            criteria.add(Restrictions.eq("templateDto.id", templateId));
        }
    }

    private void addNameRestrictionsIfSpecified(ResearchesQuery query, final Criteria criteria) {
        final String researchName = query.getName();
        if (StringUtils.hasText(researchName)) {
            criteria.add(Restrictions.ilike("name", researchName, MatchMode.ANYWHERE));
        }
    }

    private List<Long> findResearchesOfCurrentUser() {
        Query q = sf.getCurrentSession().getNamedQuery(Research.Queries.LIST_RESEARCHES);
        q.setParameter("evaluatorId", authenticationService.getCurrentUser().getId());
        return q.list();
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<EvaluationActivityDto> queryActivities(ActivitiesQuery query) {
        return activityDao.query(query, authenticationService.getCurrentUser());
    }
}
