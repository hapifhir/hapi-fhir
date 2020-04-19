package org.hl7.fhir.dstu3.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent;
import org.junit.jupiter.api.Test;

public class BackboneElementDstu3Test {
    /**
     * Ensuring that IDs of subtypes of BackboneElement get copied when
     * the {@link BackboneElement#copy()} method is called
     */
    @Test
    public void testPatientCommunicationComponentCopy() {
        PatientCommunicationComponent pcc1 = new PatientCommunicationComponent();
        pcc1.setId("1001");

        PatientCommunicationComponent copiedPcc = pcc1.copy();
        String copiedPccID = copiedPcc.getId();

       assertTrue(copiedPcc instanceof BackboneElement); // Just making sure this assumption still holds up, otherwise this test isn't very useful
       assertEquals("1001", copiedPccID);
    }
}
