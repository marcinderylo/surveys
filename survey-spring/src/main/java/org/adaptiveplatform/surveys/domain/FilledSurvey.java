package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.FilledSurveyAlreadySubmittedException;
import org.adaptiveplatform.surveys.exception.InvalidQuestionNumberException;
import org.adaptiveplatform.surveys.exception.OutsideOfFillingPeriodException;
import org.adaptiveplatform.surveys.exception.SurveyNotFilledCompletelyException;
import org.apache.commons.lang.Validate;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "FILLED_SURVEYS")
public class FilledSurvey implements Serializable {

    private static final long serialVersionUID = 5501704637967526285L;
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "PUBL_TEMPLATE_ID")
    private SurveyPublication template;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "SURVEY_ID")
    @IndexColumn(base = 1, name = "NUMBER")
    private List<AnsweredQuestion> answers = new ArrayList<AnsweredQuestion>();
    @Column(name = "START_DATE")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime startDate;
    @Column(name = "FINISH_DATE")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime finishDate;
    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    private UserDto user;

    protected FilledSurvey() {
        // To be used only by object persistence framework
    }

    public FilledSurvey(SurveyPublication publishedTemplate, UserDto user) {
        // FIXME turn into validation errors?
        Validate.notNull(publishedTemplate,
                "Survey must be created from template");
        Validate.notNull(user, "Survey must be created for a user");
        this.template = publishedTemplate;
        this.user = user;

        for (QuestionTemplate question : template.getSurveyTemplate().
                getQuestions()) {
            answers.add(question.answeredQuestion());
        }
    }

    public boolean isStarted() {
        return startDate != null;
    }

    public boolean isFinished() {
        return finishDate != null;
    }

    public void answerQuestion(int questionId, List<Integer> answerIds,
            String comment) {
        if (!isStarted()) {
            // this is so unlikely that it's better left a simple runtime exception
            throw new IllegalStateException(
                    "cant answer questions when survey has not been started yet");
        }
        if (isFinished()) {
            throw new FilledSurveyAlreadySubmittedException(id);
        }
        AnsweredQuestion question = getAnsweredQuestion(questionId);
        if (question == null) {
            throw new InvalidQuestionNumberException(questionId, id);
        }
        question.answer(answerIds, comment);
    }

    public boolean isAnswerSelected(int questionId, int answerId) {
        return getAnsweredQuestion(questionId).isAnswerSelected(answerId);
    }

    public void startFilling() {
        DateTime now = new DateTime();
        checkFillingEnabled(now);
        startDate = now;
    }

    private void checkFillingEnabled(DateTime atTime) {
        if (!template.isFillingEnabled(atTime)) {
            throw new OutsideOfFillingPeriodException(id, template.getId());
        }
    }

    /**
     * Submits given survey.
     *
     * @throws IllegalStateException
     *             if not all questions have been answered
     */
    public void submit() {
        DateTime now = new DateTime();
        checkFillingEnabled(now);
        for (AnsweredQuestion question : answers) {
            if (!question.isAnswered()) {
                throw new SurveyNotFilledCompletelyException(id);
            }
        }
        finishDate = now;
    }

    public AnsweredQuestion getAnsweredQuestion(int questionNumber) {
        Validate.isTrue(questionNumber > 0);
        Validate.isTrue(questionNumber <= answers.size());
        int index = questionNumber - 1;
        return answers.get(index);
    }

    public Long getId() {
        return id;
    }

    public Long getOwnwerId() {
        return user.getId();
    }

    @Override
    public String toString() {
        return "FilledSurvey [id=" + id + ", template=" + template + ", user="
                + user + ", startDate=" + startDate
                + ", finishDate=" + finishDate + ", answers=" + answers + "]";
    }
}
