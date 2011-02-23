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
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Marcin Deryło
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "ANSWER_EVALUATIONS")
public class AnswerEvaluationDto implements Serializable {

    @Id
    @Column(name = "ID", insertable = false, updatable = false)
    private Long id;
    @Column(name = "RESEARCH_ID", insertable = false, updatable = false)
    private Long researchId;

    @Column(name = "ANSWERED_QUESTION_ID", insertable = false, updatable = false)
    private Long answeredQuestionId;
    @Immutable
    @CollectionOfElements(fetch = FetchType.EAGER)
    @JoinTable(name = "ASSIGNED_TAGS", joinColumns = {
        @JoinColumn(name = "ANSWER_EVALUATION_ID")})
    @Column(name = "TAG")
    private Set<String> tags = new HashSet<String>();

    public AnswerEvaluationDto() {
    }

    public Long getAnsweredQuestionId() {
        return answeredQuestionId;
    }

    public void setAnsweredQuestionId(Long answeredQuestionId) {
        this.answeredQuestionId = answeredQuestionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResearchId() {
        return researchId;
    }

    public void setResearchId(Long researchId) {
        this.researchId = researchId;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
