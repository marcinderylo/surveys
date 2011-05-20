package org.adaptiveplatform.test {
import flash.events.Event;

import mx.events.PropertyChangeEvent;

import org.adaptiveplatform.surveys.dto.generated.PublishSurveyTemplateCommand;
import org.flexunit.asserts.fail;
import org.flexunit.async.Async;

public class PropertyChangeTest {
    public function PropertyChangeTest() {
    }

    [Test(async)]
    public function propertyChangeEventShouldOccur():void {
        var command:PublishSurveyTemplateCommand = new PublishSurveyTemplateCommand();
        command.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, Async.asyncHandler(this, properyChanged, 100, null, properyDidntChange));
        command.groupId = 123;
    }


    private function properyChanged(event:Event, data:*):void {
        trace(event);
    }

    private function properyDidntChange(event:Event):void {
        fail("event didn't happen");
    }
}
}
