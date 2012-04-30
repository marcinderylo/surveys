package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.Validate;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 *  Represents a survey template assigned to a group, with data about publication
 *  period, during which survey template remains accessible for students for filling.
 *
 *  @author Marcin Derylo
 */
@Entity
@Table(name = "SURVEY_PUBLICATIONS", 
        uniqueConstraints = {@UniqueConstraint(columnNames = {"GROUP_ID", "TEMPLATE_ID"})})
public class SurveyPublication implements Serializable {

        @Id
        @GeneratedValue
        private Long id;
        @NaturalId(mutable = false)
        @ManyToOne(optional = false)
        @JoinColumn(name = "TEMPLATE_ID", insertable = true, updatable = false, nullable = false)
        private SurveyTemplate surveyTemplate;
        @NaturalId(mutable = false)
        @ManyToOne(optional = false)
        @JoinColumn(name = "GROUP_ID", insertable = true, updatable = false, nullable = false)
        private StudentGroup studentGroup;
        @Type(type = "org.joda.time.contrib.hibernate.PersistentInterval")
        @Columns( columns={@Column(name="ENABLED_FROM"),@Column(name="ENABLED_TO")})
        private Interval availabilityPeriod;

        protected SurveyPublication() {
            // To be used only by object persistence framework
        }

        public SurveyPublication(SurveyTemplate surveyTemplate,
                StudentGroup studentGroup) {
                Validate.notNull(surveyTemplate,
                        "Must specify a survey template to be published");
                Validate.notNull(studentGroup,
                        "Must specify a group to publish the template in");
                this.surveyTemplate = surveyTemplate;
                this.studentGroup = studentGroup;
        }

        public Long getId() {
                return id;
        }

        public SurveyTemplate getSurveyTemplate() {
                return surveyTemplate;
        }

        /**
         * @return {@code true} if survey template publicaton is currently valid
         * for given group.
         */
        public boolean isFillingEnabled(DateTime date) {
                return availabilityPeriod.contains(date);
        }

        public StudentGroup getGroup() {
                return studentGroup;
        }

        public void enableFillingInPeriod(DateTime from, DateTime to) {
                DateTime start = from != null ? from : new DateTime();
                DateTime end = to != null ? to : start.plusYears(10);
                this.availabilityPeriod = new Interval(start, end);
        }
}
