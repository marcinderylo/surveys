package org.adaptiveplatform.surveys.application;

import static org.adaptiveplatform.surveys.test.Asserts.assertCollectionSize;
import static org.adaptiveplatform.surveys.test.Asserts.assertEmpty;
import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.adaptiveplatform.surveys.db.SqlScriptImporter;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.dto.AddGroupMemberCommand;
import org.adaptiveplatform.surveys.dto.ChangeGroupMembersCommand;
import org.adaptiveplatform.surveys.dto.ChangeSurveyPublicationCommand;
import org.adaptiveplatform.surveys.dto.CreateStudentGroupCommand;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.StudentGroupDto;
import org.adaptiveplatform.surveys.dto.StudentGroupQuery;
import org.adaptiveplatform.surveys.exception.CantQueryGroupsAsEvaluatorException;
import org.adaptiveplatform.surveys.exception.CantRemoveSelfFromGroupException;
import org.adaptiveplatform.surveys.exception.DeletingGroupWithPublishedTemplatesException;
import org.adaptiveplatform.surveys.exception.GroupAlreadyExistsException;
import org.adaptiveplatform.surveys.exception.NoSuchGroupException;
import org.adaptiveplatform.surveys.exception.PublishedSurveyTemplateAlreadyFilledException;
import org.adaptiveplatform.surveys.service.AuthenticationServiceMock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Marcin Deryło
 */
@ContextConfiguration("classpath:/testConfigurationContext.xml")
public class StudentGroupsSystemTest extends AbstractTestNGSpringContextTests {

