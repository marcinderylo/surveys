package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.RemoteObject;


/**
 *
 * @author Marcin Deryło
 */
@RemoteObject
public class ResearchesQuery implements Serializable {

    private String name;
    private Long surveyTemplateId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSurveyTemplateId() {
        return surveyTemplateId;
    }

    public void setSurveyTemplateId(Long surveyTemplateId) {
        this.surveyTemplateId = surveyTemplateId;
    }
}
