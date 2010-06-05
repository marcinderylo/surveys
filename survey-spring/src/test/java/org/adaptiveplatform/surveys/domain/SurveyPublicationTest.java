package org.adaptiveplatform.surveys.domain;

import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Marcin Deryło
 */
public class SurveyPublicationTest {

    private SurveyPublication survey;

    @BeforeMethod
    public void setUp() {
        survey = new SurveyPublication(mock(SurveyTemplate.class), mock(
                StudentGroup.class));
    }

    @Test
    public void shouldFillingBeEnabledInAClosedPeriod() throws Exception {
        whenEnablingSurveyFillingInPeriod(oneHourAgo(), inOneHour());
        expectThatSurveyFillingIsPossibleNow();
    }

    @Test
    public void shouldFillingBeDisabledOutsideOfAClosedPeriod() throws
            Exception {
        whenEnablingSurveyFillingInPeriod(inOneHour(), inTwoHours());
        expectThatSurveyFillingIsForbiddenNow();
    }

    @Test
    public void shouldFillingBeEnabledInAnEndlessPeriod() throws Exception {
        whenEnablingSurveyFillingInPeriod(oneHourAgo(), null);
        expectThatSurveyFillingIsPossibleNow();
    }

    @Test
    public void shouldFillingBeDisabledOutsideOfAnEndlessPeriod() throws
            Exception {
        whenEnablingSurveyFillingInPeriod(inOneHour(), null);
        expectThatSurveyFillingIsForbiddenNow();
    }

    @Test
    public void shouldFillingBeEnabledInAStartlessPeriod() throws Exception {
        whenEnablingSurveyFillingInPeriod(null, inOneHour());
        expectThatSurveyFillingIsPossibleNow();
    }

    @Test
    public void shouldFillingBeDisabledOutsideOfAnStartlessPeriod() throws
            Exception {
        whenEnablingSurveyFillingInPeriod(null, oneHourAgo());
        expectThatSurveyFillingIsForbiddenNow();
    }

    @Test
    public void shouldFillingBeEnabledIfNoFillingPeriodIsSet() throws
            Exception {
        whenEnablingSurveyFillingInPeriod(null, null);
        expectThatSurveyFillingIsPossibleNow();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void cantSetFillingStartDateAfterEndDate() throws Exception {
        whenEnablingSurveyFillingInPeriod(inOneHour(), oneHourAgo());
        expectException();
    }

    private DateTime now() {
        return new DateTime();
    }

    private void whenEnablingSurveyFillingInPeriod(DateTime from, DateTime to) {
        survey.enableFillingInPeriod(from, to);
    }

    private void expectThatSurveyFillingIsPossibleNow() {
        assertTrue(survey.isFillingEnabled(now()));
    }

    private void expectThatSurveyFillingIsForbiddenNow() {
        assertFalse(survey.isFillingEnabled(now()));
    }

    public static DateTime oneHourAgo() {
        return new DateTime().minusHours(1);
    }

    public static DateTime inOneHour() {
        return new DateTime().plusHours(1);
    }

    private DateTime inTwoHours() {
        return new DateTime().plusHours(2);
    }
}
