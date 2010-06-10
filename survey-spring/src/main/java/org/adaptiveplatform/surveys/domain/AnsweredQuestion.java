package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.adaptiveplatform.surveys.exception.InvalidAnswerNumbersException;
import org.adaptiveplatform.surveys.exception.NoSuchAnswerException;
import org.apache.commons.lang.Validate;
import org.hibernate.annotations.IndexColumn;

/**
 * Changes its state depending on the given answers (not answered, answered
 * incorrectly, answered correctly).
 */
@Entity
@Table(name = "ANSWERED_QUESTIONS")
public class AnsweredQuestion implements Serializable {

    private static final long serialVersionUID = -8986835577442709382L;
    public static final String QUESTION = "question";
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "COMMENT")
    private String comment;
    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private QuestionTemplate question;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ANSWERED_QUESTION_ID")
    @IndexColumn(name = "NUMBER", base = 1)
    private List<AnsweredQuestionAnswer> answers;

    protected AnsweredQuestion() {
        // To be used only by object persistence framework
    }

    public AnsweredQuestion(QuestionTemplate template,
            List<AnswerTemplate> possibleAnswers) {
        question = template;
        answers = new ArrayList<AnsweredQuestionAnswer>();
        for (AnswerTemplate answer : possibleAnswers) {
            answers.add(new AnsweredQuestionAnswer(answer));
        }
    }

    public String getComment() {
        return comment;
    }

    public Long getQuestionTemplateId() {
        return question.getId();
    }

    public boolean isAnswerSelected(Integer answerNumber) {
        AnsweredQuestionAnswer answer = getAnswer(answerNumber);
        if (answer != null) {
            return answer.isSelected();
        }
        throw new NoSuchAnswerException(answerNumber, id);
    }

    private AnsweredQuestionAnswer getAnswer(int number) {
        // FIXME turn into validation errors
        Validate.isTrue(number > 0, "Answer numbers start with 1");
        Validate.isTrue(number <= answers.size(), "No answer with number "
                + number + "in the question");
        int index = number - 1;
        return answers.get(index);
    }

    public void answer(List<Integer> answerNumbers, String comment) {
        question.validateAnswer(answerNumbers, comment);

        this.comment = comment;

        Set<Integer> answerNumbersCopy = new HashSet<Integer>();
        if (answerNumbers != null) {
            answerNumbersCopy.addAll(answerNumbers);
        }
        for (int i = 0; i < answers.size(); i++) {
            AnsweredQuestionAnswer answer = answers.get(i);
            final int number = i + 1;
            answer.setSelected(answerNumbersCopy.contains(
                    number));
            answerNumbersCopy.remove(number);
        }
        if (!answerNumbersCopy.isEmpty()) {
            throw new InvalidAnswerNumbersException(answerNumbersCopy, id);
        }
    }

    List<AnsweredQuestionAnswer> getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return "AnsweredQuestion [id=" + id + ", question=" + question
                + ", comment=" + comment + ", answers="
                + answers + "]";
    }

    public boolean isAnswered() {
        return question.isAnswered(this);
    }
}
