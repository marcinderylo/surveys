package org.adaptiveplatform.surveys.domain;

public enum UserPrivilege {

	/**
	 * Registered and authenticated user.
	 */
	USER(Role.USER),
	/**
	 * Student
	 */
	STUDENT(Role.STUDENT),
	/**
	 * Special account with virtually unlimited privileges.
	 */
	ADMINISTRATOR(Role.ADMINISTRATOR),
	/**
	 * 
	 */
	TEACHER(Role.TEACHER),
	/**
	 * 
	 */
	EVALUATOR(Role.EVALUATOR);

	public final String role;

	private UserPrivilege(String role) {
		this.role = role;
	}

	public static UserPrivilege getByRole(String name) {
		for (UserPrivilege privilege : values()) {
			if (privilege.role.equals(name)) {
				return privilege;
			}
		}
		throw new IllegalArgumentException("no UserPrivilege for role: " + name);
	}
}
