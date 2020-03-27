package ca.uhn.fhir.jpa.empi.interceptor;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
		person.addLink().setTarget(new Reference(patientId));
		try {
			myPersonDao.create(person);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("Cannot modify Person links when EMPI is enabled.", e.getMessage());
		}

		person.setId("TEST_ID");
		try {
			myPersonDao.update(person);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("Cannot modify Person links when EMPI is enabled.", e.getMessage());
		}
	}

	// FIXME EMPI add tests to check that modifying existing person links is not allowed (must use EMPI REST operations to do this)
	// FIXME EMPI Question: what to do about pre-existing Person records?
	// FIXME EMPI Person records managed by EMPI should all share the same extension.  (I believe cdr EMPI already does this.)
}
