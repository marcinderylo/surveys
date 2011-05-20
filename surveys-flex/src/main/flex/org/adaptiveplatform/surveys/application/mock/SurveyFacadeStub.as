package org.adaptiveplatform.surveys.application.mock {
import mx.collections.ArrayCollection;

import org.adaptiveplatform.communication.FaultResultHandler;
import org.adaptiveplatform.communication.ResultHandler;
import org.adaptiveplatform.communication.SuccessResultHandler;
import org.adaptiveplatform.surveys.application.generated.SurveyDao;
import org.adaptiveplatform.surveys.application.generated.SurveyFacade;
import org.adaptiveplatform.surveys.dto.generated.CreateSurveyTemplateCommand;
import org.adaptiveplatform.surveys.dto.generated.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.generated.FilledSurveyQuery;
import org.adaptiveplatform.surveys.dto.generated.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.generated.PublishedSurveyTemplateQuery;
import org.adaptiveplatform.surveys.dto.generated.QuestionTypeEnum;
import org.adaptiveplatform.surveys.dto.generated.SurveyQuestionAnswerDto;
import org.adaptiveplatform.surveys.dto.generated.SurveyQuestionDto;
import org.adaptiveplatform.surveys.dto.generated.SurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.generated.SurveyTemplateQuery;

internal class SurveyFacadeStub implements SurveyFacade {

    public function SurveyFacadeStub() {
    }

    public function removeSurveyTemplate(number:Number):ResultHandler {
        return new SuccessResultHandler();
    }

    public function createTemplate(createSurveyTemplateCommand:CreateSurveyTemplateCommand):ResultHandler {
        return new SuccessResultHandler(Math.random());
    }

    public function updateTemplate(number:Number, createSurveyTemplateCommand:CreateSurveyTemplateCommand):ResultHandler {
        return new SuccessResultHandler();
    }

    public function startFilling(number:Number):ResultHandler {
        return new SuccessResultHandler(Math.random());
    }

    public function answerQuestion(number:Number, int2:int, arrayCollection:ArrayCollection, string:String):ResultHandler {
        return new SuccessResultHandler();
    }

    public function submit(number:Number):ResultHandler {
        return new SuccessResultHandler();
    }
}
}