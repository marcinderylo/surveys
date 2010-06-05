package org.adaptiveplatform.surveys.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.adaptiveplatform.codegenerator.api.RemoteObject;

/**
 * Assign a survey template to a student group. Survey will be available for
 * filling for selected period of time (or indefinitely).
 * 
 * @author Rafal Jamroz
 */
@RemoteObject
public class PublishSurveyTemplateCommand {

	/**
	 * Id of template which will be published.
	 */
	private Set<Integer> surveyTemplateIds = new HashSet<Integer>();
	/**
	 * Group which the survey will be published in.
	 */
	private Long groupId;
	/**
	 * (optional) If defined survey will be available for filling only after
	 * this date.
	 */
	private Date startingDate;
	/**
	 * (optional) If defined survey will be available for filling only until
	 * this date.
	 */
	private Date expirationDate;

        public Set<Integer> getSurveyTemplateIds() {
                return surveyTemplateIds;
        }

        public void setSurveyTemplateIds(Set<Integer> surveyTemplateIds) {
                this.surveyTemplateIds = surveyTemplateIds;
        }

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

}
