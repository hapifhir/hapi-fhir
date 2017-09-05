package org.hl7.fhir.r4.model;

import static org.junit.Assert.assertEquals;
import org.hl7.fhir.r4.model.Patient.PatientCommunicationComponent;
import org.junit.Test;

public class BackboneElementR4Test {
    /**
     * Ensuring that IDs of subtypes of BackboneElement get copied when
     * the {@link BackboneElement#copy()} method is called
     */
    @Test
    public void testPatientCommunicationComponentIdCopy() {
        PatientCommunicationComponent pcc1 = new PatientCommunicationComponent();
        pcc1.setId("1001");

        PatientCommunicationComponent copiedPcc = pcc1.copy();
        String copiedPccID = copiedPcc.getId();
        assertEquals("1001", copiedPccID);
    }
}
