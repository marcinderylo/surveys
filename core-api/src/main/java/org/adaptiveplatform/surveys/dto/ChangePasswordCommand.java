package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;

import org.adaptiveplatform.adapt.commons.validation.constraints.NonBlank;
import org.adaptiveplatform.codegenerator.api.RemoteObject;
import org.apache.bval.constraints.Email;

@RemoteObject
public class ChangePasswordCommand implements Serializable {

    private String email;
    private String oldPassword;
    private String newPassword;

    public ChangePasswordCommand() {
    }

    public ChangePasswordCommand(String email, String oldPassword, String newPassword) {
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    @Email
    @NonBlank
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Can be omitted by administrator manually resetting the password of another user.
     */
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @NonBlank
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
