package org.adaptiveplatform.surveys.view.groupmanagement {
import flash.events.Event;

import org.adaptiveplatform.surveys.dto.generated.StudentGroupDto;

public class GroupSelectedEvent extends Event {
    public static const NAME:String="groupSelected";

    private var _selectedGroup:StudentGroupDto;

    public function GroupSelectedEvent(selectedGroup:StudentGroupDto) {
        super(NAME, false, false);
        _selectedGroup=selectedGroup;
    }

    public function get selectedGroup():StudentGroupDto {
        return _selectedGroup;
    }
}
}