package org.adaptiveplatform.surveys.acceptance;

import org.adaptiveplatform.surveys.ContainerEnabledTest;
import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureCreator;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateQuery;
import org.adaptiveplatform.surveys.dto.SurveyStatusEnum;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.adaptiveplatform.surveys.builders.AnswerBuilder.answer;
import static org.adaptiveplatform.surveys.builders.CoreFixtureBuilder.EVALUATOR_EMAIL;
import static org.adaptiveplatform.surveys.builders.CoreFixtureBuilder.STUDENT_EMAIL;
import static org.adaptiveplatform.surveys.builders.GroupBuilder.group;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.multiChoiceQuestion;
import static org.adaptiveplatform.surveys.builders.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.junit.Assert.*;

public class PublishedSurveyTemplateStatusTest extends ContainerEnabledTest {

    @Resource
    private SurveyDao dao;
    @Resource
    private SurveysFixtureCreator surveys;
    @Resource
    private CoreFixtureBuilder users;

    private Long filledPublicationId;
    private Long notFilledPublicationId;

    @Before
    public void havingTwoPublicationsWithSameGroupAndSurveyTemplate() {
        users.loginAsTeacher();
        Long groupId = surveys.createGroup(group("test group").withStudent(STUDENT_EMAIL)
                .withEvaluator(EVALUATOR_EMAIL));

        users.loginAsEvaluator();
        Long templateId = surveys.createTemplate(template("some survey").withQuestions(
                multiChoiceQuestion("another not published question").withAnswers(answer("dobrze"))));

        filledPublicationId = surveys.createResearch(research().withSurvey(templateId).forGroup(groupId));
        notFilledPublicationId = surveys.createResearch(research().withSurvey(templateId).forGroup(groupId));

        users.loginAsStudent();
        surveys.fillAndSubmitSurvey(filledPublicationId, groupId);
    }

    /**
     * This is a regression test for following bug: when a student queries for
     * fillable survey templates the published survey template is marked as
     * filled is there's a filled survey for this user with the same group &
     * template id. Currently, with researches, it's possible that multiple
     * published survey templates have same group & survey template IDs and each
     * of them should have it's status calculated separately.
     */
    @Test
    public void shouldCalculatePublishedSurveyTemplateStatusForConcretePublication() {
        final List<PublishedSurveyTemplateDto> fillablePublications = dao
                .queryPublishedTemplates(new PublishedSurveyTemplateQuery(GroupRoleEnum.STUDENT));
        assertPublicationIsNotInTheList(fillablePublications, filledPublicationId);
        assertPublicationIsNotFilled(fillablePublications, notFilledPublicationId);
    }

    private void assertPublicationIsNotInTheList(List<PublishedSurveyTemplateDto> publications, Long publicationId) {
        assertNull(selectPublication(publicationId, publications));
    }

    private void assertPublicationIsNotFilled(List<PublishedSurveyTemplateDto> publications, Long publicationId) {
        assertPublicationStatus(publications, publicationId, SurveyStatusEnum.PENDING);
    }

    private void assertPublicationStatus(List<PublishedSurveyTemplateDto> publications, Long publicationId,
                                         SurveyStatusEnum expectedStatus) {
        PublishedSurveyTemplateDto publication = selectPublication(publicationId, publications);
        if (publication == null) {
            fail("No such published survey template (ID=" + publicationId + ")");
        }
        assertEquals("Published survey template status", expectedStatus, publication.getStatus());
    }

    private PublishedSurveyTemplateDto selectPublication(Long publicationId,
                                                         List<PublishedSurveyTemplateDto> publications) {
        for (PublishedSurveyTemplateDto publication : publications) {
            if (publicationId.equals(publication.getId())) {
                return publication;
            }
        }
        return null;
    }
}
