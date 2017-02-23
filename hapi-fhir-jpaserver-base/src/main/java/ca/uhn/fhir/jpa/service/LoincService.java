package ca.uhn.fhir.jpa.service;

import org.hl7.fhir.convertor.dstu3.model.CodeableConcept;
import org.hl7.fhir.convertor.dstu3.model.Coding;
import org.hl7.fhir.convertor.dstu3.model.Observation;
import org.hl7.fhir.exceptions.FHIRException;

import java.util.List;

public class LoincService {

    private static final String LOINC_SYSTEM = "http://loinc.org";
    private static final String SYSTOLIC_CODE = "8480-6";
    private static final String DIASTOLIC_CODE = "8462-4";
    private static final String MEAN_CODE = "8478-0";
    private static final String BLOOD_PRESSURE_CODE = "55284-4";

    private static final String ISO_SYSTOLIC_CODE = "150021";
    private static final String ISO_DIASTOLIC_CODE = "150022";
    private static final String ISO_MEAN_CODE = "150023";


    private static final String RTMMS_SYSTEM = "https://rtmms.nist.gov";
    private static final String ISO_SYSTEM = "urn:iso:std:iso:11073:10101";

    /**
     * Check and assign loinc codes to {@link Observation}
     *
     * @param observationDstu3
     */
    public static void checkLoincCodes(Observation observationDstu3) {
        List<Observation.ObservationComponentComponent> componentList = observationDstu3.getComponent();
        if (componentList != null && !componentList.isEmpty()) {
            for (Observation.ObservationComponentComponent component : componentList) {
                CodeableConcept codeableConcept = component.getCode();
                if (codeableConcept != null) {
                    List<Coding> componentCodingList = codeableConcept.getCoding();
                    if (componentCodingList != null && !componentCodingList.isEmpty()) {

                        boolean hasLoincSystemComponentCode = false;
                        for (Coding coding : componentCodingList) {
                            if (LOINC_SYSTEM.equals(coding.getSystem())) {
                                hasLoincSystemComponentCode = true;
                                break;
                            }
                        }

                        if (!hasLoincSystemComponentCode) {
                            Coding loincCode = null;
                            for (Coding coding : componentCodingList) {
                                if (RTMMS_SYSTEM.equals(coding.getSystem()) && ISO_SYSTOLIC_CODE.equals(coding.getCode())) {
                                    loincCode = getSystolicCode();
                                    break;
                                }

                                if (ISO_SYSTEM.equals(coding.getSystem()) && ISO_SYSTOLIC_CODE.equals(coding.getCode())) {
                                    loincCode = getSystolicCode();
                                    break;
                                }

                                if (RTMMS_SYSTEM.equals(coding.getSystem()) && ISO_DIASTOLIC_CODE.equals(coding.getCode())) {
                                    loincCode = getDiastolicCode();
                                    break;
                                }

                                if (ISO_SYSTEM.equals(coding.getSystem()) && ISO_DIASTOLIC_CODE.equals(coding.getCode())) {
                                    loincCode = getDiastolicCode();
                                    break;
                                }

                                if (RTMMS_SYSTEM.equals(coding.getSystem()) && ISO_MEAN_CODE.equals(coding.getCode())) {
                                    loincCode = getMeanCode();
                                    break;
                                }

                                if (ISO_SYSTEM.equals(coding.getSystem()) && ISO_MEAN_CODE.equals(coding.getCode())) {
                                    loincCode = getMeanCode();
                                    break;
                                }
                            }
                            if (loincCode != null) {
                                componentCodingList.add(loincCode);
                                addLoincBloodPressureObservationCodeIfDoesNotExist(observationDstu3);
                            }

                        } else {
                            //check to see if there is a Loinc code in the observation.component.code of a blood pressure type,
                            //but no Loinc code in the observation.code
                            boolean hasLoincBloodPressureComponentCode = false;
                            for (Coding coding : componentCodingList) {
                                if (LOINC_SYSTEM.equals(coding.getSystem()) && MEAN_CODE.equals(coding.getCode())) {
                                    hasLoincBloodPressureComponentCode = true;
                                    break;
                                }
                                if (LOINC_SYSTEM.equals(coding.getSystem()) && SYSTOLIC_CODE.equals(coding.getCode())) {
                                    hasLoincBloodPressureComponentCode = true;
                                    break;
                                }
                                if (LOINC_SYSTEM.equals(coding.getSystem()) && DIASTOLIC_CODE.equals(coding.getCode())) {
                                    hasLoincBloodPressureComponentCode = true;
                                    break;
                                }
                            }
                            if (hasLoincBloodPressureComponentCode) {
                                addLoincBloodPressureObservationCodeIfDoesNotExist(observationDstu3);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Assign loinc blood pressure
     *
     * @param observationDstu3
     */
    private static void addLoincBloodPressureObservationCodeIfDoesNotExist(Observation observationDstu3) {
        //add missing loinc blood pressure code
        if (!observationDstu3.hasCode()) {
            CodeableConcept code = new CodeableConcept();
            code.addCoding(getBloodPressureCode());
            observationDstu3.setCode(code);
        } else {
            //check if there is an existing code, but not a loinc blood pressure code
            CodeableConcept code = observationDstu3.getCode();
            boolean hasLoincBloodPressure = false;
            for (Coding coding : code.getCoding()) {
                if (LOINC_SYSTEM.equals(coding.getSystem())) {
                    hasLoincBloodPressure = true;
                }
            }
            if (!hasLoincBloodPressure) {
                code.addCoding(getBloodPressureCode());
            }
        }
    }

    /**
     * Get blood pressure coding
     *
     * @return
     */
    private static Coding getBloodPressureCode() {
        Coding loincCode = new Coding();
        loincCode.setSystem(LOINC_SYSTEM);
        loincCode.setCode(BLOOD_PRESSURE_CODE);
        loincCode.setDisplay("Blood Pressure");

        return loincCode;
    }

    /**
     * Get systolic blood pressure coding
     *
     * @return
     */
    private static Coding getSystolicCode() {
        Coding loincCode = new Coding();
        loincCode.setSystem(LOINC_SYSTEM);
        loincCode.setCode(SYSTOLIC_CODE);
        loincCode.setDisplay("Systolic blood pressure");

        return loincCode;
    }

    /**
     * Get diastolic blood pressure coding
     *
     * @return
     */
    private static Coding getDiastolicCode() {
        Coding loincCode = new Coding();
        loincCode.setSystem(LOINC_SYSTEM);
        loincCode.setCode(DIASTOLIC_CODE);
        loincCode.setDisplay("Diastolic blood pressure");

        return loincCode;
    }

    /**
     * Get mean blood pressure coding
     *
     * @return
     */
    private static Coding getMeanCode() {
        Coding loincCode = new Coding();
        loincCode.setSystem(LOINC_SYSTEM);
        loincCode.setCode(MEAN_CODE);
        loincCode.setDisplay("Mean blood pressure");

        return loincCode;
    }
}