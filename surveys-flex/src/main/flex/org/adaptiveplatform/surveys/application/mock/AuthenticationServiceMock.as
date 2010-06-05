package org.adaptiveplatform.surveys.application.mock {
	import org.adaptiveplatform.surveys.application.FaultResultHandler;
	import org.adaptiveplatform.surveys.application.ResultHandler;
	import org.adaptiveplatform.surveys.application.SuccessResultHandler;
	import org.adaptiveplatform.surveys.application.AuthenticationService;
	
	import mx.collections.ArrayCollection;
	
	import org.adaptiveplatform.surveys.dto.generated.UserDto;

	public class AuthenticationServiceMock implements AuthenticationService {

		private var users:UserServiceMock;
		private var _currentUser:UserDto;
		private var _authenticated:Boolean;

		public function AuthenticationServiceMock(users:UserServiceMock) {
			this.users = users;
		}

		public function login(username:String, password:String):ResultHandler {
			var user:UserDto = users.doGetUser(username);
			if (user != null ) {
				_currentUser = user;
				return new SuccessResultHandler({name: user.name, authorities: user.roles.toArray()});
			}
			return new FaultResultHandler("123", "incorrect credentials");
		}

		public function logout():ResultHandler {
			_currentUser = null;
			return new SuccessResultHandler();
		}

		public function remindPassword(username:String, email:String):ResultHandler {
			return new SuccessResultHandler();
		}

		public function get authenticated():Boolean {
			return _currentUser != null;
		}

		public function get currentUser():String {
			if (!_authenticated) {
				return null;
			}
			return _currentUser.name;
		}

		public function get authorities():ArrayCollection{
			return new ArrayCollection();
		}
		
		public function inRole(role:String):Boolean{
			if( _currentUser != null )
				return _currentUser.roles.contains(role);
			else 
				return false
		}
	}
}