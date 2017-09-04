package ca.uhn.fhir.model;

import static org.junit.Assert.assertEquals;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.junit.Test;

public class DomainResourceDstu2_1Test {
    /**
     * Ensuring that IDs of subtypes of DomainResource get copied when
     * the {@link org.hl7.fhir.dstu2016may.model.DomainResource#copy()} method is called
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
