package org.adaptiveplatform.surveys.application {

	public interface ResultHandler {
		function onSuccess(success:Function):ResultHandler;
		function onFault(fault:Function):ResultHandler;
		function onSuccessGoto(view:String):ResultHandler;
		function onError(error:String, fault:Function):ResultHandler;
	}
}