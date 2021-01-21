package org.hl7.fhir.r4.validation;

import java.util.HashMap;
import java.util.Map;

import org.hl7.fhir.common.hapi.validation.validator.FHIRPathResourceGeneratorR4;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Address.AddressType;
import org.hl7.fhir.r4.model.Address.AddressUse;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FHIRPathResourceGeneratorR4Test {
    
    public Map<String, String> createFhirMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Patient.name.given", "Marcel");
        mapping.put("Patient.name.family", "Parciak");
        mapping.put("Patient.gender", "male");
        mapping.put("Patient.address.where(use = 'work').city", "Göttingen");
        mapping.put("Patient.address.where(use = 'work').postalCode", "37075");
        mapping.put("Patient.address.where(use = 'billing').city", "Göttingen");
        mapping.put("Patient.address.where(use = 'billing').postalCode", "37099");

        return mapping;
    }

    public Map<String, String> createFhirMapping_2() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Patient.name.where(use = 'official').family", "Parciak");
        mapping.put("Patient.name.where(use = 'maiden').family", "ParciakMaiden");
        mapping.put("Patient.name.given", "Marcel");
        mapping.put("Patient.gender", "male");
        mapping.put("Patient.address.where(use = 'work').city", "Göttingen");
        mapping.put("Patient.address.where(use = 'work').postalCode", "37075");

        return mapping;
    }

    @Test
    public void createFhirGenerator() {
        FHIRPathResourceGeneratorR4<Patient> test = new FHIRPathResourceGeneratorR4<>();
        assertNotNull(test);
    }

    @Test
    public void createFhirGeneratorWithMapping() {
        Map<String, String> mapping = this.createFhirMapping();
        FHIRPathResourceGeneratorR4<Patient> test = new FHIRPathResourceGeneratorR4<>(mapping);
        assertNotNull(test);
    }

    @Test
    public void generatePatientResource() {
        Map<String, String> mapping = this.createFhirMapping();
        FHIRPathResourceGeneratorR4<Patient> resourceGenerator = new FHIRPathResourceGeneratorR4<>(mapping);
        Patient patient = resourceGenerator.generateResource(Patient.class);
        assertNotNull(patient);

        assertEquals(patient.getName().size(), 1);
        assertEquals(patient.getNameFirstRep().getFamily(), "Parciak");

        assertEquals(patient.getNameFirstRep().getGiven().size(), 1);
        assertEquals(patient.getNameFirstRep().getGiven().get(0).asStringValue(), "Marcel");

        // note that we have parsed a String here
        assertEquals(patient.getGender(), AdministrativeGender.MALE);

        assertEquals(patient.getAddress().size(), 2);
        for(Address address: patient.getAddress()) {
            assertEquals(address.getCity(), "Göttingen");
            if(address.getUse() == AddressUse.WORK) {
                assertEquals(address.getPostalCode(), "37075");
            } else if(address.getUse() == AddressUse.BILLING) {
                assertEquals(address.getPostalCode(), "37099");
            } else {
                // an address that has no use should not be created based on the test data
                assertTrue(false);
            }
        }
    }

    @Test
    public void generateMainzellisteDefaultPatient() {
        Map<String, String> mapping = this.createFhirMapping_2();
        FHIRPathResourceGeneratorR4<Patient> resourceGenerator = new FHIRPathResourceGeneratorR4<>(mapping);
        Patient patient = resourceGenerator.generateResource(Patient.class);
        assertNotNull(patient);

        assertEquals(patient.getName().size(), 2);
        for(HumanName name: patient.getName()) {
            assertEquals(name.getGiven().size(), 1);
            assertEquals(name.getGiven().get(0).asStringValue(), "Marcel");
            if(name.getUse() == HumanName.NameUse.OFFICIAL) {
                assertEquals(name.getFamily(), "Parciak");
            } else if (name.getUse() == HumanName.NameUse.MAIDEN) {
                assertEquals(name.getFamily(), "ParciakMaiden");
            } else {
                assertTrue(false);
            }
        }

        // note that we have parsed a String here
        assertEquals(patient.getGender(), AdministrativeGender.MALE);

        assertEquals(patient.getAddress().size(), 1);
        assertEquals(patient.getAddressFirstRep().getUse(), AddressUse.WORK);
        assertEquals(patient.getAddressFirstRep().getPostalCode(), "37075");
        assertEquals(patient.getAddressFirstRep().getCity(), "Göttingen");
    }

    @Test
    public void checkGenerationWithWhereUnequal() {
        Map<String, String> mapping = this.createFhirMapping();
        mapping.put("Patient.address.where(use = 'billing').type", "postal");
        mapping.put("Patient.address.where(use != 'billing').type", "physical");
        FHIRPathResourceGeneratorR4<Patient> resourceGenerator = new FHIRPathResourceGeneratorR4<>(mapping);
        Patient patient = resourceGenerator.generateResource(Patient.class);
        assertNotNull(patient);

        assertEquals(patient.getName().size(), 1);
        assertEquals(patient.getNameFirstRep().getFamily(), "Parciak");

        assertEquals(patient.getNameFirstRep().getGiven().size(), 1);
        assertEquals(patient.getNameFirstRep().getGiven().get(0).asStringValue(), "Marcel");

        // note that we have parsed a String here
        assertEquals(patient.getGender(), AdministrativeGender.MALE);

        assertEquals(patient.getAddress().size(), 2);
        for(Address address: patient.getAddress()) {
            assertEquals(address.getCity(), "Göttingen");
            if(address.getUse() == AddressUse.WORK) {
                assertEquals(address.getPostalCode(), "37075");
                assertEquals(address.getType(), AddressType.PHYSICAL);
            } else if(address.getUse() == AddressUse.BILLING) {
                assertEquals(address.getPostalCode(), "37099");
                assertEquals(address.getType(), AddressType.POSTAL);
            } else {
                // an address that has no use should not be created based on the test data
                assertTrue(false);
            }
        }
    }
}
