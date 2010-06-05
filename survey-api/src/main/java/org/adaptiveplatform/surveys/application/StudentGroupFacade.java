package org.adaptiveplatform.surveys.application;

import org.adaptiveplatform.codegenerator.api.RemoteService;
import org.adaptiveplatform.surveys.dto.ChangeGroupMembersCommand;
import org.adaptiveplatform.surveys.dto.ChangeSurveyPublicationCommand;
import org.adaptiveplatform.surveys.dto.CreateStudentGroupCommand;
import org.adaptiveplatform.surveys.dto.PublishSurveyTemplateCommand;


/**
 * @author Marcin Deryło
 */
@RemoteService
public interface StudentGroupFacade {

    Long createGroup(CreateStudentGroupCommand command);

    /**
     * {@link PublishSurveyTemplateCommand}
     */
    void assignSurveyTemplate(PublishSurveyTemplateCommand command);

    void removeSurveyTemplate(Long publishedSurveyTemplateId);

    void changeSurveyPublication(ChangeSurveyPublicationCommand command);

    void changeGroupMembers(ChangeGroupMembersCommand command);

    void removeGroup(Long groupId);
}
