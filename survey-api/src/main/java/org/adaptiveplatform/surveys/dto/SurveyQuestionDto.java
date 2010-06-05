package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.adaptiveplatform.codegenerator.api.RemoteExclude;
import org.adaptiveplatform.codegenerator.api.RemoteObject;
import org.hibernate.annotations.IndexColumn;

@RemoteObject
@Entity
@Table(name = "SURVEY_QUESTION_DTOS")
@org.hibernate.annotations.Entity(mutable = false)
public class SurveyQuestionDto implements Serializable {

    private static final long serialVersionUID = -6346835324486752664L;
    /**
     * Won't be sent to client.
     */
    @RemoteExclude
    @Id
    @Column(name = "ID", insertable = false, updatable = false)
    private Long id;
    @Column(name = "NUMBER", insertable = false, updatable = false)
    private Integer number;
    @Column(name = "TEXT", insertable = false, updatable = false)
    private String text;
    @Column(name = "HTML_TEXT", insertable = false, updatable = false)
    private String htmlText;
    @Column(name = "COMMENT", insertable = false, updatable = false)
    private String comment;
    @Column(name = "STYLE", insertable = false, updatable = false)
    private String style;
    @Column(name = "QUESTION_TYPE")
    @Enumerated(EnumType.STRING)
    private QuestionTypeEnum type;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ANSWERED_QUESTION_ID", insertable = false, updatable =
    false)
    @IndexColumn(base = 1, name = "NUMBER")
    private List<SurveyQuestionAnswerDto> answers =
            new ArrayList<SurveyQuestionAnswerDto>();
    @Transient
    private Set<String> tags = new HashSet<String>();

    public SurveyQuestionDto() {
        // to be used rather by serialization framework
    }

    public SurveyQuestionDto(String text, QuestionTypeEnum type) {
        this.text = text;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<SurveyQuestionAnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SurveyQuestionAnswerDto> answers) {
        this.answers = answers;
    }

    public QuestionTypeEnum getType() {
        return type;
    }

    public void setType(QuestionTypeEnum type) {
        this.type = type;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "SurveyQuestionDto [id=" + id + ", questionId=" + number
                + ", text=" + text + ", comment=" + comment
                + ", answers=" + answers + "]";
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
