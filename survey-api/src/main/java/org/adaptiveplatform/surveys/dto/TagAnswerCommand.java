package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.adaptiveplatform.adapt.commons.validation.constraints.ValidId;
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

	@ValidId
	public Long getResearchId() {
		return researchId;
	}

	public void setResearchId(Long researchId) {
		this.researchId = researchId;
	}

	@ValidId
	public Long getFilledSurveyId() {
		return filledSurveyId;
	}

	public void setFilledSurveyId(Long filledSurveyId) {
		this.filledSurveyId = filledSurveyId;
	}

	@NotNull
	@Min(1)
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
