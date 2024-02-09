package org.hl7.fhir.r4.validation;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import org.hl7.fhir.common.hapi.validation.validator.FHIRPathResourceGeneratorR4;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Address.AddressType;
import org.hl7.fhir.r4.model.Address.AddressUse;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FHIRPathResourceGeneratorR4Test extends BaseValidationTestWithInlineMocks {
    
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
			assertThat(test).isNotNull();
    }

    @Test
    public void createFhirGeneratorWithMapping() {
        Map<String, String> mapping = this.createFhirMapping();
        FHIRPathResourceGeneratorR4<Patient> test = new FHIRPathResourceGeneratorR4<>(mapping);
			assertThat(test).isNotNull();
    }

    @Test
    public void generatePatientResource() {
        Map<String, String> mapping = this.createFhirMapping();
        FHIRPathResourceGeneratorR4<Patient> resourceGenerator = new FHIRPathResourceGeneratorR4<>(mapping);
        Patient patient = resourceGenerator.generateResource(Patient.class);
			assertThat(patient).isNotNull();

			assertThat(1).isEqualTo(patient.getName().size());
			assertThat("Parciak").isEqualTo(patient.getNameFirstRep().getFamily());

			assertThat(1).isEqualTo(patient.getNameFirstRep().getGiven().size());
			assertThat("Marcel").isEqualTo(patient.getNameFirstRep().getGiven().get(0).asStringValue());

			// note that we have parsed a String here
			assertThat(AdministrativeGender.MALE).isEqualTo(patient.getGender());

			assertThat(2).isEqualTo(patient.getAddress().size());
        for(Address address: patient.getAddress()) {
					assertThat("Göttingen").isEqualTo(address.getCity());
            if(address.getUse() == AddressUse.WORK) {
							assertThat("37075").isEqualTo(address.getPostalCode());
            } else if(address.getUse() == AddressUse.BILLING) {
							assertThat("37099").isEqualTo(address.getPostalCode());
            } else {
							// an address that has no use should not be created based on the test data
							assertThat(false).isTrue();
            }
        }
    }

    @Test
    public void generateMainzellisteDefaultPatient() {
        Map<String, String> mapping = this.createFhirMapping_2();
        FHIRPathResourceGeneratorR4<Patient> resourceGenerator = new FHIRPathResourceGeneratorR4<>(mapping);
        Patient patient = resourceGenerator.generateResource(Patient.class);
			assertThat(patient).isNotNull();

			assertThat(2).isEqualTo(patient.getName().size());
        for(HumanName name: patient.getName()) {
					assertThat(1).isEqualTo(name.getGiven().size());
					assertThat("Marcel").isEqualTo(name.getGiven().get(0).asStringValue());
            if(name.getUse() == HumanName.NameUse.OFFICIAL) {
							assertThat("Parciak").isEqualTo(name.getFamily());
            } else if (name.getUse() == HumanName.NameUse.MAIDEN) {
							assertThat("ParciakMaiden").isEqualTo(name.getFamily());
            } else {
							assertThat(false).isTrue();
            }
        }

			// note that we have parsed a String here
			assertThat(AdministrativeGender.MALE).isEqualTo(patient.getGender());

			assertThat(1).isEqualTo(patient.getAddress().size());
			assertThat(AddressUse.WORK).isEqualTo(patient.getAddressFirstRep().getUse());
			assertThat("37075").isEqualTo(patient.getAddressFirstRep().getPostalCode());
			assertThat("Göttingen").isEqualTo(patient.getAddressFirstRep().getCity());
    }

    @Test
    public void checkGenerationWithWhereUnequal() {
        Map<String, String> mapping = this.createFhirMapping();
        mapping.put("Patient.address.where(use = 'billing').type", "postal");
        mapping.put("Patient.address.where(use != 'billing').type", "physical");
        FHIRPathResourceGeneratorR4<Patient> resourceGenerator = new FHIRPathResourceGeneratorR4<>(mapping);
        Patient patient = resourceGenerator.generateResource(Patient.class);
			assertThat(patient).isNotNull();

			assertThat(1).isEqualTo(patient.getName().size());
			assertThat("Parciak").isEqualTo(patient.getNameFirstRep().getFamily());

			assertThat(1).isEqualTo(patient.getNameFirstRep().getGiven().size());
			assertThat("Marcel").isEqualTo(patient.getNameFirstRep().getGiven().get(0).asStringValue());

			// note that we have parsed a String here
			assertThat(AdministrativeGender.MALE).isEqualTo(patient.getGender());

			assertThat(2).isEqualTo(patient.getAddress().size());
        for(Address address: patient.getAddress()) {
					assertThat("Göttingen").isEqualTo(address.getCity());
            if(address.getUse() == AddressUse.WORK) {
							assertThat("37075").isEqualTo(address.getPostalCode());
							assertThat(AddressType.PHYSICAL).isEqualTo(address.getType());
            } else if(address.getUse() == AddressUse.BILLING) {
							assertThat("37099").isEqualTo(address.getPostalCode());
							assertThat(AddressType.POSTAL).isEqualTo(address.getType());
            } else {
							// an address that has no use should not be created based on the test data
							assertThat(false).isTrue();
            }
        }
    }
}
