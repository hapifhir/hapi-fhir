package ca.uhn.fhir.cql.common.evaluation;

import java.util.HashMap;
import java.util.Map;

public enum MeasureScoring {
    PROPORTION("proportion", "Proportion", "The measure score is defined using a proportion"),

    RATIO("ratio", "Ratio", "The measure score is defined using a ratio"),

    CONTINUOUSVARIABLE("continuous-variable", "Continuous Variable", "The score is defined by a calculation of some quantity"),

    COHORT("cohort", "Cohort", "The measure is a cohort definition");

    private String code;
    private String display;
    private String definition;

    MeasureScoring(String code, String display, String definition) {
        this.code = code;
        this.display = display;
        this.definition = definition;
    }

    private static final Map<String, MeasureScoring> lookup = new HashMap<>();

    static {
        for (MeasureScoring ms : MeasureScoring.values()) {
            lookup.put(ms.toCode(), ms);
        }
    }

    public static MeasureScoring fromCode(String code) {
        if (code != null && !code.isEmpty()) {
            if (lookup.containsKey(code)) {
                return lookup.get(code);
            }
            // } else if (Configuration.isAcceptInvalidEnums()) {
            //     return null;
            // } else {
            //     // throw new FHIRException("Unknown MeasureScoring code \'" + code + "\'");
            // }
        }

        return null;
    }

    public String toCode() {
        return this.code;
    }

    public String getSystem() {
        return "http://hl7.org/fhir/measure-scoring";
    }

    public String getDefinition() {
        return this.definition;

    }

    public String getDisplay() {
        return this.display;
    }
}
