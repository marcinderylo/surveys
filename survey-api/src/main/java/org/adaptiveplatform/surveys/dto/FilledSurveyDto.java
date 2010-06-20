package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.adaptiveplatform.codegenerator.api.RemoteExclude;
import org.adaptiveplatform.codegenerator.api.RemoteObject;
import org.hibernate.annotations.IndexColumn;

@RemoteObject
@Entity
@Table(name = "SURVEY_DTOS")
@org.hibernate.annotations.Entity(mutable = false)
@NamedQueries({
    @NamedQuery(name = FilledSurveyDto.Query.GET_SURVEY,
    query = "SELECT s FROM FilledSurveyDto s "
    + "WHERE (s.id = :surveyId) AND (s.userId = :userId OR "
    + "s.surveyTemplateId IN (SELECT st FROM SurveyTemplate st WHERE st.owner.id = :userId) )"),
    @NamedQuery(name = FilledSurveyDto.Query.GET_FOR_RESEARCH,
    query =
    "SELECT DISTINCT s FROM FilledSurveyDto s "
    + "JOIN FETCH s.questions q LEFT JOIN FETCH q.answers "
    + "WHERE  s.id IN (SELECT fs.id FROM "
    + "FilledSurvey fs WHERE fs.finishDate IS NOT NULL AND fs.template.id IN (:publishedSurveyTemplates))")
})
public class FilledSurveyDto implements Serializable {

    private static final long serialVersionUID = 7989618252691737747L;

    public static final class Query {

        public static final String GET_SURVEY = "SurveyDto.getSurvey";
        public static final String GET_FOR_RESEARCH = "SurveyDto.getForResearch";

        private Query() {
        }
    }
    @Id
    @Column(name = "ID")
    private Long id;
    @RemoteExclude
    @Column(name = "PUBLICATION_ID", insertable = false, updatable = false)
    private Long publicationId;
    @Column(name = "TEMPLATE_ID", insertable = false, updatable = false)
    private Long surveyTemplateId;
    @Column(name = "TEMPLATE_NAME", insertable = false, updatable = false)
    private String surveyTemplateName;
    @Column(name = "GROUP_NAME")
    private String groupName;
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Column(name = "START_DATE")
    private Date startDate;
    @Column(name = "FINISH_DATE")
    private Date submitDate;
    @Column(name = "TEMPLATE_DESCRIPTION", insertable = false, updatable = false)
    private String description;
    @RemoteExclude
    @Column(name = "USER_ID", insertable = false, updatable = false)
    private Long userId;
    @RemoteExclude
    @Column(name = "USER_NAME", insertable = false, updatable = false)
    private String userName;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "SURVEY_ID")
    @IndexColumn(base = 1, name = "NUMBER")
    private List<SurveyQuestionDto> questions =
            new ArrayList<SurveyQuestionDto>();

    public Long getId() {
        return id;
    }

    public void setQuestions(List<SurveyQuestionDto> questions) {
        this.questions = questions;
    }

    public List<SurveyQuestionDto> getQuestions() {
        return questions;
    }

    public Long getSurveyTemplateId() {
        return surveyTemplateId;
    }

    public String getSurveyTemplateName() {
        return surveyTemplateName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSurveyTemplateId(Long surveyTemplateId) {
        this.surveyTemplateId = surveyTemplateId;
    }

    public void setSurveyTemplateName(String surveyTemplateName) {
        this.surveyTemplateName = surveyTemplateName;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SurveyDto [id=" + id + ", startDate=" + startDate
                + ", submitDate=" + submitDate
                + ", surveyTemplateId=" + surveyTemplateId
                + ", surveyTemplateName=" + surveyTemplateName + ", userId="
                + userId + ", userName=" + userName + ", questions=" + questions
                + "]";
    }
}
