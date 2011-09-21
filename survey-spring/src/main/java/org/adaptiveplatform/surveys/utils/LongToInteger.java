package org.adaptiveplatform.surveys.utils;

import com.google.common.base.Function;

/**
 * @author Marcin Deryło
 */
final class LongToInteger implements Function<Long, Integer> {

    public static final LongToInteger INSTANCE =
            new LongToInteger();

    private LongToInteger() {
    }

    @Override
    public Integer apply(Long i) {
        return i.intValue();
    }
}
