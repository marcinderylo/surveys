package org.adaptiveplatform.surveys.application {
	
	import org.adaptiveplatform.surveys.application.AuthenticationService;
	import org.adaptiveplatform.surveys.application.generated.EvaluationDao;
	import org.adaptiveplatform.surveys.application.generated.EvaluationFacade;
    import org.adaptiveplatform.surveys.application.generated.StudentGroupDao;
	import org.adaptiveplatform.surveys.application.generated.StudentGroupFacade;
	import org.adaptiveplatform.surveys.application.generated.SurveyDao;
	import org.adaptiveplatform.surveys.application.generated.SurveyFacade;
	import org.adaptiveplatform.surveys.application.generated.SystemInformationDao;
	import org.adaptiveplatform.surveys.application.generated.UserDao;
	import org.adaptiveplatform.surveys.application.generated.UserFacade;

	public interface ServiceLocator {
		function get authentication():AuthenticationService;
		function get userFacade():UserFacade;
		function get userDao():UserDao;
		function get surveyFacade():SurveyFacade;
		function get surveyDao():SurveyDao;
		function get studentGroupFacade():StudentGroupFacade;
		function get studentGroupDao():StudentGroupDao;
		function get evaluationFacade():EvaluationFacade;
		function get evaluationDao():EvaluationDao;
		function get systemInformationDao():SystemInformationDao;


    }
}