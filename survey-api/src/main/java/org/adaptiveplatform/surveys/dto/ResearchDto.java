package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.adaptiveplatform.codegenerator.api.RemoteObject;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.IndexColumn;

/**
 *
 * @author Marcin Deryło
 */
@Entity
@Table(name = "RESEARCHES")
@org.hibernate.annotations.Entity(mutable = false)
@RemoteObject
@NamedQueries({@NamedQuery(name = ResearchDto.Queries.GET_RESEARCH,
    query = "SELECT DISTINCT r FROM ResearchDto r JOIN FETCH r.questions qs "
    + "LEFT JOIN FETCH qs.definedTags LEFT JOIN FETCH qs.searchPhrases "
    + "JOIN FETCH r.templateDto st JOIN FETCH st.questions q "
    + "LEFT JOIN FETCH q.answers "
    + "WHERE r.id = :id AND r.id IN (SELECT rs.id FROM Research rs "
    + "JOIN rs.template t WHERE t.owner.id = :userId)")
})
public class ResearchDto implements Serializable {

    public static final class Queries {

        public static final String GET_RESEARCH = "ResearchDto.getById";

        private Queries() {
        }
    }
    /**
     * Identifier of this research.
     */
    @Id
    @Column(name = "ID", insertable = false, updatable = false)
    private Long id;
    /**
     * Name of the research, assigned by creator.
     */
    @Column(name = "RESEARCH_NAME", insertable = false, updatable = false)
    private String name;
    /**
     * Survey template on which this research is based. Note that the
     * {@code questions} property of this object might not be initialized in
     * case when a list of ResearchDto is read from the DAO.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "SURVEY_TEMPLATE_ID", insertable = false, updatable =
    false)
    private SurveyTemplateDto templateDto;
    /**
     * Progress of filling of published surveys in this research. Expected total
     * count of filled surveys equals to the sum of expected surveys counts from
     * all published surveys in this research (the same for actual count).
     */
    @Transient
    private FillingProgressDto fillingProgress;
    /**
     * Indicates the status of the research, based on statuses of child published
     * templates. If all published surveys of the research are terminated, the
     * research will have status {@link FillingStatusEnum#FILLING_TERMINATED}.
     * If all published surveys are not yet available for filling the research
     * will have status {@link FillingStatusEnum#NOT_YET_FILLING}. Otherwise the
     * status will be {@link FillingStatusEnum#FILLING_IN_PROGRESS}.
     */
    @Transient
    private FillingStatusEnum status;
    /**
     * Published survey templates in the research. This property might not be
     * loaded in case when a list of ResearchDto is read from the DAO.
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESEARCH_ID")
    @IndexColumn(name = "NUMBER", base = 1)
    @Immutable
    private List<QuestionEvaluationDto> questions =
            new ArrayList<QuestionEvaluationDto>();
    @Transient
    private List<FilledSurveyDto> submittedSurveys =
            new ArrayList<FilledSurveyDto>();

    public FillingProgressDto getFillingProgress() {
        return fillingProgress;
    }

    public void setFillingProgress(FillingProgressDto fillingProgress) {
        this.fillingProgress = fillingProgress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FillingStatusEnum getStatus() {
        return status;
    }

    public void setStatus(FillingStatusEnum status) {
        this.status = status;
    }

    public SurveyTemplateDto getTemplateDto() {
        return templateDto;
    }

    public void setTemplateDto(SurveyTemplateDto templateDto) {
        this.templateDto = templateDto;
    }

    public List<QuestionEvaluationDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionEvaluationDto> questions) {
        this.questions = questions;
    }

    public List<FilledSurveyDto> getSubmittedSurveys() {
        return submittedSurveys;
    }

    public void setSubmittedSurveys(List<FilledSurveyDto> submittedSurveys) {
        this.submittedSurveys = submittedSurveys;
    }
}
