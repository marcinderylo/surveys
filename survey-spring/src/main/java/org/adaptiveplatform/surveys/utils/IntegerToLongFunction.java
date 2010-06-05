package org.adaptiveplatform.surveys.utils;

import com.google.common.base.Function;

/**
 * @author Marcin Deryło
 */
public final class IntegerToLongFunction implements Function<Integer, Long> {

        public static final IntegerToLongFunction INSTANCE =
                new IntegerToLongFunction();

        private IntegerToLongFunction() {
        }

        @Override
        public Long apply(Integer i) {
                return i.longValue();
        }
}
