package org.adaptiveplatform.surveys.view.usermanagement {
import flash.events.Event;

import org.adaptiveplatform.surveys.dto.generated.UserDto;

public class UserSelectedEvent extends Event {
    public static const NAME:String="userSelected";

    private var _selectedUser:UserDto;

    public function UserSelectedEvent(selectedUser:UserDto) {
        super(NAME, false, false);
        _selectedUser=selectedUser;
    }

    public function get selectedUser():UserDto {
        return _selectedUser;
    }
}
}