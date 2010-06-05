package org.adaptiveplatform.surveys.service;

import org.adaptiveplatform.surveys.domain.Research;

/**
 *
 * @author Marcin Deryło
 */
public interface ResearchRepository {
    Research getExisting(Long id);

    void persist(Research research);
}
