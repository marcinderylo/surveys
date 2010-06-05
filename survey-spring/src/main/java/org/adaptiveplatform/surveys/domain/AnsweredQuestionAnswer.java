package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ANSWERED_QUESTIONS_ANSWERS")
public class AnsweredQuestionAnswer implements Serializable {
	private static final long serialVersionUID = -4387002279445365967L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "ANSWER_TEMPLATE_ID")
	private AnswerTemplate answer;
	@Column(name = "SELECTED")
	private Boolean selected;

	protected AnsweredQuestionAnswer() {
        // To be used only by object persistence framework
	}

	public AnsweredQuestionAnswer(AnswerTemplate answer) {
		this.answer = answer;
		selected = false;
	}

	public boolean isSelected() {
		return selected;
	}

	void setSelected(boolean selected) {
		this.selected = selected;
	}

	AnswerTemplate getAnswerTemplate() {
		return answer;
	}

	@Override
	public String toString() {
		return "AnsweredQuestionAnswer [id=" + id + ", answer=" + answer + ", selected=" + selected + "]";
	}
}
