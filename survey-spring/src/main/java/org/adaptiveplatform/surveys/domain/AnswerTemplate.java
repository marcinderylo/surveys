package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Immutable question value object.
 * 
 * @author Marcin Derylo
 */
@Entity
@Table(name = "ANSWER_TEMPLATES")
public class AnswerTemplate implements Serializable {

    private static final long serialVersionUID = 1273636135067179988L;
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "TEXT")
    private String text;
    @Column(name = "REQUIRES_COMMENT", nullable = false)
    private Boolean requiresComment;
    @Column(name = "DISALLOWS_OTHER_ANSWERS", nullable = false)
    private Boolean disallowsOtherAnswers;

    protected AnswerTemplate() {
        // To be used only by object persistence framework
    }

    public AnswerTemplate(String text) {
        this.text = text;
        requiresComment = Boolean.FALSE;
        disallowsOtherAnswers = Boolean.FALSE;
    }

    public String getText() {
        return text;
    }

    public Boolean requiresComment() {
        return requiresComment;
    }

    void setRequiresComment(Boolean requiresComment) {
        this.requiresComment = requiresComment;
    }

    public Boolean disallowsOtherAnswers() {
        return disallowsOtherAnswers;
    }

    void setDisallowsOtherAnswers(Boolean disallowsOtherAnswers) {
        this.disallowsOtherAnswers = disallowsOtherAnswers;
    }

    @Override
    public String toString() {
        return "AnswerTemplate [id=" + id + ", text=" + text + "]";
    }
}
