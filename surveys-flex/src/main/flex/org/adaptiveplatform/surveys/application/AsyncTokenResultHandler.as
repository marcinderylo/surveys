package org.adaptiveplatform.surveys.application {
	import mx.rpc.AsyncResponder;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;

	public class AsyncTokenResultHandler implements ResultHandler {
		private var token:AsyncToken;

		public function AsyncTokenResultHandler(token:AsyncToken) {
			this.token=token;
		}

		public function onSuccess(success:Function):ResultHandler {
			token.addResponder(new AsyncResponder( //
				function(event:ResultEvent, token:Object=null):void {
					success(event.result);
				}, function(event:FaultEvent, token:Object=null):void {
				// do nothing
				}));
			return this;
		}

		public function onFault(fault:Function):ResultHandler {
			token.addResponder(new AsyncResponder( //
				function(event:ResultEvent, token:Object=null):void {
				// do nothing
				}, function(event:FaultEvent, token:Object=null):void {
					fault(event.fault);
				}));
			return this;
		}


		public function onError(error:String, fault:Function):ResultHandler {
			token.addResponder(new AsyncResponder( //
				function(event:ResultEvent, token:Object=null):void {
				// do nothing
				}, function(event:FaultEvent, token:Object=null):void {
					if (event.fault.faultCode == error) {
						fault(event.fault);
					}
				}));
			return this;
		}

		public function onSuccessGoto(view:String):ResultHandler {
			token.addResponder(new AsyncResponder( //
				function(event:ResultEvent, token:Object=null):void {
					ContextFactory.context.view.goto(view);
				}, function(event:FaultEvent, token:Object=null):void {
				// do nothing
				}));
			return this;
		}
	}
}