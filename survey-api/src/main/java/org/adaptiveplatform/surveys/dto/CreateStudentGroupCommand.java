package org.adaptiveplatform.surveys.dto;

import java.util.ArrayList;
import java.util.List;

import org.adaptiveplatform.adapt.commons.validation.constraints.NonBlank;
import org.adaptiveplatform.codegenerator.api.RemoteObject;

/**
 *
 * @author Marcin Deryło
 */
@RemoteObject
public class CreateStudentGroupCommand {

	private String groupName;
	private List<AddGroupMemberCommand> addMemberCommands = new ArrayList<AddGroupMemberCommand>();

	public CreateStudentGroupCommand() {
	}

	public CreateStudentGroupCommand(String groupName) {
		this.groupName = groupName;
	}

	@NonBlank
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<AddGroupMemberCommand> getAddMemberCommands() {
		return addMemberCommands;
	}

	public void setAddMemberCommands(List<AddGroupMemberCommand> addMemberCommands) {
		this.addMemberCommands = addMemberCommands;
	}
}
