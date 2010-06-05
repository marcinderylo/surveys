package org.adaptiveplatform.surveys.application;

import static java.util.Arrays.asList;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.openQuestion;
import static org.adaptiveplatform.surveys.domain.StudentGroupBuilder.group;
import static org.adaptiveplatform.surveys.domain.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.domain.UserAccountBuilder.user;
import static org.adaptiveplatform.surveys.utils.Collections42.asInts;
import static org.testng.Assert.assertEqualsNoOrder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class PublishedSurveysQueryingTest extends AbstractTestNGSpringContextTests {

    private static final String EVALUATOR_LOGIN = "eval@eval.com";
    private static final String STUDENT_LOGIN = "student@eval.com";
    @Resource
    private AuthenticationServiceMock authentication;
    @Resource
    private SurveyDao dao;
    @Resource
    private SurveyTestFixtureBuilder fixture;
    @Resource
    private CoreTestFixtureBuilder coreFixture;
    @Resource
    private StudentGroupTestFixtureBuilder groupsFixture;
    private Long evaluatorId;
    private Long studentId;
    private Long[] groupIds;
    private Long[] templateIds;
    private Set<Long> readTemplateIds = new HashSet<Long>();

    @BeforeMethod
    public void beforeEveryMethodExecutes() throws Exception {
        UserAccount evaluator = coreFixture.createUser(user().withEmail(
                EVALUATOR_LOGIN).inRoles(Role.USER, Role.EVALUATOR));
        evaluatorId = evaluator.getId();

        UserAccount student = coreFixture.createUser(user().withEmail(
                STUDENT_LOGIN).inRoles(Role.USER));
        studentId = student.getId();

        UserAccount teacher = coreFixture.createUser(user().withEmail(
                "teacher@adapt.com").inRoles(Role.TEACHER));

        templateIds = new Long[3];
        groupIds = new Long[3];
        readTemplateIds.clear();
        for (int i = 0; i < 3; i++) {
            SurveyTemplate template = fixture.createTemplate(template(
                    "template" + i).withQuestions(
                    openQuestion("question")).byUser(evaluator));
            templateIds[i] = template.getId();

            groupIds[i] = groupsFixture.createGroup(group("group" + i, teacher).
                    withEvaluators(evaluator).withStudents(student)).getId();
            fixture.publishTemplate(templateIds[i], groupIds[i]);
        }
    }

    @AfterMethod
    public void afterEveryMethodsExecute() throws Exception {
        authentication.logout();

        fixture.cleanUp();
        groupsFixture.cleanUp();
        coreFixture.cleanUp();
    }

    @Test
    public void evaluatorShouldBeAbleToQueryPublishedTemplatesFromASingleGroup()
            throws Exception {
        givenEvaluatorIsLoggedIn();
        whenQueriesFollowingGroupsForPublishedTemplatesAsEvaluator(groupIds[0]);
        thenFollowingPublishedTemplatesAreRead(templateIds[0]);
    }

    @Test
    public void evaluatorShouldBeAbleToQueryPublishedTemplatesFromMultipleGroups()
            throws Exception {
        givenEvaluatorIsLoggedIn();
        whenQueriesFollowingGroupsForPublishedTemplatesAsEvaluator(groupIds[0],
                groupIds[1]);
        thenFollowingPublishedTemplatesAreRead(templateIds[0], templateIds[1]);
    }

    @Test
    public void evaluatorShouldBeAbleToQueryPublishedTemplatesFromAllHisGroups()
            throws Exception {
        givenEvaluatorIsLoggedIn();
        whenQueriesAllGroupsForPublishedTemplatesAsEvaluator();
        thenFollowingPublishedTemplatesAreRead(templateIds);
    }

    @Test
    public void studentShouldBeAbleToQueryPublishedTemplatesFromASingleGroup()
            throws Exception {
        givenStudentIsLoggedIn();
        whenQueriesFollowingGroupsForPublishedTemplatesAsStudent(groupIds[0]);
        thenFollowingPublishedTemplatesAreRead(templateIds[0]);
    }

    @Test
    public void studentShouldBeAbleToQueryPublishedTemplatesFromMultipleGroups()
            throws Exception {
        givenStudentIsLoggedIn();
        whenQueriesFollowingGroupsForPublishedTemplatesAsStudent(groupIds[1],
                groupIds[2]);
        thenFollowingPublishedTemplatesAreRead(templateIds[1], templateIds[2]);
    }

    @Test
    public void studentShouldBeAbleToQueryPublishedTemplatesFromAllGroups()
            throws Exception {
        givenStudentIsLoggedIn();
        whenQueriesAllGroupsForPublishedTemplatesAsStudent();
        thenFollowingPublishedTemplatesAreRead(templateIds);
    }

    private void givenEvaluatorIsLoggedIn() {
        authentication.authenticate(evaluatorId, EVALUATOR_LOGIN,
                Role.USER, Role.EVALUATOR);
    }

    private void givenStudentIsLoggedIn() {
        authentication.authenticate(studentId, STUDENT_LOGIN,
                Role.USER);
    }

    private void whenQueriesFollowingGroupsForPublishedTemplatesAsEvaluator(
            Long... groupIds) {
        queryPublishedTemplates(groupIds, GroupRoleEnum.EVALUATOR);
    }

    private void whenQueriesAllGroupsForPublishedTemplatesAsEvaluator() {
        whenQueriesFollowingGroupsForPublishedTemplatesAsEvaluator();
    }

    private void whenQueriesFollowingGroupsForPublishedTemplatesAsStudent(
            Long... groupIds) {
        queryPublishedTemplates(groupIds, GroupRoleEnum.STUDENT);

    }

    private void whenQueriesAllGroupsForPublishedTemplatesAsStudent() {
        whenQueriesFollowingGroupsForPublishedTemplatesAsStudent();
    }

    private void thenFollowingPublishedTemplatesAreRead(Long... templateIds) {
        System.out.println("Expected " + Arrays.toString(templateIds)
                + " but found " + Arrays.toString(readTemplateIds.toArray()));
        assertEqualsNoOrder(readTemplateIds.toArray(), templateIds);

    }

    private void queryPublishedTemplates(Long[] groupIds, GroupRoleEnum role) {
        PublishedSurveyTemplateQuery query =
                createQueryForGroups(groupIds,
                role);
        final List<PublishedSurveyTemplateDto> templates =
                dao.queryPublishedTemplates(query);
        System.out.println("Read following templates: ");
        for (PublishedSurveyTemplateDto template : templates) {
            readTemplateIds.add(template.getTemplateId());
            System.out.println(template);
        }
    }

    private PublishedSurveyTemplateQuery createQueryForGroups(Long[] groupIds,
            GroupRoleEnum role) {
        PublishedSurveyTemplateQuery query =
                new PublishedSurveyTemplateQuery();
        if (groupIds.length > 0) {
            query.getGroupIds().addAll(asInts(asList(groupIds)));
        }
        query.setRunAs(role);
        return query;
    }
}
