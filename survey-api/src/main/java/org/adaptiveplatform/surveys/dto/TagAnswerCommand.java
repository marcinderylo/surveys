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
public class TagAnswerCommand implements Serializable {

    private Long researchId;
    private Long filledSurveyId;
    private Integer questionNumber;
    private Set<String> setTags = new HashSet<String>();

    public Long getResearchId() {
        return researchId;
    }

    public void setResearchId(Long researchId) {
        this.researchId = researchId;
    }

    public Long getFilledSurveyId() {
        return filledSurveyId;
    }

    public void setFilledSurveyId(Long filledSurveyId) {
        this.filledSurveyId = filledSurveyId;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public Set<String> getSetTags() {
        return setTags;
    }

    public void setSetTags(Set<String> setTags) {
        this.setTags = setTags;
    }
}
