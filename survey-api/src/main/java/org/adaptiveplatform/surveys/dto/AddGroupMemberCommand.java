package org.adaptiveplatform.surveys.dto;

import org.adaptiveplatform.codegenerator.api.RemoteObject;

/**
 * @author Marcin Deryło
 */
@RemoteObject
public class AddGroupMemberCommand {

	/**
	 * Identifier (email) of the user to be added to the group.
	 */
	private String email;
	/**
	 * Role the user should be added to the group with.
	 */
	private String role;

	public AddGroupMemberCommand() {
	}

	public AddGroupMemberCommand(String email, String role) {
		this.email = email;
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
