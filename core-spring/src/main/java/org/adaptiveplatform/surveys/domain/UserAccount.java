package org.adaptiveplatform.surveys.domain;

import static com.google.common.collect.Collections2.transform;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.adaptiveplatform.surveys.db.UserPrivilegeType;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.google.common.base.Function;

@Entity
@Table(name = "USER_ACCOUNTS")
@TypeDef(name = "UserPrivilegeType", typeClass = UserPrivilegeType.class)
public class UserAccount implements Serializable {

	public static final String USERNAME = "username";
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	@Column(name = "USERNAME")
	private String name;
	@Column(name = "PASSWORD")
	private String password;
	@NaturalId(mutable = false)
	@Column(name = "EMAIL", unique = true, nullable = false, updatable = false)
	private String email;
	@ElementCollection(fetch = FetchType.EAGER)
	@Type(type = "UserPrivilegeType")
	@JoinTable(name = "USER_ACCOUNTS_PRIVILEGES", joinColumns = @JoinColumn(name = "USER_ACCOUNT"))
	@Column(name = "PRIVILEGE")
	private Set<UserPrivilege> privileges = new HashSet<UserPrivilege>();

	protected UserAccount() {
		clearPrivileges();
	}

	public UserAccount(String password, String email) {
		this();
		this.password = password;
		this.email = email;
        privileges.add(UserPrivilege.STUDENT);
	}

	public void removeAllRoles() {
		privileges.clear();
	}

	public void addPrivilege(UserPrivilege privilege) {
		privileges.add(privilege);
	}

	public void clearPrivileges() {
		privileges.clear();
		privileges.add(UserPrivilege.USER);
	}

	public void setRoles(Set<String> grantedRoles) {
		clearPrivileges();
		for (String grantedRole : grantedRoles) {
			addRole(grantedRole);
		}
	}

	private void addRole(String role) {
		privileges.add(UserPrivilege.getByRole(role));
	}

	public Collection<String> getRoles() {
		return transform(privileges, new Function<UserPrivilege, String>() {

			@Override
			public String apply(UserPrivilege input) {
				return input.role;
			}
		});
	}

	public Long getId() {
		return id;
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

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
}
