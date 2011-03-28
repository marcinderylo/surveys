package org.adaptiveplatform.surveys.view.usermanagement {
import flash.events.Event;

import org.adaptiveplatform.surveys.dto.generated.UserDto;

public class ChangePasswordEvent extends Event {
    public static const NAME:String="changePassword";

    private var _newPassword:String;
    private var _user:UserDto;

    public function ChangePasswordEvent(user:UserDto, newPassword:String) {
        super(NAME, false, false);
        _newPassword=newPassword;
        _user=user;
    }

    public function get newPassword():String {
        return _newPassword;
    }

    public function get user():UserDto {
        return _user;
    }
}
}
