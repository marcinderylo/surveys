package org.adaptiveplatform.surveys.domain;

import java.util.Arrays;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.ConflictingGroupRoleException;
import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *  Tests that verify that a user can have multiple roles in a group, unless they conflict with each
 *  other.
 * 
 *  @author Marcin Deryło
 */
public class MultipleRolesInGroupTest {

    private UserDto admin;
    private UserDto student;
    private StudentGroup group;

    @BeforeMethod
    public void setUp() {
        admin = groupAdmin();
        student = student();
        group = new StudentGroup("test gorup", admin);
    }

    @Test
    public void shouldAllowUserToBeAdminAndEvaluatorAtTheSameTime() throws Exception {
        group.addEvaluator(groupAdmin());
        assertTrue(group.isAssignedAsEvaluator(admin));
        assertTrue(group.isGroupAdministrator(admin));
    }

    @Test(expectedExceptions = ConflictingGroupRoleException.class)
    public void shouldNotAllowUserToBeAdminAndStudentSameTime() throws Exception {
        group.addStudent(admin);
    }

    @Test
    public void shouldNotAllowUserToBeEvaluatorAndStudentSameTime() throws Exception {
        group.addEvaluator(student);
        try {
            group.addStudent(student);
            expectException();
        } catch (ConflictingGroupRoleException e) {
            // just as expected
        }
    }

    private UserDto groupAdmin() {
        UserDto teacher = new UserDto();
        teacher.setId(1L);
        teacher.setEmail("teacher@adapt.com");
        teacher.getRoles().addAll(Arrays.asList(Role.TEACHER, Role.STUDENT, Role.EVALUATOR));
        return teacher;
    }

    private UserDto student() {
        UserDto teacher = new UserDto();
        teacher.setId(2L);
        teacher.setEmail("student@adapt.com");
        teacher.getRoles().addAll(Arrays.asList(Role.STUDENT, Role.EVALUATOR));
        return teacher;
    }
}
