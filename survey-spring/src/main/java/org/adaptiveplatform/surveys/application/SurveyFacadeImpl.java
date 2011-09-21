package org.adaptiveplatform.surveys.application;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.FilledSurvey;
import org.adaptiveplatform.surveys.domain.QuestionTemplate;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.dto.CreateSurveyTemplateCommand;
import org.adaptiveplatform.surveys.dto.QuestionTemplateDto;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.NoSuchSurveyTemplateException;
import org.adaptiveplatform.surveys.exception.SurveyTemplateAlreadyExistsException;
import org.adaptiveplatform.surveys.exception.SurveyTemplateAlreadyFilledException;
import org.adaptiveplatform.surveys.exception.SurveyTemplateAlreadyPublishedException;
import org.adaptiveplatform.surveys.service.QuestionTemplateFactory;
import org.adaptiveplatform.surveys.service.SurveyFactory;
import org.adaptiveplatform.surveys.service.SurveyPublicationRepository;
import org.adaptiveplatform.surveys.service.SurveyRepository;
import org.adaptiveplatform.surveys.service.SurveyTemplateFactory;
import org.adaptiveplatform.surveys.service.SurveyTemplateRepository;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service("surveyFacade")
@RemotingDestination
public class SurveyFacadeImpl implements SurveyFacade {

    @Resource
    private SurveyFactory surveyFactory;
    @Resource
    private SurveyTemplateFactory templateFactory;
    @Resource
    private SurveyRepository repository;
    @Resource
    private SurveyTemplateRepository templateRepository;
    @Resource
    private AuthenticationService authentication;
    @Resource
    private QuestionTemplateFactory questionFactory;
    @Resource
    private SurveyPublicationRepository publicationRepository;

    @Override
    public Long startFilling(Long publicationId) {
        final UserDto user = authentication.getCurrentUser();
        FilledSurvey survey = surveyFactory.createSurvey(publicationId, user);
        survey.startFilling();
        return survey.getId();
    }

    @Secured(Role.USER)
    public void answerQuestion(Long surveyId, Integer questionNumber,
            List<Integer> answerNumber, String comment) {
        FilledSurvey survey = repository.get(surveyId);
        authentication.userSecurityCheck(survey.getOwnwerId());

        survey.answerQuestion(questionNumber, answerNumber, comment);
    }

    @Secured(Role.USER)
    @Override
    public void submit(Long surveyId) {
        FilledSurvey survey = repository.get(surveyId);
        survey.submit();
    }

    @Override
    @Secured(Role.EVALUATOR)
    public Long createTemplate(CreateSurveyTemplateCommand command) {
        checkForDuplicateTemplateName(command.getName());
        SurveyTemplate template = templateFactory.createDraft(authentication.
                getCurrentUser(), command.getName());
        template.setDescription(command.getDescription());
        for (QuestionTemplateDto questionCmd : command.getQuestions()) {
            QuestionTemplate question = questionFactory.createQuestion(
                    questionCmd);
            question = template.addQuestion(question);
        }
        template.publish();
        return template.getId();
    }

    @Override
    @Secured(Role.EVALUATOR)
    public void updateTemplate(Long templateId,
            CreateSurveyTemplateCommand updateCommand) {
        SurveyTemplate template = getExisting(templateId);
        if (!template.getTitle().equals(updateCommand.getName())) {
            checkForDuplicateTemplateName(updateCommand.getName());
        }

        authentication.userSecurityCheck(template.getOwnerId());
        verifyIsUnused(templateId);

        template.removeQuestions();
        template.setDescription(updateCommand.getDescription());
        for (QuestionTemplateDto questionCmd : updateCommand.getQuestions()) {
            QuestionTemplate question = questionFactory.createQuestion(
                    questionCmd);
            question = template.addQuestion(question);
        }
        template.setTitle(updateCommand.getName());
    }

    @Override
    @Secured(Role.EVALUATOR)
    public void removeSurveyTemplate(Long templateId) {
        SurveyTemplate template = getExisting(templateId);
        authentication.userSecurityCheck(template.getOwnerId());
        verifyIsUnused(templateId);
        templateRepository.delete(template);
    }

    private SurveyTemplate getExisting(Long templateId) {
        SurveyTemplate template = templateRepository.get(templateId);
        if (template == null) {
            throw new NoSuchSurveyTemplateException(templateId);
        }
        return template;
    }

    private void verifyIsUnused(Long templateId) {
        if (isPublished(templateId)) {
            throw new SurveyTemplateAlreadyPublishedException(
                    templateId);
        }
        // TODO add test case for following check:
        if (hasBeenFilled(templateId)) {
            throw new SurveyTemplateAlreadyFilledException(templateId);
        }
    }

    private void checkForDuplicateTemplateName(String newName) throws
            SurveyTemplateAlreadyExistsException {
        if (currentUserHasSurveyTemplateNamed(newName)) {
            throw new SurveyTemplateAlreadyExistsException(newName);
        }
    }

    private boolean currentUserHasSurveyTemplateNamed(String templateName) {
        return templateRepository.exists(authentication.getCurrentUser(),
                templateName);
    }

    private boolean isPublished(Long templateId) {
        return publicationRepository.listPublications(templateId).size() > 0;
    }

    private boolean hasBeenFilled(Long templateId) {
        return repository.list(templateId).size() > 0;
    }
}
