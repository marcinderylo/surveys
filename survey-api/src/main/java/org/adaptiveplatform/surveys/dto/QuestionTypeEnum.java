package org.adaptiveplatform.surveys.dto;

import org.adaptiveplatform.codegenerator.api.RemoteDictionary;

/**
 * Supported types of survey questions.
 * 
 * @author Marcin Derylo
 */
@RemoteDictionary
public enum QuestionTypeEnum {

	/**
	 * Question allowing only a single answer to be selected.
	 */
	SINGLE_CHOICE,
	/**
	 * Question allowing multiple answers to be selected.
	 */
	MULTIPLE_CHOICE,
	/**
	 * Question allowing no answers to be selected but requiring a text comment
	 * to be provided as answer.
	 */
	OPEN
}
