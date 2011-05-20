package org.adaptiveplatform.validation {
import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.IEventDispatcher;
import flash.utils.Dictionary;

import mx.events.PropertyChangeEvent;

import org.spicefactory.lib.reflect.ClassInfo;
import org.spicefactory.lib.reflect.Metadata;
import org.spicefactory.lib.reflect.Property;

[Bindable(event="validated")]
public class CommandValidator extends EventDispatcher {

    private static const METADATA_HANDLERS = [SizeValidator, NotNullValidator, ValidIdValidator, NonBlankValidator, MinValidator];

    public static const VALIDATED:String = "validated";

    private var _errorStrings:Dictionary;

    private var _source:Object;
    private var automaticValidation:Boolean;

    public function CommandValidator(automaticValidation:Boolean = false) {
        this.automaticValidation = automaticValidation;
        registerMetadataHandlers();
    }

    private function registerMetadataHandlers():void {
        for each (var handler:Class in METADATA_HANDLERS) {
            Metadata.registerMetadataClass(handler);
        }
    }

    public function validate():Boolean {
        _errorStrings = new Dictionary();
        var errorOccured:Boolean = false;

        var info:ClassInfo = ClassInfo.forInstance(_source);
        for each(var property:Property in info.getProperties()) {
            for each(var o:Object in  property.getAllMetadata()) {
                if (o is MetaValidator) {
                    var value:Object = property.getValue(_source);
                    var validationProblem:String = (o as MetaValidator).validate(value);
                    if (validationProblem != null) {
                        errorOccured = true;
                        _errorStrings[property.name] = validationProblem;
                    }
                }
            }
        }
        dispatchEvent(new Event(VALIDATED));
        return errorOccured;
    }

    public function get errorStrings():Dictionary {
        return _errorStrings;
    }

    public function set source(value:Object):void {
        if (_source is IEventDispatcher) {
            (_source as IEventDispatcher).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, sourcePropertyChanged);
        }
        _source = value;
        if (automaticValidation && _source is IEventDispatcher) {
            (_source as IEventDispatcher).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, sourcePropertyChanged);
        }
    }

    private function sourcePropertyChanged(event:PropertyChangeEvent):void {
        validate();
    }
}
}
