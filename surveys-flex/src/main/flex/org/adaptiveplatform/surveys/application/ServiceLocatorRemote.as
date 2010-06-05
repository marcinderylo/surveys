package org.adaptiveplatform.surveys.application {
	
	import mx.messaging.Channel;
	import mx.messaging.ChannelSet;
	import mx.messaging.channels.AMFChannel;
	
	import org.adaptiveplatform.surveys.application.generated.EvaluationDao;
	import org.adaptiveplatform.surveys.application.generated.EvaluationFacade;
	import org.adaptiveplatform.surveys.application.generated.RemoteEvaluationDao;
	import org.adaptiveplatform.surveys.application.generated.RemoteEvaluationFacade;
	import org.adaptiveplatform.surveys.application.generated.RemoteStudentGroupDao;
	import org.adaptiveplatform.surveys.application.generated.RemoteStudentGroupFacade;
	import org.adaptiveplatform.surveys.application.generated.RemoteSurveyDao;
	import org.adaptiveplatform.surveys.application.generated.RemoteSurveyFacade;
	import org.adaptiveplatform.surveys.application.generated.RemoteUserDao;
	import org.adaptiveplatform.surveys.application.generated.RemoteUserFacade;
	import org.adaptiveplatform.surveys.application.generated.StudentGroupDao;
	import org.adaptiveplatform.surveys.application.generated.StudentGroupFacade;
	import org.adaptiveplatform.surveys.application.generated.SurveyDao;
	import org.adaptiveplatform.surveys.application.generated.SurveyFacade;
	import org.adaptiveplatform.surveys.application.generated.UserDao;
	import org.adaptiveplatform.surveys.application.generated.UserFacade;
	

	public class ServiceLocatorRemote implements ServiceLocator {

		private var channels:ChannelSet;

		private var _authentication:AuthenticationService;
		private var _userFacade:UserFacade;
		private var _userDao:UserDao;
		private var _surveyFacade:SurveyFacade;
		private var _surveyDao:SurveyDao;
		private var _studentGroupFacade:StudentGroupFacade;
		private var _studentGroupDao:StudentGroupDao;
		private var _evaluationFacade:EvaluationFacade;
		private var _evaluationDao:EvaluationDao;
		
		public function ServiceLocatorRemote(channelName:String, channelUrl:String) {
			channels = new ChannelSet();
			var channel:Channel = new AMFChannel(channelName, channelUrl);
			channels.addChannel(channel);

			_authentication = new AuthenticationServiceRemote(channels);
			_userFacade = new RemoteUserFacade(channels);
			_userDao = new RemoteUserDao(channels);
			_surveyFacade = new RemoteSurveyFacade(channels);
			_surveyDao = new RemoteSurveyDao(channels);
			_studentGroupFacade = new RemoteStudentGroupFacade(channels);
			_studentGroupDao = new RemoteStudentGroupDao(channels);
			_evaluationFacade = new RemoteEvaluationFacade(channels);
			_evaluationDao = new RemoteEvaluationDao(channels);
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
	}
}
