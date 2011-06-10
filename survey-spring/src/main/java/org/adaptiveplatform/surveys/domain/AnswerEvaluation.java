package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;

/**
 * @author Marcin Deryło
 */
@Entity
@Table(name = "ANSWER_EVALUATIONS")
public class AnswerEvaluation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RESEARCH_ID")
    private Research research;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ANSWERED_QUESTION_ID")
    private AnsweredQuestion answeredQuestion;
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "ASSIGNED_TAGS", joinColumns = {
        @JoinColumn(name = "ANSWER_EVALUATION_ID")})
    @Column(name = "TAG")
    private Set<String> tags = new HashSet<String>();

    protected AnswerEvaluation() {
        // To be used only by object persistence framework
    }

    public AnswerEvaluation(Research research, AnsweredQuestion answeredQuestion) {
        // FIXME turn into validation errors?
        Validate.notNull(research, "Research must be specified");
        Validate.notNull(answeredQuestion, "Answered question must be specified");
        this.research = research;
        this.answeredQuestion = answeredQuestion;
    }

    public void setTags(Collection<String> tagsToSet) {
        tags.clear();
        tags.addAll(tagsToSet);
    }
}

