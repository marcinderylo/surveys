package org.adaptiveplatform.surveys.utils {

public class CollectionUtils {
    public function CollectionUtils() {
    }

    public static function isEmpty(collection:Object):Boolean {
        for (var property in collection) {
            return false;
        }
        return true;
    }

    public static function length(collection:Object):int {
        var i:int = 0;
        for (var key:Object in collection) {
            i++;
        }
        return i;
    }
}
}