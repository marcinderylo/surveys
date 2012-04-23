package org.adaptiveplatform.surveys.acceptance;

import com.google.common.collect.Lists;
import org.adaptiveplatform.surveys.ContainerEnabledTest;
import org.adaptiveplatform.surveys.application.EvaluationDao;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureBuilder;
import org.adaptiveplatform.surveys.dto.*;
import org.adaptiveplatform.surveys.exception.NoSuchResearchException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.openQuestion;
import static org.adaptiveplatform.surveys.builders.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.evaluator;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Rafał Jamróz
 * @author Marcin Deryło
 */
public class ResearchesManagementAcceptanceTest extends ContainerEnabledTest {

    private static final String ANOTHER_EVALUATOR_EMAIL = "anothereval@gmail.com";
    @Resource
    private CoreFixtureBuilder users;
    @Resource
    private SurveysFixtureBuilder surveys;

    @Resource
    private EvaluationDao dao;

    private Long template1Id, template2Id, template3Id;
    private Long research1Id, research2Id, research3Id;
    private List<ResearchDto> researches;

    /**
     * TODO researches should be created for groups.
     */
    @Before
    public void setupInitialData() throws Exception {
        users.createUser(evaluator(ANOTHER_EVALUATOR_EMAIL));

        users.loginAsEvaluator();
        template1Id = surveys.createTemplate(template("template1").withQuestions(openQuestion("question 1.1")));
        research1Id = surveys.createResearch(research().withName("Test research").withSurvey(template1Id));

        // needed to ensure that those templates get created at different time
        // required for shouldListLatestEvaluationActivities test to make any
        // sense
        Thread.sleep(2);

        users.loginAs(ANOTHER_EVALUATOR_EMAIL);
        template2Id = surveys.createTemplate(template("template2").withQuestions(openQuestion("question 2.1")));
        Thread.sleep(2);
        template3Id = surveys.createTemplate(template("template3").withQuestions(openQuestion("question 3.1")));
        Thread.sleep(2);
        research2Id = surveys.createResearch(research().withName("Another research").withSurvey(template2Id));
        research3Id = surveys.createResearch(research().withName("Another test research").withSurvey(template3Id));
    }

    @Test
    public void shouldFindResearchesByName() throws Exception {
        users.loginAsEvaluator();
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
    public void shouldListResearchesOfCurrentUserWithNoRestrictions() throws Exception {
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
        users.loginAsEvaluator();
        // when
        whenReadingSpecificResearch(research1Id);
        // then
        ResearchDto research = shouldFindOneResearch(template1Id);
        assertResearchName(research, "Test research");

        QuestionEvaluationDto question = getOnlyElement(research.getQuestions());
        assertThat(question.getDefinedTags()).isEmpty();
        assertThat(question.getSearchPhrases()).isEmpty();
        assertThat(research.getSubmittedSurveys()).isEmpty();
    }

    @Test(expected = NoSuchResearchException.class)
    public void shouldNotAllowGettingResearchesOfOtherUsers() throws Exception {
        users.loginAsEvaluator();
        // when
        whenReadingSpecificResearch(research3Id);
        // then - exception should be thrown
    }

    @Test
    public void shouldListLatestEvaluationActivities() throws Exception {
        // given
        ActivitiesQuery query = new ActivitiesQuery();
        // when
        final List<EvaluationActivityDto> activities = dao.queryActivities(query);
        // then
        assertThat(activities).hasSize(4);
        assertActivity(activities.get(0), research3Id, "Another test research", ActivityTypeEnum.RESEARCH);
        assertActivity(activities.get(1), research2Id, "Another research", ActivityTypeEnum.RESEARCH);
        assertActivity(activities.get(2), template3Id, "template3", ActivityTypeEnum.SURVEY_TEMPLATE);
        assertActivity(activities.get(3), template2Id, "template2", ActivityTypeEnum.SURVEY_TEMPLATE);
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
        assertThat(researches).hasSize(1);
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
        assertThat(researches).hasSize(expectedResultsCount);
    }

    private void expectNonDetailedResults() {
        for (ResearchDto researchDto : researches) {
            assertThat(researchDto.getQuestions()).isEmpty();
            assertThat(researchDto.getTemplateDto().getQuestions()).isEmpty();
        }
    }

    private void whenReadingSpecificResearch(Long researchId) {
        final ResearchDto research = dao.get(researchId);
        if (research != null) {
            researches = Lists.newArrayList(research);
        }
    }

    private void assertActivity(EvaluationActivityDto item, Long expectedId, String expectedName,
                                ActivityTypeEnum expectedType) {
        Assert.assertEquals("Activity description", expectedName, item.getName());
        Assert.assertEquals("Activity type", expectedType, item.getType());
        Assert.assertEquals("Activity ID", expectedId, item.getId());
    }
}
