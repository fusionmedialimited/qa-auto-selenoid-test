package infrastructure.icons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Implementation for Polyline Basic Shape
 *
 * @see
 * <a href="https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Basic_Shapes#polyline">Polyline</a>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Polyline extends IconSubElementBase {

    private String points;

    @Override
    public String toXpath() {
        return toXpath(this);
    }
}
