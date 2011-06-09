package org.adaptiveplatform.surveys.builders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;

public class UserAccountBuilder {

    public static final String DEFAULT_PASSWORD = "s3cr3t";

    RegisterAccountCommand command = new RegisterAccountCommand();

    private Set<String> roles = new HashSet<String>();

    public UserAccountBuilder(String email) {
        command.setEmail(email);
        command.setName("user" + UUID.randomUUID().toString().substring(0, 8));
        command.setPassword(DEFAULT_PASSWORD);
    }

    public UserAccountBuilder withPassword(String password) {
        command.setPassword(password);
        return this;
    }

    public UserAccountBuilder withEmail(String email) {
        command.setEmail(email);
        return this;
    }

    public UserAccountBuilder withName(String name) {
        command.setName(name);
        return this;
    }

    public UserAccountBuilder inRoles(String... roles) {
        this.roles.addAll(Arrays.asList(roles));
        return this;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public RegisterAccountCommand build() {
        return command;
    }

    public static UserAccountBuilder user(String email) {
        return new UserAccountBuilder(email);
    }

    public static UserAccountBuilder student(String email) {
        return user(email);
    }

    public static UserAccountBuilder evaluator(String email) {
        return user(email).inRoles(Role.EVALUATOR);
    }

    public static UserAccountBuilder teacher(String email) {
        return user(email).inRoles(Role.TEACHER);
    }
}
