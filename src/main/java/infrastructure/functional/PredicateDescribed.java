package infrastructure.functional;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Predicate;

/**
 * This implementation of the {@link Predicate} functional interface
 * helps to store and provide text description of the check, which
 * is used in the {@link Predicate#test(Object)} method
 * <br><br>
 * Text description can be set by constructor
 * <br><br>
 * Text description can be got by {@link PredicateDescribed#getDescription()} getter
 * or overriden {@link PredicateDescribed#toString()} method
 */

@AllArgsConstructor
@Getter
public class PredicateDescribed<T> implements Predicate<T> {

    private String description;
    private Predicate<T> predicate;

    @Override
    public String toString() {
        return "Predicate with condition: ".concat(getDescription().toUpperCase());
    }

    @Override
    public boolean test(T t) {
        return predicate.test(t);
    }
}
