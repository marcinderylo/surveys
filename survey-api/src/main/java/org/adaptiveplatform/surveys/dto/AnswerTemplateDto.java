package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.adaptiveplatform.codegenerator.api.RemoteObject;

@Entity
@Table(name = "ANSWER_TEMPLATES")
@RemoteObject
public class AnswerTemplateDto implements Serializable {
        private static final long serialVersionUID = 1L;
        @Id
        @Column(name = "ID", insertable = false, updatable = false)
        private Long id;
        @Column(name = "TEXT")
        private String text;
        @Column(name = "REQUIRES_COMMENT")
        private Boolean requiresComment;
        @Column(name = "DISALLOWS_OTHER_ANSWERS")
        private Boolean excludesOtherAnswers;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getText() {
                return text;
        }

        public void setText(String text) {
                this.text = text;
        }

        public Boolean getRequiresComment() {
                return requiresComment;
        }

        public void setRequiresComment(Boolean requiresComment) {
                this.requiresComment = requiresComment;
        }

        public Boolean getExcludesOtherAnswers() {
                return excludesOtherAnswers;
        }

        public void setExcludesOtherAnswers(Boolean excludesOtherAnswers) {
                this.excludesOtherAnswers = excludesOtherAnswers;
        }
}
