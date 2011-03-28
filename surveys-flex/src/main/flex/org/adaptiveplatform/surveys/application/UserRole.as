package org.adaptiveplatform.surveys.application {
import flexunit.utils.ArrayList;

import mx.collections.ArrayCollection;

public class UserRole {
    public function UserRole() {
    }
    public static const ADMINISTRATOR:String="ROLE_ADMINISTRATOR";
    public static const USER:String="ROLE_USER";
    public static const TEACHER:String="ROLE_TEACHER";
    public static const EVALUATOR:String="ROLE_EVALUATOR";

    private static const _roles:Array=[ADMINISTRATOR, USER, TEACHER, EVALUATOR];

	// TODO change to immutable
    public static function get allRoles():ArrayCollection {
        return new ArrayCollection(_roles);
    }
}
}