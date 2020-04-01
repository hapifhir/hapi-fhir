package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.api.IEmpiLinkSvc;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class EmpiLinkSvcTest extends BaseEmpiR4Test {
	@Autowired
	IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	@After
	public void after() {
		myExpungeEverythingService.expungeEverythingByType(EmpiLink.class);
		super.after();
	}
	@Test
	public void compareEmptyPatients() {
		Patient patient = new Patient();
		patient.setId("Patient/1");
		EmpiMatchResultEnum result = myEmpiResourceComparatorSvc.getMatchResult(patient, patient);
		assertEquals(EmpiMatchResultEnum.NO_MATCH, result);
	}

	@Test
	public void testCreateRemoveLink() {
		Person person = createPerson();
		IdType personId = person.getIdElement().toUnqualifiedVersionless();
		assertEquals(0, person.getLink().size());
		Patient patient = createPatient();

		{
			myEmpiLinkSvc.updateLink(person, patient, EmpiMatchResultEnum.POSSIBLE_MATCH, EmpiLinkSourceEnum.AUTO);
			assertLinkCount(1);
			Person newPerson = myPersonDao.read(personId);
			assertEquals(1, newPerson.getLink().size());
		}

		{
			myEmpiLinkSvc.updateLink(person, patient, EmpiMatchResultEnum.NO_MATCH, EmpiLinkSourceEnum.MANUAL);
			assertLinkCount(1);
			Person newPerson = myPersonDao.read(personId);
			assertEquals(0, newPerson.getLink().size());
		}
	}

	// FIXME EMPI test if a link is MANUAL, it cannot be changed by the system; only by a user operation


}
