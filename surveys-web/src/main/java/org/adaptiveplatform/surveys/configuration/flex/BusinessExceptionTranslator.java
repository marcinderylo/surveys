package org.adaptiveplatform.surveys.configuration.flex;

import java.util.Collection;
import java.util.List;
import org.adaptiveplatform.surveys.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.flex.core.ExceptionTranslator;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import flex.messaging.MessageException;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.Validate;

public class BusinessExceptionTranslator implements ExceptionTranslator {

    public static final String BUSINESS_EXCEPTION_PREFIX = "BSN_";
    public static final Set<Class<?>> SUPPORTED_TYPES = ImmutableSet.<Class<?>>of(Integer.class,
            Boolean.class, Long.class, Date.class, String.class);
    private static final char PARAMETER_SEPARATOR = '`';

    @Override
    public boolean handles(Class<?> clazz) {
        return BusinessException.class.isAssignableFrom(clazz);
    }

    @Override
    public MessageException translate(Throwable t) {
        BusinessException ex = (BusinessException) t;
        MessageException exception = new MessageException(ex.getMessage());
        exception.setCode(BUSINESS_EXCEPTION_PREFIX + ex.getCode());
        exception.setDetails(encodeParameters(ex));
        return exception;
    }

    private String encodeParameters(BusinessException ex) {
        return StringUtils.join(encodeParameters(ex.getParameters()),
                PARAMETER_SEPARATOR);
    }

    private Collection<String> encodeParameters(List<Object> parameters) {
        return Collections2.transform(parameters,
                new Function<Object, String>() {

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
            return encodeCollection(collectionParam);
        }
        return encodeSingleValueParameter(param);
    }

    private String encodeSingleValueParameter(Object param) {
        ensureSupportedParameterType(param.getClass());
        return NullSafeToString.INSTANCE.apply(param);
    }

    private String encodeCollection(Collection collectionParam) {
        Collection<String> elementRepresentations = Collections2.transform(
                collectionParam, new NullSafeToString());
        return "[" + StringUtils.join(elementRepresentations, ",") + "]";
    }

    private void ensureSupportedParameterType(Class<?> paramClass) {
        Validate.isTrue(SUPPORTED_TYPES.contains(paramClass), "Business exception's paramter type "
                + paramClass.getName() + "is not supported");
    }

    private static class NullSafeToString implements Function<Object, String> {

        private static final Function<Object, String> INSTANCE = new NullSafeToString();

        private NullSafeToString() {
        }

        @Override
        public String apply(Object from) {
            if (from == null) {
                return "null";
            }
            return from.toString();
        }
    }
}
