package org.adaptiveplatform.surveys.utils;

import static com.google.common.collect.Collections2.transform;

import java.util.Collection;

/**
 * Helper functions for collections.
 * Name of the class is inspired by {@code com.google.common.collect.Collections2} 
 * class's name.
 * 
 * @author Marcin Deryło
 */
public final class Collections42 {

    private Collections42() {
    }

    public static Collection<Long> asLongs(Collection<Integer> ints) {
        return transform(ints, IntegerToLongFunction.INSTANCE);
    }

    public static Collection<Integer> asInts(Collection<Long> ints) {
        return transform(ints, LongToInteger.INSTANCE);
    }

    public static boolean hasElements(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static <T> T firstOf(Collection<T> collection) {
        if(hasElements(collection)) {
            return collection.iterator().next();
        }
        return null;
    }
}
