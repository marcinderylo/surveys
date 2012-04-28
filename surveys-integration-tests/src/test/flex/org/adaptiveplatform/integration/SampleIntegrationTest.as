package org.adaptiveplatform.integration {
import mx.messaging.Channel;
import mx.messaging.ChannelSet;
import mx.messaging.channels.AMFChannel;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;

import org.adaptiveplatform.communication.RemoteServiceImpl;
import org.adaptiveplatform.communication.ResultHandler;
import org.adaptiveplatform.surveys.application.generated.RemoteSystemInformationDao;
import org.adaptiveplatform.surveys.application.generated.RemoteUserFacade;
import org.adaptiveplatform.surveys.dto.generated.RegisterAccountCommand;
import org.flexunit.Assert;
import org.flexunit.asserts.assertEquals;
import org.flexunit.async.Async;
import org.hamcrest.assertThat;
import org.hamcrest.collection.hasItems;

public class SampleIntegrationTest {

    /** TODO - only the first remote call should have a longer timeout because the application might still be deploying */
    const REMOTE_CALL_TIMEOUT:int = 10000;

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
        var userFacade = new RemoteUserFacade();
        userFacade.remoteService = remoteService;

        const LOGIN:String = "ee45@ggg.com";
        const PASSWORD:String = "123123";

        var command:RegisterAccountCommand = new RegisterAccountCommand();
        command.name = "bob"
        command.email = LOGIN;
        command.password = PASSWORD;

        call(authentication.logout(), function () {
            call(userFacade.registerUser(command), function (userId:int) {
                call(authentication.login(LOGIN, PASSWORD), function (result:Object):void {
                    assertEquals(LOGIN, result.name);
                    assertThat(result.authorities, hasItems("ROLE_USER", "ROLE_STUDENT"));
                });
            });
        });
    }

    private function call(resultHandler:ResultHandler, onSuccess:Function = null):void {
        var successHandler:Function = function (event:ResultEvent, passThrough:Object):void {
            onSuccess(event.result);
        };
        resultHandler.addListener(Async.asyncHandler(this, successHandler, REMOTE_CALL_TIMEOUT, null, handleTimeout), onFault);
    }


    private function onFault(fault:FaultEvent):void {
        Assert.fail("Remote call failed with: " + fault.fault.faultString);
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
