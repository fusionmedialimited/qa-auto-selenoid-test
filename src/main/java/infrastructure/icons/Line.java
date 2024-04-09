package infrastructure.icons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Implementation for Line Basic Shape
 *
 * @see
 * <a href="https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Basic_Shapes#line">Line</a>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Line extends IconSubElementBase {

    private Double x1;
    private Double y1;
    private Double x2;
    private Double y2;

    @Override
    public String toXpath() {
        return toXpath(this);
    }
}
