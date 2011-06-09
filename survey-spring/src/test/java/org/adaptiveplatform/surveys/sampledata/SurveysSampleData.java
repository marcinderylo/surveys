package org.adaptiveplatform.surveys.sampledata;

import static org.adaptiveplatform.surveys.builders.AnswerBuilder.answer;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.multiChoiceQuestion;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.openQuestion;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.singleChoiceQuestion;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.evaluator;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.student;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("development")
public class SurveysSampleData {

    @Resource
    private CoreFixtureBuilder core;

    @Resource
    private SurveysFixtureBuilder surveys;

    @PostConstruct
    public void addSampleSurveysData() {
        core.createUser(evaluator("evaluator@gmail.com").withPassword("evaluator"));
        core.createUser(student("student@gmail.com").withPassword("student"));

        core.loginAs("evaluator@gmail.com", "evaluator");

        surveys.createTemplate(template("sample survey").withDescription("this is a sample sample survey")
                .withQuestions(//
                        singleChoiceQuestion("single").withAnswers(answer("a"), answer("b")),//
                        multiChoiceQuestion("multi").withAnswers(answer("c"), answer("d")),//
                        openQuestion("open")));
    }
}
