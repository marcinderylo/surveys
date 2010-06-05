package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class NoSuchGroupException extends BusinessException implements
        Serializable {

    public static final String ERROR_CODE = "NO_SUCH_STUDENT_GROUP";

    public NoSuchGroupException(Long groupId) {
        super(ERROR_CODE, "No student group with ID={0} exists.", groupId);
    }
}
