package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.adaptiveplatform.codegenerator.api.RemoteObject;
import org.hibernate.annotations.WhereJoinTable;

/**
 *
 * @author Marcin Deryło
 */
@Entity
@Table(name = "STUDENT_GROUPS")
@org.hibernate.annotations.Entity(mutable = false)
@NamedQueries({
    @NamedQuery(name = StudentGroupDto.Query.GET_STUDENT_GROUP, query = "SELECT g FROM StudentGroupDto g "
    + "LEFT JOIN g.administrators adm "
    + "WHERE adm.id = :userId AND g.id = :groupId")
})
@RemoteObject
public class StudentGroupDto implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final class Query {

        public static final String GET_STUDENT_GROUP = "StudentGroupDto.getGroup";
    }
    @Id
    @Column(name = "ID", insertable = false, updatable = false)
    private Long id;
    @Column(name = "GROUP_NAME", insertable = false, updatable = false)
    private String groupName;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "STUDENT_GROUPS_MEMBERS", joinColumns = {
        @JoinColumn(name =
        "GROUP_ID")},
    inverseJoinColumns = {
        @JoinColumn(name = "MEMBER_ID")})
    @WhereJoinTable(clause = "GROUP_ROLE = 'STUDENT'")
    private Set<UserDto> students = new HashSet<UserDto>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "STUDENT_GROUPS_MEMBERS", joinColumns = {
        @JoinColumn(name =
        "GROUP_ID")},
    inverseJoinColumns = {
        @JoinColumn(name = "MEMBER_ID")})
    @WhereJoinTable(clause = "GROUP_ROLE = 'EVALUATOR'")
    private Set<UserDto> evaluators = new HashSet<UserDto>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "STUDENT_GROUPS_MEMBERS", joinColumns = {
        @JoinColumn(name =
        "GROUP_ID")},
    inverseJoinColumns = {
        @JoinColumn(name = "MEMBER_ID")})
    @WhereJoinTable(clause = "GROUP_ROLE = 'GROUP_ADMINISTRATOR'")
    private Set<UserDto> administrators = new HashSet<UserDto>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<UserDto> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(Set<UserDto> administrators) {
        this.administrators = administrators;
    }

    public Set<UserDto> getEvaluators() {
        return evaluators;
    }

    public void setEvaluators(Set<UserDto> evaluators) {
        this.evaluators = evaluators;
    }

    public Set<UserDto> getStudents() {
        return students;
    }

    public void setStudents(Set<UserDto> students) {
        this.students = students;
    }
}
