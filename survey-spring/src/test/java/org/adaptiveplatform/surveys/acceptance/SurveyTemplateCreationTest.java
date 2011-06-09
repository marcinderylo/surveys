package org.adaptiveplatform.surveys.acceptance;

import static org.adaptiveplatform.surveys.builders.AnswerBuilder.answer;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.multiChoiceQuestion;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.openQuestion;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.singleChoiceQuestion;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.evaluator;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.application.SurveyFacade;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureBuilder;
import org.adaptiveplatform.surveys.dto.SurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.SurveyTemplateQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Rafał Jamróz
 * @author Marcin Deryło
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/testConfigurationContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SurveyTemplateCreationTest {

    public static final String EVALUATOR_LOGIN = "eval@eval.com";
    @Resource
    private SurveyFacade facade;
    @Resource
    private SurveyDao dao;
    @Resource
    private SurveysFixtureBuilder surveys;
    @Resource
    private CoreFixtureBuilder users;

    @Before
    public void beforeMethod() throws Exception {
        users.createUser(evaluator(EVALUATOR_LOGIN).withPassword("s3cr3t"));
        users.loginAs(EVALUATOR_LOGIN, "s3cr3t");
    }

    @Test
    public void evaluatorShouldCreateSurveyTemplate() throws Exception {
        // given
        surveys.createTemplate(template("test survey").withQuestions(
                openQuestion("an open question"),//
                multiChoiceQuestion("a multiple choice question").withAnswers(answer("foo"), answer("bar")),
                singleChoiceQuestion("a single choice question").withAnswers(answer("baz"), answer("foobar"))));
        // when
        List<SurveyTemplateDto> templates = dao.queryTemplates(new SurveyTemplateQuery());
        // then
        assertThat(templates).hasSize(1);
    }

    @Test
    public void evaluatorShouldBeAbleToProvideDescriptionForSurveyTemplate() throws Exception {
        // given
        Long templateId = surveys.createTemplate(template("test survey").withDescription("a sample survey template")
                .withQuestions(openQuestion("an open question")));//
        // when
        SurveyTemplateDto template = dao.getTemplate(templateId);
        // then
        assertEquals("a sample survey template", template.getDescription());
    }
}
