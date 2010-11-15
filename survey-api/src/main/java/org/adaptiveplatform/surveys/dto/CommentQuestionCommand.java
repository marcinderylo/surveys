package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.adaptiveplatform.adapt.commons.validation.constraints.NonBlank;
import org.adaptiveplatform.adapt.commons.validation.constraints.ValidId;
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

	@NonBlank
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@NotNull
	@Min(1)
	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	@ValidId
	public Long getResearchId() {
		return researchId;
	}

	public void setResearchId(Long researchId) {
		this.researchId = researchId;
	}
}
