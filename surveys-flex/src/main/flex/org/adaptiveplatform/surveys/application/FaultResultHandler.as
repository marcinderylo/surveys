package org.adaptiveplatform.surveys.application {
	import mx.rpc.Fault;

	public class FaultResultHandler implements ResultHandler {
		private var faultReason:Fault;

		public function FaultResultHandler(code:String, string:String, detail:String=null, ... arguments) {
			this.faultReason=new Fault(code, string, detail);
		}

		public function onSuccess(success:Function):ResultHandler {
			// do nothing
			return this;
		}

		public function onFault(fault:Function):ResultHandler {
			fault(faultReason);
			return this;
		}

		public function onError(error:String, fault:Function):ResultHandler {
			// LATER create new Fault class to handle error messages
			if (error == faultReason.faultCode) {
				fault(faultReason);
			}
			return this;
		}

		public function onSuccessGoto(view:String):ResultHandler {
			// do nothing
			return this;
		}
	}
}