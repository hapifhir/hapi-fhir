package ca.uhn.fhir.jpa.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.convertor.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConvertorService {

    /**
     * Convert DSTU2 json string object to DSTU3 {@link MedicationAdministration}
     *
     * @param body
     * @return
     * @throws IOException
     * @throws FHIRException
     */
    public static MedicationAdministration convertMedicationAdministration(String body) throws IOException, FHIRException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };

        MedicationAdministration medicationAdministration = new MedicationAdministration();

        String key;
        Object reference;
        HashMap<String, Object> map = mapper.readValue(body, typeRef);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            reference = entry.getValue();
            key = entry.getKey();

            if (key.equals("patient")) {
                LinkedHashMap<String, String> referenceMap = castReference(key, reference, LinkedHashMap.class);
                String value = referenceMap.get("reference");

                Reference patientReference = new Reference();
                patientReference.setReference(value);

                medicationAdministration.setSubject(patientReference);
            } else if (key.equals("effectiveTimeDateTime")) {
                String referenceString = castReference(key, reference, String.class);

                DateTimeType date = new DateTimeType();
                date.setValueAsString(referenceString);

                medicationAdministration.setEffective(date);
            } else if (key.equals("device")) {
                ArrayList<Object> referenceList = castReference(key, reference, ArrayList.class);
                for (Object referenceIn : referenceList) {
                    LinkedHashMap<String, String> referenceInMap = castReference(key, referenceIn, LinkedHashMap.class);

                    Reference deviceReference = medicationAdministration.addDevice();
                    deviceReference.setReference(referenceInMap.get("reference"));
                }
            } else if (key.equals("status")) {
                String referenceString = castReference(key, reference, String.class);
                medicationAdministration.setStatus(MedicationAdministration.MedicationAdministrationStatus.fromCode(referenceString));
            } else if (key.equals("medicationCodeableConcept")) {
                LinkedHashMap<String, Object> referenceMap = castReference(key, reference, LinkedHashMap.class);
                ArrayList<Object> referenceList = castReference(key, referenceMap.get("coding"), ArrayList.class);

                for (Object referenceIn : referenceList) {
                    LinkedHashMap<String, String> referenceInMap = castReference(key, referenceIn, LinkedHashMap.class);

                    CodeableConcept codeableConcept = new CodeableConcept();
                    Coding coding = codeableConcept.addCoding();
                    coding.setSystem(referenceInMap.get("system"));
                    coding.setCode(referenceInMap.get("code"));
                    coding.setDisplay(referenceInMap.get("display"));

                    medicationAdministration.setMedication(codeableConcept);
                }
            } else if (key.equals("note")) {
                String referenceString = castReference(key, reference, String.class);

                Annotation annotation = medicationAdministration.addNote();
                annotation.setText(referenceString);
            } else {
                System.out.println("skipping field: " + key);
            }
        }

        return medicationAdministration;
    }

    /**
     * Cast to specific class type. Fire an exception if it is a different type
     *
     * @param key
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> T castReference(String key, Object value, Class<T> clazz) {
        if (clazz.isAssignableFrom(value.getClass())) {
            return clazz.cast(value);
        } else {
            throw new IllegalArgumentException("Not supported conversion for " + key + " field");
        }
    }
}
