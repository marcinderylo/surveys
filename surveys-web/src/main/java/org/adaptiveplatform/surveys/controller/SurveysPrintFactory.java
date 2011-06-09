package org.adaptiveplatform.surveys.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
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
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class SurveysPrintFactory {
	private static final String SINGLE_SURVEY_REPORT_PATH = "classpath:/singleSurvey.jasper";

	private static final String SURVEY_TEMPALTE_NAME_PARAM = "surveyTemplateName";
	private static final String GROUP_NAME_PARAM = "groupName";
	private static final String START_DATE_PARAM = "startDate";
	private static final String SUBMIT_DATE_PARAM = "submitDate";
	private static final String DESCRIPTION_PARAM = "description";

	@Resource
	private ResourceLoader resources;

	public byte[] printSurveysToPdf(List<FilledSurveyDto> surveys) {
		try {
			return doPrintSurveysToPdf(surveys);
		} catch (IOException e) {
			throw new RuntimeException("report not found", e);
		} catch (JRException e) {
			throw new RuntimeException("error during pdf generation", e);
		}
	}

	private byte[] doPrintSurveysToPdf(List<FilledSurveyDto> surveys) throws IOException, JRException {
		JasperReport jasperReport = loadReport(SINGLE_SURVEY_REPORT_PATH);
		Collection<JasperPrint> prints = new LinkedList<JasperPrint>();
		for (FilledSurveyDto survey : surveys) {
			prints.add(printSurveyReport(jasperReport, survey));
		}
		return exportReportToPdfBytes(prints);
	}

	private JasperReport loadReport(String resourcePath) throws IOException, JRException {
		InputStream inputStream = resources.getResource(resourcePath).getInputStream();
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
		return jasperReport;
	}

	private JasperPrint printSurveyReport(JasperReport jasperReport, FilledSurveyDto survey) throws JRException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		putSurveyParametersToMap(survey, parameters);
		parameters.put(JRParameter.REPORT_DATA_SOURCE, new JRBeanCollectionDataSource(survey.getQuestions()));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
		return jasperPrint;
	}

	private void putSurveyParametersToMap(FilledSurveyDto survey, Map<String, Object> map) {
		map.put(SURVEY_TEMPALTE_NAME_PARAM, survey.getSurveyTemplateName());
		map.put(GROUP_NAME_PARAM, survey.getGroupName());
		map.put(START_DATE_PARAM, survey.getStartDate());
		map.put(SUBMIT_DATE_PARAM, survey.getSubmitDate());
		map.put(DESCRIPTION_PARAM, survey.getDescription());
	}

	private byte[] exportReportToPdfBytes(Collection<JasperPrint> prints) throws JRException {
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, prints);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
		exporter.exportReport();
		return byteArrayOutputStream.toByteArray();
	}

}
