package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.helper.ResourceTableHelper;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EmpiMatchLinkSvcTest extends BaseEmpiR4Test {
	@Autowired
	private EmpiMatchLinkSvc myEmpiMatchLinkSvc;
	@Autowired
	private ResourceTableHelper myResourceTableHelper;
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	@Test
	public void testAddPatientLinksToNewPersonIfNoneFound() {
		createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(1);
	}

	@Test
	public void testAddPatientLinksToNewPersonIfNoMatch() {
		createPatientAndUpdateLinks(buildJanePatient());
		createPatientAndUpdateLinks(buildPaulPatient());

		assertLinkCount(2);
		assertDifferentPerson();
	}

	@Test
	public void testAddPatientLinksToExistingPersonIfMatch() {
		createPatientAndUpdateLinks(buildJanePatient());
		createPatientAndUpdateLinks(buildJanePatient());

		assertLinkCount(2);
		assertSamePerson();
	}

	@Test
	public void testPatientLinksToPersonIfMatch() {
		Person janePerson = buildJanePerson();
		DaoMethodOutcome outcome = myPersonDao.create(janePerson);
		Long origPersonPid = myResourceTableHelper.getPidOrNull(outcome.getResource());

		createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(1);
		List<EmpiLink> links = myEmpiLinkDao.findAll();
		EmpiLink link = links.get(0);
		Long linkedPersonPid = link.getPersonPid();
		assertEquals(EmpiMatchResultEnum.MATCH, link.getMatchResult());
		assertEquals(origPersonPid, linkedPersonPid);
	}

	@Test
	public void testMatchStatusSetPossibleIfMultiplePersonMatch() {
		Person janePerson1 = buildJanePerson();
		DaoMethodOutcome outcome1 = myPersonDao.create(janePerson1);
		Long origPersonPid1 = myResourceTableHelper.getPidOrNull(outcome1.getResource());

		Person janePerson2 = buildJanePerson();
		DaoMethodOutcome outcome2 = myPersonDao.create(janePerson2);
		Long origPersonPid2 = myResourceTableHelper.getPidOrNull(outcome2.getResource());

		createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(2);
		List<EmpiLink> links = myEmpiLinkDao.findAll();
		{
			EmpiLink link = links.get(0);
			Long linkedPersonPid = link.getPersonPid();
			assertEquals(EmpiMatchResultEnum.POSSIBLE_MATCH, link.getMatchResult());
			assertEquals(origPersonPid1, linkedPersonPid);
		}
		{
			EmpiLink link = links.get(1);
			Long linkedPersonPid = link.getPersonPid();
			assertEquals(EmpiMatchResultEnum.POSSIBLE_MATCH, link.getMatchResult());
			assertEquals(origPersonPid2, linkedPersonPid);
		}
	}

	private void createPatientAndUpdateLinks(Patient thePatient) {
		myPatientDao.create(thePatient);
		myEmpiMatchLinkSvc.updateEmpiLinksForPatient(thePatient);
	}

	private void assertDifferentPerson() {
		List<EmpiLink> links = myEmpiLinkDao.findAll();
		assertNotEquals(links.get(0).getPersonPid(), links.get(1).getPersonPid());
	}

	private void assertSamePerson() {
		List<EmpiLink> links = myEmpiLinkDao.findAll();
		assertEquals(links.get(0).getPersonPid(), links.get(1).getPersonPid());
	}
}
