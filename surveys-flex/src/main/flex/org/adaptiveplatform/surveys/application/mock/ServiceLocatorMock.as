package org.adaptiveplatform.surveys.application.mock {
	import org.adaptiveplatform.surveys.application.AuthenticationService;
	import org.adaptiveplatform.surveys.application.ServiceLocator;
	
	import org.adaptiveplatform.surveys.application.generated.EvaluationDao;
	import org.adaptiveplatform.surveys.application.generated.EvaluationFacade;
	import org.adaptiveplatform.surveys.application.generated.StudentGroupDao;
	import org.adaptiveplatform.surveys.application.generated.StudentGroupFacade;
	import org.adaptiveplatform.surveys.application.generated.SurveyDao;
	import org.adaptiveplatform.surveys.application.generated.SurveyFacade;
	import org.adaptiveplatform.surveys.application.generated.UserDao;
	import org.adaptiveplatform.surveys.application.generated.UserFacade;

	public class ServiceLocatorMock implements ServiceLocator {

		private var _authentication:AuthenticationServiceMock;
		private var _userFacade:UserServiceMock;
		private var _surveyFacade:SurveyServiceMock;

		public function ServiceLocatorMock() {
			_userFacade=new UserServiceMock();
			_authentication=new AuthenticationServiceMock(_userFacade);
			_surveyFacade=new SurveyServiceMock(_authentication);

		}

		public function get authentication():AuthenticationService {
			return _authentication;
		}

		public function get userFacade():UserFacade {
			return _userFacade;
		}

		public function get userDao():UserDao {
			return _userFacade;
		}

		public function get surveyFacade():SurveyFacade {
			return _surveyFacade;
		}

		public function get surveyDao():SurveyDao {
			return _surveyFacade;
		}

		public function get studentGroupFacade():StudentGroupFacade {
			return null;
		}

		public function get studentGroupDao():StudentGroupDao {
			return null
		}

		public function get evaluationFacade():EvaluationFacade {
			return null;
		}

		public function get evaluationDao():EvaluationDao {
			return null;
		}
	}
}