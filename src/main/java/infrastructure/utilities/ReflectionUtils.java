package infrastructure.utilities;

import infrastructure.exceptions.InvestingException;
import infrastructure.exceptions.ReflectionUtilsRuntimeException;
import infrastructure.functional.PredicateDescribed;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Log4j2
public class ReflectionUtils {

    private ReflectionUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Reflection method to get property by name
     * You should know which cost you pay for using it
     *
     * @param bean - object to find property value
     * @param name - name of property
     * @return property value as Object
     */
    public static Object getProperty(Object bean, String name) {
        try {
            return PropertyUtils.getProperty(bean, name);
        } catch (IllegalAccessException e1) {
            throw new ReflectionUtilsRuntimeException("Cannot obtain an access to property accessor method", e1);
        } catch (InvocationTargetException e2) {
            throw new ReflectionUtilsRuntimeException("Exception has been thrown from property accessor", e2);
        } catch (NoSuchMethodException e3) {
            throw new ReflectionUtilsRuntimeException("Accessor method for this property cannot be found", e3);
        }
    }

    /**
     * Reflection method to get the first property from object (like POJO) which is not null
     * and get value
     * @param object
     * @return - entry where getKey() and getValue() methods could be used, could be null
     */
    public static Map.Entry<String, Object> getFirstNonNullProperty(Object object) {
        try {
            return PropertyUtils.describe(object).entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() != null)
                    .findFirst()
                    .orElse(null);
        } catch (IllegalAccessException e1) {
            throw new ReflectionUtilsRuntimeException("Cannot obtain an access to property accessor method", e1);
        } catch (InvocationTargetException e2) {
            throw new ReflectionUtilsRuntimeException("Exception has been thrown from property accessor", e2);
        } catch (NoSuchMethodException e3) {
            throw new ReflectionUtilsRuntimeException("Accessor method for this property cannot be found", e3);
        }
    }

    /**
     * Creates new instance from current object and then copying values from newObjects, ignoring new NULLs
     * Old values are overwritten.
     *
     * @param newObject Same type object with new values.
     */
    public static <T> T merge(T currentObject, T newObject) {

        checkIfClassesTheSameOrThrowError(currentObject, newObject);
        T result = newInstanceOf(currentObject);

        for (Field field : result.getClass().getDeclaredFields()) {
            for (Field newField : result.getClass().getDeclaredFields()) {
                if (field.getName().equals(newField.getName())) {
                    try {
                        field.setAccessible(true);
                        newField.setAccessible(true);
                        field.set(
                                result,
                                newField.get(newObject) == null
                                        ? field.get(currentObject)
                                        : newField.get(newObject));
                    } catch (IllegalAccessException e) {
                        log.error(e);
                        // Field update exception on final modifier and other cases.
                    }
                }
            }
        }
        return result;
    }

    private static <T> void checkIfClassesTheSameOrThrowError(T c1, T c2) {
        if (!c1.getClass().equals(c2.getClass())) {
            throw new IllegalArgumentException(String.format("Objects has a different classes [%s] vs [%s]",
                    c1.getClass(),
                    c2.getClass()));
        }
    }

    private static <T> T newInstanceOf(T object) {
        try {
            return (T) object.getClass().getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionUtilsRuntimeException("Cannot create a new instance of the object: ", e);
        }
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
     * Parses all static fields of source class, which are assignable from required class.
     * Maps these fields to objects of class, which they are assignable from
     *
     * @param source         - class, which fields should be parsed and mapped
     * @param assignableFrom - class, from which filed should be assignable
     * @return list of mapped fields
     */
    @SuppressWarnings("unchecked")
    public static <T1, T2> List<T2> getAndMapStaticFields(Class<T1> source, Class<T2> assignableFrom) {
        return getStaticFields(source, assignableFrom).stream()
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        return (T2) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
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


    /**
     * Get first found method from provided class,
     * which takes at least 1 parameter of provided type
     *
     * @param sourceClass class, where method is searched for
     * @param paramType   type of parameter
     */
    public static Method getDeclaredMethodByParamType(Class<?> sourceClass, Class<?> paramType) {
        PredicateDescribed<Method> predicate = new PredicateDescribed<>(
                "Declared method with at least 1 argument of " + paramType.getSimpleName() + "type",
                method -> isMethodDeclared(sourceClass, method) && ArrayUtils.contains(method.getParameterTypes(), paramType)
        );

        return getMethod(sourceClass, predicate);
    }

    /**
     * Check if class declares method
     *
     * @param clazz  source class
     * @param method checked method
     */
    public static boolean isMethodDeclared(Class<?> clazz, Method method) {
        try {
            return method.getDeclaringClass().equals(clazz);
        } catch (Exception cause) {
            throw new InvestingException(
                    String.format("Couldn't check if method %s is declared in the class %s", method.getName(), clazz.getSimpleName()),
                    cause
            );
        }
    }

    /**
     * ClassSimpleName -> Class Simple Name
     *
     * @param classObj class, which name should be made pretty
     * @return simple name of the provided class with whitespaces between words
     */
    public static String getSimpleNamePretty(Class<?> classObj) {
        return classObj.getSimpleName().chars()
                .mapToObj(c -> {
                    String prefix = Character.isUpperCase(c)
                            ? " "
                            : "";
                    return prefix + (char) c;
                })
                .collect(Collectors.joining())
                .trim();
    }
}
