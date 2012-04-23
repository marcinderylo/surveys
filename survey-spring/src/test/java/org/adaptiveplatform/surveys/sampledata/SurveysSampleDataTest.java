package org.adaptiveplatform.surveys.sampledata;

import org.adaptiveplatform.surveys.ContainerEnabledTest;
import org.adaptiveplatform.surveys.application.EvaluationDao;
import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.application.UserDao;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.dto.ResearchDto;
import org.adaptiveplatform.surveys.dto.SurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.SurveyTemplateQuery;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.List;

import static com.google.common.collect.Iterables.get;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@ActiveProfiles({"development", "test"})
public class SurveysSampleDataTest extends ContainerEnabledTest {

    @Resource
    private SurveysSampleData sampleData;

    @Resource
    private CoreFixtureBuilder users;

    @Resource
    private UserDao userDao;

    @Resource
    private SurveyDao surveysDao;

    @Resource
    private EvaluationDao evaluationDao;

    @Test
    public void shouldUsersExist() throws Exception {
        users.loginAsAdmin();
        assertUserExists("evaluator@gmail.com");
        assertUserExists("student@gmail.com");
    }

    private void assertUserExists(String email) {
        UserDto user = userDao.getByEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void shouldSurveysExist() throws Exception {
        users.loginAsEvaluator();
        List<SurveyTemplateDto> templates = surveysDao.queryTemplates(new SurveyTemplateQuery());
        assertThat(templates).isNotEmpty();
    }

    @Test
    public void shouldResearchesExist() throws Exception {
        // given
        users.loginAsEvaluator();
        ResearchDto research = evaluationDao.get(get(sampleData.getResearchIds(), 0));
        // then
        assertThat(research.getSubmittedSurveys()).isNotEmpty();
    }
}
