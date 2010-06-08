package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class NoSuchGroupException extends BusinessException implements Serializable {

	public static final String ERROR_CODE = "NO_SUCH_STUDENT_GROUP";

	public NoSuchGroupException(@Param("groupId") Long groupId) {
		super(ERROR_CODE, "No student group with ID={0} exists.", groupId);
	}
}
