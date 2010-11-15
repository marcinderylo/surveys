package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.Date;

import org.adaptiveplatform.codegenerator.api.RemoteObject;

@RemoteObject
public class FilledSurveyQuery implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * List only filled surveys for given template.
	 */
	private Long templateId;
	/**
	 * <p>
	 * List only filled surveys submitted for templates published in given
	 * group.
	 * </p>
	 * <p>
	 * When used with {@link #templateId} indicates that only filled surveys for
	 * given published template should be listed.
	 */
	private Long groupId;
	private Date fromDate;
	private Date untilDate;
	private String keyword;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getUntilDate() {
		return untilDate;
	}

	public void setUntilDate(Date untilDate) {
		this.untilDate = untilDate;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
}
