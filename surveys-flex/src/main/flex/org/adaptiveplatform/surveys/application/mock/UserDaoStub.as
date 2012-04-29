package org.adaptiveplatform.surveys.application.mock {
import mx.collections.ArrayCollection;

import org.adaptiveplatform.communication.ResultHandler;
import org.adaptiveplatform.communication.SuccessResultHandler;
import org.adaptiveplatform.surveys.application.UserRole;
import org.adaptiveplatform.surveys.application.generated.UserDao;
import org.adaptiveplatform.surveys.dto.generated.UserDto;
import org.adaptiveplatform.surveys.dto.generated.UserQuery;

internal class UserDaoStub implements UserDao {
    private var random:RandomDtoGenerator = new RandomDtoGenerator();
    private var users:ArrayCollection = new ArrayCollection();

    public function UserDaoStub(count:int = 100) {
        for (var i:int = 0; i < count; i++) {
            users.addItem(random.user());
        }
    }

    public function getUser(number:Number):ResultHandler {
        return new SuccessResultHandler(users.getItemAt(number % users.length));
    }

    public function getByEmail(string:String):ResultHandler {
        return new SuccessResultHandler(users.getItemAt(HashUtils.hash(string) % users.length));
    }

    public function query(userQuery:UserQuery):ResultHandler {
        return new SuccessResultHandler(users);
    }
}
}
