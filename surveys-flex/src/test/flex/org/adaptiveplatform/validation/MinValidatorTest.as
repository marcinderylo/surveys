package org.adaptiveplatform.validation {
import org.flexunit.asserts.assertNotNull;
import org.flexunit.asserts.assertNull;

public class MinValidatorTest {

    private var validator:MinValidator;

    public function MinValidatorTest() {
    }

    [Before]
    public function createValidator():void {
        validator = new MinValidator();
    }

    [Test]
    public function defaultCorrectValues():void {
        validationPassesFor(-10.0, -1, 5, Number.MIN_VALUE, Number.MAX_VALUE);
    }

    [Test]
    public function correctValues():void {
        validator.value = 80000;
        validationPassesFor(80000, 100000, 4.9E+323, Number.MAX_VALUE, Number.POSITIVE_INFINITY);
    }

    [Test]
    public function incorrectValues():void {
        validator.value = 2;
        validationFailsFor("nan", 1.9999, 0, 1, -1, Number.MIN_VALUE, Number.NEGATIVE_INFINITY);
    }

    private function validationPassesFor(... values):void {
        for each(var value:Object in values) {
            assertNull("validation failed for: " + value, validator.validate(value));
        }
    }

    private function validationFailsFor(... values):void {
        for each(var value:Object in values) {
            assertNotNull("validation passed for: " + value, validator.validate(value));
        }
    }
}
}
