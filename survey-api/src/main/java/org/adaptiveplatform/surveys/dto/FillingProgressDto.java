package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.RemoteObject;

/**
 *
 * @author Marcin Deryło
 */
@RemoteObject
public class FillingProgressDto implements Serializable {

    private int expectedSurveysCount;
    private int actualSurveysCount;

    public FillingProgressDto() {
        // to be used rather by serialization framework
    }

    public FillingProgressDto(int actualSurveysCount, int expectedSurveysCount) {
        this.expectedSurveysCount = expectedSurveysCount;
        this.actualSurveysCount = actualSurveysCount;
    }

    public int getActualSurveysCount() {
        return actualSurveysCount;
    }

    public void setActualSurveysCount(int actualSurveysCount) {
        this.actualSurveysCount = actualSurveysCount;
    }

    public int getExpectedSurveysCount() {
        return expectedSurveysCount;
    }

    public void setExpectedSurveysCount(int expectedSurveysCount) {
        this.expectedSurveysCount = expectedSurveysCount;
    }
}
