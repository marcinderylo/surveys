package org.adaptiveplatform.surveys.application;

import java.util.List;

import org.adaptiveplatform.codegenerator.api.RemoteService;
import org.adaptiveplatform.surveys.dto.ActivitiesQuery;
import org.adaptiveplatform.surveys.dto.EvaluationActivityDto;
import org.adaptiveplatform.surveys.dto.ResearchDto;
import org.adaptiveplatform.surveys.dto.ResearchesQuery;

/**
 *
 * @author Marcin Deryło
 */
@RemoteService
public interface EvaluationDao {

    List<ResearchDto> queryResearches(ResearchesQuery query);

    ResearchDto get(Long researchId);

    List<EvaluationActivityDto> queryActivities(ActivitiesQuery query);
}
