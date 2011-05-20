package org.adaptiveplatform.validation {
import flash.utils.Dictionary;

import mx.collections.ArrayCollection;

import mx.collections.IList;
import org.adaptiveplatform.surveys.dto.generated.PublishSurveyTemplateCommand;
import org.adaptiveplatform.surveys.utils.CollectionUtils;
import org.flexunit.asserts.assertEquals;
import org.flexunit.asserts.fail;

public class MetadataDrivenValidationTest {

    private var commandValidator:CommandValidator;
    private var errors:Dictionary;


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
        errors = commandValidator.errorStrings;
    }

    private function validationPassed():void {
        assertEquals(0, CollectionUtils.length(errors));
    }

    private function validationFailedOnProperties(...properties):void {
        var failingProperties:Array = [];
        for (var error:String in errors) {
            if (failingProperties.indexOf(error) == -1)
                failingProperties.push(error);
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