package org.adaptiveplatform.surveys.view.usermanagement {
import flash.events.Event;

public class ChangePasswordEvent extends Event {
    public static const NAME:String="changePassword";

    private var _newPassword:String;

    public function ChangePasswordEvent(newPassword:String) {
        super(NAME, false, false);
        _newPassword=newPassword;
    }

    public function get newPassword():String {
        return _newPassword;
    }
}
}
