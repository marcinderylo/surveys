package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class PublishingTemplateWithoutQuestionsException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE =
            "PUBLISHING_TEMPLATE_WITHOUT_QUESTIONS";

    public PublishingTemplateWithoutQuestionsException(@Param("templateId")Long templateId) {
        super(ERROR_CODE,
                "Attempted to publish survey template ID={0} but it doesn't "
                + "have any questions yet", templateId);
    }
}
