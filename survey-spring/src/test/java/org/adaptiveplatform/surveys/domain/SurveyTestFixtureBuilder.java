package org.adaptiveplatform.surveys.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

@Service
public class SurveyTestFixtureBuilder {

        @Resource
        private SessionFactory sf;
        List<Long> templatesIds = new ArrayList<Long>();
        List<Long> filledSurveysIds = new ArrayList<Long>();
        private Set<Long> publicationIds = new HashSet<Long>();

        public SurveyTemplate createTemplate(SurveyTemplateBuilder builder) {
                Session session = sf.openSession();
                Transaction tx = session.beginTransaction();
                SurveyTemplate template = builder.build();
                Long templateId = (Long) session.save(template);
                tx.commit();
                session.close();
                templatesIds.add(templateId);
                return template;
        }

        public void deleteSurveyTemplate(Long templateId) {
                templatesIds.add(templateId);
        }

        public void deleteFilledSurvey(Long surveyId) {
                filledSurveysIds.add(surveyId);
        }

        public void deletePublication(Long templateId, Long groupId) {
                Long publicationId = getPublicationId(templateId, groupId);
                publicationIds.add(publicationId);
        }

        private Long getPublicationId(Long templateId, Long groupId) {
                Session session = sf.openSession();
                Long publicationId = (Long) session.createCriteria(
                        SurveyPublication.class).
                        add(Restrictions.eq("surveyTemplate.id", templateId)).
                        add(Restrictions.eq("studentGroup.id", groupId)).
                        setProjection(Projections.property("id")).uniqueResult();
                session.close();
                return publicationId;
        }

        public void cleanUp() {
                Session session = sf.openSession();
                Transaction tx = session.beginTransaction();
                for (Long id : filledSurveysIds) {
                        Object survey = session.load(FilledSurvey.class, id);
                        session.delete(survey);
                }
                for (Long id : publicationIds) {
                        Object publ = session.load(SurveyPublication.class, id);
                        session.delete(publ);
                }
                for (Long id : templatesIds) {
                        Object survey = session.load(SurveyTemplate.class, id);
                        session.delete(survey);
                }
                tx.commit();
                session.close();
                templatesIds.clear();
                filledSurveysIds.clear();
                publicationIds.clear();
        }

        public SurveyPublication publishTemplate(SurveyTemplate template1, StudentGroup group) {
                Session session = sf.openSession();
                Transaction tx = session.beginTransaction();
                SurveyPublication publication = new SurveyPublication(template1,
                        group);
                session.persist(publication);
                tx.commit();
                session.close();
                publicationIds.add(publication.getId());
                return publication;
        }

        public SurveyPublication publishTemplate(Long surveyTemplateId, Long groupId) {
                Session session = sf.openSession();
                Transaction tx = session.beginTransaction();
                final SurveyTemplate template =
                        (SurveyTemplate) session.get(SurveyTemplate.class,
                        surveyTemplateId);
                final StudentGroup group = (StudentGroup) session.get(
                        StudentGroup.class, groupId);
                SurveyPublication publication = new SurveyPublication(template,
                        group);
                session.persist(publication);
                tx.commit();
                session.close();
                publicationIds.add(publication.getId());
                return publication;
        }

        public FilledSurvey fillSurvey(Long templateId, Long groupId,
                UserAccount user) {
                Session session = sf.openSession();
                Transaction tx = session.beginTransaction();

                SurveyTemplate template = (SurveyTemplate) session.get(
                        SurveyTemplate.class, templateId);
                UserDto userDto = new UserDto();
                userDto.setId(user.getId());

                SurveyPublication publication = (SurveyPublication) session.
                        createQuery(
                        "FROM SurveyPublication s WHERE s.surveyTemplate.id = :templateId "
                        + "AND s.studentGroup.id = :groupId").setParameter(
                        "templateId",
                        templateId).setParameter("groupId", groupId).
                        uniqueResult();

                FilledSurvey filledSurvey = new FilledSurvey(publication,
                        userDto);
                filledSurvey.startFilling();

                List<QuestionTemplate> questions = template.getQuestions();
                for (int i = 0; i < questions.size(); i++) {
                    QuestionTemplate question = questions.get(i);
                    List<Integer> answerNumbers = new ArrayList<Integer>();
                    if (!question.getAnswers().isEmpty()) {
                            answerNumbers.add(1);
                    }
                    filledSurvey.answerQuestion(i+1, answerNumbers, "some comment");
                }

                filledSurvey.submit();
                filledSurveysIds.add((Long) session.save(filledSurvey));
                tx.commit();
                session.close();
                return filledSurvey;
        }

        public void dontDeleteTemplate(Long existingTemplateId) {
                templatesIds.remove(existingTemplateId);
        }

        public void dontDeletePublication(Long templateId, Long groupId) {
                Long publicationId = getPublicationId(templateId, groupId);
                publicationIds.remove(publicationId);
        }
}
