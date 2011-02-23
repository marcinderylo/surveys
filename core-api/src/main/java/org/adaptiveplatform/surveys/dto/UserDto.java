package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.adaptiveplatform.codegenerator.api.RemoteObject;
import org.apache.commons.lang.ObjectUtils;
import org.hibernate.annotations.CollectionOfElements;


@RemoteObject
@Entity
@Table(name = "USER_ACCOUNTS")
@org.hibernate.annotations.Entity(mutable = false)
public class UserDto implements Serializable {
	private static final long serialVersionUID = -4226054307901887002L;

	public static final String USERNAME = "name";
	public static final String EMAIL = "email";

	@Id
	@Column(name = "ID", insertable = false, updatable = false)
	private Long id;
	@Column(name = "USERNAME")
	private String name;
	@Column(name = "EMAIL")
	private String email;
	@CollectionOfElements(fetch = FetchType.EAGER)
	@JoinTable(name = "USER_ACCOUNTS_PRIVILEGES", joinColumns = @JoinColumn(name = "USER_ACCOUNT"))
	@Column(name = "PRIVILEGE")
	private List<String> roles = new ArrayList<String>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", name=" + name + ", email=" + email + ", roles=" + roles + "]";
	}

    public boolean isSameAs(UserDto other) {
        return ObjectUtils.equals(id, other.getId());
    }
}
