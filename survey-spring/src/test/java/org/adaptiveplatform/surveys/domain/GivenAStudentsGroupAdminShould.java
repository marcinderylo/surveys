package org.adaptiveplatform.surveys.domain;

import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.ConflictingGroupRoleException;
import org.adaptiveplatform.surveys.exception.NotEligibleForGroupRoleException;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Marcin Deryło
 */
public class GivenAStudentsGroupAdminShould {

	private UserDto user;
	private StudentGroup group;

	@Before
	public void init() {
		user = teacher(123L);
		group = new StudentGroup("testGroup", user);
	}

	@Test
	public void beGroupsAdministrator() throws Exception {
		assertTrue(group.isGroupAdministrator(user));
	}

	@Test
	public void beAbleToAddStudents() throws Exception {
		UserDto student = student();
		group.addStudent(student);
		assertTrue(group.isStudent(student));
	}

	@Test
	public void beAbleToAddOtherTeacherAsGroupAdministrator() throws Exception {
		UserDto anotherTeacher = teacher(234L);
		group.addGroupAdmin(anotherTeacher);
		assertTrue(group.isGroupAdministrator(user));
		assertTrue(group.isGroupAdministrator(anotherTeacher));
	}

	@Test(expected = NotEligibleForGroupRoleException.class)
	public void notBeAbleToAddNonTeacherAsGroupAdministrator() throws Exception {
		group.addGroupAdmin(student());
		expectException();
	}

	@Test
	public void beAbleToAddEvaluatorToTheGroup() throws Exception {
		UserDto evaluator = evaluator();
		group.addEvaluator(evaluator);
		assertTrue(group.isAssignedAsEvaluator(evaluator));
	}

	@Test(expected = NotEligibleForGroupRoleException.class)
	public void notBeAbleToAddEdvaluatorWithoutSuitableRole() throws Exception {
		group.addEvaluator(student());
		expectException();
	}

	@Test(expected = ConflictingGroupRoleException.class)
	public void notBeAbleToAddUserToGroupWithDifferentRoles() throws Exception {
		UserDto evaluator = evaluator();
		group.addStudent(evaluator);
		group.addEvaluator(evaluator);
		expectException();
	}

	private UserDto teacher(Long id) {
		return user(id, Role.TEACHER);
	}

	private UserDto student() {
		return user(234L);
	}

	private UserDto evaluator() {
		return user(134L, Role.EVALUATOR);
	}

	private UserDto user(Long id, String... roles) {
		UserDto evaluator = new UserDto();
		evaluator.setId(id);
		evaluator.getRoles().addAll(Arrays.asList(roles));
		return evaluator;
	}
}
