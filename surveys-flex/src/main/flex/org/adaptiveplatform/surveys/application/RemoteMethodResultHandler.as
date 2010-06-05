package org.adaptiveplatform.surveys.application {
	import flash.events.Event;

	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class RemoteMethodResultHandler implements ResultHandler {

		private var remoteMethod:RemoteObject;

		public function RemoteMethodResultHandler(remoteMethod:RemoteObject) {
			this.remoteMethod=remoteMethod;
		}

		public function onSuccess(success:Function):ResultHandler {
			remoteMethod.addEventListener(ResultEvent.RESULT, function(event:ResultEvent):void {
					success(event.result);
				});
			return this;
		}

		public function onFault(fault:Function):ResultHandler {
			remoteMethod.addEventListener(FaultEvent.FAULT, function(event:FaultEvent):void {
					fault(event.fault);
				});
			return this;
		}

		public function onError(error:String, fault:Function):ResultHandler {
			remoteMethod.addEventListener(FaultEvent.FAULT, function(event:FaultEvent):void {
					if (event.fault.faultCode == error) {
						fault(event.fault);
					}
				});
			return this;
		}

		public function onSuccessGoto(view:String):ResultHandler {
			remoteMethod.addEventListener(ResultEvent.RESULT, function(event:Event):void {
					ContextFactory.context.view.goto(view);
				});
			return this;
		}
	}
}
