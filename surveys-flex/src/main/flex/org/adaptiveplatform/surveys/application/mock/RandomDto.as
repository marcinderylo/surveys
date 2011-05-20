package org.adaptiveplatform.surveys.application.mock {
import mx.collections.ArrayCollection;

import org.adaptiveplatform.surveys.application.UserRole;
import org.adaptiveplatform.surveys.application.generated.UserDao;
import org.adaptiveplatform.surveys.dto.generated.ActivityTypeEnum;
import org.adaptiveplatform.surveys.dto.generated.AnswerTemplateDto;
import org.adaptiveplatform.surveys.dto.generated.EvaluationActivityDto;
import org.adaptiveplatform.surveys.dto.generated.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.generated.FillingProgressDto;
import org.adaptiveplatform.surveys.dto.generated.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.generated.QuestionEvaluationDto;
import org.adaptiveplatform.surveys.dto.generated.QuestionTemplateDto;
import org.adaptiveplatform.surveys.dto.generated.QuestionTypeEnum;
import org.adaptiveplatform.surveys.dto.generated.ResearchDto;
import org.adaptiveplatform.surveys.dto.generated.StudentGroupDto;
import org.adaptiveplatform.surveys.dto.generated.SurveyQuestionAnswerDto;
import org.adaptiveplatform.surveys.dto.generated.SurveyQuestionDto;
import org.adaptiveplatform.surveys.dto.generated.SurveyStatusEnum;
import org.adaptiveplatform.surveys.dto.generated.SurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.generated.UserDto;

public class RandomDto {

    private var random:Random = new Random();

    public function RandomDto() {
    }

    public function user():UserDto {
        var user:UserDto = new UserDto();
        user.id = random.id();
        user.name = random.fullName();
        user.email = random.email();
        user.roles = new ArrayCollection([random.element(UserRole.USER, UserRole.EVALUATOR, UserRole.TEACHER)]);
        return user;
    }

    public function research():ResearchDto {
        var research:ResearchDto = new ResearchDto();
        research.id = random.id();
        research.name = random.name();
        research.templateDto = surveyTemplate();
        research.fillingProgress = fillingProgress();
        research.questions = new ArrayCollection();
        for (var i:int = 0; i < 5; i++) {
            research.questions.addItem(questionEvaluation());
        }
        research.submittedSurveys = new ArrayCollection();
        for (var i:int = 0; i < 5; i++) {
            research.submittedSurveys.addItem(filledSurvey());
        }
        research.groups = new ArrayCollection();
        for (var i:int = 0; i < 5; i++) {
            research.groups.addItem(filledSurvey());
        }
        return research;
    }

    private function questionEvaluation():QuestionEvaluationDto {
        var question:QuestionEvaluationDto = new QuestionEvaluationDto();
        question.questionTemplateId = random.id();
        question.definedTags = new ArrayCollection();
        for (var i:int = 0; i < 5; i++) {
            question.definedTags.addItem(random.word());
        }
        question.searchPhrases = new ArrayCollection();
        for (var i:int = 0; i < 5; i++) {
            question.searchPhrases.addItem(random.sentence());
        }
        question.comments = random.sentence();
        return question;
    }

    private function fillingProgress():FillingProgressDto {
        var progress:FillingProgressDto = new FillingProgressDto();
        progress.expectedSurveysCount = 5;
        progress.actualSurveysCount = random.integer(1, 5);
        return progress;
    }

    public function evaluationActivity():EvaluationActivityDto {
        var activity:EvaluationActivityDto = new EvaluationActivityDto();
        activity.id = random.id();
        activity.name = random.name();
        activity.type = ActivityTypeEnum(random.element(ActivityTypeEnum.RESEARCH, ActivityTypeEnum.SURVEY_TEMPLATE));
        activity.creationDate = random.pastDate();
        return activity;
    }

    public function studentGroup():StudentGroupDto {
        var group:StudentGroupDto = new StudentGroupDto();
        group.id = random.id();
        group.groupName = random.sentence();
        group.students = new ArrayCollection();
        for (var i:int = 0; i < 10; i++) {
            group.students.addItem(user());
        }
        group.evaluators = new ArrayCollection();
        for (var i:int = 0; i < 3; i++) {
            group.evaluators.addItem(user());
        }
        group.administrators = new ArrayCollection();
        for (var i:int = 0; i < 2; i++) {
            group.administrators.addItem(user());
        }
        group.studentsCanSignUp = random.boolean();
        return group;
    }


    public function filledSurvey():FilledSurveyDto {
        var filled:FilledSurveyDto = new FilledSurveyDto();
        filled.id = random.id();
        filled.surveyTemplateName = random.word();
        filled.groupName = random.word();
        filled.startDate = random.pastDate();
        filled.submitDate = random.pastDate();
        filled.description = random.sentence();

        filled.questions = new ArrayCollection();
        for (var i:int = 0; i < 10; i++) {
            filled.questions.addItem(surveyQuestion(i));
        }
        return filled;
    }

    private function surveyQuestion(questionNumber:int):SurveyQuestionDto {
        var question:SurveyQuestionDto = new SurveyQuestionDto();
        question.number = questionNumber;
        question.text = random.sentence();
        question.htmlText = random.sentence();
        question.comment = random.sentence();
        question.type = QuestionTypeEnum(random.element(QuestionTypeEnum.OPEN, QuestionTypeEnum.SINGLE_CHOICE, QuestionTypeEnum.MULTIPLE_CHOICE));
        question.answers = new ArrayCollection();
        for (var i:int = 0; i < 5; i++) {
            question.answers.addItem(surveyQuestionAnswer(i));
        }
        question.tags = new ArrayCollection();
        for (var i:int = 0; i < 5; i++) {
            question.answers.addItem(random.word());
        }
        return question;
    }

    private function surveyQuestionAnswer(answerNumber:int):SurveyQuestionAnswerDto {
        var answer:SurveyQuestionAnswerDto = new SurveyQuestionAnswerDto();
        answer.number = answerNumber;
        answer.text = random.sentence();
        answer.selected = random.boolean();
        answer.requiresComment = random.boolean();
        answer.disallowsOtherAnswers = random.boolean();
        return answer;
    }

    public function publishedSurveyTemplate():PublishedSurveyTemplateDto {
        var published:PublishedSurveyTemplateDto = new PublishedSurveyTemplateDto();
        published.id = random.id();
        published.templateId = random.id();
        published.name = random.name();
        published.groupId = random.id();
        published.groupName = random.name();
        published.startingDate = random.pastDate();
        published.expirationDate = random.futureDate();
        published.filledSurveyId = random.id();
        published.submitted = random.boolean();
        published.status = SurveyStatusEnum(random.element(SurveyStatusEnum.STARTED, SurveyStatusEnum.PENDING, SurveyStatusEnum.SUBMITTED, SurveyStatusEnum.OUTSIDE_PUBLICATION_PERIOD));
        return published;
    }

    public function surveyTemplate():SurveyTemplateDto {
        var template:SurveyTemplateDto = new SurveyTemplateDto();
        template.id = random.id();
        template.name = random.name();
        template.description = random.sentence();
        template.questions = new ArrayCollection();
        for (var i:int = 0; i < 10; i++) {
            template.questions.addItem(questionTemplate());
        }
        return template;
    }

    private function questionTemplate():QuestionTemplateDto {
        var question:QuestionTemplateDto = new QuestionTemplateDto();
        question.id = random.id();
        question.text = random.sentence();
        question.htmlText = random.sentence();
        question.type = QuestionTypeEnum(random.element(QuestionTypeEnum.OPEN, QuestionTypeEnum.MULTIPLE_CHOICE, QuestionTypeEnum.SINGLE_CHOICE));
        question.style = random.word();
        question.answers = new ArrayCollection();
        for (var i:int = 0; i < 10; i++) {
            question.answers.addItem(answerTemplate());
        }
        return question;
    }

    private function answerTemplate():AnswerTemplateDto {
        var answer:AnswerTemplateDto = new AnswerTemplateDto();
        answer.id = random.id();
        answer.text = random.sentence();
        answer.requiresComment = random.boolean();
        answer.excludesOtherAnswers = random.boolean();
        return answer;
    }
}
}

