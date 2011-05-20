package org.adaptiveplatform.test {
import flash.utils.Timer
import flash.events.TimerEvent;

import org.flexunit.async.Async;
import org.flexunit.Assert;

public class AsynchTest {
    private var timer:Timer;

    [Test(async)]
    public function timerLongWay():void {
        var o:Object = new Object();
        o.repeatCount = 3;
        timer.repeatCount = 3;
        timer.addEventListener(TimerEvent.TIMER_COMPLETE,
                Async.asyncHandler(this, handleTimerComplete, 500, o, handleTimeout), false, 0, true);
        timer.start();
    }

    private function handleTimerComplete(event:TimerEvent, passThroughData:Object):void {
        Assert.assertEquals(( event.target as Timer ).currentCount, passThroughData.repeatCount);
    }

    private function handleTimeout(passThroughData:Object):void {
        Assert.fail("Timeout reached before event");
    }

    public function AsynchTest() {
    }

    [Before]
    public function setUp():void {
        timer = new Timer(100, 1);
    }

    [After]
    public function tearDown():void {
        if (timer) {
            timer.stop();
        }
        timer = null;
    }
}
}
