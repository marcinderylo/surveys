package org.adaptiveplatform.surveys.builders;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.application.UserFacade;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;
import org.springframework.stereotype.Service;

@Service
public class CoreFixtureBuilder {

    public static final String ADMIN_EMAIL = "adaptserver@gmail.com";
    private static final String ADMIN_PASSWORD = "adapt2010";
    public static final String STUDENT_EMAIL = "student@gmail.com";
    public static final String TEACHER_EMAIL = "teacher@gmail.com";
    public static final String EVALUATOR_EMAIL = "evaluator@gmail.com";

    @Resource
    private UserFacade facade;

    @Resource
    private AuthenticationService authentication;

    @PostConstruct
    public void createUsers() {
        createUser(UserAccountBuilder.student(STUDENT_EMAIL));
        createUser(UserAccountBuilder.teacher(TEACHER_EMAIL));
        createUser(UserAccountBuilder.evaluator(EVALUATOR_EMAIL));
    }

    public Long createUser(UserAccountBuilder user) {
        authentication.logout();
        RegisterAccountCommand command = user.build();
        Long userId = facade.registerUser(command);

        if (!user.getRoles().isEmpty()) {
            loginAsAdmin();
            facade.setUserRoles(command.getEmail(), user.getRoles());
        }
        return userId;
    }

    public void loginAsAdmin() {
        loginAs(ADMIN_EMAIL, ADMIN_PASSWORD);
    }

    public void loginAsTeacher() {
        loginAs(TEACHER_EMAIL);
    }

    public void loginAsEvaluator() {
        loginAs(EVALUATOR_EMAIL);
    }

    public void loginAsStudent() {
        loginAs(STUDENT_EMAIL);
    }

    public void loginAs(String email) {
        authentication.login(email, UserAccountBuilder.DEFAULT_PASSWORD);
    }

    public void loginAs(String email, String password) {
        authentication.login(email, password);
    }

    public void logout() {
        authentication.logout();
    }
}
