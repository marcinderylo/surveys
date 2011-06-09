package org.adaptiveplatform.surveys.acceptance;

import static org.adaptiveplatform.surveys.builders.GroupBuilder.group;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.openQuestion;
import static org.adaptiveplatform.surveys.builders.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.evaluator;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.student;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.teacher;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.adaptiveplatform.surveys.application.StudentGroupDao;
import org.adaptiveplatform.surveys.application.StudentGroupFacade;
import org.adaptiveplatform.surveys.application.SurveyFacade;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureBuilder;
import org.adaptiveplatform.surveys.dto.AddGroupMemberCommand;
import org.adaptiveplatform.surveys.dto.ChangeGroupMembersCommand;
import org.adaptiveplatform.surveys.dto.CreateStudentGroupCommand;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.GroupSignUpCommand;
import org.adaptiveplatform.surveys.dto.SetGroupSignUpModeCommand;
import org.adaptiveplatform.surveys.dto.StudentGroupDto;
import org.adaptiveplatform.surveys.dto.StudentGroupQuery;
import org.adaptiveplatform.surveys.exception.CantQueryGroupsAsEvaluatorException;
import org.adaptiveplatform.surveys.exception.CantRemoveSelfFromGroupException;
import org.adaptiveplatform.surveys.exception.DeletingGroupWithPublishedTemplatesException;
import org.adaptiveplatform.surveys.exception.GroupAlreadyExistsException;
import org.adaptiveplatform.surveys.exception.NoSuchGroupException;
import org.adaptiveplatform.surveys.exception.PublishedSurveyTemplateAlreadyFilledException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Marcin Deryło
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/testConfigurationContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class StudentGroupsSystemTest {

    public static final String EXISTING_GROUP_NAME = "some group";
    @Resource
    private StudentGroupFacade groupFacade;
    @Resource
    private StudentGroupDao groupDao;
    @Resource
    private SurveyFacade surveyFacade;
    @Resource
    private CoreFixtureBuilder users;
    @Resource
    private SurveysFixtureBuilder surveys;

    @Before
    public void initializeData() throws Exception {
        users.createUser(student("student@adapt.com"));
        users.createUser(teacher("teacher@adapt.com"));
        users.createUser(teacher("bad_teacher@adapt.com"));
        users.createUser(evaluator("evaluator@adapt.com"));
        users.createUser(evaluator("evaluator2@adapt.com"));

        users.loginAs("teacher@adapt.com");
        surveys.createGroup(group("some group").withEvaluator("evaluator@adapt.com").withStudent("student@adapt.com"));
        surveys.createGroup(group("another group").openForSignup());
        Long groupWithResearch = surveys.createGroup(group("yet another group").withEvaluator("evaluator@adapt.com")
                .withStudent("student@adapt.com").openForSignup());

        users.loginAs("evaluator@adapt.com");
        Long templateId = surveys.createTemplate(template("not published survey").withQuestions(
                openQuestion("qwestion")));
        surveys.createResearch(research().withSurvey(templateId).forGroup(groupWithResearch));
    }

    @Test
    public void shouldTeacherCreateUserGroup() throws Exception {
        authenticatedAsTeacher();
        // when
        Long groupId = groupFacade.createGroup(new CreateStudentGroupCommand("test group"));
        // then
        StudentGroupDto dto = groupDao.getGroup(groupId);
        assertEquals(dto.getGroupName(), "test group");

        assertThat(dto.getAdministrators()).hasSize(1);
        assertThat(dto.getEvaluators()).isEmpty();
        assertThat(dto.getStudents()).isEmpty();
    }

    @Test(expected = AccessDeniedException.class)
    public void cantAllowNonTeacherToCreateGroup() throws Exception {
        // given
        users.loginAs("evaluator@adapt.com");
        // when
        groupFacade.createGroup(new CreateStudentGroupCommand("test group"));
        // then - exception should be thrown
    }

    @Test
    public void shouldAddGroupMembersUponCreation() throws Exception {
        // given
        authenticatedAsTeacher();
        // when
        CreateStudentGroupCommand createGroupCmd = new CreateStudentGroupCommand("test group");
        createGroupCmd.getAddMemberCommands().add(
                new AddGroupMemberCommand("student@adapt.com", GroupRoleEnum.STUDENT.name()));
        createGroupCmd.getAddMemberCommands().add(
                new AddGroupMemberCommand("evaluator@adapt.com", GroupRoleEnum.EVALUATOR.name()));

        Long groupId = groupFacade.createGroup(createGroupCmd);
        // then
        StudentGroupDto dto = groupDao.getGroup(groupId);

        assertThat(dto.getEvaluators()).hasSize(1);
        assertThat(dto.getStudents()).hasSize(1);
    }

    @Test
    public void shouldRemoveGroupMembers() throws Exception {
        // given
        authenticatedAsTeacher();
        // when
        ChangeGroupMembersCommand cmd = removeMemberCmd(1L, "student@adapt.com");

        groupFacade.changeGroupMembers(cmd);

        // then
        StudentGroupDto group = groupDao.getGroup(1L);
        assertThat(group.getStudents()).isEmpty();
    }

    @Test(expected = CantRemoveSelfFromGroupException.class)
    public void cantTeacherRemoveHimselfFromTheGroup() throws Exception {
        // given
        authenticatedAsTeacher();
        // when
        groupFacade.changeGroupMembers(removeMemberCmd(1L, "teacher@adapt.com"));

        // then
        fail("Exception should have been thrown");
    }

    @Test
    public void shouldAddGroupMembersAfterCreation() throws Exception {
        // given
        authenticatedAsTeacher();
        Long groupId = groupFacade.createGroup(new CreateStudentGroupCommand("test group"));
        // when
        ChangeGroupMembersCommand cmd = changeCmd(groupId);

        cmd.getAddMembers().add(new AddGroupMemberCommand("student@adapt.com", GroupRoleEnum.STUDENT.name()));

        groupFacade.changeGroupMembers(cmd);

        // then
        StudentGroupDto group = groupDao.getGroup(1L);
        assertThat(group.getStudents()).hasSize(1);
    }

    @Test(expected = NoSuchGroupException.class)
    public void shouldAllowOnlyGroupAdminsToSeeItsDetails() throws Exception {
        // given
        users.loginAs("bad_teacher@adapt.com");
        // when
        groupDao.getGroup(1L);
        // then - exception should be thrown
    }

    @Test
    public void shouldEvaluatorReadHisGroups() throws Exception {
        // given
        users.loginAs("evaluator@adapt.com");
        // when
        List<StudentGroupDto> groups = groupDao.query(evaluatorQuery());
        // then
        assertThat(groups).hasSize(2);
    }

    @Test
    public void shouldTeacherReadHisGroups() throws Exception {
        // given
        authenticatedAsTeacher();
        // when
        List<StudentGroupDto> groups = groupDao.query(adminQuery());
        // then
        assertThat(groups).hasSize(3);
    }

    @Test
    public void shouldReadGroupsSortedByName() throws Exception {
        // given
        authenticatedAsTeacher();
        // when
        List<StudentGroupDto> groups = groupDao.query(adminQuery());
        // then
        assertEquals(groups.get(0).getGroupName(), "another group");
        assertEquals(groups.get(1).getGroupName(), "some group");
        assertEquals(groups.get(2).getGroupName(), "yet another group");
    }

    @Test
    public void cantAllowTeacherToReadGroupsHeIsNotAssignedTo() throws Exception {
        users.loginAs("bad_teacher@adapt.com");
        assertThat(groupDao.query(adminQuery())).isEmpty();
    }

    @Test
    public void cantAllowEvaluatorToReadGroupsHeIsNotAssignedTo() throws Exception {
        users.loginAs("evaluator2@adapt.com");
        assertThat(groupDao.query(evaluatorQuery())).isEmpty();
    }

    @Test
    public void shouldTeacherReadGroupsUnanonymized() throws Exception {
        // given
        authenticatedAsTeacher();

        // when
        StudentGroupDto group = groupDao.getGroup(1L);

        // then
        assertThat(group.getStudents()).hasSize(1);
        assertThat(group.getEvaluators()).hasSize(1);
        assertThat(group.getAdministrators()).hasSize(1);
    }

    @Test(expected = CantQueryGroupsAsEvaluatorException.class)
    public void cantTeacherReadGroupsAsEvaluator() throws Exception {
        // given/
        authenticatedAsTeacher();
        // when
        groupDao.query(evaluatorQuery());
        // then - exception should be thrown
    }

    @Test
    public void shouldAdminRemoveGroupWithoutSurveyTemplatesAssigned() throws Exception {
        // given
        authenticatedAsTeacher();
        // when
        groupFacade.removeGroup(2L);
        // then
        List<StudentGroupDto> groups = groupDao.query(adminQuery());
        assertThat(groups).hasSize(2);
    }

    @Test(expected = DeletingGroupWithPublishedTemplatesException.class)
    public void cantRemoveGroupWithSurveyTemplatesAssigned() throws Exception {
        // given
        authenticatedAsTeacher();
        // when
        groupFacade.removeGroup(3L);
        // then - exception should be thrown
    }

    @Test(expected = PublishedSurveyTemplateAlreadyFilledException.class)
    public void cantRemoveTemplateFromGroupIfItHasBeenFilledByStudents() throws Exception {
        authenticateAsStudent();
        surveyFacade.startFilling(1L);
        // when
        groupFacade.removeSurveyTemplate(1L);
        // then - exception should be thrown
    }

    @Test
    public void shouldQueryGroupsByName() throws Exception {
        // given
        authenticatedAsTeacher();
        StudentGroupQuery query = adminQuery();
        query.setGroupNamePattern("anot");
        // when
        final List<StudentGroupDto> groups = groupDao.query(query);
        // then
        assertThat(groups).hasSize(2);
    }

    @Test(expected = ConstraintViolationException.class)
    public void cantCreateGroupWithPureWhitespaceName() throws Exception {
        authenticatedAsTeacher();
        groupFacade.createGroup(new CreateStudentGroupCommand(" \n \t  "));
        // then - exception should be thrown
    }

    @Test(expected = GroupAlreadyExistsException.class)
    public void cantTeacherCreateGroupWithSameNameAsExistingOne() throws Exception {
        authenticatedAsTeacher();
        groupFacade.createGroup(new CreateStudentGroupCommand(EXISTING_GROUP_NAME));
        // then - exception should be thrown
    }

    @Test
    public void shouldBeAbleToOpenGroupForStudentToSignUpThemselves() throws Exception {
        // given
        final long groupId = 2L;
        authenticatedAsTeacher();
        groupFacade.setGroupSignUpMode(new SetGroupSignUpModeCommand(groupId, true));
        // when
        authenticateAsStudent();
        groupFacade.signUpAsStudent(new GroupSignUpCommand(groupId));
        // then
        authenticatedAsTeacher();
        final StudentGroupDto group = groupDao.getGroup(groupId);
        assertTrue(group.getStudentsCanSignUp());
        assertThat(group.getStudents()).hasSize(1);
    }

    @Test
    public void shouldListGroupsStudentsCanJoinOnTheirOwn() throws Exception {
        // given
        authenticateAsStudent();
        assertThat(groupDao.getAvailableGroups()).hasSize(1);
        groupFacade.signUpAsStudent(new GroupSignUpCommand(2L));
        // when
        final List<StudentGroupDto> groups = groupDao.getAvailableGroups();
        // then
        assertThat(groups).isEmpty();
    }

    private void authenticatedAsTeacher() {
        users.loginAs("teacher@adapt.com");
    }

    private void authenticateAsStudent() {
        users.loginAs("student@adapt.com");
    }

    private static <T> T first(Collection<T> collection) {
        if (collection.isEmpty()) {
            return null;
        } else {
            return collection.iterator().next();
        }
    }

    private static StudentGroupQuery adminQuery() {
        return new StudentGroupQuery(GroupRoleEnum.GROUP_ADMINISTRATOR);
    }

    private static StudentGroupQuery evaluatorQuery() {
        return new StudentGroupQuery(GroupRoleEnum.EVALUATOR);
    }

    private static ChangeGroupMembersCommand changeCmd(Long groupId) {
        ChangeGroupMembersCommand cmd = new ChangeGroupMembersCommand();
        cmd.setGroupId(groupId);
        return cmd;
    }

    private static ChangeGroupMembersCommand removeMemberCmd(Long groupId, String email) {
        ChangeGroupMembersCommand cmd = changeCmd(groupId);
        cmd.getRemoveMembers().add(email);
        return cmd;
    }
}
