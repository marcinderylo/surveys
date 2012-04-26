package org.adaptiveplatform.surveys.application {

import mx.collections.ArrayCollection;

public class UserRole {
    public function UserRole() {
    }
    public static const USER:String="ROLE_USER";
    public static const ADMINISTRATOR:String="ROLE_ADMINISTRATOR";
    public static const STUDENT:String="ROLE_STUDENT";
    public static const TEACHER:String="ROLE_TEACHER";
    public static const EVALUATOR:String="ROLE_EVALUATOR";

    private static const _assignable_roles:Array=[ADMINISTRATOR, STUDENT, TEACHER, EVALUATOR];

	// TODO change to immutable
    public static function get allRoles():ArrayCollection {
        return new ArrayCollection(_assignable_roles);
    }
}
}