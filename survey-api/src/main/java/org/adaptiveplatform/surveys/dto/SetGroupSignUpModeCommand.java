package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.adaptiveplatform.adapt.commons.validation.constraints.ValidId;
import org.adaptiveplatform.codegenerator.api.RemoteObject;

@RemoteObject
public class SetGroupSignUpModeCommand implements Serializable {

    @ValidId
    private Long groupId;
    @NotNull
    private Boolean allowStudentsToSignUp;

    public SetGroupSignUpModeCommand() {
    }

    public SetGroupSignUpModeCommand(Long groupId, boolean allowStudentsToSignUp) {
        this.groupId = groupId;
        this.allowStudentsToSignUp = allowStudentsToSignUp;
    }

    public boolean isAllowStudentsToSignUp() {
        return allowStudentsToSignUp;
    }

    public void setAllowStudentsToSignUp(boolean allowStudentsToSignUp) {
        this.allowStudentsToSignUp = allowStudentsToSignUp;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
