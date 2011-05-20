package org.adaptiveplatform.surveys.application.mock {
import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.application.ServiceLocator;
import org.adaptiveplatform.surveys.application.generated.EvaluationDao;
import org.adaptiveplatform.surveys.application.generated.EvaluationFacade;
import org.adaptiveplatform.surveys.application.generated.StudentGroupDao;
import org.adaptiveplatform.surveys.application.generated.StudentGroupFacade;
import org.adaptiveplatform.surveys.application.generated.SurveyDao;
import org.adaptiveplatform.surveys.application.generated.SurveyFacade;
import org.adaptiveplatform.surveys.application.generated.SystemInformationDao;
import org.adaptiveplatform.surveys.application.generated.UserDao;
import org.adaptiveplatform.surveys.application.generated.UserFacade;

public class StubServiceLocator implements ServiceLocator {

    private var _authentication:AuthenticationServiceStub;
    private var _userFacade:UserFacadeStub;
    private var _surveyFacade:SurveyFacadeStub;
    private var _userDao:UserDao;
    private var _surveyDao:SurveyDao;
    private var _studentGroupFacade:StudentGroupFacade;
    private var _studentGroupDao:StudentGroupDao;
    private var _evaluationFacade:EvaluationFacade;
    private var _evaluationDao:EvaluationDao;
    private var _systemInformationDao:SystemInformationDao;

    public function StubServiceLocator() {
        _authentication = new AuthenticationServiceStub();
        _userFacade = new UserFacadeStub;
        _surveyFacade = new SurveyFacadeStub;
        _userDao = new UserDaoStub();
        _surveyDao = new SurveyDaoStub();
        _studentGroupFacade = new StudentGroupFacadeStub();
        _studentGroupDao = new StudentGroupDaoStub();
        _evaluationFacade = new EvaluationFacadeStub();
        _evaluationDao = new EvaluationDaoStub();
        _systemInformationDao = new SystemInformationDaoStub();
    }

    public function get authentication():AuthenticationService {
        return _authentication;
    }

    public function get userFacade():UserFacade {
        return _userFacade;
    }

    public function get userDao():UserDao {
        return _userDao;
    }

    public function get surveyFacade():SurveyFacade {
        return _surveyFacade;
    }

    public function get surveyDao():SurveyDao {
        return _surveyDao;
    }

    public function get studentGroupFacade():StudentGroupFacade {
        return _studentGroupFacade;
    }

    public function get studentGroupDao():StudentGroupDao {
        return _studentGroupDao;
    }

    public function get evaluationFacade():EvaluationFacade {
        return _evaluationFacade;
    }

    public function get evaluationDao():EvaluationDao {
        return _evaluationDao;
    }

    public function get systemInformationDao():SystemInformationDao {
        return _systemInformationDao;
    }
}
}