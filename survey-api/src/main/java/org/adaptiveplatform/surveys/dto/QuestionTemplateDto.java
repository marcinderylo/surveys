package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.adaptiveplatform.codegenerator.api.RemoteObject;
import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "QUESTION_TEMPLATES")
@RemoteObject
public class QuestionTemplateDto implements Serializable {

    public static final String NUMBER = "NUMBER";
    @Id
    @Column(name = "ID", insertable = false, updatable = false)
    private Long id;
    @Column(name = "TEXT")
    private String text;
    @Column(name = "HTML_TEXT")
    private String htmlText;
    @Enumerated(EnumType.STRING)
    @Column(name = "QUESTION_TYPE", nullable = false)
    private QuestionTypeEnum type;
    @Column(name = "STYLE")
    private String style;
    @OneToMany
    @JoinColumn(name = "QUESTION_ID")
    @IndexColumn(base = 1, name = "NUMBER")
    private List<AnswerTemplateDto> answers =
            new ArrayList<AnswerTemplateDto>();

    public List<AnswerTemplateDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerTemplateDto> answers) {
        this.answers = answers;
    }

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

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public QuestionTypeEnum getType() {
        return type;
    }

    public void setType(QuestionTypeEnum type) {
        this.type = type;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
