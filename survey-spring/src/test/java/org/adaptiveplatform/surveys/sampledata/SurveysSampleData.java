package org.adaptiveplatform.surveys.sampledata;

import static org.adaptiveplatform.surveys.builders.AnswerBuilder.answer;
import static org.adaptiveplatform.surveys.builders.CoreFixtureBuilder.EVALUATOR_EMAIL;
import static org.adaptiveplatform.surveys.builders.GroupBuilder.group;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.multiChoiceQuestion;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.openQuestion;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.singleChoiceQuestion;
import static org.adaptiveplatform.surveys.builders.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.student;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.GroupBuilder;
import org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureBuilder;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateQuery;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("development")
public class SurveysSampleData {

    @Resource
    private CoreFixtureBuilder users;
    @Resource
    private SurveysFixtureBuilder surveys;
    @Resource
    private SurveyDao surveyDao;

    private Set<Long> groupIds = new HashSet<Long>();
    private Set<Long> researchIds = new HashSet<Long>();
    private Set<Long> templateIds = new HashSet<Long>();
    private Set<String> studentEmails = new HashSet<String>();

    private RandomStringGenerator random = new RandomStringGenerator();

    @PostConstruct
    public void addSampleSurveysData() {
        registerStudents(10);
        createGroups(6);
        createTemplates(4);
        createResearches();
        fillSurveys();
    }

    private void registerStudents(int count) {
        for (int i = 0; i < count; i++) {
            String email = random.email();
            users.createUser(student(email).withName(random.fullName()));
            studentEmails.add(email);
        }
    }

    private void createGroups(int count) {
        users.loginAsTeacher();
        for (int i = 0; i < count; i++) {
            groupIds.add(surveys.createGroup(createGroup()));
        }
    }

    private GroupBuilder createGroup() {
        GroupBuilder group = group(random.sentence()).withEvaluator(EVALUATOR_EMAIL);
        if (random.chance(0.75)) {
            group.openForSignup();
        }
        for (String studentEmail : studentEmails) {
            if (random.chance(0.5)) {
                group.withStudent(studentEmail);
            }
        }
        return group;
    }

    private void createTemplates(int count) {
        users.loginAsEvaluator();
        for (int i = 0; i < count; i++) {
            templateIds.add(surveys.createTemplate(createTemplate()));
        }
    }

    private SurveyTemplateBuilder createTemplate() {
        return template(random.sentence()).withDescription(random.paragraph()).withQuestions(
                singleChoiceQuestion(random.question()).withAnswers(answer(random.sentence()),
                        answer(random.sentence())),//
                multiChoiceQuestion(random.question())
                        .withAnswers(answer(random.sentence()), answer(random.sentence())),//
                openQuestion(random.sentence()));
    }

    private void createResearches() {
        users.loginAsEvaluator();
        for (Long groupId : groupIds) {
            for (Long templateId : templateIds)
                if (random.chance(0.8)) {
                    researchIds.add(surveys.createResearch(research().withName(random.sentence())
                            .withSurvey(templateId).forGroup(groupId)));
                }
        }
    }

    private void fillSurveys() {
        for (String studenEmail : studentEmails) {
            users.loginAs(studenEmail);
            List<PublishedSurveyTemplateDto> templates = surveyDao
                    .queryPublishedTemplates(new PublishedSurveyTemplateQuery(GroupRoleEnum.STUDENT));
            for (PublishedSurveyTemplateDto template : templates) {
                if (random.chance(0.4)) {
                    surveys.fillAndSubmitSurvey(template.getTemplateId(), template.getGroupId());
                }
            }
        }
    }

    public Collection<Long> getResearchIds() {
        return researchIds;
    }

    public Collection<Long> getGroupIds() {
        return groupIds;
    }

}
