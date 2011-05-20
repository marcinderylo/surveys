package org.adaptiveplatform.validation {

[Metadata(name="Min")]
public class MinValidator implements MetaValidator {

    public static const NUMBER_TO_SMALL_ERROR:String = "validation.MinValidator.numberToSmallError";

    public static const NAN_ERROR:String = "validation.MinValidator.nanError";

    public var value:Number = Number.NEGATIVE_INFINITY;

    public function MinValidator() {
    }

    public function validate(object:Object):String {
        const asNumber:Number = Number(object);
        if (isNaN(asNumber)) {
            return NAN_ERROR;
        } else if (asNumber < value) {
            return NUMBER_TO_SMALL_ERROR
        }
        return null;
    }
}
}
