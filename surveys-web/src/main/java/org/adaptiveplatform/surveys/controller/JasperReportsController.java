package org.adaptiveplatform.surveys.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.adaptiveplatform.surveys.dto.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.SurveyQuestionAnswerDto;
import org.adaptiveplatform.surveys.dto.SurveyQuestionDto;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JasperReportsController {

	@Resource
	private ResourceLoader resources;

	@RequestMapping("/export.pdf")
	public HttpEntity<byte[]> exportSurveysPdf() throws IOException, JRException {
		JasperReport jasperReport = loadReport("classpath:/singleSurvey.jasper");

		JasperPrint print1 = printReport(jasperReport, questions(10));
		JasperPrint print2 = printReport(jasperReport, questions(10));
		JasperPrint print3 = printReport(jasperReport, questions(10));

		byte[] byteArray = exportReportToPdf(Arrays.asList(print1, print2, print3));
		return createHttpResponse(byteArray);
	}

	private JasperReport loadReport(String resourcePath) throws IOException, JRException {
		InputStream inputStream = resources.getResource(resourcePath).getInputStream();
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
		return jasperReport;
	}

	private byte[] exportReportToPdf(Collection<JasperPrint> prints) throws JRException {
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, prints);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
		exporter.exportReport();
		return byteArrayOutputStream.toByteArray();
	}

	private JasperPrint printReport(JasperReport jasperReport, Collection<?> data) throws JRException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(JRParameter.REPORT_DATA_SOURCE, new JRBeanCollectionDataSource(data));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
		return jasperPrint;
	}

	private HttpEntity<byte[]> createHttpResponse(byte[] body) {
		HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "text/csv; charset=UTF-8");
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
