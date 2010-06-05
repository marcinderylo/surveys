package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.adaptiveplatform.codegenerator.api.RemoteObject;


/**
 *
 * @author Marcin Deryło
 */
@RemoteObject
public class PrepareResearchCommand implements Serializable {

    private String name;
    private Long surveyTemplateId;
    private List<AddGroupToResearchCommand> groupsToAdd =
            new ArrayList<AddGroupToResearchCommand>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSurveyTemplateId() {
        return surveyTemplateId;
    }

    public void setSurveyTemplateId(Long surveyTemplateId) {
        this.surveyTemplateId = surveyTemplateId;
    }

    public List<AddGroupToResearchCommand> getGroupsToAdd() {
        return groupsToAdd;
    }

    public void setGroupsToAdd(List<AddGroupToResearchCommand> addGroupCommands) {
        this.groupsToAdd = addGroupCommands;
    }
}
