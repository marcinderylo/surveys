package org.adaptiveplatform.surveys.view.validators {
import flash.events.EventDispatcher;
import flash.utils.Dictionary;

import mx.events.PropertyChangeEvent;

import mx.events.ValidationResultEvent;
import mx.validators.StringValidator;
import mx.validators.ValidationResult;

import org.adaptiveplatform.surveys.dto.generated.CreateStudentGroupCommand;

[Bindable]
public class CreateStudentGroupCommandValidator {
    private var _source:CreateStudentGroupCommand;

    private var _valid:Boolean;

    private var _errors:Dictionary = new Dictionary();

    public var groupName;

    public function CreateStudentGroupCommandValidator() {
    }

    public function set source(source:CreateStudentGroupCommand):void {
        _source = source;
        _source.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, validateProperties);
    }

    public function validateProperties(ignore:PropertyChangeEvent = null):void {
        _valid = true;
        var groupNameResult:ValidationResult = validateGroupName();
        if (groupNameResult) {
            _errors.groupName = groupNameResult.errorMessage;
            dispatchEvent(new PropertyChangeEvent("propertChange", false, null, "groupName"));
            _valid = false;
        } else {
            _errors.groupName = null;
            dispatchEvent(new PropertyChangeEvent("propertChange", false, null, "groupName"));
        }
    }

    public function validateGroupName():ValidationResult {
        var groupNameValidator:StringValidator = new StringValidator();
        groupNameValidator.required = true;
        groupNameValidator.minLength = 5;
        var event:ValidationResultEvent = groupNameValidator.validate(_source.groupName, true);
        for each (var result:ValidationResult in event.results) {
            if (result.isError) {
                return result;
            }
        }
        return null;
    }

    public function get valid():Boolean {
        return _valid;
    }

    [Bindable(event="propertyChange")]
    public function get errors():Dictionary {
        return _errors;
    }
}
}
