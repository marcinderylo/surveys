package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.adaptiveplatform.codegenerator.api.RemoteExclude;
import org.adaptiveplatform.codegenerator.api.RemoteObject;


@RemoteObject
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "SURVEY_QUESTION_ANSWER_DTOS")
public class SurveyQuestionAnswerDto implements Serializable {

	private static final long serialVersionUID = 4809497042996851576L;
	/**
	 * Won't be sent to client.
	 */
	@RemoteExclude
	@Id
	@Column(name = "ID")
	private Long id;
	@Column(name = "NUMBER", insertable = false, updatable = false)
	private Integer number;
	@Column(name = "TEXT", insertable = false, updatable = false)
	private String text;
	@Column(name = "SELECTED", insertable = false, updatable = false)
	private Boolean selected;
	@Column(name = "REQUIRES_COMMENT")
	private Boolean requiresComment;
        @Column(name = "DISALLOWS_OTHER_ANSWERS")
        private Boolean disallowsOtherAnswers;

	public SurveyQuestionAnswerDto() {
		requiresComment = Boolean.FALSE;
	}

	public SurveyQuestionAnswerDto(String text) {
		this();
		this.text = text;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public Boolean getRequiresComment() {
		return requiresComment;
	}

	public void setRequiresComment(Boolean requiresComment) {
		this.requiresComment = requiresComment;
	}

        public Boolean getDisallowsOtherAnswers() {
                return disallowsOtherAnswers;
        }

        public void setDisallowsOtherAnswers(Boolean disallowsOtherAnswers) {
                this.disallowsOtherAnswers = disallowsOtherAnswers;
        }

	@Override
	public String toString() {
		return "SurveyQuestionAnswerDto [id=" + id + ", answerId=" + number + ", selected=" + selected + ", text="
				+ text + "]";
	}
}
