package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.RemoteObject;


/**
 *
 * @author Marcin Deryło
 */
@RemoteObject
public class CommentQuestionCommand implements Serializable {

    private Long researchId;
    private Integer questionId;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Long getResearchId() {
        return researchId;
    }

    public void setResearchId(Long researchId) {
        this.researchId = researchId;
    }
}
