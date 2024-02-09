package org.hl7.fhir.r4.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class DomainResourceR4Test {
    /**
     * Ensuring that fields defined in {@link DomainResource} and {@link Resource}
     * get copied when the {@link DomainResource#copy()} method is called
     */
    @Test
    public void testPatientCopy() {
       Patient p1 = new Patient();
       p1.setId("1001");
       p1.setText(new Narrative().setStatus(Narrative.NarrativeStatus.ADDITIONAL));

       Patient copiedPatient = p1.copy();
       String copiedPatientID = copiedPatient.getIdElement().getIdPart();
       Narrative.NarrativeStatus copiedPatientTextStatus = copiedPatient.getText().getStatus();

			assertThat(copiedPatient instanceof DomainResource).isTrue(); // Just making sure this assumption still holds up, otherwise this test isn't very useful
			assertThat(copiedPatientID).isEqualTo("1001");
			assertThat(copiedPatientTextStatus).isEqualTo(new Narrative().setStatus(Narrative.NarrativeStatus.ADDITIONAL).getStatus());
    }
}
