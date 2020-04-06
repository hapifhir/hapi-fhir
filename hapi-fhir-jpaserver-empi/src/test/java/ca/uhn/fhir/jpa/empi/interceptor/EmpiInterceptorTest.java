package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.empi.svc.ResourceTableHelper;
import ca.uhn.fhir.jpa.empi.util.EmpiHelperR4;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Reference;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static ca.uhn.fhir.rest.api.Constants.CODE_HAPI_EMPI_MANAGED;
import static ca.uhn.fhir.rest.api.Constants.SYSTEM_EMPI_MANAGED;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

public class EmpiInterceptorTest extends BaseEmpiR4Test {

	private static final Logger ourLog = getLogger(EmpiInterceptorTest.class);

	@Rule
	@Autowired
	public EmpiHelperR4 myEmpiHelper;
	@Autowired
	private ResourceTableHelper myResourceTableHelper;

	@Test
	public void testCreatePatient() throws InterruptedException {
		myEmpiHelper.createWithLatch(new Patient());
		assertLinkCount(1);
	}

	@Test
	public void testCreatePerson() throws InterruptedException {
		myPersonDao.create(new Person());
		assertLinkCount(0);
	}

	@Test
	public void testCreateUpdatePersonWithLinkForbiddenWhenEmpiEnabledAndPersonIsEmpiManaged() throws InterruptedException {
		// When EMPI is enabled, only the EMPI system is allowed to modify Person links of Persons with the EMPI-MANAGED tag.
		Patient patient = new Patient();
		IIdType patientId = myEmpiHelper.createWithLatch(new Patient()).getId().toUnqualifiedVersionless();
		patient.setId(patientId);

		//Just a small sanity check for this custom matcher
		assertThat(patient, samePersonAs(patient));

		//Person with no EMPI-Managed tag is fine.

		Person person = new Person();
		DaoMethodOutcome daoMethodOutcome = myEmpiHelper.doCreatePerson(person, true);
		assertNotNull(daoMethodOutcome.getId());


		//Creating a person with the EMPI-MANAGED tag should fail
		person.getMeta().addTag(SYSTEM_EMPI_MANAGED, CODE_HAPI_EMPI_MANAGED, "User is managed by EMPI");
		try {
			myEmpiHelper.doCreatePerson(person, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("Cannot create or modify Persons who are managed by EMPI.", e.getMessage());
		}

		//Updating a Person who was created via EMPI should fail.
		EmpiLink empiLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(myResourceTableHelper.getPidOrNull(patient)).get();
		Long personPid = empiLink.getPersonPid();
		Person empiPerson= (Person)myPersonDao.readByPid(new ResourcePersistentId(personPid));
		try {
			myEmpiHelper.doUpdatePerson(empiPerson, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("Cannot create or modify Persons who are managed by EMPI.", e.getMessage());
		}
	}

	@Test
	public void testUpdatingExistingLinksIsForbiddenViaPersonEndpoint() throws InterruptedException {
		myEmpiHelper.createWithLatch(new Patient());
		assertLinkCount(1);
	}

	@Test
	public void testPersonRecordsManagedByEmpiAllShareSameTag() throws InterruptedException {
		DaoMethodOutcome janePatient = myEmpiHelper.createWithLatch(buildJanePatient());
		DaoMethodOutcome paulPatient = myEmpiHelper.createWithLatch(buildPaulPatient());

		EmpiLink janeLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(myResourceTableHelper.getPidOrNull(janePatient.getResource())).get();
		EmpiLink paulLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(myResourceTableHelper.getPidOrNull(paulPatient.getResource())).get();

		boolean janeHasTag = janeLink.getPerson().getTags().stream().anyMatch(tag -> tag.getTag().getSystem().equalsIgnoreCase("EMPI-MANAGED"));
		boolean paulHasTag = paulLink.getPerson().getTags().stream().anyMatch(tag -> tag.getTag().getSystem().equalsIgnoreCase("EMPI-MANAGED"));

		assertThat(janeHasTag, is(true));
		assertThat(paulHasTag, is(true));
	}
}
