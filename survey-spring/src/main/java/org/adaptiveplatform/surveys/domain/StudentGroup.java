package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.AlreadyGroupMemberException;
import org.adaptiveplatform.surveys.exception.NotEligibleForGroupRoleException;
import org.adaptiveplatform.surveys.exception.SignupByAdministratorOnlyGroupException;
import org.apache.commons.lang.Validate;
import org.hibernate.annotations.CollectionOfElements;

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
    @CollectionOfElements(targetElement = GroupMember.class)
    @JoinTable(name = "STUDENT_GROUPS_MEMBERS", joinColumns = {
        @JoinColumn(name =
        "GROUP_ID")})
    private List<GroupMember> members = new ArrayList<GroupMember>();
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
        members.add(new GroupMember(user, GroupRole.GROUP_ADMINISTRATOR));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isGroupAdministrator(UserDto user) {
        GroupMember member = findUserMembership(user);
        return member != null && GroupRole.GROUP_ADMINISTRATOR.equals(member.getRole());
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
        GroupMember member = findUserMembership(user);
        return member != null && GroupRole.STUDENT.equals(
                member.getRole());
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
        GroupMember member = findUserMembership(user);
        return member != null && GroupRole.EVALUATOR.equals(member.getRole());
    }

    public void addMember(UserDto user, GroupRole role) {
        validateNotInGroupYet(user);
        if (!role.isEligible(user)) {
            throw new NotEligibleForGroupRoleException(user.getName(), role.asPublicRole());
        }
        members.add(new GroupMember(user, role));
    }

    private static void validateIsInRole(UserDto user, String role) {
        Validate.isTrue(user.getRoles().contains(role));
    }

    private GroupMember findUserMembership(UserDto user) {
        for (GroupMember member : members) {
            if (member.getUser().getId().equals(user.getId())) {
                return member;
            }
        }
        return null;
    }

    private void validateNotInGroupYet(UserDto user) {
        GroupMember member = findUserMembership(user);
        if (null != member) {
            throw new AlreadyGroupMemberException(user.getEmail(), name, member.getRole().
                    asPublicRole());
        }
    }

    public void removeMember(UserDto user) {
        GroupMember member = findUserMembership(user);
        Validate.notNull(member, "User '" + user.getName()
                + "' is not a member of the group");
        members.remove(member);
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
