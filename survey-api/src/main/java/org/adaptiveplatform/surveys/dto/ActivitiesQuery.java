package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.RemoteObject;

/**
 *
 * @author Marcin Deryło
 */
@RemoteObject
public class ActivitiesQuery implements Serializable {

    private int maxNumberOfResults;

    public int getMaxNumberOfResults() {
        return maxNumberOfResults;
    }

    public void setMaxNumberOfResults(int maxNumberOfResults) {
        this.maxNumberOfResults = maxNumberOfResults;
    }
}
