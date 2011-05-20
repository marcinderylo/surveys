package org.adaptiveplatform.surveys.application.mock {
public class HashUtils {
    public function HashUtils() {
    }

    public static function hash(string:String):int {
        var hash:int = 113;
        for (var i:int = 0; i < string.length; i++) {
            hash = hash * 1327144003 + string.charCodeAt(i);
        }
        return hash;
    }
}
}
