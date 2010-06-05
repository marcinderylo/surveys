package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class PublishingTemplateWithoutQuestionsException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE =
            "PUBLISHING_TEMPLATE_WITHOUT_QUESTIONS";

    public PublishingTemplateWithoutQuestionsException(Long templateId) {
        super(ERROR_CODE,
                "Attempted to publish survey template ID={0} but it doesn't "
                + "have any questions yet", templateId);
    }
}
