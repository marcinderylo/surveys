package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.Date;

import org.adaptiveplatform.codegenerator.api.RemoteObject;

/**
 *
 * @author Marcin Deryło
 */
@RemoteObject
public class EvaluationActivityDto implements Serializable {

    private Long id;
    private String name;
    private ActivityTypeEnum type;
    private Date creationDate;

    public EvaluationActivityDto() {
        // to be used rather by serialization framework
    }

    public EvaluationActivityDto(Long id, String name, ActivityTypeEnum type,
            Date creationDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActivityTypeEnum getType() {
        return type;
    }

    public void setType(ActivityTypeEnum type) {
        this.type = type;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
