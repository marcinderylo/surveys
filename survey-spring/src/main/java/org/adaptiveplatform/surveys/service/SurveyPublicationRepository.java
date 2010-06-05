package org.adaptiveplatform.surveys.service;

import java.util.List;

import org.adaptiveplatform.surveys.domain.SurveyPublication;

/**
 *
 * @author Marcin Deryło
 */
public interface SurveyPublicationRepository {

    SurveyPublication get(Long publicationId);

    void persist(SurveyPublication newPublication);

    List<SurveyPublication> getByGroup(Long groupId);

    void remove(SurveyPublication publication);

    List<SurveyPublication> listPublications(Long templateId);

    SurveyPublication getExisting(Long publishedSurveyTemplateId);
}
