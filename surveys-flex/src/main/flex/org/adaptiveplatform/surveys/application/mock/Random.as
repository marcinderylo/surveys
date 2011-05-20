package org.adaptiveplatform.surveys.application.mock {
public class Random {

    private static const ALPHABET:Array = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");

    public function Random() {
    }

    public static function letter():String {
        return  ALPHABET[Math.floor(Math.random() * ALPHABET.length)];
    }

    public function word():String {
        var word:StringBuffer = new StringBuffer();
        for (var i:int = 0; i < integer(3, 10); i++) {
            word.append(letter());
        }
        return word.toString();
    }

    public function name():String {
        return capitalize(word());
    }

    private function capitalize(string:String):String {
        return string.charAt(0).toUpperCase() + string.substring(1);
    }

    public function fullName():String {
        return name() + " " + name();
    }

    public function integer(min:int, max:int):int {
        return number(min, max);
    }

    public function number(min:Number, max:Number):Number {
        return Math.random() * (max - min + 1) + min;
    }

    public function id():Number {
        return number(1, Number.MAX_VALUE);
    }

    public function email():String {
        return word() + "@" + word() + ".com";
    }

    public function pastDate():Date {
        const now:Number = new Date().getTime();
        const oneYear:Number = 31556926000;
        var date:Date = new Date(number(now - oneYear, now));
        return date;
    }

    public function sentence():String {
        var sentence:StringBuffer = new StringBuffer(capitalize(word()));
        const wordsCount:int = integer(4, 12);
        for (var i:int = 1; i < wordsCount; i++) {
            sentence.append(word());
            sentence.append(" ");
        }
        return sentence + ".";
    }

    public function element(...elements):Object {
        return elements[integer(0, element.length)];
    }

    public function boolean():Boolean {
        return Math.random() < 0.5;
    }

    public function futureDate():Date {
        const now:Number = new Date().getTime();
        const oneYear:Number = 31556926000;
        var date:Date = new Date(number(now, now + oneYear));
        return date;
    }
}
}
