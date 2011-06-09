/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * Indicates that an attempt by a student has been made to sign up into a group that does not allow
 * students to sign up themselves, only by explicit adminstrator action.
 * @author Marcin Deryło
 */
@RemoteException
public class SignupByAdministratorOnlyGroupException extends BusinessException implements
        Serializable {

    public static final String ERROR_CODE = "SIGNUP_ONLY_BY_ADMINISTRATOR";

    public SignupByAdministratorOnlyGroupException(@Param("groupId")String groupName) {
        super(ERROR_CODE,
                "Group '{0}' does not allow students to sign up themselves. ", groupName);
    }
}
