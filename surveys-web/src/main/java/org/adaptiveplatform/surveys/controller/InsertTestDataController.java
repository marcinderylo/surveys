package org.adaptiveplatform.surveys.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.application.EvaluationFacade;
import org.adaptiveplatform.surveys.application.StudentGroupDao;
import org.adaptiveplatform.surveys.application.StudentGroupFacade;
import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.application.SurveyFacade;
import org.adaptiveplatform.surveys.application.UserFacade;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.dto.AddGroupMemberCommand;
import org.adaptiveplatform.surveys.dto.AddGroupToResearchCommand;
import org.adaptiveplatform.surveys.dto.AnswerTemplateDto;
import org.adaptiveplatform.surveys.dto.ChangeGroupMembersCommand;
import org.adaptiveplatform.surveys.dto.CreateStudentGroupCommand;
import org.adaptiveplatform.surveys.dto.CreateSurveyTemplateCommand;
import org.adaptiveplatform.surveys.dto.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.PrepareResearchCommand;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateQuery;
import org.adaptiveplatform.surveys.dto.QuestionTemplateDto;
import org.adaptiveplatform.surveys.dto.QuestionTypeEnum;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;
import org.adaptiveplatform.surveys.dto.StudentGroupDto;
import org.adaptiveplatform.surveys.dto.StudentGroupQuery;
import org.adaptiveplatform.surveys.dto.SurveyQuestionDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Sets;

@Controller
public class InsertTestDataController {

	private static final String ADMIN_EMAIL = "adaptserver@gmail.com";
	private static final String ADMIN_PASSWORD = "adapt2010";
	private static final String STUDENT_LOGIN = "student1@ap.ap";
	private static final String TEACHER_LOGIN = "teacher@ap.ap";
	private static final String EVALUATOR_LOGIN = "evaluator@ap.ap";
	private static final String PASSWORD = "test1";

	@Resource
	private UserFacade userFacade;

	@Resource
	private StudentGroupFacade groupFacade;

	@Resource
	private StudentGroupDao groupDao;

	@Resource
	private EvaluationFacade evaluationFacade;

	@Resource
	private SurveyFacade surveyFacade;

	@Resource
	private SurveyDao surveyDao;

	@Resource
	private AuthenticationService authenticationService;
	
	@RequestMapping("/test")
	public String testingTools(){
		return "insertTestData";
	}

	@RequestMapping("/createUsers")
	public String createUsers() {
		userFacade.registerUser(new RegisterAccountCommand("test evaluator", PASSWORD, EVALUATOR_LOGIN));
		userFacade.registerUser(new RegisterAccountCommand("test teacher", PASSWORD, TEACHER_LOGIN));
		userFacade.registerUser(new RegisterAccountCommand("test student1", PASSWORD, STUDENT_LOGIN));

		authenticationService.login(ADMIN_EMAIL, ADMIN_PASSWORD);
		userFacade.setUserRoles(EVALUATOR_LOGIN, Sets.newHashSet(Role.EVALUATOR));
		userFacade.setUserRoles(TEACHER_LOGIN, Sets.newHashSet(Role.TEACHER));

		authenticationService.login(TEACHER_LOGIN, PASSWORD);
		Long groupId = groupFacade.createGroup(new CreateStudentGroupCommand("grupka"));
		groupFacade.changeGroupMembers(new ChangeGroupMembersCommand(groupId, //
				new AddGroupMemberCommand(EVALUATOR_LOGIN, GroupRoleEnum.EVALUATOR.name()),//
				new AddGroupMemberCommand(STUDENT_LOGIN, GroupRoleEnum.STUDENT.name())));
		return "redirect:test";
	}

	@RequestMapping("/createTemplate")
	public String createTemplate() {
		authenticationService.login(EVALUATOR_LOGIN, PASSWORD);

		CreateSurveyTemplateCommand survey = new CreateSurveyTemplateCommand();
		survey.setName("survey " + UUID.randomUUID());
		survey.setQuestions(questions());
		Long templateId = surveyFacade.createTemplate(survey);

		PrepareResearchCommand research = new PrepareResearchCommand();
		research.setName("research " + UUID.randomUUID());
		research.setSurveyTemplateId(templateId);
		research.setGroupsToAdd(groups());
		evaluationFacade.createResearch(research);
		return "redirect:test";
	}
	
	@RequestMapping("/fillSurveys")
	public String fillSurveys() {
		authenticationService.login(STUDENT_LOGIN, PASSWORD);
		PublishedSurveyTemplateQuery query = new PublishedSurveyTemplateQuery();
		query.setRunAs(GroupRoleEnum.STUDENT);
		List<PublishedSurveyTemplateDto> publishedTemplates = surveyDao.queryPublishedTemplates(query);
		for (PublishedSurveyTemplateDto publishedTemplate : publishedTemplates) {
			Long publicationId = publishedTemplate.getId();
			Long surveyId = surveyFacade.startFilling(publicationId);
			FilledSurveyDto survey = surveyDao.getSurvey(surveyId);
			for (SurveyQuestionDto question : survey.getQuestions()) {
				surveyFacade.answerQuestion(surveyId, question.getNumber(), Arrays.asList(1), null);
			}
			surveyFacade.submit(surveyId);
		}
		return "redirect:test";
	}

	private List<QuestionTemplateDto> questions() {
		List<QuestionTemplateDto> questions = new ArrayList<QuestionTemplateDto>();
		for (int i = 0; i < 10; i++) {
			QuestionTemplateDto dto = new QuestionTemplateDto();
			dto.setText("sample text " + i);
			dto.setType(QuestionTypeEnum.MULTIPLE_CHOICE);
			List<AnswerTemplateDto> answers = new ArrayList<AnswerTemplateDto>();
			for (int j = 0; j < 3; j++) {
				AnswerTemplateDto answer = new AnswerTemplateDto();
				answer.setText("answer text " + j);
				answers.add(answer);
			}
			dto.setAnswers(answers);
			questions.add(dto);
		}
		return questions;
	}

	private List<AddGroupToResearchCommand> groups() {
		List<StudentGroupDto> groups = groupDao.query(new StudentGroupQuery(GroupRoleEnum.EVALUATOR));
		List<AddGroupToResearchCommand> commands = new ArrayList<AddGroupToResearchCommand>();
		for (StudentGroupDto group : groups) {
			commands.add(new AddGroupToResearchCommand(group.getId()));
		}
		return commands;
	}
}
