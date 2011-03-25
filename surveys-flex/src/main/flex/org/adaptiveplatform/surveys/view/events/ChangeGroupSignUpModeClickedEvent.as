package org.adaptiveplatform.surveys.view.events {
import flash.events.Event;

public class ChangeGroupSignUpModeClickedEvent extends Event {

    public static const NAME:String="ChangeGroupSignUpModeClickedEvent";

    private var _groupId:Number;
    private var _allowStudentsToSignup:Boolean;

    public function ChangeGroupSignUpModeClickedEvent(groupId:Number, allowStudentsToSignup:Boolean) {
        super(NAME, false, false);
        _groupId=groupId;
        _allowStudentsToSignup=allowStudentsToSignup;
    }

    public function get groupId():Number {
        return _groupId;
    }

    public function get allowStudentsToSignup():Boolean {
        return _allowStudentsToSignup;
    }

}
}