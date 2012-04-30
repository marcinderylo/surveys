package org.adaptiveplatform.surveys.acceptance;

import org.adaptiveplatform.surveys.ContainerEnabledTest;
import org.adaptiveplatform.surveys.application.StudentGroupDao;
import org.adaptiveplatform.surveys.application.StudentGroupFacade;
import org.adaptiveplatform.surveys.application.SurveyFacade;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureCreator;
import org.adaptiveplatform.surveys.dto.*;
import org.adaptiveplatform.surveys.exception.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.adaptiveplatform.surveys.builders.CoreFixtureBuilder.*;
import static org.adaptiveplatform.surveys.builders.GroupBuilder.group;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.openQuestion;
import static org.adaptiveplatform.surveys.builders.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.evaluator;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.teacher;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Marcin Deryło
 */
public class StudentGroupsSystemTest extends ContainerEnabledTest {

    private static final String ANOTHER_EVALUATOR_EMAIL = "anotherEvaluator@adapt.com";
    private static final String ANOTHER_TEACHER_EMAIL = "anotherTeacher@adapt.com";
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
    private SurveysFixtureCreator surveys;

    @Before
    public void initializeData() throws Exception {
        users.createUser(teacher(ANOTHER_TEACHER_EMAIL));
        users.createUser(evaluator(ANOTHER_EVALUATOR_EMAIL));

        users.loginAsTeacher();
        surveys.createGroup(group("some group").withEvaluator(EVALUATOR_EMAIL).withStudent(STUDENT_EMAIL));
        surveys.createGroup(group("another group").openForSignup());
        Long groupWithResearch = surveys.createGroup(group("yet another group").withEvaluator(EVALUATOR_EMAIL)
                .withStudent(STUDENT_EMAIL).openForSignup());

        users.loginAsEvaluator();
        Long templateId = surveys.createTemplate(template("not published survey").withQuestions(
                openQuestion("qwestion")));
        surveys.createResearch(research().withSurvey(templateId).forGroup(groupWithResearch));
    }

    @Test
    public void shouldTeacherCreateUserGroup() throws Exception {
        users.loginAsTeacher();
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
        users.loginAsEvaluator();
        // when
        groupFacade.createGroup(new CreateStudentGroupCommand("test group"));
        // then - exception should be thrown
    }

    @Test
    public void shouldAddGroupMembersUponCreation() throws Exception {
        // given
        users.loginAsTeacher();
        // when
        CreateStudentGroupCommand createGroupCmd = new CreateStudentGroupCommand("test group");
        createGroupCmd.getAddMemberCommands().add(
                new AddGroupMemberCommand(STUDENT_EMAIL, GroupRoleEnum.STUDENT.name()));
        createGroupCmd.getAddMemberCommands().add(
                new AddGroupMemberCommand(EVALUATOR_EMAIL, GroupRoleEnum.EVALUATOR.name()));

        Long groupId = groupFacade.createGroup(createGroupCmd);
        // then
        StudentGroupDto dto = groupDao.getGroup(groupId);

        assertThat(dto.getEvaluators()).hasSize(1);
        assertThat(dto.getStudents()).hasSize(1);
    }

    @Test
    public void shouldRemoveGroupMembers() throws Exception {
        // given
        users.loginAsTeacher();
        // when
        ChangeGroupMembersCommand cmd = removeMemberCmd(1L, STUDENT_EMAIL);
        groupFacade.changeGroupMembers(cmd);
        // then
        StudentGroupDto group = groupDao.getGroup(1L);
        assertThat(group.getStudents()).isEmpty();
    }

    @Test(expected = CantRemoveSelfFromGroupException.class)
    public void cantTeacherRemoveHimselfFromTheGroup() throws Exception {
        // given
        users.loginAsTeacher();
        // when
        groupFacade.changeGroupMembers(removeMemberCmd(1L, TEACHER_EMAIL));
        // then
        fail("Exception should have been thrown");
    }

    @Test
    public void shouldAddGroupMembersAfterCreation() throws Exception {
        // given
        users.loginAsTeacher();
        Long groupId = groupFacade.createGroup(new CreateStudentGroupCommand("test group"));
        // when
        ChangeGroupMembersCommand cmd = changeCmd(groupId);
        cmd.getAddMembers().add(new AddGroupMemberCommand(STUDENT_EMAIL, GroupRoleEnum.STUDENT.name()));
        groupFacade.changeGroupMembers(cmd);
        // then
        StudentGroupDto group = groupDao.getGroup(1L);
        assertThat(group.getStudents()).hasSize(1);
    }

    @Test(expected = NoSuchGroupException.class)
    public void shouldAllowOnlyGroupAdminsToSeeItsDetails() throws Exception {
        // given
        users.loginAs(ANOTHER_TEACHER_EMAIL);
        // when
        groupDao.getGroup(1L);
        // then - exception should be thrown
    }

    @Test
    public void shouldEvaluatorReadHisGroups() throws Exception {
        // given
        users.loginAs(EVALUATOR_EMAIL);
        // when
        List<StudentGroupDto> groups = groupDao.query(evaluatorQuery());
        // then
        assertThat(groups).hasSize(2);
    }

