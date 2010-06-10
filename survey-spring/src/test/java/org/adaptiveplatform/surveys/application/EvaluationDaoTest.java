
package org.adaptiveplatform.surveys.application;

import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.openQuestion;
import static org.adaptiveplatform.surveys.domain.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.domain.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.domain.UserAccountBuilder.user;
import static org.adaptiveplatform.surveys.test.Asserts.assertCollectionSize;
import static org.adaptiveplatform.surveys.test.Asserts.assertEmpty;
import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.adaptiveplatform.surveys.utils.Collections42.firstOf;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.CoreTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.ResearchTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.StudentGroupTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.domain.SurveyTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.dto.ActivitiesQuery;
import org.adaptiveplatform.surveys.dto.ActivityTypeEnum;
import org.adaptiveplatform.surveys.dto.EvaluationActivityDto;
import org.adaptiveplatform.surveys.dto.ResearchDto;
import org.adaptiveplatform.surveys.dto.ResearchesQuery;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.NoSuchResearchException;
import org.adaptiveplatform.surveys.service.AuthenticationServiceMock;
import org.adaptiveplatform.surveys.test.UserAccountToDto;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 *
 * @author Marcin Deryło
 */
@ContextConfiguration("classpath:/testConfigurationContext.xml")
public class EvaluationDaoTest extends AbstractTestNGSpringContextTests {

    @Resource
    private EvaluationDao dao;
    @Resource
    private SurveyTestFixtureBuilder surveyFixture;
    @Resource
    private CoreTestFixtureBuilder userFixture;
    @Resource
    private ResearchTestFixtureBuilder researchFixture;
    @Resource
    private AuthenticationServiceMock authentication;
    @Resource
    private StudentGroupTestFixtureBuilder groupFixture;
    //
    private Long template1Id, template2Id;
    private Long research1Id, research2Id, research3Id;
    private UserDto evaluator;
    private List<ResearchDto> researches;

    @BeforeMethod
    public void init() throws Exception {
        UserAccount user1 = userFixture.createUser(user("eval1", "eval",
                "eval1@adapt.com", Role.EVALUATOR));
        evaluator = UserAccountToDto.INSTANCE.apply(user1);
        UserAccount user2 = userFixture.createUser(user("eval2", "eval",
                "eval2@adapt.com", Role.EVALUATOR));

        SurveyTemplate template1 =
                surveyFixture.createTemplate(template("template1").byUser(user1).
                withQuestions(openQuestion("question 1.1")));
        template1Id = template1.getId();
        // needed to ensure that those templates get created at different time
        // required for shouldListLatestEvaluationActivities test to make any
        // sense
        Thread.sleep(2);
        SurveyTemplate template2 =
                surveyFixture.createTemplate(template("template2").byUser(user1).
                withQuestions(openQuestion("question 2.1")));
        template2Id = template2.getId();
        Thread.sleep(2);

        SurveyTemplate template3 =
                surveyFixture.createTemplate(template("template3").byUser(user2).
                withQuestions(openQuestion("question 3.1")));

        research1Id = researchFixture.createResearch(research("Test research",
                template1)).getId();
        Thread.sleep(2);
        research2Id = researchFixture.createResearch(research("Another research",
                template2)).getId();
        research3Id = researchFixture.createResearch(research("Another test "
                + "research", template3)).getId();

        authentication.authenticate(user1.getId(), "eval1@adapt.com",
                Role.EVALUATOR);
    }

    @AfterMethod
    public void cleanUp() {
        authentication.logout();
        researchFixture.cleanUp();
        surveyFixture.cleanUp();
        groupFixture.cleanUp();
        userFixture.cleanUp();
        if (researches != null) {
            researches.clear();
        }
    }

    @Test
    public void shouldFindResearchesByName() throws Exception {
        // when
        whenQueryingResearchesHavingNameLike("t res");
        // then
        ResearchDto research = shouldFindOneResearch(template1Id);
        assertResearchName(research, "Test research");
    }

    @Test
    public void shouldFindResearchesByTemplateId() throws Exception {
        // when
        whenQueryingResearchesForTemplate(template2Id);
        // then
        ResearchDto research = shouldFindOneResearch(template2Id);
        assertResearchName(research, "Another research");
    }

