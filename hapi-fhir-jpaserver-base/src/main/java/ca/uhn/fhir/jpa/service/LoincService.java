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

    private static final String ISO_SYSTOLIC_CODE = "150021";
    private static final String ISO_DIASTOLIC_CODE = "150022";
    private static final String ISO_MEAN_CODE = "150023";

    private static final String RTMMS_SYSTEM = "https://rtmms.nist.gov";
    private static final String ISO_SYSTEM = "urn:iso:std:iso:11073:10101";

    public static void checkLoincCodes(Observation observationDstu3) throws FHIRException {
        List<Observation.ObservationComponentComponent> componentList = observationDstu3.getComponent();
        if (componentList != null && !componentList.isEmpty()) {
            for (Observation.ObservationComponentComponent component : componentList) {
                CodeableConcept codeableConcept = component.getCode();
                if (codeableConcept != null) {
                    List<Coding> codingList = codeableConcept.getCoding();
                    if (codingList != null && !codingList.isEmpty()) {

                        boolean hasLoinc = false;
                        for (Coding coding : codingList) {
                            if (LOINC_SYSTEM.equals(coding.getSystem())) {
                                hasLoinc = true;
                                break;
                            }
                        }

                        if (!hasLoinc) {
                            Coding loincCode = null;
                            for (Coding coding : codingList) {
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
                                codingList.add(loincCode);
                            }
                        }
                    }
                }

            }
        }
    }

    public static Coding getSystolicCode() {
        Coding loincCode = new Coding();
        loincCode.setSystem(LOINC_SYSTEM);
        loincCode.setCode(SYSTOLIC_CODE);
        loincCode.setDisplay("Systolic blood pressure");

        return loincCode;
    }

    public static Coding getDiastolicCode() {
        Coding loincCode = new Coding();
        loincCode.setSystem(LOINC_SYSTEM);
        loincCode.setCode(DIASTOLIC_CODE);
        loincCode.setDisplay("Diastolic blood pressure");

        return loincCode;
    }

    public static Coding getMeanCode() {
        Coding loincCode = new Coding();
        loincCode.setSystem(LOINC_SYSTEM);
        loincCode.setCode(MEAN_CODE);
        loincCode.setDisplay("Mean blood pressure");

        return loincCode;
    }
}