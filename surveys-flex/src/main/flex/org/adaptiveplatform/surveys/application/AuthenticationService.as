package org.adaptiveplatform.surveys.application {

	
	import mx.collections.ArrayCollection;

	public interface AuthenticationService {
		function login(username:String, password:String):ResultHandler;
		function logout():ResultHandler;
		function get authenticated():Boolean;
		function get currentUser():String;
		function get authorities():ArrayCollection;
		function inRole(role:String):Boolean;
	}
}