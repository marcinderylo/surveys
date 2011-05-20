package org.adaptiveplatform.surveys.application.mock {
import org.adaptiveplatform.communication.ResultHandler;
import org.adaptiveplatform.communication.SuccessResultHandler;
import org.adaptiveplatform.surveys.application.generated.StudentGroupFacade;
import org.adaptiveplatform.surveys.dto.generated.ChangeGroupMembersCommand;
import org.adaptiveplatform.surveys.dto.generated.ChangeSurveyPublicationCommand;
import org.adaptiveplatform.surveys.dto.generated.CreateStudentGroupCommand;
import org.adaptiveplatform.surveys.dto.generated.GroupSignUpCommand;
import org.adaptiveplatform.surveys.dto.generated.PublishSurveyTemplateCommand;
import org.adaptiveplatform.surveys.dto.generated.SetGroupSignUpModeCommand;

internal class StudentGroupFacadeStub implements StudentGroupFacade {
    public function StudentGroupFacadeStub() {
    }

    public function assignSurveyTemplate(publishSurveyTemplateCommand:PublishSurveyTemplateCommand):ResultHandler {
        return new SuccessResultHandler();
    }

    public function removeSurveyTemplate(number:Number):ResultHandler {
        return new SuccessResultHandler();
    }

    public function changeSurveyPublication(changeSurveyPublicationCommand:ChangeSurveyPublicationCommand):ResultHandler {
        return new SuccessResultHandler();
    }

    public function changeGroupMembers(changeGroupMembersCommand:ChangeGroupMembersCommand):ResultHandler {
        return new SuccessResultHandler();
    }

    public function removeGroup(number:Number):ResultHandler {
        return new SuccessResultHandler();
    }

    public function setGroupSignUpMode(setGroupSignUpModeCommand:SetGroupSignUpModeCommand):ResultHandler {
        return new SuccessResultHandler();
    }

    public function signUpAsStudent(groupSignUpCommand:GroupSignUpCommand):ResultHandler {
        return new SuccessResultHandler();
    }

    public function createGroup(createStudentGroupCommand:CreateStudentGroupCommand):ResultHandler {
        return new SuccessResultHandler(Math.random());
    }
}
}
