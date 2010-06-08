package org.adaptiveplatform.surveys.exception.security;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.RemoteException;
import org.adaptiveplatform.surveys.exception.BusinessException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class CantRevokeOwnAdminRights extends BusinessException implements Serializable {

    public static final String ERROR_CODE = "CANT_REVOKE_OWN_ADMIN_RIGHTS";

    public CantRevokeOwnAdminRights() {
        super(ERROR_CODE, "Can't revoke administrator privileges from self");
    }
}
