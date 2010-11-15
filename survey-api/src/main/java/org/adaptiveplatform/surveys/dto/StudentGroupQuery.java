package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.RemoteObject;

@RemoteObject
public class StudentGroupQuery implements Serializable {

	private static final long serialVersionUID = 1L;
	private GroupRoleEnum runAs;
	private String groupNamePattern;

	public StudentGroupQuery() {
	}

	public StudentGroupQuery(GroupRoleEnum runAs) {
		this.runAs = runAs;
	}

	public GroupRoleEnum getRunAs() {
		return runAs;
	}

	public void setRunAs(GroupRoleEnum runAs) {
		this.runAs = runAs;
	}

	public String getGroupNamePattern() {
		return groupNamePattern;
	}

	public void setGroupNamePattern(String groupNamePattern) {
		this.groupNamePattern = groupNamePattern;
	}
}
