package org.adaptiveplatform.surveys.view.usermanagement {
import flash.events.Event;

import org.adaptiveplatform.surveys.dto.generated.UserDto;

public class UserSearchEvent extends Event {
    public static const NAME:String="userSearch";

    private var _searchQuery:String;

    public function UserSearchEvent(searchQuery:String) {
        super(NAME, false, false);
        _searchQuery=searchQuery;
    }

    public function get searchQuery():String {
        return _searchQuery;
    }
}
}