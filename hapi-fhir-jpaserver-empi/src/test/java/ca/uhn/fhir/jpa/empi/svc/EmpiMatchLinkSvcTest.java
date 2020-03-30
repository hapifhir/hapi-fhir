package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

public class EmpiMatchLinkSvcTest extends BaseEmpiR4Test {
	private static final Logger ourLog = getLogger(EmpiMatchLinkSvcTest.class);

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
		Patient patient1 = createPatientAndUpdateLinks(buildJanePatient());
		Patient patient2 = createPatientAndUpdateLinks(buildPaulPatient());

		assertLinkCount(2);
		assertThat(patient1, is(not(samePersonAs(patient2))));
	}

	@Test
	public void testAddPatientLinksToExistingPersonIfMatch() {
		Patient patient1 = createPatientAndUpdateLinks(buildJanePatient());
		Patient patient2 = createPatientAndUpdateLinks(buildJanePatient());

		assertLinkCount(2);
		assertThat(patient1, is(samePersonAs(patient2)));
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

	private Patient createPatientAndUpdateLinks(Patient thePatient) {
		//Note that since our empi-rules block on active=true, all patients must be active.
		thePatient.setActive(true);
		DaoMethodOutcome daoMethodOutcome = myPatientDao.create(thePatient);
		thePatient.setId(daoMethodOutcome.getId());
		myEmpiMatchLinkSvc.updateEmpiLinksForPatient(thePatient);
		return thePatient;
	}

}
