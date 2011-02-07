package org.adaptiveplatform.surveys.domain;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.SignupByAdministratorOnlyGroupException;
import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

/**
 *  Unit tests for student self-signup into a student group.
 * 
 *  @author Marcin Deryło
 */
public class SignIntoStudentsGroupTest {

    @Test
    public void shouldNotAllowStudentsSignUpIntoAClosedGroup() throws Exception {
        // given
        StudentGroup group = new StudentGroup("closed group", aTeacher());
        UserDto student = aStudent();
        // when
        try {
            group.signUpStudent(student);
            // then
            expectException();
        } catch (SignupByAdministratorOnlyGroupException e) {
            // just as expected;
        }
    }

    @Test
    public void shouldAllowStudentsSignUpIntoAnOpenGroup() throws Exception {
        // given
        StudentGroup group = new StudentGroup("open group", aTeacher());
        group.setStudentsCanSignUp(true);
        UserDto student = aStudent();
        // when
        group.signUpStudent(student);
        // then
        assertTrue(group.isStudent(student));
    }

    private UserDto aTeacher() {
        UserDto teacher = new UserDto();
        teacher.setId(777L);
        teacher.setName("Dumbledore");
        teacher.getRoles().add(Role.TEACHER);
        return teacher;
    }

    private UserDto aStudent() {
        UserDto student = new UserDto();
        student.setId(666L);
        student.setName("HarryPotter");
        student.getRoles().add(Role.STUDENT);
        return student;
    }
}
