package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IManualLinkUpdaterSvc;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ManualLinkUpdaterSvcTest extends BaseEmpiR4Test {
	@Autowired
	IManualLinkUpdaterSvc myManualLinkUpdaterSvc;
	private Patient myPatient;
	private Person myPerson;
	private EmpiLink myLink;

	@Before
	public void before() {
		myPatient = createPatientAndUpdateLinks(new Patient());
		myPerson = getPersonFromTarget(myPatient);
		myLink = getLink();
		assertEquals(EmpiLinkSourceEnum.AUTO, myLink.getLinkSource());
		assertEquals(EmpiMatchResultEnum.MATCH, myLink.getMatchResult());
	}

	@Test
	public void testUpdateLinkHappyPath() {
		myManualLinkUpdaterSvc.updateLink(myPerson, myPatient, EmpiMatchResultEnum.NO_MATCH);

		myLink = getLink();
		assertEquals(EmpiLinkSourceEnum.MANUAL, myLink.getLinkSource());
		assertEquals(EmpiMatchResultEnum.NO_MATCH, myLink.getMatchResult());
	}

	@NotNull
	private EmpiLink getLink() {
		return myEmpiLinkDaoSvc.findEmpiLinkByTarget(myPatient).get();
	}

	@Test
	public void testUpdateIllegalResultPM() {
		try {
			myManualLinkUpdaterSvc.updateLink(myPerson, myPatient, EmpiMatchResultEnum.POSSIBLE_MATCH);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Match Result may only be set to NO_MATCH or MATCH", e.getMessage());
		}
	}

	@Test
	public void testUpdateIllegalResultPD() {
		try {
			myManualLinkUpdaterSvc.updateLink(myPerson, myPatient, EmpiMatchResultEnum.POSSIBLE_DUPLICATE);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Match Result may only be set to NO_MATCH or MATCH", e.getMessage());
		}
	}

	@Test
	public void testUpdateIllegalFirstArg() {
		try {
			myManualLinkUpdaterSvc.updateLink(myPatient, myPatient, EmpiMatchResultEnum.NO_MATCH);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("First argument to updateLink must be a Person.  Was Patient", e.getMessage());
		}
	}

	@Test
	public void testUpdateIllegalSecondArg() {
		try {
			myManualLinkUpdaterSvc.updateLink(myPerson, myPerson, EmpiMatchResultEnum.NO_MATCH);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Second argument to updateLink must be a Patient or Practitioner.  Was Person", e.getMessage());
		}
	}

	@Test
	public void testUpdateStrangePerson() {
		try {
			myManualLinkUpdaterSvc.updateLink(new Person(), myPatient, EmpiMatchResultEnum.NO_MATCH);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Only EMPI Managed Person resources may be updated via this operation.  The Person resource provided is not tagged as managed by hapi-empi", e.getMessage());
		}
	}

	@Test
	public void testExcludedPerson() {
		Patient patient = new Patient();
		patient.getMeta().addTag().setSystem(EmpiConstants.SYSTEM_EMPI_MANAGED).setCode(EmpiConstants.CODE_NO_EMPI_MANAGED);
		try {
			myManualLinkUpdaterSvc.updateLink(myPerson, patient, EmpiMatchResultEnum.NO_MATCH);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("The target is marked with the " + EmpiConstants.CODE_NO_EMPI_MANAGED + " tag which means it may not be EMPI linked.", e.getMessage());
		}
	}


}
