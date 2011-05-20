package org.adaptiveplatform.validation {

[Metadata(name="NonBlank")]
public class NonBlankValidator implements MetaValidator {

    private const NULL_ERROR:String = "validation.NonBlankValidator.nullError";
    private const BLANK_STRING_ERROR:String = "validation.NonBlankValidator.blankStringError";

    public function NonBlankValidator() {
    }

    public function validate(object:Object):String {
        if (object == null) {
            return NULL_ERROR;
        }
        if (object is String) {
            if ((object as String).length == 0) {
                return BLANK_STRING_ERROR;
            }
            return null;
        }
        throw new Error("type error: String expected");
    }
}
}