    public static final String EXISTING_GROUP_NAME = "some group";
    @Resource
    private SqlScriptImporter ex;
    @Resource
    private AuthenticationServiceMock authMock;
    @Resource
    private StudentGroupFacade groupFacade;
    @Resource
    private StudentGroupDao groupDao;
    @Resource
    private SurveyDao surveyDao;
    @Resource
    private SurveyFacade surveyFacade;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        ex.executeScript("groupsSystemTestImport.sql");
    }

    @AfterMethod
    public void afterMethod() throws Exception {
        ex.executeScript("deleteAll.sql");
        authMock.logout();
    }

    @Test
    public void shouldTeacherCreateUserGroup() throws Exception {
        authenticatedAsTeacher();
        // when
        Long groupId = groupFacade.createGroup(new CreateStudentGroupCommand(
                "test group"));
        // then
        StudentGroupDto dto = groupDao.getGroup(groupId);
        assertEquals(dto.getGroupName(), "test group");

        assertCollectionSize(dto.getAdministrators(), 1);
        assertEquals(first(dto.getAdministrators()).getId(), id(2L));

        assertEmpty(dto.getEvaluators());
        assertEmpty(dto.getStudents());
    }

    @Test(expectedExceptions = AccessDeniedException.class)
    public void cantAllowNonTeacherToCreateGroup() throws Exception {
        // given
        authMock.authenticate(2L, "teacher@adapt.com", Role.EVALUATOR, Role.USER,
                Role.ADMINISTRATOR);
        // when
        groupFacade.createGroup(new CreateStudentGroupCommand(
                "test group"));
        // then
        expectException();
    }

    @Test
    public void shouldAddGroupMembersUponCreation() throws Exception {
        // given
        authenticatedAsTeacher();
        // when
        CreateStudentGroupCommand createGroupCmd =
                new CreateStudentGroupCommand("test group");
        createGroupCmd.getAddMemberCommands().add(new AddGroupMemberCommand(
                "student@adapt.com", GroupRoleEnum.STUDENT.name()));
        createGroupCmd.getAddMemberCommands().add(
                new AddGroupMemberCommand("evaluator@adapt.com", GroupRoleEnum.EVALUATOR.name()));

        Long groupId = groupFacade.createGroup(createGroupCmd);
        // then
        StudentGroupDto dto = groupDao.getGroup(groupId);

        assertCollectionSize(dto.getEvaluators(), 1);
        assertEquals(first(dto.getEvaluators()).getId(), id(3L));

        assertCollectionSize(dto.getStudents(), 1);
        assertEquals(first(dto.getStudents()).getId(), id(1L));
    }

    @Test
    public void shouldRemoveGroupMembers() throws Exception {
        // given
        authenticatedAsTeacher();
        // when
        ChangeGroupMembersCommand cmd =
                removeMemberCmd(1L, "student@adapt.com");

        groupFacade.changeGroupMembers(cmd);

        // then
        StudentGroupDto group = groupDao.getGroup(1L);
        assertEmpty(group.getStudents());
    }

    @Test(expectedExceptions = {CantRemoveSelfFromGroupException.class})
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
        Long groupId = groupFacade.createGroup(new CreateStudentGroupCommand(
                "test group"));
        // when
        ChangeGroupMembersCommand cmd = changeCmd(groupId);

        cmd.getAddMembers().add(new AddGroupMemberCommand("student@adapt.com",
                GroupRoleEnum.STUDENT.name()));

        groupFacade.changeGroupMembers(cmd);

        // then
        StudentGroupDto group = groupDao.getGroup(1L);
        assertCollectionSize(group.getStudents(), 1);
    }

    @Test(expectedExceptions = {NoSuchGroupException.class})
    public void shouldAllowOnlyGroupAdminsToSeeItsDetails() throws Exception {
        // given
        authMock.authenticate(4L, "bad_teacher@adapt.com", Role.TEACHER,
                Role.USER);
        // when
        groupDao.getGroup(1L);
        // then
        expectException();
    }

    @Test
    public void shouldEvaluatorReadHisGroups() throws Exception {
        // given
        authMock.authenticate(3L, "evaluator@adapt.com", Role.EVALUATOR,
                Role.USER);
        // when
        List<StudentGroupDto> groups = groupDao.query(evaluatorQuery());
        // then
        assertCollectionSize(groups, 2);
    }

    @Test
    public void shouldTeacherReadHisGroups() throws Exception {
        // given
        authenticatedAsTeacher();
        // when
        List<StudentGroupDto> groups = groupDao.query(adminQuery());
        // then
        assertCollectionSize(groups, 3);
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
    public void cantAllowTeacherToReadGroupsHeIsNotAssignedTo() throws
            Exception {
        authMock.authenticate(4L, "bad_teacher@adapt.com", Role.TEACHER,
                Role.USER);

        assertEmpty(groupDao.query(adminQuery()));
    }

    @Test
    public void cantAllowEvaluatorToReadGroupsHeIsNotAssignedTo() throws
            Exception {
        authMock.authenticate(5L, "evaluator2@adapt.com", Role.EVALUATOR,
                Role.USER);

        assertEmpty(groupDao.query(evaluatorQuery()));
    }

    @Test
    public void shouldTeacherReadGroupsUnanonymized() throws Exception {
        // given
        authenticatedAsTeacher();

        // when
        StudentGroupDto group = groupDao.getGroup(1L);

        // then
        assertCollectionSize(group.getStudents(), 1);
        assertCollectionSize(group.getEvaluators(), 1);
        assertCollectionSize(group.getAdministrators(), 1);

    }

    @Test(expectedExceptions = CantQueryGroupsAsEvaluatorException.class)
    public void cantTeacherReadGroupsAsEvaluator() throws Exception {
        // given/
        authenticatedAsTeacher();
        // when
        groupDao.query(evaluatorQuery());
        // then
        expectException();
    }

    @Test
    public void shouldAdminRemoveGroupWithoutSurveyTemplatesAssigned()
            throws Exception {
        // given
        authenticatedAsTeacher();
        // when
        groupFacade.removeGroup(2L);
        // then
        List<StudentGroupDto> groups = groupDao.query(adminQuery());
        assertCollectionSize(groups, 2);
        assertEquals(first(groups).getId(), id(1L));
    }

    @Test(expectedExceptions = {
        DeletingGroupWithPublishedTemplatesException.class})
    public void cantRemoveGroupWithSurveyTemplatesAssigned() throws
            Exception {
        // given
        authenticatedAsTeacher();
        // when
        groupFacade.removeGroup(3L);
        // then
        expectException();
    }

    @Test(expectedExceptions = {
        PublishedSurveyTemplateAlreadyFilledException.class})
    public void cantRemoveTemplateFromGroupIfItHasBeenFilledByStudents()
            throws Exception {
        // given
        authMock.authenticate(1L, "student@adapt.com", Role.USER);
        surveyFacade.startFilling(1L);
        // when
        groupFacade.removeSurveyTemplate(1L);
        // then
        expectException();
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
        assertCollectionSize(groups, 2);
    }

    @Test(expectedExceptions = {ConstraintViolationException.class})
    public void cantCreateGroupWithPureWhitespaceName() throws Exception {
        authenticatedAsTeacher();
        groupFacade.createGroup(new CreateStudentGroupCommand(" \n \t  "));
        expectException();
    }

    @Test(expectedExceptions = {GroupAlreadyExistsException.class})
    public void cantTeacherCreateGroupWithSameNameAsExistingOne() throws
            Exception {
        authenticatedAsTeacher();
        groupFacade.createGroup(new CreateStudentGroupCommand(
                EXISTING_GROUP_NAME));
        expectException();
    }

    private void authenticatedAsTeacher() {
        // given
        authMock.authenticate(2L, "teacher@adapt.com",
                Role.TEACHER, Role.USER);
    }

    private static ChangeSurveyPublicationCommand changePublicationCmd(
            Long publicationId, Date from, Date to) {
        ChangeSurveyPublicationCommand cmd =
                new ChangeSurveyPublicationCommand();
        cmd.setPublicationId(publicationId);
        cmd.setStartingDate(from);
        cmd.setExpirationDate(to);
        return cmd;
    }

    private static <T> T first(Collection<T> collection) {
        if (collection.isEmpty()) {
            return null;
        } else {
            return collection.iterator().next();
        }
    }

    private static Long id(long i) {
        return Long.valueOf(i);
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

    private static ChangeGroupMembersCommand removeMemberCmd(Long groupId,
            String email) {
        ChangeGroupMembersCommand cmd = changeCmd(groupId);
        cmd.getRemoveMembers().add(email);
        return cmd;
    }
}

