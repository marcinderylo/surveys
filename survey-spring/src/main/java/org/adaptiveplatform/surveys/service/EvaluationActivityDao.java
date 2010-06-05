package org.adaptiveplatform.surveys.service;

import java.util.List;

import org.adaptiveplatform.surveys.dto.ActivitiesQuery;
import org.adaptiveplatform.surveys.dto.EvaluationActivityDto;
import org.adaptiveplatform.surveys.dto.UserDto;

/**
 *
 * @author Marcin Deryło
 */
public interface EvaluationActivityDao {
    List<EvaluationActivityDto> query(ActivitiesQuery query, UserDto caller);
}
