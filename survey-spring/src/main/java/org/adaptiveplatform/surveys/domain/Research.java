package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.adaptiveplatform.surveys.exception.PublicationOfDifferentTemplateException;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author Marcin Deryło
 */
@Entity
@Table(name = "RESEARCHES")
@NamedQueries({
    @NamedQuery(name = Research.Queries.LIST_RESEARCHES, query = "SELECT r.id "
    + "FROM Research r JOIN r.template t WHERE t.owner.id = :evaluatorId")
})
public class Research implements Serializable {

    public static final class Queries {

        public static final String LIST_RESEARCHES = "Research.getAll";

        private Queries() {
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "SURVEY_TEMPLATE_ID")
    private SurveyTemplate template;
    @Column(name = "RESEARCH_NAME")
    private String name;
    @ManyToMany
    @JoinTable(name = "RESEARCHES_PUBLICATIONS", joinColumns =
    @JoinColumn(name = "PUBLICATION_ID"), inverseJoinColumns =
    @JoinColumn(name = "RESEARCH_ID"))
    private List<SurveyPublication> publishedSurveys =
            new ArrayList<SurveyPublication>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "RESEARCH_ID")
    @IndexColumn(name = "NUMBER", base = 1)
    private List<QuestionEvaluation> questionEvaluations =
            new ArrayList<QuestionEvaluation>();
    @Column(name = "CREATION_DATE")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime creationDate = new DateTime();

    protected Research() {
        // To be used only by object persistence framework
    }

    public Research(String name, SurveyTemplate template) {
        this.name = name;
        this.template = template;
        for (QuestionTemplate question : template.getQuestions()) {
            QuestionEvaluation evaluation = new QuestionEvaluation(question);
            questionEvaluations.add(evaluation);
        }
    }

    public QuestionEvaluation getQuestionEvaluation(int number) {
        return questionEvaluations.get(number - 1);
    }

    public Long getEvaluatorId() {
        return template.getOwnerId();
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public List<QuestionEvaluation> getQuestionEvaluations() {
        return Collections.unmodifiableList(questionEvaluations);
    }

    public List<SurveyPublication> getPublications() {
        return Collections.unmodifiableList(publishedSurveys);
    }

    public void removePublication(SurveyPublication publication) {
        publishedSurveys.remove(publication);
    }

    public void addSurveyPublication(SurveyPublication publication) {
        final Long templateId = template.getId();
        final SurveyTemplate publishedTemplate = publication.getSurveyTemplate();
        if (!templateId.equals(publishedTemplate.getId())) {
            throw new PublicationOfDifferentTemplateException(publication.
                    getSurveyTemplate().getId(), templateId, name);
        }
        publishedSurveys.add(publication);
    }
}
