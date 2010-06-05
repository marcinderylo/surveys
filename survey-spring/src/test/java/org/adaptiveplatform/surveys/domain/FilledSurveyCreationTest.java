package org.adaptiveplatform.surveys.domain;

import static java.util.Collections.EMPTY_LIST;
import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertNotNull;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.testng.annotations.Test;

/**
 *
 * @author Marcin Deryło
 */
public class FilledSurveyCreationTest {

    private SurveyPublication publishedTemplate;
    private UserDto user;
    private FilledSurvey survey;

    @Test
    public void shouldCreateWithPublishedTemplateAndUser() throws Exception {
        givenAPublishedSurveyTemplate(aPublishedSurveyTemplate());
        andUser(someUser());
        whenCreatingAFilledSurvey();
        expectThatEverythingGoesWell();
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void cantCreateWithoutPublishedTemplate() throws Exception {
        givenAPublishedSurveyTemplate(null);
        andUser(someUser());
        whenCreatingAFilledSurvey();
        expectException();
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void cantCreateWithoutUser() throws Exception {
        givenAPublishedSurveyTemplate(aPublishedSurveyTemplate());
        andUser(null);
        whenCreatingAFilledSurvey();
        expectException();
    }

    private void givenAPublishedSurveyTemplate(
            SurveyPublication publishedTemplate) {
        this.publishedTemplate = publishedTemplate;
    }

    private void andUser(UserDto user) {
        this.user = user;
    }

    private void whenCreatingAFilledSurvey() {
        survey = new FilledSurvey(publishedTemplate, user);
    }

    private void expectThatEverythingGoesWell() {
        assertNotNull(survey);
    }

    private static SurveyPublication aPublishedSurveyTemplate() {
        SurveyPublication publication = mock(SurveyPublication.class,
                RETURNS_DEEP_STUBS);
        given(publication.getSurveyTemplate().getQuestions()).willReturn(
                EMPTY_LIST);
        return publication;
    }

    private static UserDto someUser() {
        return mock(UserDto.class);
    }
}
