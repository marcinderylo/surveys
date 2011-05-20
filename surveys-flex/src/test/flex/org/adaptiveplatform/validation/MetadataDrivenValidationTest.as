package org.adaptiveplatform.validation {
import mx.collections.ArrayCollection;

import mx.collections.IList;
import org.adaptiveplatform.surveys.dto.generated.PublishSurveyTemplateCommand;
import org.flexunit.asserts.assertEquals;
import org.flexunit.asserts.fail;

public class MetadataDrivenValidationTest {

    private var commandValidator:CommandValidator;
    private var errors:Array;


    [Before]
    public function registerMetadataClasses():void {
        commandValidator = new CommandValidator();
    }

    [Test]
    public function shouldCreateValidationErrors():void {
        // given
        var command:PublishSurveyTemplateCommand = new PublishSurveyTemplateCommand();
        // when
        validate(command);
        // then
        validationFailedOnProperties("surveyTemplateIds", "groupId");
    }

    [Test]
    public function shouldUseCustomMetadataClasses():void {
        // given
        var command:PublishSurveyTemplateCommand = new PublishSurveyTemplateCommand();
        command.surveyTemplateIds = new ArrayCollection([16]);
        command.groupId = 14;
        // when
        validate(command);
        // then
        validationPassed();
    }

    private function validate(command:Object):void {
        commandValidator.source = command;
        commandValidator.validate();
        errors = commandValidator.errors;
    }

    private function validationPassed():void {
        assertEquals(0, errors.length);
    }

    private function validationFailedOnProperties(...properties):void {
        var failingProperties:Array = [];
        for each(var error:ValidationError in errors) {
            if (failingProperties.indexOf(error.property) == -1)
                failingProperties.push(error.property);
        }
        assertContainsOnly(failingProperties, properties);
    }

    public static function assertContainsOnly(elements:Array, expectedElements:Array):void {
        var remaining:IList = new ArrayCollection(elements.concat());
        var missing:IList = new ArrayCollection();

        for each(var expected:Object in expectedElements) {
            var foundIndex:int = remaining.getItemIndex(expected);
            if (foundIndex >= 0) {
                remaining.removeItemAt(foundIndex);
            } else {
                missing.addItem(expected);
            }
        }
        if (remaining.length > 0 || missing.length > 0) {
            fail("array doesn't contain expected elements. remaining: [" + remaining.toArray() + "] missing: [" + missing + "] ");
        }
    }

    public function MetadataDrivenValidationTest() {
    }
}
}