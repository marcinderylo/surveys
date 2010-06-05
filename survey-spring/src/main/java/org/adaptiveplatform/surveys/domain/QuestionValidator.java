package org.adaptiveplatform.surveys.domain;

/**
 * @author Marcin Deryło
 */
public interface QuestionValidator {
        /**
         * <p>Validates given question template. If it is found valid, method
         * should return silently. In case of validation error, {@link
         * IllegalArgumentException} should be thrown.
         *
         * <p>Exact validity criteria depend on the concrete implementator of
         * this interface.</p>
         * 
         * @param template question template to be validated
         * @throws IllegalArgumentException if validation fails.
         */
        void validateQuestion(QuestionTemplate template) throws IllegalArgumentException;
}
