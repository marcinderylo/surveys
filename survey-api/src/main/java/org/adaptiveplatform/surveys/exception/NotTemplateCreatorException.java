package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 *
 * @author Marcin Deryło
 */
@RemoteException
public class NotTemplateCreatorException extends BusinessException implements
        Serializable {

    public static final String ERROR_CODE = "NOT_CREATEOR_OF_TEMPLATE";

    public NotTemplateCreatorException(@Param("templateId")Long templateId) {
        super(ERROR_CODE, "Must be the creator of survey template to manage it "
                + "(offending template ID={0}).", templateId);
    }
}
