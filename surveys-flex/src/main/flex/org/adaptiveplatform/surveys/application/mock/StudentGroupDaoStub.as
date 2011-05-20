package org.adaptiveplatform.surveys.application.mock {
import mx.collections.ArrayCollection;

import org.adaptiveplatform.communication.ResultHandler;
import org.adaptiveplatform.communication.SuccessResultHandler;
import org.adaptiveplatform.surveys.application.generated.StudentGroupDao;
import org.adaptiveplatform.surveys.dto.generated.StudentGroupQuery;

internal class StudentGroupDaoStub implements StudentGroupDao {

    private var random:RandomDto = new RandomDto();
    private var groups:ArrayCollection = new ArrayCollection();

    public function StudentGroupDaoStub() {
        for (var i:int = 0; i < 100; i++) {
            groups.addItem(random.studentGroup());
        }
    }

    public function getAvailableGroups():ResultHandler {
        return new SuccessResultHandler(groups);
    }

    public function query(studentGroupQuery:StudentGroupQuery):ResultHandler {
        return new SuccessResultHandler(groups);
    }

    public function getGroup(number:Number):ResultHandler {
        return new SuccessResultHandler(groups.getItemAt(number % groups.length));
    }
}
}
