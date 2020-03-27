package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.util.EmpiHelper;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Reference;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class EmpiInterceptorTest extends BaseEmpiR4Test {

	@Rule
	@Autowired
	public EmpiHelper myEmpiHelper;

	@Test
	public void testCreatePatient() throws InterruptedException {
		myEmpiHelper.createWithLatch(new Patient());
		assertLinkCount(1);
	}

	@Test
	public void testCreatePerson() throws InterruptedException {
		myEmpiHelper.createWithLatch(new Person());
		assertLinkCount(1);
	}

	@Test
	public void testCreateUpdatePersonWithLinkForbiddenWhenEmpiEnabled() {
		// When EMPI is enabled, only the EMPI system is allowed to modify Person links
		IdType patientId = createPatient(new Patient()).getIdElement().toUnqualifiedVersionless();
		Person person = new Person();

		//With no links is fine
		DaoMethodOutcome daoMethodOutcome = myPersonDao.create(person);
		assertNotNull(daoMethodOutcome.getId());

		person.addLink().setTarget(new Reference(patientId));
		try {
			myPersonDao.create(person);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("Cannot modify Person links when EMPI is enabled.", e.getMessage());
		}

		person.setId("TESTID");
		try {
			myPersonDao.update(person);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("Cannot modify Person links when EMPI is enabled.", e.getMessage());
		}
	}

	@Test
	public void testUpdatingExistingLinksIsForbiddenViaPersonEndpoint() {
		// FIXME EMPI add tests to check that modifying existing person links is not allowed (must use EMPI REST operations to do this)
		// When EMPI is enabled, only the EMPI system is allowed to modify Person links
		IdType patientId = createPatient(new Patient()).getIdElement().toUnqualifiedVersionless();
		assertLinkCount(1);
	}


	// FIXME EMPI Question: what to do about pre-existing Person records?
	// FIXME EMPI Person records managed by EMPI should all share the same extension.  (I believe cdr EMPI already does this.)
}
