package org.adaptiveplatform.surveys.application.mock {
	import mx.collections.ArrayCollection;
	
	import org.adaptiveplatform.communication.FaultResultHandler;
	import org.adaptiveplatform.communication.ResultHandler;
	import org.adaptiveplatform.communication.SuccessResultHandler;
	import org.adaptiveplatform.surveys.application.generated.SurveyDao;
	import org.adaptiveplatform.surveys.application.generated.SurveyFacade;
	import org.adaptiveplatform.surveys.dto.generated.CreateSurveyTemplateCommand;
	import org.adaptiveplatform.surveys.dto.generated.FilledSurveyDto;
	import org.adaptiveplatform.surveys.dto.generated.FilledSurveyQuery;
	import org.adaptiveplatform.surveys.dto.generated.PublishedSurveyTemplateDto;
	import org.adaptiveplatform.surveys.dto.generated.PublishedSurveyTemplateQuery;
	import org.adaptiveplatform.surveys.dto.generated.QuestionTypeEnum;
	import org.adaptiveplatform.surveys.dto.generated.SurveyQuestionAnswerDto;
	import org.adaptiveplatform.surveys.dto.generated.SurveyQuestionDto;
	import org.adaptiveplatform.surveys.dto.generated.SurveyTemplateDto;
	import org.adaptiveplatform.surveys.dto.generated.SurveyTemplateQuery;

	internal class SurveyServiceMock implements SurveyDao, SurveyFacade {

		private var quizTemplates:ArrayCollection = new ArrayCollection();
		private var quizTemplateNextId:Number = 1;

		private var submittedQuizes:ArrayCollection = new ArrayCollection();
		private var submittedQuizNextId:Number = 1;
		
		private var publishedQuizTemplates:ArrayCollection = new ArrayCollection();
		private var publishedQuizNextId:Number = 1;

		private var authentication:AuthenticationServiceMock;

		public function SurveyServiceMock(authentication:AuthenticationServiceMock) {
			this.authentication = authentication;
			quizTemplates.addItem(createQuizTemplate(1, "ocena kursu - podstawy javy", 5, 4));
			quizTemplates.addItem(createQuizTemplate(2, "ocena kursu - wzorce projektowe", 6, 3));
			quizTemplates.addItem(createQuizTemplate(3, "ocena semestru", 6, 3));
			submittedQuizes.addItem(createSubmittedQuiz(submittedQuizNextId++, 1, new Date(), "rafal", 5, 4));
			submittedQuizes.addItem(createSubmittedQuiz(submittedQuizNextId++, 2, new Date(), "rafal", 6, 3));
			submittedQuizes.addItem(createSubmittedQuiz(submittedQuizNextId++, 3, new Date(), "rafal", 6, 3));
			publishedQuizTemplates.addItem(createPublishedQuiz(publishedQuizNextId++, "ocena kursu - podstawy javy", 5, 4));
			publishedQuizTemplates.addItem(createPublishedQuiz(publishedQuizNextId++, "ocena kursu - wzorce projektowe", 6, 3));
			publishedQuizTemplates.addItem(createPublishedQuiz(publishedQuizNextId++, "ocena semestru", 6, 3));			
		}

		public function queryTemplates(query:SurveyTemplateQuery):ResultHandler {
			var result:ArrayCollection = new ArrayCollection();
			for each (var survey:SurveyTemplateDto in quizTemplates) {
				/* if (query.keyword && query.keyword != survey.name) {
					continue;
				}
				if (query.status && query.status != survey.published) {
					continue;
				} */
				result.addItem(survey);
			}
			return new SuccessResultHandler(result);
		}

		public function querySurveys(query:FilledSurveyQuery):ResultHandler {
			var result:ArrayCollection = new ArrayCollection();
			for each (var survey:FilledSurveyDto in submittedQuizes) {
				if (query.fromDate && survey.startDate < query.fromDate) {
					continue;
				}
				if (query.untilDate && survey.startDate > query.untilDate) {
					continue;
				}
				result.addItem(survey);
			}
			return new SuccessResultHandler(result);
		}

		public function getSurvey(id:Number):ResultHandler {
			for each (var quiz:FilledSurveyDto in submittedQuizes) {
				if (quiz.id == id) {
					return new SuccessResultHandler(quiz);
				}
			}
			return new FaultResultHandler("404", "Resource not found.");
		}

		public function startFilling(long:Number):ResultHandler{
			return new SuccessResultHandler(1);
		}

		public function submit(surveyId:Number):ResultHandler {
			return new SuccessResultHandler();
		}

		public function answerQuestion(long:Number, integer:int, list:ArrayCollection, string:String):ResultHandler{
			return new SuccessResultHandler();
		}

		// INTERNAL 
		internal function doGetTemplate(id:Number):SurveyTemplateDto {
			for each (var quiz:SurveyTemplateDto in quizTemplates) {
				if (quiz.id == id) {
					return quiz;
				}
			}
			return null;
		}

		internal function createQuizTemplate(id:Number, name:String, questions:int, answers:int):SurveyTemplateDto {
			var quiz:SurveyTemplateDto = new SurveyTemplateDto();
			quiz.id = id;
			quiz.name = name;
		//	quiz.published = 1;
//			for (var q:int = 1; q < questions + 1; q++) {
//				var question:SurveyQuestionDto = createQuestion(name, q, answers);
////				quiz.questions.addItem(question);
//			}
			return quiz;
		}
		
		internal function createPublishedQuiz(id:Number, name:String, questions:int, answers:int):PublishedSurveyTemplateDto{
			var quiz:PublishedSurveyTemplateDto = new PublishedSurveyTemplateDto();
			quiz.templateId = id;
			quiz.filledSurveyId = id;
			quiz.name = name;
			quiz.submitted = false;
			quiz.startingDate = new Date();
			quiz.expirationDate = new Date();
			return quiz;
		}

		internal function submitQuiz(templateId:Number):Number {
			var dto:FilledSurveyDto = createSubmittedQuiz(submittedQuizNextId++, templateId, new Date(), authentication.currentUser, null, null);
			submittedQuizes.addItem(dto);
			return dto.id;
		}

		internal function createSubmittedQuiz(id:Number, templateId:Number, date:Date, user:String, questions:int, answers:int):FilledSurveyDto {
			var survey:FilledSurveyDto = new FilledSurveyDto();
			survey.id = id;
			survey.surveyTemplateId = templateId;
			survey.surveyTemplateName = doGetTemplate(templateId).name;
			survey.startDate = date;
			survey.questions = new ArrayCollection();
			for (var q:int = 1; q < questions + 1; q++) {
				var question:SurveyQuestionDto = createQuestion(survey.surveyTemplateName, q, answers);				
				survey.questions.addItem(question);
			}
			return survey;
		}

		internal function createQuestion(keyword:String, questionNo:int, answers:int):SurveyQuestionDto {
			var question:SurveyQuestionDto = new SurveyQuestionDto();
			question.number = questionNo;
			question.text = keyword + " " + keyword + " " + keyword + " " + questionNo + " ?";


			question.answers = new ArrayCollection();
			for (var a:int = 1; a < answers + 1; a++) {
				question.answers.addItem(createAnswer(keyword, a));
			}
			
			if( (questionNo - 1) % 3 == 0)
				question.type = QuestionTypeEnum.SINGLE_CHOICE;
			if( (questionNo - 1) % 3 == 1)
				question.type = QuestionTypeEnum.MULTIPLE_CHOICE;
			if( (questionNo - 1) % 3 == 2){
				question.type = QuestionTypeEnum.OPEN;
				question.answers = new ArrayCollection();
			}
			
			return question;
		}

		internal function createAnswer(keyword:String, answerNo:int):SurveyQuestionAnswerDto {
			var answer:SurveyQuestionAnswerDto = new SurveyQuestionAnswerDto;
//			answer.correct = (answerNo - 1 % 3 == 0);
			answer.text = keyword + " " + keyword + " " + answerNo + " (" + (answer.requiresComment ? "+)" : "-)");
			answer.requiresComment = (answerNo - 1) % 3 ? true : false;
			return answer;
		}
		
		internal function queryPublishedSurveys(query:PublishedSurveyTemplateQuery):ResultHandler {
			var result:ArrayCollection = new ArrayCollection();
			for each (var survey:PublishedSurveyTemplateDto in publishedQuizTemplates) {
				/* if (query.keyword && query.keyword != survey.name) {
					continue;
				}
				if (query.status && query.status != survey.published) {
					continue;
				} */
				result.addItem(survey);
			}
			return new SuccessResultHandler(result);
		}
		
		public function getTemplate(long:Number):ResultHandler{ return new SuccessResultHandler();}
		public function getPublishedSurveyTemplate(long:Number, long2:Number):ResultHandler{ return new SuccessResultHandler();}
		public function getSurveyStatistics(long:Number):ResultHandler{return new SuccessResultHandler();}
		public function createTemplate(createSurveyTemplateCommand:CreateSurveyTemplateCommand):ResultHandler{return new SuccessResultHandler();}
		public function updateTemplate(long:Number, createSurveyTemplateCommand:CreateSurveyTemplateCommand):ResultHandler{return new SuccessResultHandler();}
		
		public function queryPublishedTemplates(publishedSurveyTemplateQuery:PublishedSurveyTemplateQuery):ResultHandler{ return queryPublishedSurveys(null);}
		public function getPublishedSurveyStatistics(long:Number, long2:Number):ResultHandler{ return new SuccessResultHandler();}
		public function removeSurveyTemplate(long:Number):ResultHandler{return new SuccessResultHandler();}
	}
}