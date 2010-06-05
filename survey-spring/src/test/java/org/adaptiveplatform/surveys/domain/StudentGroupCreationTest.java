package org.adaptiveplatform.surveys.domain;

import static org.adaptiveplatform.surveys.test.Asserts.expectException;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Marcin Deryło
 */
public class StudentGroupCreationTest {

    private UserDto user;

    @Test
    public void shouldBeCreatedByTeacher() throws Exception {
        givenUserInTeacherRole();
        creatingAGroupNamed("testGroup");
        shouldSucceed();
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void cantBeCreatedWithoutUser() throws Exception {
        withoutAUser();
        creatingAGroupNamed("testGroup");
        shouldFail();
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void cantBeCreatedByUserWithoutTeacherRole() throws Exception {
        givenUserWithoutTeacherRole();
        creatingAGroupNamed("testGroup");
        shouldFail();
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void cantCreateGroupWithEmptyFriendlyName() throws Exception {
        givenUserInTeacherRole();
        creatingAGroupNamed("");
        shouldFail();
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void cantCreateGroupWithNullFriendlyName() throws Exception {
        givenUserInTeacherRole();
        creatingAGroupNamed(null);
        shouldFail();
    }

    @BeforeMethod
    public void init() {
        user = null;
    }

    private void givenUserWithoutTeacherRole() {
        givenAUser(321L, Role.STUDENT);
    }

    private void givenUserInTeacherRole() {
        givenAUser(123L, Role.TEACHER);
    }

    private void givenAUser(Long id, String role) {
        user = new UserDto();
        user.setId(id);
        user.getRoles().add(role);
    }

    private void creatingAGroupNamed(String groupName) {
        new StudentGroup(groupName, user);
    }

    private void shouldSucceed() {
        // just a marked method for better test readability
    }

    private void shouldFail() {
        expectException();
    }

    private void withoutAUser() {
        user = null;
    }
}
