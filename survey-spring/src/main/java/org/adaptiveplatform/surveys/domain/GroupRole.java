package org.adaptiveplatform.surveys.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.UserDto;

/**
 *
 * @author Marcin Deryło
 */
public enum GroupRole {

    GROUP_ADMINISTRATOR(GroupRoleEnum.GROUP_ADMINISTRATOR, Role.TEACHER),
    STUDENT(GroupRoleEnum.STUDENT),
    EVALUATOR(GroupRoleEnum.EVALUATOR, Role.EVALUATOR);
    private Set<String> roles;
    private GroupRoleEnum correspondingPublicRole;

    private GroupRole(GroupRoleEnum publicRole, String... requiredRoles) {
        this.correspondingPublicRole = publicRole;
        roles = new HashSet<String>(Arrays.asList(requiredRoles));
    }

    boolean isEligible(UserDto user) {
        return user.getRoles().containsAll(roles);
    }

    GroupRoleEnum asPublicRole() {
        return correspondingPublicRole;
    }
}
