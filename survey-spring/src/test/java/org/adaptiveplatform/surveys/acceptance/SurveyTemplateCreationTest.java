package org.adaptiveplatform.surveys.acceptance;

import org.adaptiveplatform.surveys.ContainerEnabledTest;
import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureBuilder;
import org.adaptiveplatform.surveys.dto.SurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.SurveyTemplateQuery;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.adaptiveplatform.surveys.builders.AnswerBuilder.answer;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.*;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author Rafał Jamróz
 * @author Marcin Deryło
 */
public class SurveyTemplateCreationTest extends ContainerEnabledTest {

    @Resource
    private SurveyDao dao;
    @Resource
    private SurveysFixtureBuilder surveys;
    @Resource
    private CoreFixtureBuilder users;

    @Before
    public void beforeMethod() throws Exception {
        users.loginAsEvaluator();
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
