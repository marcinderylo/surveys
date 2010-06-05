package org.adaptiveplatform.surveys.application {
	import mx.collections.ArrayCollection;
	import mx.rpc.Fault;

	public class SuccessResultHandler implements ResultHandler {
		private var result:Object;

		private var faultHandlers:ArrayCollection=new ArrayCollection();
		private var faultHappened:Boolean=false;
		private var fault:Fault;

		public function SuccessResultHandler(result:Object=null) {
			this.result=result;
		}

		public function onSuccess(success:Function):ResultHandler {
			try {
				if (result != null) {
					success(result);
				} else {
					success();
				}
			} catch (error:Error) {
				faultHappened=true;
				fault=new Fault("" + error.errorID, "" + error.name, "" + error.message);
				for each (var faultHandler:Function in faultHandlers) {
					faultHandler(fault);
				}
			}
			return this;
		}

		public function onFault(faultHandler:Function):ResultHandler {
			if (faultHappened) {
				faultHandler(fault);
			} else {
				faultHandlers.addItem(faultHandler);
			}
			return this;
		}

		public function onError(error:String, faultHandler:Function):ResultHandler {
			if (faultHappened) {
				if(fault.faultCode==error){
					faultHandler(fault);	
				}
			} else {
				faultHandlers.addItem(faultHandler);
			}
			return this;
		}

		public function onSuccessGoto(view:String):ResultHandler {
			ContextFactory.context.view.goto(view);
			return this;
		}
	}
}