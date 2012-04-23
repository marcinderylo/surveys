package org.adaptiveplatform.surveys.builders;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Component
@Profile({"development", "test"})
public class CreateUsersOnStartup {

    @Resource
    private CoreFixtureBuilder fixtureBuilder;

    @PostConstruct
    public void createUsers() {
        fixtureBuilder.createUser(UserAccountBuilder.student(CoreFixtureBuilder.STUDENT_EMAIL));
        fixtureBuilder.createUser(UserAccountBuilder.teacher(CoreFixtureBuilder.TEACHER_EMAIL));
        fixtureBuilder.createUser(UserAccountBuilder.evaluator(CoreFixtureBuilder.EVALUATOR_EMAIL));
    }
}
