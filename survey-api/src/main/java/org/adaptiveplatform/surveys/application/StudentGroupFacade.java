package org.adaptiveplatform.surveys.application;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.adaptiveplatform.adapt.commons.validation.constraints.ValidId;
import org.adaptiveplatform.codegenerator.api.RemoteService;
import org.adaptiveplatform.surveys.dto.ChangeGroupMembersCommand;
import org.adaptiveplatform.surveys.dto.ChangeSurveyPublicationCommand;
import org.adaptiveplatform.surveys.dto.CreateStudentGroupCommand;
import org.adaptiveplatform.surveys.dto.GroupSignUpCommand;
import org.adaptiveplatform.surveys.dto.PublishSurveyTemplateCommand;
import org.adaptiveplatform.surveys.dto.SetGroupSignUpModeCommand;

/**
 * @author Marcin Deryło
 */
@RemoteService
public interface StudentGroupFacade {

    Long createGroup(@NotNull @Valid CreateStudentGroupCommand command);

    /**
     * @deprecated use researches instead
     */
    void assignSurveyTemplate(@NotNull @Valid PublishSurveyTemplateCommand command);

    void removeSurveyTemplate(@ValidId Long publishedSurveyTemplateId);

    void changeSurveyPublication(@NotNull @Valid ChangeSurveyPublicationCommand command);

    void changeGroupMembers(@NotNull @Valid ChangeGroupMembersCommand command);

    void removeGroup(@ValidId Long groupId);

    void setGroupSignUpMode(@NotNull @Valid SetGroupSignUpModeCommand setGroupSignUpModeCommand);

    void signUpAsStudent(@NotNull @Valid GroupSignUpCommand command);
}
