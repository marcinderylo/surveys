package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.PublishingTemplateWithoutQuestionsException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "SURVEY_TEMPLATES")
public class SurveyTemplate implements Serializable {

    private static final long serialVersionUID = 6610987952720609853L;
    public static String QUESTIONS = "questions";
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "TEMPLATE", nullable = false)
    @IndexColumn(base = 1, name = QuestionTemplate.NUMBER)
    private List<QuestionTemplate> questions;
    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserDto owner;
    @Column(name = "CREATION_DATE")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime creationDate = new DateTime();

    protected SurveyTemplate() {
        // To be used only by object persistence framework
    }

    public SurveyTemplate(UserDto creator, String title) {
        this.title = title;
        this.owner = creator;
        questions = new ArrayList<QuestionTemplate>();
    }

    public QuestionTemplate getQuestion(int questionNumber) {
        return questions.get(questionNumber - 1);
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public void publish() {
        if (questionsCount() == 0) {
            throw new PublishingTemplateWithoutQuestionsException(id);
        }
    }

    public QuestionTemplate addQuestion(QuestionTemplate question) {
        questions.add(question);
        return question;
    }

    public long questionsCount() {
        return questions.size();
    }

    public List<QuestionTemplate> getQuestions() {
        return new ArrayList<QuestionTemplate>(questions);
    }

    public Long getOwnerId() {
        return owner.getId();
    }

    @Override
    public String toString() {
        return "SurveyTemplate [id=" + id + ", title=" + title + ", questions=" + questions + "]";
    }

    public void removeQuestions() {
        questions.clear();
    }

    public void setTitle(String name) {
        Validate.notNull(name, "Survey template must have a name");
        this.title = name;
    }

    public void setDescription(String description) {
        this.description = StringUtils.defaultIfEmpty(description, "");
    }

    public String getDescription() {
        return description;
    }
}
