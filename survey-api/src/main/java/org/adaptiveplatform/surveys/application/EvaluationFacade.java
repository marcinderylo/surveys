package org.adaptiveplatform.surveys.application;

import org.adaptiveplatform.codegenerator.api.RemoteService;
import org.adaptiveplatform.surveys.dto.CommentQuestionCommand;
import org.adaptiveplatform.surveys.dto.PrepareResearchCommand;
import org.adaptiveplatform.surveys.dto.TagAnswerCommand;


/**
 *
 * @author Marcin Deryło
 */
@RemoteService
public interface EvaluationFacade {

    void tagAnswer(TagAnswerCommand command);

    void commentQuestion(CommentQuestionCommand command);

    void rememberSearchPhrase(Long researchId, Integer questionId, String phrase);

    Long createResearch(PrepareResearchCommand command);
}