    @Test
    public void shouldListResearchesOfCurrentUserWithNoRestrictions() throws
            Exception {
        // given
        whenQueryingResearchesWithoutRestrictions();
        // then
        shouldFindResearches(2);
        expectNonDetailedResults();
    }

    @Test
    public void shouldListResearchesOfCurrentUserWhenUsingNullQueryObject() {
        // given
        queryResearches(null);
        // then
        shouldFindResearches(2);
        expectNonDetailedResults();
    }

    @Test
    public void shouldGetResearchWithDetailsById() throws Exception {
        // when
        whenReadingSpecificResearch(research1Id);
        // then
        ResearchDto research = shouldFindOneResearch(template1Id);
        assertResearchName(research, "Test research");

        assertCollectionSize(research.getQuestions(), 1);
        assertEmpty(firstOf(research.getQuestions()).getDefinedTags());
        assertEmpty(firstOf(research.getQuestions()).getSearchPhrases());
        assertEmpty(research.getSubmittedSurveys());
    }

    @Test(expectedExceptions = NoSuchResearchException.class)
    public void shouldNotAllowGettingResearchesOfOtherUsers() throws Exception {
        // when
        whenReadingSpecificResearch(research3Id);
        // then
        expectException();
    }

    @Test
    public void shouldListLatestEvaluationActivities() throws Exception {
        // given
        ActivitiesQuery query = new ActivitiesQuery();
        // when
        final List<EvaluationActivityDto> activities =
                dao.queryActivities(query);
        // then
        assertCollectionSize(activities, 4);
        assertActivity(activities.get(0), research2Id, "Another research",
                ActivityTypeEnum.RESEARCH);
        assertActivity(activities.get(1), research1Id, "Test research",
                ActivityTypeEnum.RESEARCH);
        assertActivity(activities.get(2), template2Id, "template2",
                ActivityTypeEnum.SURVEY_TEMPLATE);
        assertActivity(activities.get(3), template1Id, "template1",
                ActivityTypeEnum.SURVEY_TEMPLATE);
    }

    private void whenQueryingResearchesHavingNameLike(String namePattern) {
        ResearchesQuery query = new ResearchesQuery();
        query.setName(namePattern);
        queryResearches(query);
    }

    private void queryResearches(ResearchesQuery query) {
        researches = dao.queryResearches(query);
    }

    private ResearchDto shouldFindOneResearch(Long templateId) {
        assertCollectionSize(researches, 1);
        ResearchDto dto = researches.get(0);
        Assert.assertNotNull(dto);
        Assert.assertEquals(dto.getTemplateDto().getId(), templateId);
        return dto;
    }

    private void assertResearchName(ResearchDto research, String expectedName) {
        Assert.assertEquals(research.getName(), expectedName);
    }

    private void whenQueryingResearchesForTemplate(Long templateId) {
        ResearchesQuery query = new ResearchesQuery();
        query.setSurveyTemplateId(templateId);
        queryResearches(query);
    }

    private void whenQueryingResearchesWithoutRestrictions() {
        queryResearches(new ResearchesQuery());
    }

    private void shouldFindResearches(int expectedResultsCount) {
        assertCollectionSize(researches, expectedResultsCount);
    }

    private void expectNonDetailedResults() {
        for (ResearchDto researchDto : researches) {
            assertEmpty(researchDto.getQuestions());
            assertEmpty(researchDto.getTemplateDto().getQuestions());
        }
    }

    private void whenReadingSpecificResearch(Long researchId) {
        final ResearchDto research = dao.get(researchId);
        if (research != null) {
            researches = Lists.newArrayList(research);
        }
    }

    private void assertActivity(EvaluationActivityDto item, Long expectedId,
            String expectedName, ActivityTypeEnum expectedType) {
        Assert.assertEquals(item.getName(), expectedName, "Activity description");
        Assert.assertEquals(item.getType(), expectedType, "Activity type");
        Assert.assertEquals(item.getId(), expectedId, "Activity ID");
    }
}
