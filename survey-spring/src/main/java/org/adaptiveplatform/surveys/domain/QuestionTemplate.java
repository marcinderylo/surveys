package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.adaptiveplatform.surveys.exception.AnswerWithCommentAllowingOtherAnswersException;
import org.hibernate.annotations.IndexColumn;

/**
 * @author Rafal Jamroz
 */
@Entity
@Table(name = "QUESTION_TEMPLATES")
public class QuestionTemplate implements Serializable {

    private static final long serialVersionUID = 6316885542723608632L;
    public static final String NUMBER = "NUMBER";
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "TEXT")
    private String text;
    @Column(name = "HTML_TEXT")
    private String htmlText;
    @Enumerated(EnumType.STRING)
    @Column(name = "QUESTION_TYPE", nullable = false)
    private QuestionType type;
    @Column(name = "STYLE")
    private String style;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "QUESTION_ID")
    @IndexColumn(base = 1, name = "NUMBER")
    private List<AnswerTemplate> possibleAnswers =
            new ArrayList<AnswerTemplate>();

    protected QuestionTemplate() {
        // To be used only by object persistence framework
    }

    public QuestionTemplate(String text, String htmlText,
            QuestionType questionType) {
        this.text = text;
        this.htmlText = htmlText;
        this.type = questionType;
    }

    public boolean hasAnswer(int answerNumber) {
        return getAnswer(answerNumber) != null;
    }

    public AnswerTemplate getAnswer(Integer answerNumber) {
        return possibleAnswers.get(answerNumber - 1);
    }

    public AnswerTemplate addAnswer(String text) {
        return addAnswer(text, false, false);
    }

    public AnswerTemplate addAnswer(String text, boolean requiresComment,
            boolean disallowsOtherAnswers) {
        if (requiresComment && !disallowsOtherAnswers) {
            throw new AnswerWithCommentAllowingOtherAnswersException(text, id);
        }
        AnswerTemplate answerTemplate = new AnswerTemplate(text);
        answerTemplate.setRequiresComment(requiresComment);
        answerTemplate.setDisallowsOtherAnswers(disallowsOtherAnswers);
        possibleAnswers.add(answerTemplate);
        return answerTemplate;
    }

    public AnsweredQuestion answeredQuestion() {
        AnsweredQuestion question = new AnsweredQuestion(this,
                possibleAnswers);
        return question;
    }

    public String getText() {
        return text;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public QuestionType getType() {
        return type;
    }

    boolean hasAnswers() {
        return !possibleAnswers.isEmpty();
    }

    @Override
    public String toString() {
        return "QuestionTemplate [id=" + id + ", text=" + text
                + ", possibleAnswers="
                + possibleAnswers + "]";
    }

    public boolean isAnswered(AnsweredQuestion answer) {
        return type.isAnswered(answer);
    }

    void validateAnswer(List<Integer> answerIds, String comment) {
        type.validateAnswer(this, answerIds, comment);
    }

    List<AnswerTemplate> getAnswers() {
        return possibleAnswers;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Long getId() {
        return id;
    }
}
