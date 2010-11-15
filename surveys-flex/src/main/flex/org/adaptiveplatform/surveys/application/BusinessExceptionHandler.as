package org.adaptiveplatform.surveys.application {
	import mx.controls.Alert;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.rpc.Fault;
	import mx.utils.StringUtil;
	
	import org.adaptiveplatform.communication.RemoteBusinessExceptionParametersUnmarshaller;

	public class BusinessExceptionHandler {
		
		public static const BUSINESS_EXCEPTION_PREFIX :String ="BSN_";
		
		public function BusinessExceptionHandler() {
		}
		
		public static function displayAlert(resources:IResourceManager, defaultMessage:String =null):Function{
			return function(fault:Fault):void{
				var key:String;
				if(fault.faultCode.substr(0,4) == BUSINESS_EXCEPTION_PREFIX){
					arguments = new RemoteBusinessExceptionParametersUnmarshaller().split(fault.faultDetail);
					key = "error."+fault.faultCode.substr(4);	
				}else{
					key = "error."+fault.faultCode;
				}
				var message:String = resources.getString("adaptive", key);
				if(message==null){
					if(defaultMessage!=null){
						message = resources.getString("adaptive",defaultMessage);
					}
					if(message==null){
						message = fault.faultString;
					}
				}
				message = StringUtil.substitute(message, arguments);
				Alert.show(message,resources.getString("adaptive","error"));
			}
		}
	}
}
