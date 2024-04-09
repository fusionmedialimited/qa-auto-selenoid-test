package infrastructure.icons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Implementation for Circle Basic Shape
 *
 * @see
 * <a href="https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Basic_Shapes#circle">Circle</a>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Circle extends IconSubElementBase {

    private Double cx;
    private Double cy;
    private Double r;

    @Override
    public String toXpath() {
        return toXpath(this);
    }
}
