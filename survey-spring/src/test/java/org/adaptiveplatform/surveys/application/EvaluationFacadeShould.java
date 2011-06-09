package org.adaptiveplatform.surveys.application;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.adaptiveplatform.surveys.domain.QuestionEvaluation;
import org.adaptiveplatform.surveys.domain.Research;
import org.adaptiveplatform.surveys.dto.CommentQuestionCommand;
import org.adaptiveplatform.surveys.service.ResearchRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author Marcin Deryło
 */
public class EvaluationFacadeShould {

    @InjectMocks
    private EvaluationFacade facade;
    @Mock
    private ResearchRepository researchRepository;
    @Mock
    private AuthenticationService authenticationService;

    @Before
    public void init() {
        facade = new EvaluationFacadeImpl();

        MockitoAnnotations.initMocks(this);

        doNothing().when(authenticationService).userSecurityCheck(anyLong());
    }

    @Test
    public void beAbleToSetACommentForQuestionInResearch() throws Exception {
        // given
        CommentQuestionCommand cmd = new CommentQuestionCommand();
        cmd.setResearchId(12L);
        cmd.setQuestionId(3);
        cmd.setComment("some comment");

        QuestionEvaluation questionEvaluation = givenAResearchWithQuestionEvaluation(12L, 3);

        // when
        facade.commentQuestion(cmd);

        // then
        verify(questionEvaluation).setEvaluationComment("some comment");
    }

    @Test
    public void beAbleToRememberUsedSearchPhrasesPerQuestion() throws Exception {
        // given
        QuestionEvaluation questionEvaluation = givenAResearchWithQuestionEvaluation(13L, 5);

        // when
        facade.rememberSearchPhrase(13L, 5, "foobar");

        // then
        verify(questionEvaluation).rememberSearchPhrase("foobar");
    }

    private QuestionEvaluation givenAResearchWithQuestionEvaluation(long researchId, int questionNumber) {
        Research research = mock(Research.class);

        QuestionEvaluation questionEvaluation = mock(QuestionEvaluation.class);

        given(researchRepository.getExisting(researchId)).willReturn(research);
        given(research.getQuestionEvaluation(questionNumber)).willReturn(questionEvaluation);

        return questionEvaluation;
    }
}
