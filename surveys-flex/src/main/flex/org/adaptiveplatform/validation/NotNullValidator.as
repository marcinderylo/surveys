package org.adaptiveplatform.validation {

[Metadata(name="NotNull")]
public class NotNullValidator implements MetaValidator {

    private const NULL_ERROR:String = "validation.NotNullValidator.nullError";

    public function NotNullValidator() {
    }

    public function validate(object:Object):String {
        return object == null ? NULL_ERROR : null;
    }
}
}
