package org.adaptiveplatform.surveys.application.mock {
public class StringBuffer {

    private var buffer:Array = new Array();

    public function StringBuffer(initial:String = null) {
        if (initial != null) {
            append(initial);
        }
    }

    public function append(str:String):void {
        for (var i:Number = 0; i < str.length; i++) {
            buffer.push(str.charCodeAt(i));
        }
    }

    public function toString():String {
        return String.fromCharCode.apply(this, buffer);
    }
}
}
