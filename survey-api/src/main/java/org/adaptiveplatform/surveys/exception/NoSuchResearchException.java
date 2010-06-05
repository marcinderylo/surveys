package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class NoSuchResearchException extends BusinessException implements
        Serializable {

    public static final String ERROR_CODE = "NO_SUCH_RESEARCH";

    public NoSuchResearchException(Long researchId) {
        super(ERROR_CODE, "No research with ID={0} exists.", researchId);
    }
}
