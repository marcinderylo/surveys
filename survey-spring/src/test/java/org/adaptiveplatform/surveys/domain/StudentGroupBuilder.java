package org.adaptiveplatform.surveys.domain;

import static com.google.common.collect.Collections2.transform;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.test.UserAccountToDto;

/**
 * @author Marcin Derylo
 */
public class StudentGroupBuilder {

    private String name;
    private UserDto creator;
    private List<UserDto> students = new ArrayList<UserDto>();
    private List<UserDto> evaluators = new ArrayList<UserDto>();

    public StudentGroupBuilder(String name, UserDto creator) {
        this.name = name;
        this.creator = creator;
    }

    public StudentGroupBuilder withStudents(UserDto... students) {
        this.students.addAll(asList(students));
        return this;
    }

    public StudentGroupBuilder withEvaluators(UserAccount... evaluators) {
        this.evaluators.addAll(toDtos(evaluators));
        return this;
    }

    public StudentGroupBuilder withStudents(UserAccount... students) {
        this.students.addAll(toDtos(students));
        return this;
    }

    public static StudentGroupBuilder group(String name, UserDto creator) {
        return new StudentGroupBuilder(name,creator);
    }

    public static StudentGroupBuilder group(String name, UserAccount creator) {
        UserDto user = UserAccountToDto.INSTANCE.apply(creator);
        return new StudentGroupBuilder(name, user);
    }

    StudentGroup build() {
        StudentGroup group = new StudentGroup(name, creator);
        for (UserDto student : students) {
            group.addStudent(student);
        }
        for (UserDto evaluator : evaluators) {
            group.addEvaluator(evaluator);
        }
        return group;
    }

    private Collection<UserDto> toDtos(UserAccount[] users) {
        return transform(asList(users), UserAccountToDto.INSTANCE);
    }
}
