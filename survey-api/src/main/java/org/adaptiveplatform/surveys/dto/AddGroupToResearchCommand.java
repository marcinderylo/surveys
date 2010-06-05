package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.Date;

import org.adaptiveplatform.codegenerator.api.RemoteObject;


/**
 *
 * @author Marcin Deryło
 */
@RemoteObject
public class AddGroupToResearchCommand implements Serializable {

    private Date validFrom;
    private Date validTo;
    private Long groupId;

    public AddGroupToResearchCommand() {
    }

    public AddGroupToResearchCommand(Long groupId) {
        this.groupId = groupId;
    }

    public AddGroupToResearchCommand(Long groupId, Date validFrom, Date validTo) {
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }
}
