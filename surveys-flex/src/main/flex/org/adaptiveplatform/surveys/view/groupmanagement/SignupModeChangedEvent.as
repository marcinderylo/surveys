package org.adaptiveplatform.surveys.view.groupmanagement {
import flash.events.Event;

import org.adaptiveplatform.surveys.dto.generated.StudentGroupDto;

[Deprecated("not used")]
public class SignupModeChangedEvent extends Event {
    public static const NAME:String="signupModeChanged";

    private var _group:StudentGroupDto;
    private var _studentsCanSignup:Boolean;

    public function SignupModeChangedEvent(group:StudentGroupDto, studentsCanSignup:Boolean) {
        super(NAME, false, false);
        group=group;
        _studentsCanSignup=studentsCanSignup;
    }

    public function get group():StudentGroupDto {
        return _group;
    }

    public function get studentsCanSignup():Boolean {
        return _studentsCanSignup;
    }
}
}