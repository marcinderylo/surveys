package org.adaptiveplatform.surveys.application.mock {

	import mx.collections.ArrayCollection;
	
	import org.adaptiveplatform.communication.FaultResultHandler;
	import org.adaptiveplatform.communication.ResultHandler;
	import org.adaptiveplatform.communication.SuccessResultHandler;
	import org.adaptiveplatform.surveys.application.UserRole;
	import org.adaptiveplatform.surveys.application.generated.UserDao;
	import org.adaptiveplatform.surveys.application.generated.UserFacade;
	import org.adaptiveplatform.surveys.dto.generated.RegisterAccountCommand;
	import org.adaptiveplatform.surveys.dto.generated.UserDto;
	import org.adaptiveplatform.surveys.dto.generated.UserQuery;
  	import org.adaptiveplatform.surveys.dto.generated.ChangePasswordCommand;

	internal class UserServiceMock implements UserDao, UserFacade {

		[ArrayElementType("adaptlearning.serverapi.dto.generated.UserDto")]
		private var users:Array = new Array();
		private var id:int = 1;

		public function UserServiceMock() {
			registerUser(createRegisterAccountCommand("jim@jim.com", "jim", "j"));
			registerUser(createRegisterAccountCommand("bob@bob.com", "bob", "b"));
			users["bob@bob.com"].roles.addItem(UserRole.ADMINISTRATOR);
		}

		public function registerUser(registerAccountCommand:RegisterAccountCommand):ResultHandler {

			if (registerAccountCommand.email != null && registerAccountCommand.name != null) {
				var found:UserDto = doGetUser(registerAccountCommand.email);
				if (found == null) {
					var user:UserDto = new UserDto();
					user.email = registerAccountCommand.email;
					user.name = registerAccountCommand.name;
					user.id = ++id;
					user.roles = new ArrayCollection();
					user.roles.addItem(UserRole.USER);
					users[user.email] = user;
					return new SuccessResultHandler(registerAccountCommand.email);
				} else {
					return new FaultResultHandler("USR92", "someone has registered using this email");
				}
			} else {
				return new FaultResultHandler("USR003", "name, email and password cannot be null");
			}
		}

		public function setUserRoles(string:String, set2:ArrayCollection):ResultHandler {
			return new SuccessResultHandler();
		}

		public function query(userQuery:UserQuery):ResultHandler {
			var result:ArrayCollection = new ArrayCollection();
			for each (var user:UserDto in users) {
				if (user == null)
					continue;

				if (userQuery.nameContains != null && userQuery.nameContains != "") {
					if ((user.email != null && user.email.indexOf(userQuery.nameContains) < 0) //
						|| (user.name != null && user.name.indexOf(userQuery.nameContains) < 0))
						continue;
				}
				result.addItem(user);
			}
			return new SuccessResultHandler(result);
		}

		public function getByEmail(text:String):ResultHandler {
			return new SuccessResultHandler(doGetUser(text));
		}


		public function getUser(id:Number):ResultHandler {
			for each (var user:UserDto in users) {
				if (user.id == id) {
					return new SuccessResultHandler(user);
				}
			}
			return new FaultResultHandler("NEFE", "no user with id=" + id);
		}

		internal function doGetUser(email:String):UserDto {
			for each (var user:UserDto in users) {
				if (user.email == email) {
					return user;
				}
			}
			return null;
		}

		internal function createRegisterAccountCommand(email:String, name:String, pass:String):RegisterAccountCommand {
			var command:RegisterAccountCommand = new RegisterAccountCommand();
			command.email = email;
			command.name = name;
			return command;
		}

       public function changePassword(changePasswordCommand:ChangePasswordCommand):ResultHandler {
            return new SuccessResultHandler();
       }
	}
}