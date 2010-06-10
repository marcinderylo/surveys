package org.adaptiveplatform.surveys.application {
	import mx.controls.Alert;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.rpc.Fault;

	public class FaultHandler {
		public function FaultHandler() {
		}
		
		public static function displayAlert(resources:IResourceManager, defaultMessage:String =null):Function{
			return function(fault:Fault):void{
				var key:String = "error."+fault.faultCode;
				var message:String = resources.getString("adaptive",key);
				if(message==null){
					if(defaultMessage!=null){
						message = resources.getString("adaptive",defaultMessage);
					}
					if(message==null){
						message = fault.faultString;
					}
				}
				Alert.show(message,resources.getString("adaptive","error"));
			}
		}
	}
}
