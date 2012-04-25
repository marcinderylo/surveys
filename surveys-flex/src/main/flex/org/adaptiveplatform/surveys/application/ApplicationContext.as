package org.adaptiveplatform.surveys.application {
	import flash.events.IEventDispatcher;
	import org.adaptiveplatform.communication.ResultHandler;
    import org.adaptiveplatform.surveys.dto.generated.UserDto;

	public interface ApplicationContext extends IEventDispatcher {
		function get services();
		function get views();
		function login(username:String, password:String):ResultHandler;
		function get currentUser():UserDto;
	}
}