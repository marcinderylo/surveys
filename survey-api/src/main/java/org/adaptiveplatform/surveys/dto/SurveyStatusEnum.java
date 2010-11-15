package org.adaptiveplatform.surveys.dto;

import org.adaptiveplatform.codegenerator.api.RemoteDictionary;

/**
 *
 * @author Marcin Deryło
 */
@RemoteDictionary
public enum SurveyStatusEnum {

	/**
	 * A filled survey that has been already published.
	 */
	SUBMITTED,
	/**
	 * A filled survey that has been started but not yet submitted and is
	 * currently in it's publication period.
	 */
	STARTED,
	/**
	 * A survey that has not been filled but can be filled at the moment.
	 */
	PENDING,
	/**
	 * A started or pending survey that can't be currently filled due to being
	 * outside of it's availability period.
	 */
	OUTSIDE_PUBLICATION_PERIOD;
}
