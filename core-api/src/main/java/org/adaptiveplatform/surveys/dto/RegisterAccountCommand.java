package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.RemoteObject;


/**
 * @author Rafal
 */
@RemoteObject
public class RegisterAccountCommand implements Serializable {
	private static final long serialVersionUID = -3059429636827523198L;

	private String name;
	private String password;
	private String email;

	public RegisterAccountCommand() {
        // to be used rather by serialization framework
	}

	public RegisterAccountCommand(String name, String password, String email) {
		this.name = name;
		this.password = password;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
