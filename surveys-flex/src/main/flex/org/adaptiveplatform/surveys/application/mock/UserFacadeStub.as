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

internal class UserFacadeStub implements UserFacade {

    public function UserFacadeStub() {
    }

    public function registerUser(registerAccountCommand:RegisterAccountCommand):ResultHandler {
        return new SuccessResultHandler(Math.random());
    }

    public function setUserRoles(string:String, arrayCollection:ArrayCollection):ResultHandler {
        return new SuccessResultHandler();
    }

    public function changePassword(changePasswordCommand:ChangePasswordCommand):ResultHandler {
        return new SuccessResultHandler();
    }
}
}