package org.adaptiveplatform.surveys.controller;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.dto.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.FilledSurveyQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExportSurveysForPrintingController {

	@Resource
	private SurveyDao surveyDao;

	@RequestMapping("/export")
	public String exportSurveys(@RequestParam("templateId") Long templateId, @RequestParam("groupId") Long groupId, Model model) {
		FilledSurveyQuery query = new FilledSurveyQuery();
		query.setTemplateId(templateId);
		query.setGroupId(groupId);
		List<FilledSurveyDto> surveys = surveyDao.querySurveys(query);
		model.addAttribute("surveys", surveys);
		return "view";
	}
}
