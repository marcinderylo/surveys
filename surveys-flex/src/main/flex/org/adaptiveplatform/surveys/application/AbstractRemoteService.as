package org.adaptiveplatform.surveys.application {

	import mx.controls.Alert;
	import mx.messaging.ChannelSet;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class AbstractRemoteService {

		protected var channels:ChannelSet;
		protected var destination:String;

		public function AbstractRemoteService(channels:ChannelSet, destination:String) {
			this.channels=channels;
			this.destination=destination;
		}

		protected function createService():RemoteObject {
			var service:RemoteObject=new RemoteObject();
			service.channelSet=channels;
			service.destination=destination;
			service.makeObjectsBindable=true;
			service.showBusyCursor=true;

			// FIXME REMOVE
			service.addEventListener(FaultEvent.FAULT, function faultHandler(event:FaultEvent):void {
					Alert.show(event.fault.faultString);
				});

			return service;
		}

		protected function createResponder(remoteMethod:RemoteObject):ResultHandler {
			return new RemoteMethodResultHandler(remoteMethod);
		}
	}
}