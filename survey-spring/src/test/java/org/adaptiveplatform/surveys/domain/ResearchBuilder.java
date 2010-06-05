package org.adaptiveplatform.surveys.domain;


/**
 *
 * @author Marcin Deryło
 */
public final class ResearchBuilder {

    private String name;
    private SurveyTemplate surveyTemplate;

    private ResearchBuilder(String name, SurveyTemplate surveyTemplate) {
        this.name = name;
        this.surveyTemplate = surveyTemplate;
    }

    public Research build() {
        return new Research(name, surveyTemplate);
    }

    public static ResearchBuilder research(String name,
            SurveyTemplate surveyTemplate) {
        return new ResearchBuilder(name, surveyTemplate);
    }
}
