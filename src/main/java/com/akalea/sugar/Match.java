package com.akalea.sugar;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Pattern matching utility for expressive conditional logic.
 * Provides a fluent API for matching values against patterns.
 */
public interface Match {

    /**
     * Creates a matcher for the given value.
     */
    static <T> Matcher<T> of(T value) {
        return new Matcher<>(value);
    }

    /**
     * Pattern matcher builder with fluent API.
     */
    class Matcher<T> {
        private final T value;
        private Object result = null;
        private boolean matched = false;

        Matcher(T value) {
            this.value = value;
        }

        // Private constructor for internal state copying
        private Matcher(T value, Object result, boolean matched) {
            this.value = value;
            this.result = result;
            this.matched = matched;
        }

        /**
         * Matches if the value equals the expected value.
         */
        public <R> Matcher<T> when(T expected, Supplier<R> supplier) {
            if (!matched && Objects.equals(value, expected)) {
                result = supplier.get();
                matched = true;
            }
            return this;
        }

        /**
         * Matches if the value equals the expected value, using the value in the result.
         */
        public <R> Matcher<T> when(T expected, Function<T, R> transformer) {
            if (!matched && Objects.equals(value, expected)) {
                result = transformer.apply(value);
                matched = true;
            }
            return this;
        }

        /**
         * Matches if the predicate returns true.
         */
        public <R> Matcher<T> when(Predicate<T> predicate, Supplier<R> supplier) {
            if (!matched && value != null && predicate.test(value)) {
                result = supplier.get();
                matched = true;
            }
            return this;
        }

        /**
         * Matches if the predicate returns true, using the value in the result.
         */
        public <R> Matcher<T> when(Predicate<T> predicate, Function<T, R> transformer) {
            if (!matched && value != null && predicate.test(value)) {
                result = transformer.apply(value);
                matched = true;
            }
            return this;
        }

        /**
         * Matches if the value is null.
         */
        public <R> Matcher<T> whenNull(Supplier<R> supplier) {
            if (!matched && value == null) {
                result = supplier.get();
                matched = true;
            }
            return this;
        }

        /**
         * Matches if the value is an instance of the specified type.
         */
        @SuppressWarnings("unchecked")
        public <U, R> Matcher<T> whenType(Class<U> type, Function<U, R> transformer) {
            if (!matched && value != null && type.isInstance(value)) {
                result = transformer.apply((U) value);
                matched = true;
            }
            return this;
        }

        /**
         * Matches if the value is an instance of the specified type.
         */
        public <R> Matcher<T> whenType(Class<?> type, Supplier<R> supplier) {
            if (!matched && value != null && type.isInstance(value)) {
                result = supplier.get();
                matched = true;
            }
            return this;
        }

        /**
         * Matches any of the expected values.
         */
        @SafeVarargs
        public final <R> Matcher<T> whenAny(Supplier<R> supplier, T... expected) {
            if (!matched) {
                for (T exp : expected) {
                    if (Objects.equals(value, exp)) {
                        result = supplier.get();
                        matched = true;
                        break;
                    }
                }
            }
            return this;
        }

        /**
         * Matches if value is in the specified range (for Comparable types).
         */
        @SuppressWarnings("unchecked")
        public <R> Matcher<T> whenInRange(T lower, T upper, Supplier<R> supplier) {
            if (!matched && value != null && value instanceof Comparable) {
                Comparable<T> cValue = (Comparable<T>) value;
                if (cValue.compareTo(lower) >= 0 && cValue.compareTo(upper) <= 0) {
                    result = supplier.get();
                    matched = true;
                }
            }
            return this;
        }

        /**
         * Matches if value is in the specified range (for Comparable types), using the value in the result.
         */
        @SuppressWarnings("unchecked")
        public <R> Matcher<T> whenInRange(T lower, T upper, Function<T, R> transformer) {
            if (!matched && value != null && value instanceof Comparable) {
                Comparable<T> cValue = (Comparable<T>) value;
                if (cValue.compareTo(lower) >= 0 && cValue.compareTo(upper) <= 0) {
                    result = transformer.apply(value);
                    matched = true;
                }
            }
            return this;
        }

        /**
         * Default case if no other pattern matched.
         */
        @SuppressWarnings("unchecked")
        public <R> R otherwise(Supplier<R> supplier) {
            if (!matched) {
                return supplier.get();
            }
            return (R) result;
        }

        /**
         * Default case with a constant value.
         */
        @SuppressWarnings("unchecked")
        public <R> R otherwise(R defaultValue) {
            if (!matched) {
                return defaultValue;
            }
            return (R) result;
        }

        /**
         * Default case that uses the original value in the result.
         */
        @SuppressWarnings("unchecked")
        public <R> R otherwise(Function<T, R> transformer) {
            if (!matched) {
                return transformer.apply(value);
            }
            return (R) result;
        }

        /**
         * Throws an exception if no pattern matched.
         */
        @SuppressWarnings("unchecked")
        public <R> R otherwiseThrow(Supplier<? extends RuntimeException> exceptionSupplier) {
            if (!matched) {
                throw exceptionSupplier.get();
            }
            return (R) result;
        }

        /**
         * Returns the result, which may be null if no pattern matched.
         */
        @SuppressWarnings("unchecked")
        public <R> R get() {
            return (R) result;
        }

        /**
         * Returns the result as an Optional.
         */
        @SuppressWarnings("unchecked")
        public <R> Optional<R> toOptional() {
            return Optional.ofNullable((R) result);
        }

        /**
         * Returns true if a pattern has matched.
         */
        public boolean isMatched() {
            return matched;
        }
    }
}
