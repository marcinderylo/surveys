package org.adaptiveplatform.validation {

[Metadata(name="ValidId")]
public class ValidIdValidator implements MetaValidator {

    public static const TYPE_ERROR:String = "validation.ValidIdValidator.typeError";
    public static const NOT_INTEGER_ERROR:String = "validation.ValidIdValidator.notIntegerError";
    public static const NOT_POSITIVE_ERROR:String = "validation.ValidIdValidator.notPositiveError";


    public function ValidIdValidator() {
    }

    public function validate(object:Object):String {
        if (!(object is Number)) {
            return TYPE_ERROR;
        }
        var number:Number = object as Number;

        if (!isInteger(number)) {
            return NOT_INTEGER_ERROR;
        }
        if (number < 1) {
            return NOT_POSITIVE_ERROR;
        }
        return null;
    }

    private function isInteger(number:Number):Boolean {
        return number == Math.floor(number);
    }
}
}

