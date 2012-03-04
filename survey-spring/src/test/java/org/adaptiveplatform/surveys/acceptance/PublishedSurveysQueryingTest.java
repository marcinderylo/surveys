package org.adaptiveplatform.surveys.acceptance;

import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureBuilder;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateQuery;
import org.adaptiveplatform.surveys.utils.Collections42;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.adaptiveplatform.surveys.builders.CoreFixtureBuilder.EVALUATOR_EMAIL;
import static org.adaptiveplatform.surveys.builders.CoreFixtureBuilder.STUDENT_EMAIL;
import static org.adaptiveplatform.surveys.builders.GroupBuilder.group;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.openQuestion;
import static org.adaptiveplatform.surveys.builders.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Rafał Jamróz
 * @author Marcin Deryło
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/testConfigurationContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PublishedSurveysQueryingTest {
    @Resource
    private SurveyDao dao;
    @Resource
    private SurveysFixtureBuilder surveys;
    @Resource
    private CoreFixtureBuilder users;

    private static final int RESEARCHES_COUNT = 3;
    private Long[] groupIds = new Long[RESEARCHES_COUNT];
    private Long[] templateIds = new Long[RESEARCHES_COUNT];
    private Set<Long> readTemplateIds = new HashSet<Long>();

    @Before
    public void beforeEveryMethodExecutes() throws Exception {
        for (int i = 0; i < RESEARCHES_COUNT; i++) {
            users.loginAsTeacher();
            groupIds[i] = surveys.createGroup(group("group" + i).withEvaluator(EVALUATOR_EMAIL).withStudent(
                    STUDENT_EMAIL));

            users.loginAsEvaluator();
            templateIds[i] = surveys.createTemplate(template("template" + i).withQuestions(openQuestion("question")));
            surveys.createResearch(research().withSurvey(templateIds[i]).forGroup(groupIds[i]));
        }
    }

    @Test
    public void evaluatorShouldBeAbleToQueryPublishedTemplatesFromASingleGroup() throws Exception {
        users.loginAsEvaluator();
        whenQueriesFollowingGroupsForPublishedTemplatesAsEvaluator(groupIds[0]);
        thenFollowingPublishedTemplatesAreRead(templateIds[0]);
    }

    @Test
    public void evaluatorShouldBeAbleToQueryPublishedTemplatesFromMultipleGroups() throws Exception {
        users.loginAsEvaluator();
        whenQueriesFollowingGroupsForPublishedTemplatesAsEvaluator(groupIds[0], groupIds[1]);
        thenFollowingPublishedTemplatesAreRead(templateIds[0], templateIds[1]);
    }

    @Test
    public void evaluatorShouldBeAbleToQueryPublishedTemplatesFromAllHisGroups() throws Exception {
        users.loginAsEvaluator();
        whenQueriesAllGroupsForPublishedTemplatesAsEvaluator();
        thenFollowingPublishedTemplatesAreRead(templateIds);
    }

    @Test
    public void studentShouldBeAbleToQueryPublishedTemplatesFromASingleGroup() throws Exception {
        users.loginAsStudent();
        whenQueriesFollowingGroupsForPublishedTemplatesAsStudent(groupIds[0]);
        thenFollowingPublishedTemplatesAreRead(templateIds[0]);
    }

    @Test
    public void studentShouldBeAbleToQueryPublishedTemplatesFromMultipleGroups() throws Exception {
        users.loginAsStudent();
        whenQueriesFollowingGroupsForPublishedTemplatesAsStudent(groupIds[1], groupIds[2]);
        thenFollowingPublishedTemplatesAreRead(templateIds[1], templateIds[2]);
    }

    @Test
    public void studentShouldBeAbleToQueryPublishedTemplatesFromAllGroups() throws Exception {
        users.loginAsStudent();
        whenQueriesAllGroupsForPublishedTemplatesAsStudent();
        thenFollowingPublishedTemplatesAreRead(templateIds);
    }

    @Test
    public void studentShouldBeAbleToQueryPublishedTemplatesByGroupName() throws Exception {
        users.loginAsStudent();
        whenQueriesPublishedTemplatesAsStudentWithKeyword("up2");
        thenFollowingPublishedTemplatesAreRead(templateIds[2]);
    }

    @Test
    public void studentShouldBeAbleToQueryPublishedTemplatesByTemplateName() throws Exception {
        users.loginAsStudent();
        whenQueriesPublishedTemplatesAsStudentWithKeyword("te1");
        thenFollowingPublishedTemplatesAreRead(templateIds[1]);
    }

    private void whenQueriesFollowingGroupsForPublishedTemplatesAsEvaluator(Long... groupIds) {
        queryPublishedTemplates(groupIds, GroupRoleEnum.EVALUATOR, null);
    }

    private void whenQueriesAllGroupsForPublishedTemplatesAsEvaluator() {
        whenQueriesFollowingGroupsForPublishedTemplatesAsEvaluator();
    }

    private void whenQueriesFollowingGroupsForPublishedTemplatesAsStudent(Long... groupIds) {
        queryPublishedTemplates(groupIds, GroupRoleEnum.STUDENT, null);

    }

    private void whenQueriesAllGroupsForPublishedTemplatesAsStudent() {
        whenQueriesFollowingGroupsForPublishedTemplatesAsStudent();
    }

    private void whenQueriesPublishedTemplatesAsStudentWithKeyword(String keyword) {
        queryPublishedTemplates(groupIds, GroupRoleEnum.STUDENT, keyword);
    }

    private void thenFollowingPublishedTemplatesAreRead(Long... templateIds) {
        assertThat(readTemplateIds).containsOnly((Object[]) templateIds);

    }

    private void queryPublishedTemplates(Long[] groupIds, GroupRoleEnum role, String keyword) {
        PublishedSurveyTemplateQuery query = createQueryForGroups(groupIds, role);
        query.setKeyword(keyword);
        final List<PublishedSurveyTemplateDto> templates = dao.queryPublishedTemplates(query);
        for (PublishedSurveyTemplateDto template : templates) {
            readTemplateIds.add(template.getTemplateId());
        }
    }

    private PublishedSurveyTemplateQuery createQueryForGroups(Long[] groupIds, GroupRoleEnum role) {
        PublishedSurveyTemplateQuery query = new PublishedSurveyTemplateQuery(role);
        if (groupIds.length > 0) {
            query.getGroupIds().addAll(Collections42.asInts(asList(groupIds)));
        }
        return query;
    }
}
