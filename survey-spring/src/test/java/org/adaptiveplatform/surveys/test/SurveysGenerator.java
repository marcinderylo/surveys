package org.adaptiveplatform.surveys.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.adaptiveplatform.surveys.dto.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.SurveyQuestionAnswerDto;
import org.adaptiveplatform.surveys.dto.SurveyQuestionDto;

public class SurveysGenerator {
	private static final int SURVEYS_NUMBER = 20;
	private static final int QUESTIONS_NUMBER_MIN = 5;
	private static final int QUESTIONS_NUMBER_MAX = 20;
	private static final int ANSWERS_NUMBER_MIN = 2;
	private static final int ANSWERS_NUMBER_MAX = 6;

	private static RandomStringGenerator random = new RandomStringGenerator();

	public static List<FilledSurveyDto> surveysToPrint() {
		List<FilledSurveyDto> list = new ArrayList<FilledSurveyDto>();
		for (int i = 0; i < SURVEYS_NUMBER; i++) {
			list.add(survey());
		}
		return list;
	}

	public static List<SurveyQuestionDto> questionsToPrint() {
		return questions(random.number(QUESTIONS_NUMBER_MIN, QUESTIONS_NUMBER_MAX));
	}

	private static FilledSurveyDto survey() {
		FilledSurveyDto survey = new FilledSurveyDto();
		survey.setDescription(random.paragraph());
		survey.setGroupName(random.word());
		survey.setUserName(random.word());
		survey.setSurveyTemplateName(random.sentence());
		survey.setSubmitDate(new Date());
		survey.setQuestions(questionsToPrint());
		return survey;
	}

	private static List<SurveyQuestionDto> questions(int questionsNumber) {
		List<SurveyQuestionDto> questions = new ArrayList<SurveyQuestionDto>();
		for (int i = 0; i < questionsNumber; i++) {
			SurveyQuestionDto question = new SurveyQuestionDto();
			question.setNumber(i + 1);
			question.setText(random.paragraph());
			question.setComment(random.sentence());
			question.setAnswers(answers(random.number(ANSWERS_NUMBER_MIN, ANSWERS_NUMBER_MAX)));
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
			answer.setText(random.sentence());
			answers.add(answer);
		}
		return answers;
	}
}
