package org.adaptiveplatform.surveys.service;

import static org.adaptiveplatform.surveys.utils.DateTimeUtils.asDateTime;

import java.util.Date;

import org.adaptiveplatform.surveys.domain.StudentGroup;
import org.adaptiveplatform.surveys.domain.SurveyPublication;
import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Marcin Deryło
 */
@Service
public class SurveyPublicationFactory {

    public SurveyPublication create(SurveyTemplate template, StudentGroup group,
            Date publishFrom, Date publishTo) {
        // ensure that template is ready
        template.publish();

        SurveyPublication publication = new SurveyPublication(
                template, group);

        publication.enableFillingInPeriod(asDateTime(publishFrom), asDateTime(
                publishTo));
        
        return publication;
    }
}
