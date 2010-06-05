package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.adaptiveplatform.codegenerator.api.RemoteObject;


/**
 *
 * @author Marcin Deryło
 */
@RemoteObject
public class QuestionAnswerDto implements Serializable {

    /**
     * Comment given by the filling student, suplementing the answers selected,
     * or being the answer for open question.
     */
    private String comment;
    /**
     * Indexes of answers selected by the filling student.
     */
    private Set<Integer> selectedAnswers = new HashSet<Integer>();
    /**
     * Flags applied to the answer during evaluation.
     */
    private Set<String> flags = new HashSet<String>();
    /**
     * Identifier of the group from which the filled survey containing this
     * answer origins.
     */
    private Long groupId;
    /**
     * Identifier of the filled survey containing this answer.
     */
    private Long filledSurveyId;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<String> getFlags() {
        return flags;
    }

    public void setFlags(Set<String> flags) {
        this.flags = flags;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Set<Integer> getSelectedAnswers() {
        return selectedAnswers;
    }

    public void setSelectedAnswers(Set<Integer> selectedAnswers) {
        this.selectedAnswers = selectedAnswers;
    }

    public Long getFilledSurveyId() {
        return filledSurveyId;
    }

    public void setFilledSurveyId(Long filledSurveyId) {
        this.filledSurveyId = filledSurveyId;
    }
}
