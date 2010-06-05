package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.Date;

import org.adaptiveplatform.codegenerator.api.RemoteObject;


/**
 * 
 * @author Marcin Deryło
 */
@RemoteObject
public class ChangeSurveyPublicationCommand implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long publicationId;
    private Date startingDate;
    private Date expirationDate;

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public Long getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(Long publicationId) {
        this.publicationId = publicationId;
    }
}
