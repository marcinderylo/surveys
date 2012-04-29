/**
 * Created with IntelliJ IDEA.
 * User: rafal
 * Date: 4/29/12
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
package org.adaptiveplatform.surveys.view.researchCreator.steps {
import mx.collections.ArrayCollection;
import mx.events.PropertyChangeEvent;

import org.adaptiveplatform.surveys.application.BusinessExceptionHandler;
import org.adaptiveplatform.surveys.application.generated.SurveyDao;
import org.adaptiveplatform.surveys.dto.generated.SurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.generated.SurveyTemplateQuery;
import org.adaptiveplatform.surveys.view.researchCreator.ResearchCreatorStepModel;

[Bindable]
public class SurveySelectionStepModel implements ResearchCreatorStepModel {

    private const STEP_NUMBER:int = 1;
    public var valid:Boolean;

    private var surveyDao:SurveyDao;

    public var surveyTemplateQuery:SurveyTemplateQuery;
    public var surveysList:ArrayCollection;
    public var selectedSurvey:SurveyTemplateDto;

    public function SurveySelectionStepModel(surveyDao:SurveyDao) {
        this.surveyDao = surveyDao;
        this.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertyChanged);
    }

    public function initialize():void {
        resetSurveyTemplates();
    }

    private function propertyChanged(event:PropertyChangeEvent):void {
        valid = (selectedSurvey != null);
    }

    public function findSurveyTemplates():void {
        surveyDao.queryTemplates(surveyTemplateQuery)//
                .onSuccess(function (result:ArrayCollection):void {
                    surveysList = result;
                }).onFault(BusinessExceptionHandler.displayAlert());
    }

    public function resetSurveyTemplates():void {
        surveyTemplateQuery = new SurveyTemplateQuery();
        surveysList = new ArrayCollection();
        findSurveyTemplates();
    }


    public function get stepNumber():int {
        return STEP_NUMBER;
    }
}
}
