package infrastructure.utilities;

import infrastructure.exceptions.InvestingException;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Predicate;

@Log4j2
public class ReflectionUtils {

    private ReflectionUtils() {
        throw new IllegalStateException("Utility class");
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
