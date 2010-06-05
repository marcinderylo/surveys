package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.RemoteObject;


@RemoteObject
public class UserQuery implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nameContains;

	public String getNameContains() {
		return nameContains;
	}

	public void setNameContains(String nameContains) {
		this.nameContains = nameContains;
	}

}
