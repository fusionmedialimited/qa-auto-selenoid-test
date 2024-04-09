package infrastructure.icons;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.By;

import java.util.List;
import java.util.Objects;

/**
 * Contains {@link IconSubElement} elements
 * and method to build locator, depending on these elements
 */
@NoArgsConstructor
@Getter
@Setter
public class Icon {

    private IconSubElement[] subElements;

    public Icon(IconSubElement... subElements) {
        this.subElements = subElements;
    }

    /**
     * Build xpath of svg element <br>
     *
     * @param isChild      is icon expected as child element (xpath starts with '.' char)
     * @param fromRootNode is element located in the root node ('/') or in any node ('//')
     * @return             locator from xpath, like: <br>
     *                     <i>//*[name()="svg" and ./*[name()="line" and @x1 = "1" and @y1 = "1" and @x2 = "5" and @y2 = "5"]]</i>
     */
    public String buildXpath(boolean isChild, boolean fromRootNode) {
        StringBuilder sb = isChild
                ? new StringBuilder(".")
                : new StringBuilder();

        if (fromRootNode)
            sb.append("/");
        else sb.append("//");

        sb.append("*[name()=\"svg\"");

        for (IconSubElement subElement : List.of(subElements)) {

            sb.append(" and ").append(Objects.requireNonNull(subElement).toXpath());
        }

        return sb.append("]").toString();
    }

    /**
     * Get locator by xpath, built in {@link #buildXpath(boolean, boolean)}
     */
    public By buildLocator(boolean isChild, boolean fromRootNode) {
        return By.xpath(buildXpath(isChild, fromRootNode));
    }

}
