package org.adaptiveplatform.surveys.sampledata;

import static com.google.common.collect.Iterables.get;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.EvaluationDao;
import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.application.UserDao;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.dto.ResearchDto;
import org.adaptiveplatform.surveys.dto.SurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.SurveyTemplateQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/testConfigurationContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SurveysSampleDataTest {

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

    @Before
    public void beforeEachMethod() throws Exception {
        sampleData.addSampleSurveysData();
        users.logout();
    }

    @Test
    public void shouldUsersExist() throws Exception {
        users.loginAsAdmin();
        userDao.getByEmail("evaluator@gmail.com");
        userDao.getByEmail("student@gmail.com");
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
