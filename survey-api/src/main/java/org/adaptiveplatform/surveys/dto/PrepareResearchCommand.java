package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.adaptiveplatform.adapt.commons.validation.constraints.NonBlank;
import org.adaptiveplatform.adapt.commons.validation.constraints.ValidId;
import org.adaptiveplatform.codegenerator.api.RemoteObject;

/**
 *
 * @author Marcin Deryło
 */
@RemoteObject
public class PrepareResearchCommand implements Serializable {

	private String name;
	private Long surveyTemplateId;
	private List<AddGroupToResearchCommand> groupsToAdd = new ArrayList<AddGroupToResearchCommand>();

	@NonBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@ValidId
	public Long getSurveyTemplateId() {
		return surveyTemplateId;
	}

	public void setSurveyTemplateId(Long surveyTemplateId) {
		this.surveyTemplateId = surveyTemplateId;
	}

	@NotNull
	@Size(min = 1)
	public List<AddGroupToResearchCommand> getGroupsToAdd() {
		return groupsToAdd;
	}

	public void setGroupsToAdd(List<AddGroupToResearchCommand> addGroupCommands) {
		this.groupsToAdd = addGroupCommands;
	}
}
