package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class NotAllowedToChangePublicationDetailsException extends BusinessException implements Serializable {

	public static final String ERROR_CODE = "MUST_BE_GROUP_EVALUATOR_TO_CHANGE_PUBLICATIONS";

	public NotAllowedToChangePublicationDetailsException(@Param("publicationId") Long publicationId,
			@Param("groupName") String groupName) {
		super(ERROR_CODE, "You must be an evaluator in group \"{1}\" to change" + "details of publication ID={0}.",
				publicationId, groupName);
	}
}
