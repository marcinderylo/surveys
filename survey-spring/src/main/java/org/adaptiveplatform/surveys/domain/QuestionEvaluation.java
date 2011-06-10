package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;

/**
 *
 * @author Marcin Deryło
 */
@Entity
@Table(name = "QUESTION_EVALUATIONS")
public class QuestionEvaluation implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_TEMPLATE_ID")
    private QuestionTemplate question;
    @Column(name = "COMMENT", length = 2048)
    private String evaluationComment;
    @ElementCollection
    @JoinTable(name = "EVALUATOR_DEFINED_TAGS", joinColumns = {
        @JoinColumn(name = "QUESTION_EVALUATION_ID")})
    @Column(name = "TAG")
    private Set<String> definedTags = new HashSet<String>();
    @ElementCollection
    @JoinTable(name = "SEARCH_PHRASES", joinColumns = {
        @JoinColumn(name = "QUESTION_EVALUATION_ID")})
    @Column(name = "SEARCH_PHRASE")
    private Set<String> searchPhrases = new HashSet<String>();

    protected QuestionEvaluation() {
        // To be used only by object persistence framework
    }

    public QuestionEvaluation(QuestionTemplate question) {
        Validate.notNull(question,
                "Must specify a question template to be evaluated");
        this.question = question;
    }

    public void setEvaluationComment(String evaluationComment) {
        this.evaluationComment = evaluationComment;
    }

    public void rememberSearchPhrase(String phrase) {
        searchPhrases.add(phrase);
    }

    public void defineTag(String tag) {
        definedTags.add(tag);
    }

    public void deleteTag(String tag) {
        definedTags.remove(tag);
    }
}
