package org.adaptiveplatform.surveys.application.mock {
import mx.collections.ArrayCollection;

import org.adaptiveplatform.communication.ResultHandler;
import org.adaptiveplatform.communication.SuccessResultHandler;
import org.adaptiveplatform.surveys.application.generated.EvaluationDao;
import org.adaptiveplatform.surveys.dto.generated.ActivitiesQuery;
import org.adaptiveplatform.surveys.dto.generated.ResearchesQuery;

internal class EvaluationDaoStub implements EvaluationDao {

    private var random:RandomDtoGenerator = new RandomDtoGenerator();

    private var researches:ArrayCollection = new ArrayCollection();
    private var activities:ArrayCollection = new ArrayCollection();

    public function EvaluationDaoStub() {
        for (var i:int = 0; i < 10; i++) {
            researches.addItem(random.research());
        }
        for (var i:int = 0; i < 10; i++) {
            activities.addItem(random.evaluationActivity());
        }
    }

    public function queryResearches(researchesQuery:ResearchesQuery):ResultHandler {
        return new SuccessResultHandler(researches);
    }

    public function queryActivities(activitiesQuery:ActivitiesQuery):ResultHandler {
        return new SuccessResultHandler(activities);
    }

    public function get (number:Number):ResultHandler {
        return new SuccessResultHandler(researches.getItemAt(number % researches.length));
    }
}
}
