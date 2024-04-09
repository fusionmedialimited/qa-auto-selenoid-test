package infrastructure.icons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Implementation for Path Basic Shape
 *
 * @see
 * <a href="https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Basic_Shapes#path">Path</a>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Path extends IconSubElementBase {

    private String d;

    @Override
    public String toXpath() {
        return toXpath(this);
    }
}
