package org.adaptiveplatform.surveys.application;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.AnswerEvaluation;
import org.adaptiveplatform.surveys.domain.AnsweredQuestion;
import org.adaptiveplatform.surveys.domain.FilledSurvey;
import org.adaptiveplatform.surveys.domain.QuestionEvaluation;
import org.adaptiveplatform.surveys.domain.Research;
import org.adaptiveplatform.surveys.domain.StudentGroup;
import org.adaptiveplatform.surveys.domain.SurveyPublication;
import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.dto.AddGroupToResearchCommand;
import org.adaptiveplatform.surveys.dto.CommentQuestionCommand;
import org.adaptiveplatform.surveys.dto.PrepareResearchCommand;
import org.adaptiveplatform.surveys.dto.TagAnswerCommand;
import org.adaptiveplatform.surveys.service.EvaluationRepository;
import org.adaptiveplatform.surveys.service.ResearchRepository;
import org.adaptiveplatform.surveys.service.StudentGroupRepository;
import org.adaptiveplatform.surveys.service.SurveyPublicationFactory;
import org.adaptiveplatform.surveys.service.SurveyPublicationRepository;
import org.adaptiveplatform.surveys.service.SurveyRepository;
import org.adaptiveplatform.surveys.service.SurveyTemplateRepository;
import org.apache.commons.lang.Validate;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Marcin Deryło
 */
@Service("evaluationFacade")
@RemotingDestination
@Transactional(propagation = Propagation.REQUIRED)
public class EvaluationFacadeImpl implements EvaluationFacade {

    private ResearchRepository researchRepository;
    private AuthenticationService authenticationService;
    private SurveyTemplateRepository surveyTemplateRepository;
    private StudentGroupRepository studentGroupRepository;
    private SurveyPublicationFactory publicationFactory;
    private SurveyPublicationRepository publicationRepository;
    private SurveyRepository surveyRepository;
    private EvaluationRepository evaluationRepository;

    @Override
    public void commentQuestion(CommentQuestionCommand command) {
        QuestionEvaluation questionEvaluation =
                getQuestionEvaluation(command.getResearchId(), command.
                getQuestionId());
        questionEvaluation.setEvaluationComment(command.getComment());
    }

    @Override
    public Long createResearch(PrepareResearchCommand cmd) {
        // FIXME turn into validation error
        Validate.notNull(cmd.getSurveyTemplateId(),
                "Must specify a survey template to use for research");
        final SurveyTemplate template =
                surveyTemplateRepository.getExisting(
                cmd.getSurveyTemplateId());
        authenticationService.userSecurityCheck(template.getOwnerId());

        // TODO might push this down to a factory
        // but then the factory would call the repository to persist publications...
        Research research = new Research(cmd.getName(), template);

        for (AddGroupToResearchCommand publishCmd : cmd.getGroupsToAdd()) {
            final StudentGroup group =
                    studentGroupRepository.getExisting(publishCmd.getGroupId());
            final SurveyPublication publication =
                    publicationFactory.create(template, group,
                    publishCmd.getValidFrom(), publishCmd.getValidTo());
            publicationRepository.persist(publication);
            research.addSurveyPublication(publication);
        }

        researchRepository.persist(research);
        return research.getId();
    }

    @Override
    public void rememberSearchPhrase(Long researchId, Integer questionId,
            String phrase) {
        final QuestionEvaluation questionEvaluation =
                getQuestionEvaluation(researchId, questionId);
        questionEvaluation.rememberSearchPhrase(phrase);
    }

    @Override
    public void tagAnswer(TagAnswerCommand command) {
        defineTagsForQuestionTemplate(command);
        defineTagsForAnswer(command);
    }

    private AnswerEvaluation createNewEvaluation(TagAnswerCommand command,
            final AnsweredQuestion answeredQuestion) {
        AnswerEvaluation evaluation =
                new AnswerEvaluation(researchRepository.getExisting(command.
                getResearchId()),
                answeredQuestion);
        evaluationRepository.persist(evaluation);
        return evaluation;
    }

    private void defineTagsForAnswer(TagAnswerCommand command) {
        AnswerEvaluation answerEvaluation = getAnswerEvaluation(command);
        answerEvaluation.setTags(command.getSetTags());
    }

    private AnsweredQuestion getAnsweredQuestion(TagAnswerCommand command) {
        final FilledSurvey survey =
                surveyRepository.get(command.getFilledSurveyId());
        return survey.getAnsweredQuestion(command.getQuestionNumber());
    }

    private void defineTagsForQuestionTemplate(TagAnswerCommand command) {
        final QuestionEvaluation questionEvaluation =
                getQuestionEvaluation(command);
        assignTags(command, questionEvaluation);
    }

    private void assignTags(TagAnswerCommand command,
            final QuestionEvaluation questionEvaluation) {
        for (String tag : command.getSetTags()) {
            questionEvaluation.defineTag(tag);
        }
    }

    private QuestionEvaluation getQuestionEvaluation(TagAnswerCommand command) {
        return getQuestionEvaluation(command.getResearchId(), command.
                getQuestionNumber());
    }

    private QuestionEvaluation getQuestionEvaluation(Long researchId,
            Integer questionId) {// FIXME turn into validation errors
        Validate.notNull(researchId, "Must specify a research");
        Validate.notNull(questionId, "Must specify question number");
        final Research research =
                researchRepository.getExisting(researchId);

        authenticationService.userSecurityCheck(research.getEvaluatorId());

        final QuestionEvaluation questionEvaluation =
                research.getQuestionEvaluation(questionId);
        return questionEvaluation;
    }

    private AnswerEvaluation getAnswerEvaluation(TagAnswerCommand command) {
        final AnsweredQuestion answeredQuestion = getAnsweredQuestion(command);
        AnswerEvaluation evaluation = evaluationRepository.get(command.
                getResearchId(), answeredQuestion);
        if (evaluation == null) {
            evaluation = createNewEvaluation(command, answeredQuestion);
        }
        return evaluation;
    }

    @Resource
    public void setResearchRepository(ResearchRepository repo) {
        this.researchRepository = repo;
    }

    @Resource
    public void setAuthenticationService(
            AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Resource
    public void setSurveyTemplateRepository(SurveyTemplateRepository repo) {
        this.surveyTemplateRepository = repo;
    }

    @Resource
    public void setStudentGroupRepository(
            StudentGroupRepository studentGroupRepository) {
        this.studentGroupRepository = studentGroupRepository;
    }

    @Resource
    public void setSurveyRepository(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Resource
    public void setEvaluationRepository(
            EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    @Resource
    public void setPublicationFactory(
            SurveyPublicationFactory publicationFactory) {
        this.publicationFactory = publicationFactory;
    }

    @Resource
    public void setPublicationRepository(
            SurveyPublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }
}
