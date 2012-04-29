package org.adaptiveplatform.surveys.view.researchCreator.steps {
import mx.events.PropertyChangeEvent;

import org.adaptiveplatform.surveys.view.researchCreator.ResearchCreatorStepModel;

[Bindable]
public class RangeSelectionStepModel implements ResearchCreatorStepModel {

    private const STEP_NUMBER:int = 0;

    public var valid:Boolean;

    public var defaultStartingDate:Date;
    public var defaultEndingDate:Date;
    public var surveyName:String;

    public function RangeSelectionStepModel() {
        this.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertyChanged);
    }

    private function propertyChanged(event:PropertyChangeEvent):void {
        valid = (surveyName != null && surveyName.length > 0);
    }

    public function initialize():void {
    }

    public function get stepNumber():int {
        return STEP_NUMBER;
    }


}
}
