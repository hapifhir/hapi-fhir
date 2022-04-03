package ca.uhn.fhir.cql.common.evaluation;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import java.util.HashMap;
import java.util.Map;

public enum MeasurePopulationType {
    INITIALPOPULATION("initial-population", "Initial Population",
            "The initial population refers to all patients or events to be evaluated by a quality measure involving patients who share a common set of specified characterstics. All patients or events counted (for example, as numerator, as denominator) are drawn from the initial population"),

    NUMERATOR("numerator", "Numerator",
            "\tThe upper portion of a fraction used to calculate a rate, proportion, or ratio. Also called the measure focus, it is the target process, condition, event, or outcome. Numerator criteria are the processes or outcomes expected for each patient, or event defined in the denominator. A numerator statement describes the clinical action that satisfies the conditions of the measure"),

    NUMERATOREXCLUSION("numerator-exclusion", "Numerator Exclusion",
            "Numerator exclusion criteria define patients or events to be removed from the numerator. Numerator exclusions are used in proportion and ratio measures to help narrow the numerator (for inverted measures)"),

    DENOMINATOR("denominator", "Denominator",
            "The lower portion of a fraction used to calculate a rate, proportion, or ratio. The denominator can be the same as the initial population, or a subset of the initial population to further constrain the population for the purpose of the measure"),

    DENOMINATOREXCLUSION("denominator-exclusion", "Denominator Exclusion",
            "Denominator exclusion criteria define patients or events that should be removed from the denominator before determining if numerator criteria are met. Denominator exclusions are used in proportion and ratio measures to help narrow the denominator. For example, patients with bilateral lower extremity amputations would be listed as a denominator exclusion for a measure requiring foot exams"),

    DENOMINATOREXCEPTION("denominator-exception", "Denominator Exception",
            "Denominator exceptions are conditions that should remove a patient or event from the denominator of a measure only if the numerator criteria are not met. Denominator exception allows for adjustment of the calculated score for those providers with higher risk populations. Denominator exception criteria are only used in proportion measures"),

    MEASUREPOPULATION("measure-population", "Measure Population",
            "Measure population criteria define the patients or events for which the individual observation for the measure should be taken. Measure populations are used for continuous variable measures rather than numerator and denominator criteria"),

    MEASUREPOPULATIONEXCLUSION("measure-population-exclusion", "Measure Population Exclusion",
            "Measure population criteria define the patients or events that should be removed from the measure population before determining the outcome of one or more continuous variables defined for the measure observation. Measure population exclusion criteria are used within continuous variable measures to help narrow the measure population"),

    MEASUREOBSERVATION("measure-observation", "Measure Observation",
            "Defines the individual observation to be performed for each patient or event in the measure population. Measure observations for each case in the population are aggregated to determine the overall measure score for the population");

    private String code;
    private String display;
    private String definition;

    MeasurePopulationType(String code, String display, String definition) {
        this.code = code;
        this.display = display;
        this.definition = definition;
    }

    private static final Map<String, MeasurePopulationType> lookup = new HashMap<>();

    static {
        for (MeasurePopulationType mpt : MeasurePopulationType.values()) {
            lookup.put(mpt.toCode(), mpt);
        }
    }

    // This method can be used for reverse lookup purpose
    public static MeasurePopulationType fromCode(String code) {
        if (code != null && !code.isEmpty()) {
            if (lookup.containsKey(code)) {
                return lookup.get(code);
            }
            // } else if (Configuration.isAcceptInvalidEnums()) {
            //     return null;
            // } else {
            //     // throw new FHIRException(Msg.code(1655) + "Unknown MeasureScoring code \'" + code + "\'");
            // }
        }

        return null;
    }

    public String getSystem() {
        return "http://hl7.org/fhir/measure-population";
    }

    public String toCode() {
        return this.code;
    }

    public String getDisplay() {
        return this.display;
    }

    public String getDefinition() {
        return this.definition;
    }
}
