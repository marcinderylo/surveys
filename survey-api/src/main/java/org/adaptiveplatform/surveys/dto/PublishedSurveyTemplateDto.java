package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.adaptiveplatform.codegenerator.api.RemoteObject;

@RemoteObject
@Entity
@Table(name = "PUBLISHED_SURVEY_TEMPLATE_DTOS")
@org.hibernate.annotations.Entity(mutable = false)
@NamedQueries({
    @NamedQuery(name = PublishedSurveyTemplateDto.Query.GET_SURVEY_TEMPLATE_IDS_IN_GROUPS,
    query = "SELECT sp.id FROM SurveyPublication sp JOIN sp.studentGroup g "
    + "JOIN g.members.members m WHERE g.id IN (:groupIds) AND m.user = :user AND m.role = 'STUDENT'"),
    @NamedQuery(name = PublishedSurveyTemplateDto.Query.GET_SURVEY_TEMPLATE_IDS,
    query = "SELECT sp.id FROM SurveyPublication sp JOIN sp.studentGroup g JOIN g.members.members m "
    + "WHERE m.user = :user AND m.role = 'STUDENT'"),
    @NamedQuery(name = PublishedSurveyTemplateDto.Query.GET_SURVEY_TEMPLATE, query = "SELECT st "
    + "FROM PublishedSurveyTemplateDto st WHERE "
    + "st.templateId = :templateId AND " + "st.groupId = :groupId "
    + "AND (st.templateId IN (SELECT s.id FROM SurveyTemplate s WHERE (s.owner = :user) "
    + "OR s.id IN (SELECT sp.surveyTemplate.id FROM SurveyPublication sp "
    + "JOIN sp.studentGroup g JOIN g.members.members m "
    + "WHERE m.user = :user AND m.role = 'STUDENT') ))"),
    @NamedQuery(name = PublishedSurveyTemplateDto.Query.GET_IDS_IN_RESEARCH, query = "SELECT s.id FROM Research r "
    + "JOIN r.publishedSurveys s WHERE r.id = :id")
})
public class PublishedSurveyTemplateDto implements Serializable {

    private static final long serialVersionUID = -6450748034763875895L;

    public static final class Query {

        public static final String GET_SURVEY_TEMPLATE_IDS_IN_GROUPS =
                "SurveyTemplate.getSurveyTemplatesInGroup";
        public static final String GET_SURVEY_TEMPLATE_IDS =
                "SurveyTemplate.getSurveyTemplates";
        public static final String GET_SURVEY_TEMPLATE =
                "SurveyTemplate.getSurveyTemplate";
        public static final String GET_IDS_IN_RESEARCH =
                "PublishedSurveyTemplateDto.getIdsInResearch";

        private Query() {
        }
    }
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "TEMPLATE_ID")
    private Long templateId;
    @Column(name = "TITLE")
    private String name;
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Column(name = "GROUP_NAME")
    private String groupName;
    @Column(name = "ENABLED_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startingDate;
    @Column(name = "ENABLED_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
    @Transient
    private Long filledSurveyId;
    @Transient
    private Boolean submitted;
    @Transient
    private SurveyStatusEnum status;

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

    public boolean isFilled() {
        return Boolean.TRUE.equals(submitted) || (null != filledSurveyId);
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getFilledSurveyId() {
        return filledSurveyId;
    }

    public void setFilledSurveyId(Long filledSurveyId) {
        this.filledSurveyId = filledSurveyId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Boolean getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public SurveyStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SurveyStatusEnum status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PublishedSurveyTemplateDto other =
                (PublishedSurveyTemplateDto) obj;
        if (this.id != other.id
                && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(
                other.name)) {
            return false;
        }
        return true;
    }

    public boolean isFillable(Date date) {
        boolean beforeStart = startingDate != null && date.before(startingDate);
        boolean afterExpiration = expirationDate != null && date.after(
                expirationDate);
        return !(beforeStart || afterExpiration);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.id == null ? 0 : this.id.hashCode());
        hash = 89 * hash + (this.name == null ? 0 : this.name.hashCode());
        return hash;
    }

    @Override
    public String toString() {
        return "SurveyTemplateDto [id=" + id + ", name=" + name + ", groupId="
                + groupId + "]";
    }
}
