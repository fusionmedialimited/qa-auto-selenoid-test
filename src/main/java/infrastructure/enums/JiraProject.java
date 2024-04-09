package infrastructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JiraProject {

    CONTENT ("Content"),
    CT      ("Core Tech"),
    FNTL    ("Financial Tools (v.2)"),
    PRO     ("Pro"),
    REVUX   ("RevenUX"),
    SWAT    ("SWAT"),
    WA      ("Warren - AI"),
    WQC     ("Web QA Chapter");

    private final String name;

}
