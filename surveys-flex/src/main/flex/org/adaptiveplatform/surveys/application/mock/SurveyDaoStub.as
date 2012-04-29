package org.adaptiveplatform.surveys.application.mock {
import mx.collections.ArrayCollection;

import org.adaptiveplatform.communication.ResultHandler;
import org.adaptiveplatform.communication.SuccessResultHandler;
import org.adaptiveplatform.surveys.application.generated.SurveyDao;
import org.adaptiveplatform.surveys.dto.generated.FilledSurveyQuery;
import org.adaptiveplatform.surveys.dto.generated.PublishedSurveyTemplateQuery;
import org.adaptiveplatform.surveys.dto.generated.SurveyTemplateQuery;

internal class SurveyDaoStub implements SurveyDao {

    private var random:RandomDtoGenerator = new RandomDtoGenerator();
    private var filled:ArrayCollection = new ArrayCollection();
    private var published:ArrayCollection = new ArrayCollection();
    private var templates:ArrayCollection = new ArrayCollection();

    public function SurveyDaoStub() {
        for (var i:int = 0; i < 10; i++) {
            filled.addItem(random.filledSurvey());
            templates.addItem(random.surveyTemplate());
        }
        for (var i:int = 0; i < 100; i++) {
            published.addItem(random.publishedSurveyTemplate());

        }
    }

    public function getTemplate(number:Number):ResultHandler {
        return new SuccessResultHandler(templates.getItemAt(number % templates.length));
    }

    public function getSurvey(number:Number):ResultHandler {
        return new SuccessResultHandler(filled.getItemAt(number % templates.length));
    }

    public function querySurveys(filledSurveyQuery:FilledSurveyQuery):ResultHandler {
        return new SuccessResultHandler(filled);
    }

    public function queryPublishedTemplates(publishedSurveyTemplateQuery:PublishedSurveyTemplateQuery):ResultHandler {
        return new SuccessResultHandler(published);
    }

    public function queryTemplates(surveyTemplateQuery:SurveyTemplateQuery):ResultHandler {
        return new SuccessResultHandler(templates);
    }
}
}
