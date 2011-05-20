package org.adaptiveplatform.surveys.application.mock {
import org.adaptiveplatform.communication.ResultHandler;
import org.adaptiveplatform.communication.SuccessResultHandler;
import org.adaptiveplatform.surveys.application.generated.EvaluationFacade;
import org.adaptiveplatform.surveys.dto.generated.CommentQuestionCommand;
import org.adaptiveplatform.surveys.dto.generated.PrepareResearchCommand;
import org.adaptiveplatform.surveys.dto.generated.TagAnswerCommand;

internal class EvaluationFacadeStub implements EvaluationFacade {
    public function EvaluationFacadeStub() {
    }

    public function tagAnswer(tagAnswerCommand:TagAnswerCommand):ResultHandler {
        return new SuccessResultHandler();
    }

    public function commentQuestion(commentQuestionCommand:CommentQuestionCommand):ResultHandler {
        return new SuccessResultHandler();
    }

    public function rememberSearchPhrase(number:Number, int2:int, string:String):ResultHandler {
        return new SuccessResultHandler();
    }

    public function createResearch(prepareResearchCommand:PrepareResearchCommand):ResultHandler {
        return new SuccessResultHandler(Math.random());
    }
}
}
