package org.adaptiveplatform.surveys.configuration.flex;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.adaptiveplatform.surveys.exception.BusinessException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import flex.messaging.MessageException;

public class BusinessExceptionTranslatorTest {

    private BusinessExceptionTranslator translator;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        translator = new BusinessExceptionTranslator();
    }

    @Test
    public void shouldAcceptBusinessExceptions() throws Exception {
        assertTrue(translator.handles(BusinessException.class));
    }

    @Test
    public void shouldNotAcceptExceptions() throws Exception {
        assertFalse(translator.handles(Exception.class));
    }

    @Test
    public void shouldAcceptBusinessExceptionDerivatives() throws Exception {
        class BusinessExceptionDerivative extends BusinessException {

            private static final long serialVersionUID = 1L;

            public BusinessExceptionDerivative() {
                super("CODE", "MSG");
            }
        }
        assertTrue(translator.handles(BusinessExceptionDerivative.class));
    }

    @Test
    public void shouldTranslateSimpleBusinessException() throws Exception {
        // given
        BusinessException be = error("BZNS_CODE", "sample message");
        // when

        MessageException translated = translator.translate(be);
        // then
        assertEquals(translated.getCode(), "BZNS_CODE");
        assertEquals(translated.getMessage(), "sample message");
    }

    @Test
    public void shouldTranslateBusinessExceptionWithStringParameters() throws
            Exception {
        // given
        BusinessException be = error("", "", "42");
        // when
        MessageException translated = translator.translate(be);
        // then
        assertEquals(translated.getDetails(), "S42");
    }

    @Test
    public void shouldFormatBusinessExceptionMessageWithItsParameters() throws
            Exception {
        // given
        BusinessException be = error("", "Should be {0}", 567867L);
        // when
        MessageException translated = translator.translate(be);
        // then
        // formatting is always performed according to Locale.UK rules :]
        assertEquals(translated.getMessage(), "Should be 567,867");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldNotTranslateUnsupportedTypes() throws Exception {
        // given
        class UnsupportedParameterType {
        }

        BusinessException be = error("", "{0}", UnsupportedParameterType.class);
        // when
        translator.translate(be);
    }

    @Test
    public void shouldAllowOmmitingParametersInMessagePattern() throws Exception {
        // given
        BusinessException be = error("", "{0}", "foo", 765);
        // when
        MessageException translated = translator.translate(be);
        // then
        assertEquals(translated.getMessage(), "foo");
    }

    @Test
    public void shouldAllowMorePlaceholdersInMessagePatternThanParameters()
            throws Exception {
        BusinessException be = error("", "{0},{1},{2}", "foo", 765);
        // when
        MessageException translated = translator.translate(be);
        // then
        assertEquals(translated.getMessage(), "foo,765,{2}");
    }

    @Test
    public void shouldTranslateNullParameters() throws Exception {
        BusinessException be = error("", "{0}", new Object[]{null});
        // when
        MessageException translated = translator.translate(be);
        // then
        assertEquals(translated.getMessage(), "null");
    }

    @Test
    public void shouldTranslateCollectionParameters() throws Exception {
        BusinessException be = error("", "{0}", Arrays.asList(1L, 23L, 42123L));
        // when
        MessageException translated = translator.translate(be);
        // then
        assertEquals(translated.getDetails(), "*L[1,23,42123]");
    }

    @Test
    public void shouldTranslateEmptyCollections() throws Exception {
        BusinessException be = error("", "{0}", Collections.EMPTY_SET);
        // when
        MessageException translated = translator.translate(be);
        // then
        assertEquals(translated.getDetails(), "*?[]");
    }

    @Test
    public void shouldTranslateCollectionOfNullsAsCollectionOfUnknownTypes()
            throws Exception {
        BusinessException be = error("", "{0}", Arrays.asList(null, null));
        // when
        MessageException translated = translator.translate(be);
        // then
        assertEquals(translated.getDetails(), "*?[null,null]");
    }

    @Test
    public void shouldTranslateCollectionsWithTypeOfFirstNonNullElement() throws
            Exception {
        BusinessException be = error("", "{0}", Arrays.asList(null, "some string"));
        // when
        MessageException translated = translator.translate(be);
        // then
        assertEquals(translated.getDetails(), "*S[null,some string]");
    }

    private BusinessException error(String code, String message, Object... args) {
        return new BusinessException(code, message, args);
    }
}
