package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.adaptiveplatform.codegenerator.api.RemoteObject;

@RemoteObject
public class PublishedSurveyTemplateQuery implements Serializable {

    private static final long serialVersionUID = -3788480319081301185L;
    /**
     * Specifies whether published templates should be read with STUDENT or
     * EVALUATOR role. In former case, only published templates assigned to the
     * group in which calling user is a student should be listed. In latter,
     * templates published by the caller should be listed.
     */
    private GroupRoleEnum runAs;
    private String keyword;
    private Integer status;
    private Set<Integer> groupIds = new HashSet<Integer>();

    protected PublishedSurveyTemplateQuery() {
    }

    public PublishedSurveyTemplateQuery(GroupRoleEnum runAs) {
        this.runAs = runAs;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public GroupRoleEnum getRunAs() {
        return runAs;
    }

    public void setRunAs(GroupRoleEnum runAs) {
        this.runAs = runAs;
    }

    public Set<Integer> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(Set<Integer> groupIds) {
        this.groupIds = groupIds;
    }
}
