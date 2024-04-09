package infrastructure.enums;

import org.apache.commons.lang3.RegExUtils;

public enum Result {
    POSITIVE,
    NEGATIVE,
    ZERO;

    public static Result fromDouble(double value) {
        if (value < 0)
            return NEGATIVE;

        if (value == 0)
            return ZERO;

        return POSITIVE;
    }

    public static Result fromInt(int value) {
        return fromDouble(value);
    }

    public static Result fromString(String value) {
        return fromDouble(Double.parseDouble(RegExUtils.replaceAll(value, "[%,]", "")));
    }
}
