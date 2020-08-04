package ca.uhn.fhir.model;

import org.hl7.fhir.dstu2.model.BackboneElement;
import org.hl7.fhir.dstu2.model.Patient.PatientCommunicationComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BackboneElementHL7_Dstu2Test {
    /**
     * Ensuring that IDs of subtypes of BackboneElement get copied when
     * the {@link org.hl7.fhir.instance.model.BackboneElement#copy()} method is called
     */
    @Test
    public void testPatientCommunicationComponentIdCopy() {
        PatientCommunicationComponent pcc1 = new PatientCommunicationComponent();
        pcc1.setId("1001");

        PatientCommunicationComponent copiedPcc = pcc1.copy();
        String copiedPccID = copiedPcc.getIdElement().getIdPart();

        assertTrue(copiedPcc instanceof BackboneElement); // Just making sure this assumption still holds up, otherwise this test isn't very useful
        assertEquals("1001", copiedPccID);
    }
}
