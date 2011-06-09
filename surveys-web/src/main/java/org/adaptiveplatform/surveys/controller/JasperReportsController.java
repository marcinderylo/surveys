package org.adaptiveplatform.surveys.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.jasperreports.engine.JRException;

import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.dto.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.FilledSurveyQuery;
import org.adaptiveplatform.surveys.dto.SurveyQuestionAnswerDto;
import org.adaptiveplatform.surveys.dto.SurveyQuestionDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JasperReportsController {

	@Resource
	private SurveysPrintFactory surveysReportFactory;

	@Resource
	private SurveyDao surveyDao;

	@RequestMapping("/export.pdf")
	public HttpEntity<byte[]> exportSurveysPdf(@RequestParam("templateId") Long templateId,
			@RequestParam("groupId") Long groupId) throws IOException, JRException {
		FilledSurveyQuery query = new FilledSurveyQuery();
		query.setGroupId(groupId);
		query.setTemplateId(templateId);

		List<FilledSurveyDto> surveys = surveyDao.querySurveys(query);
		byte[] byteArray = surveysReportFactory.printSurveysToPdf(surveys);
		return createHttpResponse(byteArray);
	}

	private HttpEntity<byte[]> createHttpResponse(byte[] body) {
		HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/pdf; charset=UTF-8");
		header.set("Content-Length", String.valueOf(body.length));
		return new ResponseEntity<byte[]>(body, header, HttpStatus.OK);
	}

	private static final int SURVEYS_NUMBER = 20;
	private static final int QUESTIONS_NUMBER = 8;
	private static final int ANSWERS_NUMBER = 4;

	public static List<FilledSurveyDto> surveysToPrint() {
		List<FilledSurveyDto> list = new ArrayList<FilledSurveyDto>();
		for (int i = 0; i < SURVEYS_NUMBER; i++) {
			list.add(survey());
		}
		return list;
	}

	private static FilledSurveyDto survey() {
		FilledSurveyDto survey = new FilledSurveyDto();
		survey.setDescription("survey description");
		survey.setGroupName("group name");
		survey.setUserName("user name");
		survey.setSurveyTemplateName("template name");
		survey.setSubmitDate(new Date());
		survey.setQuestions(questions(QUESTIONS_NUMBER));
		return survey;
	}

	private static List<SurveyQuestionDto> questions(int questionsNumber) {
		List<SurveyQuestionDto> questions = new ArrayList<SurveyQuestionDto>();
		for (int i = 0; i < questionsNumber; i++) {
			SurveyQuestionDto question = new SurveyQuestionDto();
			question.setNumber(i + 1);
			question.setText("question text");
			question.setComment("comment text");
			question.setAnswers(answers(ANSWERS_NUMBER));
			questions.add(question);
		}
		return questions;
	}

	private static List<SurveyQuestionAnswerDto> answers(int answersNumber) {
		List<SurveyQuestionAnswerDto> answers = new ArrayList<SurveyQuestionAnswerDto>();
		for (int i = 0; i < answersNumber; i++) {
			SurveyQuestionAnswerDto answer = new SurveyQuestionAnswerDto();
			answer.setNumber(i + 1);
			answer.setSelected(i == 0);
			answer.setRequiresComment(false);
			answer.setText("answer text");
			answers.add(answer);
		}
		return answers;
	}
}
