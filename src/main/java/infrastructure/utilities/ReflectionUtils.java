package infrastructure.utilities;

import infrastructure.exceptions.InvestingException;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Log4j2
public class ReflectionUtils {

    private ReflectionUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Parses all static fields of source class, which are assignable from required class
     *
     * @param source         - class, which fields should be parsed
     * @param assignableFrom - class, from which filed should be assignable
     * @return list of fields
     */
    public static <T1, T2> List<Field> getStaticFields(Class<T1> source, Class<T2> assignableFrom) {
        return Arrays.stream(source.getFields())
                .filter(field ->
                        field.getType().isAssignableFrom(assignableFrom)
                                && Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());
    }

    /**
     * Get first found method from provided class,
     * which satisfy provided predicate
     *
     * @param clazz     source class
     * @param predicate provides condition to define and get method
     */
    public static Method getMethod(Class<?> clazz, Predicate<Method> predicate) {
        return Arrays.stream(clazz.getMethods())
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> new InvestingException(
                            String.format("Couldn't find method in the %s class, which satisfy condition: %s", clazz.getSimpleName(), predicate)
                        )
                );
    }

}
