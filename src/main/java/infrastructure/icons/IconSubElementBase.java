package infrastructure.icons;

import infrastructure.logger.Log;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.List;

public abstract class IconSubElementBase implements IconSubElement {

    /**
     * Cast provided value of SVG Basic Shape attribute to string <br> <br>
     *
     * Instance of String is cast as it is <br>
     *
     * Instance of Double is cast to string without fractional part,
     * if it isn't present
     *
     * @param value - SVG Basic Shape attribute value
     * @return value as String
     */
    protected String valueToString(Object value) {
        if (value instanceof Double) {
            Double numeric = (Double) value;
            String numericStr = String.valueOf(numeric);

            return numeric % 1 == 0
                    ? numericStr.substring(0, numericStr.indexOf("."))
                    : numericStr;
        }

        return (String) value;
    }

    /**
     * Build xpath for provided {@link IconSubElement} object
     */
    @SneakyThrows
    protected String toXpath(IconSubElement subElement) {

        String name = subElement.getClass().getSimpleName().toLowerCase();
        StringBuilder sb = new StringBuilder(".//*[name() = \"")
                .append(name)
                .append("\"");

        for (Field field : List.of(subElement.getClass().getDeclaredFields())) {
            Object value;
            field.setAccessible(true);
            value = field.get(subElement);

            if (value != null)
                sb.append(" and @")
                        .append(field.getName())
                        .append(" = \"")
                        .append(valueToString(value))
                        .append("\"");
        }

        String xpath = sb.append("]").toString();
        Log.debug("Line has xpath: ".concat(xpath));

        return xpath;
    }
}
