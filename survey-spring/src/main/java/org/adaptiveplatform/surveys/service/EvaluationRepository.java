package org.adaptiveplatform.surveys.service;

import org.adaptiveplatform.surveys.domain.AnswerEvaluation;
import org.adaptiveplatform.surveys.domain.AnsweredQuestion;

/**
 *
 * @author Marcin Deryło
 */
public interface EvaluationRepository {

    AnswerEvaluation get(Long researchId, AnsweredQuestion answeredQuestion);

    void persist(AnswerEvaluation evaluation);
}
