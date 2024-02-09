package ca.uhn.fhir.model;

import org.hl7.fhir.dstu2.model.DomainResource;
import org.hl7.fhir.dstu2.model.Narrative;
import org.hl7.fhir.dstu2.model.Patient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DomainResourceHL7_Dstu2Test {
    /**
     * Ensuring that fields defined in {@link org.hl7.fhir.instance.model.DomainResource} and {@link org.hl7.fhir.dstu2.model.Resource}
     * the {@link org.hl7.fhir.instance.model.DomainResource#copy()} method is called
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
