package org.adaptiveplatform.surveys.application {
	
	import mx.collections.ArrayCollection;
	import mx.messaging.ChannelSet;
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	
	import org.adaptiveplatform.communication.AsyncTokenResultHandler;
	import org.adaptiveplatform.communication.FaultResultHandler;
	import org.adaptiveplatform.communication.ResultHandler;

	public class AuthenticationServiceRemote implements AuthenticationService {

		private var channels:ChannelSet;

		private var _currentUser:String;
		private var _currentUserId:Number;
		private var _authorities:ArrayCollection;
		private var _authenticated:Boolean = false;

		public function AuthenticationServiceRemote(channels:ChannelSet) {
			this.channels = channels;
		}

		public function login(username:String, password:String):ResultHandler {
			if (authenticated) {
				return new FaultResultHandler("Authentication.AlreadyAuthenticated", "user you are already logged in");
			}
			var token:AsyncToken = channels.login(username, password);
			var resultHandler:ResultHandler = new AsyncTokenResultHandler(token);
			resultHandler.onSuccess(function(result:Object = null):void {
					_currentUser = result.name;
					_authorities = new ArrayCollection(result.authorities);
					_authenticated = true;
				}).onFault(function(fault:Fault):void {
					trace(fault.faultCode, fault.faultDetail, fault.faultString);
				});
			return resultHandler;
		}

		public function logout():ResultHandler {
			var token:AsyncToken = channels.logout();
			var resultHandler:ResultHandler = new AsyncTokenResultHandler(token);
			resultHandler.onSuccess(function(result:Object = null):void {
					_currentUser = null;
					_currentUserId = -1;
					_authorities = new ArrayCollection();
					_authenticated = false;
				});
			return resultHandler;
		}

		public function get currentUser():String {
			return _currentUser;
		}
		
		public function get authenticated():Boolean {
			return _authenticated;
		}

		public function get authorities():ArrayCollection {
			return new ArrayCollection(_authorities.toArray());
		}

		public function inRole(role:String):Boolean {
			return authenticated && _authorities.contains(role);
		}
	}
}