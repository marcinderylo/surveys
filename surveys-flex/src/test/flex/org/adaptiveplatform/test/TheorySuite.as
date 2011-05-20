package org.adaptiveplatform.test {
import flash.sampler.isGetterSetter;

import org.flexunit.Assert;
import org.flexunit.assumeThat;
import org.flexunit.experimental.theories.Theories;
import org.hamcrest.core.not;
import org.hamcrest.number.greaterThan;
import org.hamcrest.object.equalTo;

[RunWith("org.flexunit.experimental.theories.Theories")]
public class TheorySuite {

    public function TheorySuite() {
    }

    private var theory:Theories;
    [DataPoint]
    public static var value1:int = 10;
    [DataPoint]
    public static var value2:int = 5;
    [DataPoints]
    [ArrayElementType("Number")]
    public static var numberValues:Array = [-5,-3.5,-1,0,1,2.2,3,4,5,6,7,8,9];

    [DataPoints]
    [ArrayElementType("String")]
    public static var stringValues:Array = ["one","two","three","four","five"];

    [Theory]
    public function testDivideMultiply(value1:Number, value2:Number):void {
        assumeThat(value2, not(equalTo(0)));
        var simpleMath:Object = new Object();
        simpleMath.divide = function(a:Number, b:Number):Number {
            return a / b;
        }
        simpleMath.multiply = function(a:Number, b:Number):Number {
            return a * b;
        }

        var div:Number = simpleMath.divide(value1, value2);
        var mul:Number = simpleMath.multiply(div, value2);

        Assert.assertEquals(mul, value1);
    }
}
}
