package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class PublicationOfDifferentTemplateException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "PUBLICATION_OF_DIFFERENT_TEMPLATE";

    public PublicationOfDifferentTemplateException(Long actualTemplateId,
            Long expectedTemplateId, String researchName) {
        super(ERROR_CODE, "Attempted to add survey publication for template "
                + "ID={0} to research \"{2}\" which expectets publications for "
                + "template ID={1} ", actualTemplateId, expectedTemplateId,
                researchName);

    }
}
