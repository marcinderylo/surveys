package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.adaptiveplatform.codegenerator.api.RemoteExclude;
import org.adaptiveplatform.codegenerator.api.RemoteObject;
import org.hibernate.annotations.CollectionOfElements;

/**
 *
 * @author Marcin Deryło
 */
@RemoteObject
@Entity
@Table(name = "QUESTION_EVALUATIONS")
@org.hibernate.annotations.Entity(mutable = false)
public class QuestionEvaluationDto implements Serializable {

    @Id
    @Column(name = "ID", insertable = false, updatable = false)
    @RemoteExclude
    private Long id;
    /**
     * Identifier of the {@link QuestionTemplateDto} represented, with extra
     * information, by this object.
     */
    @Column(name = "QUESTION_TEMPLATE_ID", insertable = false,
    updatable = false)
    private Long questionTemplateId;
    /**
     * Name of tags/categories defined by the evaluator for this question in
     * scope of the research.
     */
    @CollectionOfElements
    @JoinTable(name = "EVALUATOR_DEFINED_TAGS", joinColumns = {
        @JoinColumn(name = "QUESTION_EVALUATION_ID")})
    @Column(name = "TAG")
    private Set<String> definedTags = new HashSet<String>();
    /**
     * Search pharses remembered for the question.
     */
    @CollectionOfElements
    @JoinTable(name = "SEARCH_PHRASES", joinColumns = {
        @JoinColumn(name = "QUESTION_EVALUATION_ID")})
    @Column(name = "SEARCH_PHRASE")
    private Set<String> searchPhrases = new HashSet<String>();
    /**
     * Clipboard for evaluator's comments for this question in the research.
     */
    // TODO do we need 2 versions (plain text/html) like for question text?
    @Column(name = "COMMENT", length = 2048, insertable = false,
    updatable = false)
    private String comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<String> getDefinedTags() {
        return definedTags;
    }

    public void setDefinedTags(Set<String> definedTags) {
        this.definedTags = definedTags;
    }

    public Long getQuestionTemplateId() {
        return questionTemplateId;
    }

    public void setQuestionTemplateId(Long questionTemplateId) {
        this.questionTemplateId = questionTemplateId;
    }

    public Set<String> getSearchPhrases() {
        return searchPhrases;
    }

    public void setSearchPhrases(Set<String> searchPhrases) {
        this.searchPhrases = searchPhrases;
    }
}
