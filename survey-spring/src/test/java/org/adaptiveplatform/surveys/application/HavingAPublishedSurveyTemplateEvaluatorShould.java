package org.adaptiveplatform.surveys.application;

import static org.adaptiveplatform.surveys.domain.AnswerTemplateBuilder.answer;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.multipleChoiceQuestion;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.openQuestion;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.singleChoiceQuestion;
import static org.adaptiveplatform.surveys.domain.StudentGroupBuilder.group;
import static org.adaptiveplatform.surveys.domain.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.domain.UserAccountBuilder.user;
import static org.adaptiveplatform.surveys.test.Asserts.assertCollectionSize;
import static org.adaptiveplatform.surveys.test.Asserts.assertEmpty;
import static org.adaptiveplatform.surveys.test.Asserts.expectException;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.CoreTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.StudentGroupTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.domain.SurveyTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateQuery;
import org.adaptiveplatform.surveys.exception.SurveyTemplateAlreadyPublishedException;
import org.adaptiveplatform.surveys.service.AuthenticationServiceMock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 * @author Marcin Deryło
 */
@ContextConfiguration("classpath:/testConfigurationContext.xml")
public class HavingAPublishedSurveyTemplateEvaluatorShould extends AbstractTestNGSpringContextTests {

    /**
     * Login (email) of a user with evaluator privileges.
     */
    public static final String EVALUATOR_LOGIN = "eval@eval.com";
    @Resource
    private AuthenticationServiceMock authentication;
    @Resource
    private SurveyDao dao;
    @Resource
    private SurveyTestFixtureBuilder fixture;
    @Resource
    private CoreTestFixtureBuilder coreFixture;
    @Resource
    private StudentGroupFacade groupFacade;
    @Resource
    private SurveyFacade surveyFacade;
    @Resource
    private StudentGroupTestFixtureBuilder groupsFixture;
    private Long groupId;
    private Long existingTemplateId;
    private Long publicationId;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        UserAccount evaluator = coreFixture.createUser(user().withEmail(
                EVALUATOR_LOGIN).inRoles(Role.USER, Role.EVALUATOR));

        SurveyTemplate template = fixture.createTemplate(template(
                "a test template").withQuestions(
                /*multiple choice question (nr 1)*/
                multipleChoiceQuestion("multi-choice question").
                withAnswers(answer("answer 1"), answer("answer 2"),
                answer("other: ...").requiresComment(),
                answer("none of the above").disallowsOtherAnswers()),
                /* single choice question (nr 2)*/
                singleChoiceQuestion("single-choice question").
                withAnswers(answer("option 1").disallowsOtherAnswers(),
                answer("option 2").disallowsOtherAnswers(),
                answer("other: ...").requiresComment()),
                /* open question (nr 3) */
                openQuestion("open question")).byUser(evaluator));
        existingTemplateId = template.getId();

        UserAccount teacher = coreFixture.createUser(user().withEmail(
                "teacher@umcs.com.pl").inRoles(Role.TEACHER));

        groupId = groupsFixture.createGroup(group("test group", teacher).
                withEvaluators(evaluator)).getId();

        publicationId = fixture.publishTemplate(existingTemplateId, groupId).
                getId();

        authentication.authenticate(evaluator.getId(), EVALUATOR_LOGIN,
                Role.USER, Role.EVALUATOR);
    }

    @Test
    public void beAbleToQueryHisPublications() throws
            Exception {
        // when
        PublishedSurveyTemplateQuery query =
                new PublishedSurveyTemplateQuery();
        query.setRunAs(GroupRoleEnum.EVALUATOR);

        List<PublishedSurveyTemplateDto> publications = dao.
                queryPublishedTemplates(query);
        // then
        assertCollectionSize(publications, 1);
    }

    @Test
    public void beAbleToRemoveThePublication() throws
            Exception {
        // disable cleanup for entity being deleted
        fixture.dontDeletePublication(existingTemplateId, groupId);
        // when
        groupFacade.removeSurveyTemplate(publicationId);
        // then
        PublishedSurveyTemplateQuery query =
                new PublishedSurveyTemplateQuery();
        query.setRunAs(GroupRoleEnum.EVALUATOR);
        assertEmpty(dao.queryPublishedTemplates(query));
    }

    @Test(expectedExceptions = {SurveyTemplateAlreadyPublishedException.class})
    public void notBeAbleToRemoveSurveyTemplate() throws Exception {
        surveyFacade.removeSurveyTemplate(existingTemplateId);
        expectException();
    }

    @AfterMethod
    public void afterMethod() throws Exception {
        authentication.logout();
        fixture.cleanUp();
        groupsFixture.cleanUp();
        coreFixture.cleanUp();
    }
}