    @Test
    public void shouldTeacherReadHisGroups() throws Exception {
        // given
        users.loginAsTeacher();
        // when
        List<StudentGroupDto> groups = groupDao.query(adminQuery());
        // then
        assertThat(groups).hasSize(3);
    }

    @Test
    public void shouldReadGroupsSortedByName() throws Exception {
        // given
        users.loginAsTeacher();
        // when
        List<StudentGroupDto> groups = groupDao.query(adminQuery());
        // then
        assertEquals(groups.get(0).getGroupName(), "another group");
        assertEquals(groups.get(1).getGroupName(), "some group");
        assertEquals(groups.get(2).getGroupName(), "yet another group");
    }

    @Test
    public void cantAllowTeacherToReadGroupsHeIsNotAssignedTo() throws Exception {
        users.loginAs(ANOTHER_TEACHER_EMAIL);
        assertThat(groupDao.query(adminQuery())).isEmpty();
    }

    @Test
    public void cantAllowEvaluatorToReadGroupsHeIsNotAssignedTo() throws Exception {
        users.loginAs(ANOTHER_EVALUATOR_EMAIL);
        assertThat(groupDao.query(evaluatorQuery())).isEmpty();
    }

    @Test
    public void shouldTeacherReadGroupsUnanonymized() throws Exception {
        // given
        users.loginAsTeacher();

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
        users.loginAsTeacher();
        // when
        groupDao.query(evaluatorQuery());
        // then - exception should be thrown
    }

    @Test
    public void shouldAdminRemoveGroupWithoutSurveyTemplatesAssigned() throws Exception {
        // given
        users.loginAsTeacher();
        // when
        groupFacade.removeGroup(2L);
        // then
        List<StudentGroupDto> groups = groupDao.query(adminQuery());
        assertThat(groups).hasSize(2);
    }

    @Test(expected = DeletingGroupWithPublishedTemplatesException.class)
    public void cantRemoveGroupWithSurveyTemplatesAssigned() throws Exception {
        // given
        users.loginAsTeacher();
        // when
        groupFacade.removeGroup(3L);
        // then - exception should be thrown
    }

    @Test(expected = PublishedSurveyTemplateAlreadyFilledException.class)
    public void cantRemoveTemplateFromGroupIfItHasBeenFilledByStudents() throws Exception {
        users.loginAsStudent();
        surveyFacade.startFilling(1L);
        // when
        groupFacade.removeSurveyTemplate(1L);
        // then - exception should be thrown
    }

    @Test
    public void shouldQueryGroupsByName() throws Exception {
        // given
        users.loginAsTeacher();
        StudentGroupQuery query = adminQuery();
        query.setGroupNamePattern("anot");
        // when
        final List<StudentGroupDto> groups = groupDao.query(query);
        // then
        assertThat(groups).hasSize(2);
    }

    @Test(expected = ConstraintViolationException.class)
    public void cantCreateGroupWithPureWhitespaceName() throws Exception {
        users.loginAsTeacher();
        groupFacade.createGroup(new CreateStudentGroupCommand(" \n \t  "));
        // then - exception should be thrown
    }

    @Test(expected = GroupAlreadyExistsException.class)
    public void cantTeacherCreateGroupWithSameNameAsExistingOne() throws Exception {
        users.loginAsTeacher();
        groupFacade.createGroup(new CreateStudentGroupCommand(EXISTING_GROUP_NAME));
        // then - exception should be thrown
    }

    @Test
    public void shouldBeAbleToOpenGroupForStudentToSignUpThemselves() throws Exception {
        // given
        final long groupId = 2L;
        users.loginAsTeacher();
        groupFacade.setGroupSignUpMode(new SetGroupSignUpModeCommand(groupId, true));
        // when
        users.loginAsStudent();
        groupFacade.signUpAsStudent(new GroupSignUpCommand(groupId));
        // then
        users.loginAsTeacher();
        final StudentGroupDto group = groupDao.getGroup(groupId);
        assertTrue(group.getStudentsCanSignUp());
        assertThat(group.getStudents()).hasSize(1);
    }

    @Test
    public void shouldListGroupsStudentsCanJoinOnTheirOwn() throws Exception {
        // given
        users.loginAsStudent();
        assertThat(groupDao.getAvailableGroups()).hasSize(1);
        groupFacade.signUpAsStudent(new GroupSignUpCommand(2L));
        // when
        final List<StudentGroupDto> groups = groupDao.getAvailableGroups();
        // then
        assertThat(groups).isEmpty();
    }

    /**
     * regression test case for bug causing students and evaluators being removed from group when
     * non-group-admin queries
     */
    @Test
    public void shouldntQueryForAnonymizedGroupsRemoveMembersFromGroup() throws Exception {
        // given
        users.loginAs(EVALUATOR_EMAIL);
        List<StudentGroupDto> groups = groupDao.query(evaluatorQuery());
        // when
        users.logout();
        users.loginAs(TEACHER_EMAIL);
        StudentGroupDto group = groupDao.getGroup(1L);
        // then
        assertThat(group.getStudents()).hasSize(1);
        assertThat(group.getEvaluators()).hasSize(1);
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
