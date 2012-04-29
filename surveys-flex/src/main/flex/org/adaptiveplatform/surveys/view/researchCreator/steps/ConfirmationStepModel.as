package org.adaptiveplatform.surveys.view.researchCreator.steps {
import mx.collections.ArrayCollection;

import org.adaptiveplatform.surveys.view.researchCreator.ResearchCreatorStepModel;

[Bindable]
public class ConfirmationStepModel implements ResearchCreatorStepModel {

    private const STEP_NUMBER:int = 3;
    public var valid:Boolean;

    // set in range selection step
    public var defaultStartingDate:Date;
    public var defaultEndingDate:Date;
    public var surveyName:String;
    public var groupsToAdd:ArrayCollection;

    public function ConfirmationStepModel() {
    }

    public function initialize():void {
    }

    public function get stepNumber():int {
        return STEP_NUMBER;
    }

}
}
