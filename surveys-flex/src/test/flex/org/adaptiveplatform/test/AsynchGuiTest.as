package org.adaptiveplatform.test {
import mx.controls.TextInput;
import mx.events.FlexEvent;

import flash.events.Event;

import org.flexunit.asserts.assertEquals;
import org.flexunit.async.Async;
import org.flexunit.Assert;
import org.fluint.uiImpersonation.UIImpersonator;

public class AsynchGuiTest {
    private var textInput:TextInput;

    [Before(async, ui )]
    public function setUp():void {
        textInput = new TextInput();
        UIImpersonator.addChild(textInput);
        Async.proceedOnEvent(this, textInput, FlexEvent.CREATION_COMPLETE, 1000);
    }

    [After(ui)]
    public function tearDown():void {
        UIImpersonator.removeChild(textInput);
        textInput = null;
    }

    [Test(async, ui)]
    public function testSetTextProperty():void {
        var passThroughData:Object = new Object();
        passThroughData.propertyName = 'text';
        passThroughData.propertyValue = 'digitalprimates';
        textInput.addEventListener(FlexEvent.VALUE_COMMIT,
                Async.asyncHandler(this, handleVerifyProperty, 100, passThroughData, handleEventNeverOccurred),
                false, 0, true);
        textInput.text = passThroughData.propertyValue;
    }

    protected function handleVerifyProperty(event:Event, passThroughData:Object):void {
        Assert.assertEquals(event.target[ passThroughData.propertyName ], passThroughData.propertyValue);
    }

    protected function handleEventNeverOccurred(passThroughData:Object):void {
        Assert.fail('Pending Event Never Occurred');
    }

    public function AsynchGuiTest() {

    }
}
}
