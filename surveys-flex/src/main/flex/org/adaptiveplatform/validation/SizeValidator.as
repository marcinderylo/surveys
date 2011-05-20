package org.adaptiveplatform.validation {
import mx.collections.ArrayCollection;

import org.as3commons.collections.framework.ICollection;
import org.spicefactory.lib.reflect.ClassInfo;

[Metadata(name="Size")]
public class SizeValidator implements MetaValidator {

    public static const TYPE_ERROR:String = "validation.SizeValidator.typeError";
    public static const COLLECTION_TO_SMALL:String = "validation.SizeValidator.collectionToSmall";
    public static const COLLECTION_TO_BIG:String = "validation.SizeValidator.collectionToBig";

    public var min:int;

    public var max:int;

    public var type:ClassInfo;

    public function SizeValidator() {
    }

    public function validate(object:Object):String {
        var size:int;
        if (object is ArrayCollection) {
            size = (object as ArrayCollection).length;
        } else if (object is Array) {
            size = (object as Array).length;
        } else if (object is ICollection) {
            size = (object as ICollection).size;
        } else {
            return TYPE_ERROR;
        }

        if (size < min) {
            return COLLECTION_TO_SMALL;
        } else if (size > max) {
            return COLLECTION_TO_BIG;
        }
        return null;
    }
}
}
