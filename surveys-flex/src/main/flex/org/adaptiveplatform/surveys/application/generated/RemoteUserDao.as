package org.adaptiveplatform.surveys.application.generated {
	
  	import mx.collections.ArrayCollection;
  	import org.adaptiveplatform.surveys.dto.generated.UserDto;
  	import org.adaptiveplatform.surveys.dto.generated.UserQuery;
	
	import org.adaptiveplatform.surveys.application.ResultHandler;
	import org.adaptiveplatform.surveys.application.AbstractRemoteService;
	
	import mx.messaging.ChannelSet;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * NOTE: This file is autogenerated and will be overwritten every time.
	 */
	public class RemoteUserDao extends AbstractRemoteService implements UserDao{
			
		public function RemoteUserDao(channels:ChannelSet) {
			super(channels, "userDao");
		}
		
		public function query(userQuery:UserQuery):ResultHandler{
			var service:RemoteObject = createService();
			service.query(userQuery);
			return createResponder(service);
		}
		public function getUser(long:Number):ResultHandler{
			var service:RemoteObject = createService();
			service.getUser(long);
			return createResponder(service);
		}
		public function getByEmail(string:String):ResultHandler{
			var service:RemoteObject = createService();
			service.getByEmail(string);
			return createResponder(service);
		}
	}
}
