package org.adaptiveplatform.surveys.sampledata;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.application.UserDao;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.dto.SurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.SurveyTemplateQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Iterables;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/testConfigurationContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SurveysSampleDataTest {

	@Resource
	private SurveysSampleData sampleData;

	@Resource
	private CoreFixtureBuilder fixture;

	@Resource
	private UserDao userDao;

	@Resource
	private SurveyDao surveysDao;

	@Before
	public void beforeEachMethod() throws Exception {
		sampleData.addSampleSurveysData();
		fixture.logout();
	}

	@Test
	public void shouldUsersExist() throws Exception {
		fixture.loginAsAdmin();
		userDao.getByEmail("evaluator@gmail.com");
		userDao.getByEmail("student@gmail.com");
	}

	@Test
	public void shouldSurveysExist() throws Exception {
		fixture.loginAs("evaluator@gmail.com", "evaluator");
		SurveyTemplateDto template = Iterables.getOnlyElement(surveysDao.queryTemplates(new SurveyTemplateQuery()));
		Assert.assertEquals("this is a sample sample survey", template.getDescription());
	}
}
