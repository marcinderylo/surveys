package org.adaptiveplatform.integration {
import mx.messaging.Channel;
import mx.messaging.ChannelSet;
import mx.messaging.channels.AMFChannel;
import mx.rpc.Fault;
import mx.rpc.events.ResultEvent;

import org.adaptiveplatform.communication.RemoteServiceImpl;
import org.adaptiveplatform.communication.ResultHandler;
import org.adaptiveplatform.surveys.application.generated.RemoteSystemInformationDao;
import org.flexunit.Assert;
import org.flexunit.asserts.assertEquals;
import org.flexunit.async.Async;

public class SampleIntegrationTest {

    public function SampleIntegrationTest() {
    }

    [Test(async)]
    public function test1():void {
        var channelName:String = "my-amf";
        var destination:String = "messagebroker/amf";
        var serverUrl:String = "htttp://localhost:8081/surveys-web/";
        var channelUrl:String = serverUrl + destination;

        var channels:ChannelSet = createChannels(channelName, channelUrl);
        var remoteService:RemoteServiceImpl = new RemoteServiceImpl();
        remoteService.channelSet = channels;

        var authentication:AuthenticationServiceRemote = new AuthenticationServiceRemote(channels);
        var systemInformationDao:RemoteSystemInformationDao = new RemoteSystemInformationDao();
        systemInformationDao.remoteService = remoteService;

        call(systemInformationDao.getSystemVersion(), function(version:String):void {
            assertEquals("0.9.1.SNAPSHOT", version);
        });
    }

    private function call(resultHandler:ResultHandler, onSuccess:Function):void {
        var successHandler:Function = function (event:ResultEvent, passThrough:Object):void {
            onSuccess(event.result);
        };
        resultHandler.addListener(Async.asyncHandler(this, successHandler, 500, null, handleTimeout), onFault);
    }


    private function onFault(fault:Fault):void {
        Assert.fail("Remote call failed with: " + fault.faultString);
    }

    private function handleTimeout(passThroughData:Object):void {
        Assert.fail("Timeout reached before event");
    }

    private function createChannels(channelName:String, channelUrl:String):ChannelSet {
        var channelSet:ChannelSet = new ChannelSet();
        var channel:Channel = new AMFChannel(channelName, channelUrl);
        channelSet.addChannel(channel);
        return channelSet;
    }

}
}
