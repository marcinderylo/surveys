package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class NoSuchResearchException extends BusinessException implements
        Serializable {

    public static final String ERROR_CODE = "NO_SUCH_RESEARCH";

    public NoSuchResearchException(@Param("researchId") Long researchId) {
        super(ERROR_CODE, "No research with ID={0} exists.", researchId);
    }
}
