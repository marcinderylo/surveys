package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import org.adaptiveplatform.codegenerator.api.RemoteObject;

@RemoteObject
public class GroupSignUpCommand implements Serializable {

    private Long groupId;

    public GroupSignUpCommand() {
    }

    public GroupSignUpCommand(Long groupId) {
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
