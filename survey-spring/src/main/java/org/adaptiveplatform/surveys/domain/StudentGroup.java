package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.ConflictingGroupRoleException;
import org.adaptiveplatform.surveys.exception.NotEligibleForGroupRoleException;
import org.adaptiveplatform.surveys.exception.SignupByAdministratorOnlyGroupException;
import org.apache.commons.lang.Validate;

/**
 * @author Marcin Deryło
 */
@Entity
@Table(name = "STUDENT_GROUPS")
public class StudentGroup implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "GROUP_NAME")
    private String name;
    @Embedded
    private GroupMemberships members = new GroupMemberships();
    @Column(name = "ALLOW_STUDENT_SIGNUP", nullable = false)
    private Boolean allowStudentSignup = Boolean.FALSE;

    protected StudentGroup() {
        // To be used only by object persistence framework
    }

    /**
     * Creates a new students group, administered by given user.
     * @param groupName name of the group (mandatory).
     * @param user owner of the group. Must be a teacher. Will be granted the administrator role in
     * this group.
     */
    public StudentGroup(String groupName, UserDto user) {
        Validate.notNull(user, "Group has to be created by a user");
        validateIsInRole(user, Role.TEACHER);
        Validate.notEmpty(groupName, "Group name must not be empty");
        this.name = groupName;
        members.addMemberWithRole(user, GroupRole.GROUP_ADMINISTRATOR);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isGroupAdministrator(UserDto user) {
        return rolesOf(user).contains(GroupRole.GROUP_ADMINISTRATOR);
    }

    /**
     * <p>Adds a new student to the group. He/she will be able to submit
     * answers to survey templates assigned to this group by it's evaluators.</p>
     * <p>This action is allowed for administrators of this group only.</p>
     *
     * @param student
     */
    public void addStudent(UserDto student) {
        addMember(student, GroupRole.STUDENT);
    }

    public boolean isStudent(UserDto user) {
        return rolesOf(user).contains(GroupRole.STUDENT);
    }

    /**
     * <p>Adds a new teacher with rights to administer the group. This action
     * is allowed for group administrators only.</p>
     * <p>Added person must be in role {@link Role#TEACHER}.</p>
     * @param teacher
     */
    public void addGroupAdmin(UserDto teacher) {
        addMember(teacher, GroupRole.GROUP_ADMINISTRATOR);
    }

    /**
     * <p>Adds a new evaluator to the group, so he is allowed to assign created
     * survey templates to this group and analyze answers to this template
     * submitted by students of this group.</p>
     * <p>Added person must be in role {@link Role#EVALUATOR}.</p>
     *
     * @param evaluator
     */
    public void addEvaluator(UserDto evaluator) {
        addMember(evaluator, GroupRole.EVALUATOR);
    }

    public boolean isAssignedAsEvaluator(UserDto user) {
        return rolesOf(user).contains(GroupRole.EVALUATOR);
    }

    public void addMember(UserDto user, GroupRole role) {
        validateUserIsEligibleForRole(role, user);
        validateUserDoesntAlreadyHaveAConflictingRole(user, role);
        members.addMemberWithRole(user, role);
    }

    private void validateUserIsEligibleForRole(GroupRole role, UserDto user) {
        if (!role.isEligible(user)) {
            throw new NotEligibleForGroupRoleException(user.getName(), role.asPublicRole());
        }
    }

    private void validateUserDoesntAlreadyHaveAConflictingRole(UserDto user, GroupRole groupRole) {
        final Set<GroupRole> roles = rolesOf(user);
        roles.add(groupRole);
        if (roles.contains(GroupRole.STUDENT) && (roles.contains(GroupRole.EVALUATOR) ||
                roles.contains(GroupRole.GROUP_ADMINISTRATOR)) ) {
            throw new ConflictingGroupRoleException(user.getEmail(), name, groupRole.asPublicRole());
        }
    }

    private static void validateIsInRole(UserDto user, String role) {
        Validate.isTrue(user.getRoles().contains(role));
    }

    private Set<GroupRole> rolesOf(UserDto user) {
        return members.getRoleOf(user);
    }

    private void validateNotInGroupYet(UserDto user) {
        Collection<GroupRole> roles = rolesOf(user);
        if (!roles.isEmpty()) {
            throw new ConflictingGroupRoleException(user.getEmail(), name, roles.iterator().next().
                    asPublicRole());
        }
    }

    public void removeMember(UserDto user) {
        Collection<GroupRole> roles = rolesOf(user);
        Validate.isTrue(!roles.isEmpty(), "User '" + user.getName()
                + "' is not a member of the group");
        members.removeMember(user);
    }

    /**
     * Signs a student up into the group, if the group administrator allows it.
     * @param student
     */
    public void signUpStudent(UserDto student) {
        if (onlyAdministratorCanAddUsers()) {
            throw new SignupByAdministratorOnlyGroupException(name);
        }
        addMember(student, GroupRole.STUDENT);
    }

    private boolean onlyAdministratorCanAddUsers() {
        return !allowStudentSignup;
    }

    public void setStudentsCanSignUp(boolean studentsSignupAllowed) {
        this.allowStudentSignup = studentsSignupAllowed;
    }
}
