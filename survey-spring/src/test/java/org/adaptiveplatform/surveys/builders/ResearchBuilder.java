package org.adaptiveplatform.surveys.builders;

import org.adaptiveplatform.surveys.dto.AddGroupToResearchCommand;
import org.adaptiveplatform.surveys.dto.PrepareResearchCommand;

public class ResearchBuilder {
    private PrepareResearchCommand command = new PrepareResearchCommand();

    public ResearchBuilder() {
        command.setName("random research name");
    }

    public ResearchBuilder withName(String name) {
        command.setName(name);
        return this;
    }

    public ResearchBuilder withSurvey(Long surveyTemplateId) {
        command.setSurveyTemplateId(surveyTemplateId);
        return this;
    }

    public ResearchBuilder forGroup(Long groupId) {
        command.getGroupsToAdd().add(new AddGroupToResearchCommand(groupId));
        return this;
    }

    public PrepareResearchCommand build() {
        return command;
    }

    public static ResearchBuilder research() {
        return new ResearchBuilder();
    }

}
