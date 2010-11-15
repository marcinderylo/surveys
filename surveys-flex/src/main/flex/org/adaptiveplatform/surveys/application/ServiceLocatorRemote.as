package org.adaptiveplatform.surveys.application {
	
	import mx.messaging.Channel;
	import mx.messaging.ChannelSet;
	import mx.messaging.channels.AMFChannel;
	
	import org.adaptiveplatform.communication.RemoteService;
	import org.adaptiveplatform.communication.RemoteServiceImpl;
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

		private var _authentication:AuthenticationServiceRemote;
		private var _userFacade:RemoteUserFacade;
		private var _userDao:RemoteUserDao;
		private var _surveyFacade:RemoteSurveyFacade;
		private var _surveyDao:RemoteSurveyDao;
		private var _studentGroupFacade:RemoteStudentGroupFacade;
		private var _studentGroupDao:RemoteStudentGroupDao;
		private var _evaluationFacade:RemoteEvaluationFacade;
		private var _evaluationDao:RemoteEvaluationDao;
		
		public function ServiceLocatorRemote(channelName:String, channelUrl:String) {
			var channels:ChannelSet = createChannels(channelName, channelUrl);
			var remoteService:RemoteServiceImpl = new RemoteServiceImpl();
			remoteService.channelSet=channels;

			_authentication = new AuthenticationServiceRemote(channels);
			_userFacade = new RemoteUserFacade();
			_userFacade.remoteService = remoteService;
			_userDao = new RemoteUserDao();
			_userDao.remoteService = remoteService;
			_surveyFacade = new RemoteSurveyFacade();
			_surveyFacade.remoteService = remoteService;
			_surveyDao = new RemoteSurveyDao();
			_surveyDao.remoteService = remoteService;
			_studentGroupFacade = new RemoteStudentGroupFacade();
			_studentGroupFacade.remoteService = remoteService;
			_studentGroupDao = new RemoteStudentGroupDao();
			_studentGroupDao.remoteService = remoteService;
			_evaluationFacade = new RemoteEvaluationFacade();
			_evaluationFacade.remoteService = remoteService;
			_evaluationDao = new RemoteEvaluationDao();
			_evaluationDao.remoteService = remoteService;
		}
		private function createChannels(channelName:String, channelUrl:String):ChannelSet{
			var channelSet:ChannelSet=new ChannelSet();
			var channel:Channel=new AMFChannel(channelName, channelUrl);
			channelSet.addChannel(channel);
			return channelSet;
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
