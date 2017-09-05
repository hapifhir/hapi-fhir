package org.hl7.fhir.r4.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DomainResourceR4Test {
    /**
     * Ensuring that IDs of subtypes of DomainResource get copied when
     * the {@link DomainResource#copy()} method is called
     */
    @Test
    public void testPatientIdCopy() {
       Patient p1 = new Patient();
       p1.setId("1001");

       Patient copiedPatient = p1.copy();
       String copiedPatientID = copiedPatient.getIdElement().getIdPart();
       assertEquals("1001", copiedPatientID);
    }
}
