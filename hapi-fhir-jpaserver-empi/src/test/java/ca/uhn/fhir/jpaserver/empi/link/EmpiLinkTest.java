package ca.uhn.fhir.jpaserver.empi.link;

import ca.uhn.fhir.jpaserver.api.IEmpiLinkSvc;
import ca.uhn.fhir.jpaserver.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpaserver.rules.EmpiMatchResultEnum;
import ca.uhn.fhir.jpaserver.rules.EmpiResourceComparator;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class EmpiLinkTest extends BaseEmpiR4Test {
	@Autowired
	EmpiResourceComparator myEmpiResourceComparator;

	@Autowired
	IEmpiLinkSvc myEmpiLinkSvc;

	@Test
	public void compareEmptyPatients() {
		Patient patient = new Patient();
		patient.setId("Patient/1");
		EmpiMatchResultEnum result = myEmpiResourceComparator.getMatchResult(patient, patient);
		assertEquals(EmpiMatchResultEnum.NO_MATCH, result);
	}

	@Test
	public void testCreateLink() {
		myEmpiLinkSvc.createLink(myPerson, myPatient);
	}
}
