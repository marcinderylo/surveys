package org.adaptiveplatform.surveys.application.mock {
import org.adaptiveplatform.communication.ResultHandler;
import org.adaptiveplatform.communication.SuccessResultHandler;
import org.adaptiveplatform.surveys.application.generated.SystemInformationDao;

internal class SystemInformationDaoStub implements SystemInformationDao {
    public function SystemInformationDaoStub() {
    }

    public function getSystemVersion():ResultHandler {
        return new SuccessResultHandler("TEST");
    }
}
}
