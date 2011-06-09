package org.adaptiveplatform.surveys.builders;

import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.dto.AddGroupMemberCommand;
import org.adaptiveplatform.surveys.dto.CreateStudentGroupCommand;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;

public class GroupBuilder {

    private CreateStudentGroupCommand command = new CreateStudentGroupCommand();

    public GroupBuilder(String groupName) {
        command.setGroupName(groupName);
    }

    public GroupBuilder withEvaluator(String evaluatorEmail) {
        command.getAddMemberCommands().add(new AddGroupMemberCommand(evaluatorEmail, GroupRoleEnum.EVALUATOR.name()));
        return this;
    }

    public GroupBuilder withStudent(String studentEmail) {
        command.getAddMemberCommands().add(new AddGroupMemberCommand(studentEmail, GroupRoleEnum.STUDENT.name()));
        return this;
    }

    public GroupBuilder openForSignup() {
        command.setOpenForSignup(true);
        return this;
    }

    public CreateStudentGroupCommand build() {
        return command;
    }

    public static GroupBuilder group(String groupName) {
        return new GroupBuilder(groupName);
    }
}
