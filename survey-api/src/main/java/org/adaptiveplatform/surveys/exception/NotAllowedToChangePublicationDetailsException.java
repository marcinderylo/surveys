package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class NotAllowedToChangePublicationDetailsException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE =
            "MUST_BE_GROUP_EVALUATOR_TO_CHANGE_PUBLICATIONS";

    public NotAllowedToChangePublicationDetailsException(Long publicationId,
            String groupName) {
        super(ERROR_CODE, "You must be an evaluator in group \"{1}\" to change"
                + "details of publication ID={0}.", publicationId, groupName);
    }
}
