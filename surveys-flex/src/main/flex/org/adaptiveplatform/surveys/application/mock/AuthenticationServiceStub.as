package org.adaptiveplatform.surveys.application.mock {
import flash.utils.Dictionary;

import mx.collections.ArrayCollection;

import org.adaptiveplatform.communication.FaultResultHandler;
import org.adaptiveplatform.communication.ResultHandler;
import org.adaptiveplatform.communication.SuccessResultHandler;
import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.application.UserRole;
import org.adaptiveplatform.surveys.dto.generated.UserDto;

internal class AuthenticationServiceStub implements AuthenticationService {

    private var users:Dictionary = new Dictionary();
    private var _currentUser:UserDto;

    public function AuthenticationServiceStub() {
        addUser("e@v.al", UserRole.EVALUATOR);
        addUser("a@dm.in", UserRole.ADMINISTRATOR, UserRole.TEACHER);
        addUser("u@s.er", UserRole.USER);
    }

    private function addUser(email:String, ...roles):void {
        var user:UserDto = new UserDto();
        user.id = new Random().id();
        user.name = email;
        user.email = email;
        user.roles = new ArrayCollection(roles);
        users[email] = user;
    }

    public function login(username:String, password:String):ResultHandler {
        var user:UserDto = users[username];
        if (user != null) {
            _currentUser = user;
            return new SuccessResultHandler({name: user.name, authorities: user.roles.toArray()});
        }
        return new FaultResultHandler("123", "incorrect credentials");
    }

    public function logout():ResultHandler {
        _currentUser = null;
        return new SuccessResultHandler();
    }

    public function get authenticated():Boolean {
        return _currentUser != null;
    }

    public function get currentUser():String {
        if (!authenticated) {
            return null;
        }
        return _currentUser.name;
    }

    public function get authorities():ArrayCollection {
        if (!authenticated) {
            return new ArrayCollection();
        }
        return _currentUser.roles;
    }

    public function inRole(role:String):Boolean {
        if (authenticated) {
            return _currentUser.roles.contains(role);
        } else {
            return false;
        }
    }
}
}