package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.adaptiveplatform.codegenerator.api.RemoteObject;
import org.hibernate.annotations.IndexColumn;

@Entity
@RemoteObject
@Table(name = "SURVEY_TEMPLATES")
@org.hibernate.annotations.Entity(mutable = false)
@NamedQueries({
        @NamedQuery(name = SurveyTemplateDto.Query.GET_TEMPLATE_QUERY, 
        query = "SELECT s FROM SurveyTemplateDto s " +
                "JOIN FETCH s.questions q " +
                "LEFT JOIN FETCH q.answers WHERE s.id = " +
                "(SELECT st.id FROM SurveyTemplate st " +
                "WHERE st.id= :templateId AND st.owner = :owner)"),
        @NamedQuery(name = SurveyTemplateDto.Query.GET_TEMPLATES_QUERY,
        query = "FROM SurveyTemplateDto s WHERE s.id IN " +
                "(SELECT DISTINCT st.id FROM SurveyTemplate st WHERE st.owner = :owner)")
})
public class SurveyTemplateDto implements Serializable {

        public static final class Query {

                public static final String GET_TEMPLATE_QUERY =
                        "SurveyTemplateDto.getTemplate";
                public static final String GET_TEMPLATES_QUERY =
                        "SurveyTemplateDto.getTemplates";

                private Query() {
                }
        }
        @Id
        @Column(name = "ID", insertable = false, updatable = false)
        private Long id;
        @Column(name = "TITLE", insertable = false, updatable = false)
        private String name;
        @OneToMany
        @JoinColumn(name = "TEMPLATE", nullable = false, insertable = false, updatable = false)
        @IndexColumn(base = 1, name = "NUMBER")
        private List<QuestionTemplateDto> questions =
                new ArrayList<QuestionTemplateDto>();

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public List<QuestionTemplateDto> getQuestions() {
                return questions;
        }

        public void setQuestions(List<QuestionTemplateDto> questions) {
                this.questions = questions;
        }
}
