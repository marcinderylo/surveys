package org.adaptiveplatform.surveys.configuration.flex;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.adaptiveplatform.surveys.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.flex.core.ExceptionTranslator;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

import flex.messaging.MessageException;

public class BusinessExceptionTranslator implements ExceptionTranslator {

    private static final char PARAMETER_SEPARATOR = '`';
    private static final String COLLECTION_TYPE_PREFIX = "*";
    private static final String UNKNOWN_COLLECTION_ELEMENT_TYPE = "?";
    private static final String EMPTY_COLLECTION_TYPE = COLLECTION_TYPE_PREFIX
            + UNKNOWN_COLLECTION_ELEMENT_TYPE;
    @SuppressWarnings("serial")
    private static final Map<Class<?>, String> PARAMETER_TYPE_CODES;

    static {
        Map<Class<?>, String> parameterTypeCodes = Maps.newHashMap();
        parameterTypeCodes.put(Long.class, "L");
        parameterTypeCodes.put(String.class, "S");
        parameterTypeCodes.put(Integer.class, "I");
        parameterTypeCodes.put(Date.class, "D");
        parameterTypeCodes.put(Boolean.class, "B");
        parameterTypeCodes.put(UnknownType.class,
                UNKNOWN_COLLECTION_ELEMENT_TYPE);
        PARAMETER_TYPE_CODES = Collections.unmodifiableMap(parameterTypeCodes);
    }

    private static final class UnknownType {
    }

    @Override
    public boolean handles(Class<?> clazz) {
        return BusinessException.class.isAssignableFrom(clazz);
    }

    @Override
    public MessageException translate(Throwable t) {
        BusinessException ex = (BusinessException) t;
        MessageException exception = new MessageException(ex.getMessage());
        exception.setCode(ex.getCode());
        exception.setDetails(encodeParameters(ex));
        return exception;
    }

    private String encodeParameters(BusinessException ex) {
        return StringUtils.join(encodeParameters(ex.getParameters()),
                PARAMETER_SEPARATOR);
    }

    private Collection<String> encodeParameters(List<Object> parameters) {
        return Collections2.transform(parameters, new Function<Object, String>() {

            @Override
            public String apply(Object from) {
                return encodeParameter(from);
            }
        });
    }

    private String encodeParameter(Object param) {
        if (param == null) {
            return "null";
        }
        if (param instanceof Collection) {
            final Collection collectionParam = (Collection) param;
            return encodeCollectionParameterType(collectionParam) + encodeCollection(
                    collectionParam);
        }
        return encodeRegularParameterType(param.getClass()) + param;
    }

    private String encodeRegularParameterType(
            Class<? extends Object> parameterType) {
        String code = PARAMETER_TYPE_CODES.get(parameterType);
        Validate.notNull(code, "Unsupported error parameter type: "
                + parameterType);
        return code;
    }

    private String encodeCollectionParameterType(Collection collection) {
        if (collection.isEmpty()) {
            return EMPTY_COLLECTION_TYPE;
        }
        Class<?> elementType = determineElementType(collection);
        return COLLECTION_TYPE_PREFIX + encodeRegularParameterType(elementType);
    }

    private Class<?> determineElementType(Collection<?> nonEmptyCollection) {
        for (Object element : nonEmptyCollection) {
            if (element != null) {
                return element.getClass();
            }
        }
        return UnknownType.class;
    }

    private String encodeCollection(Collection collectionParam) {
        Collection<String> elementRepresentations =
                Collections2.transform(collectionParam, new NullSafeToString());
        return "[" + StringUtils.join(elementRepresentations, ",") + "]";
    }

    private static class NullSafeToString implements Function<Object, String> {

        @Override
        public String apply(Object from) {
            if (from == null) {
                return "null";
            }
            return from.toString();
        }
    }
}
