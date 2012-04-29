package org.adaptiveplatform.surveys.view.researchCreator {
import mx.collections.ArrayCollection;

import org.adaptiveplatform.surveys.application.generated.StudentGroupDao;
import org.adaptiveplatform.surveys.application.generated.SurveyDao;
import org.adaptiveplatform.surveys.dto.generated.PrepareResearchCommand;
import org.adaptiveplatform.surveys.view.researchCreator.steps.ConfirmationStepModel;
import org.adaptiveplatform.surveys.view.researchCreator.steps.GroupSelectionStepModel;
import org.adaptiveplatform.surveys.view.researchCreator.steps.RangeSelectionStepModel;
import org.adaptiveplatform.surveys.view.researchCreator.steps.SurveySelectionStepModel;

public class ResearchCreatorModel {

    [Bindable]
    public var command:PrepareResearchCommand;

    // navigation
    [Bindable]
    public var currentStep:ResearchCreatorStepModel;
    private var steps:ArrayCollection = null;
    [Bindable]
    public var createResearchEnabled:Boolean;

    // steps
    [Bindable]
    public var rangeSelection:RangeSelectionStepModel;
    [Bindable]
    public var surveySelection:SurveySelectionStepModel;
    [Bindable]
    public var groupSelection:GroupSelectionStepModel;
    [Bindable]
    public var confirmation:ConfirmationStepModel;

    public function ResearchCreatorModel(surveyDao:SurveyDao, groupDao:StudentGroupDao) {
        currentStep = rangeSelection = new RangeSelectionStepModel();
        surveySelection = new SurveySelectionStepModel(surveyDao);
        groupSelection = new GroupSelectionStepModel(groupDao);
        confirmation = new ConfirmationStepModel();
        steps = new ArrayCollection();
        steps.addItem(rangeSelection);
        steps.addItem(surveySelection);
        steps.addItem(groupSelection);
        steps.addItem(confirmation);
//        BindingUtils.bindProperty(this, "rangeSelection.surveyName", confirmation, "surveyName");
//        BindingUtils.bindProperty(this, "rangeSelection.defaultStartingDate", confirmation, "defaultStartingDate");
//        BindingUtils.bindProperty(this, "rangeSelection.defaultEndingDate", confirmation, "defaultEndingDate");
    }

    public function initialize() {
        changeStep(0);
    }

    public function previousStep():void {
        if (currentStep.stepNumber > 0) {
            changeStep(currentStep.stepNumber - 1);
        }
    }

    public function nextStep():void {
        if (currentStep.stepNumber < steps.length - 1 && currentStep.valid) {
            changeStep(currentStep.stepNumber + 1);
        }
    }

    private function changeStep(stepNumber:int):void {
        currentStep = steps.getItemAt(stepNumber) as ResearchCreatorStepModel
        currentStep.initialize();
    }

    public function save():void {
    }

    public function cancel():void {
    }
}
}
// TODO fix starting/ending dates
//if (start != null) {
//    start.hours = 0;
//    start.minutes = 0;
//    start.seconds = 0;
//    start.milliseconds = 0;
//}
//if (end != null) {
//    end.hours = 23;
//    end.minutes = 59;
//    end.seconds = 59;
//    end.milliseconds = 999;
//}