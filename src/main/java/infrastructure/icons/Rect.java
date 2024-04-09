package infrastructure.icons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Implementation for Rect Basic Shape
 *
 * @see
 * <a href="https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Basic_Shapes#rect">Rect</a>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Rect extends IconSubElementBase {
    private Double x;
    private Double y;
    private Double width;
    private Double height;
    private Double rx;
    private Double ry;

    @Override
    public String toXpath() {
        return toXpath(this);
    }
}
