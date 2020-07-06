package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.StringType;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.fail;

public class EmpiProviderClearLinkR4Test extends BaseLinkR4Test {


	protected Practitioner myPractitioner;
	protected StringType myPractitionerId;

	@Before
	public void before() {
		super.before();
		myPractitioner = createPractitionerAndUpdateLinks(new Practitioner());
		myPractitionerId = new StringType(myPractitioner.getIdElement().getValue());
	}

	@Test
	public void testClearAllLinks() {
		assertLinkCount(2);
		myEmpiProviderR4.clearEmpiLinks(null);
		assertLinkCount(0);
		assertNoLinksExist();
	}

	private void assertNoLinksExist() {
		assertNoPatientLinksExist();
		assertNoPractitionerLinksExist();
	}

	private void assertNoPatientLinksExist() {
		assertThat(getPatientLinks(), hasSize(0));
	}

	private void assertNoPractitionerLinksExist() {
		assertThat(getPractitionerLinks(), hasSize(0));
	}

	@Test
	public void testClearPatientLinks() {
		assertLinkCount(2);
		myEmpiProviderR4.clearEmpiLinks(new StringType("patient"));
		assertLinkCount(1);

		assertNoLinksExist();
	}

	@Test
	public void testClearPractitionerLinks() {
		myEmpiProviderR4.updateLink(myPersonId, myPatientId, MATCH_RESULT, myRequestDetails);
		try {
			myEmpiProviderR4.updateLink(myPersonId, myPatientId, NO_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage(), matchesPattern("Requested resource Person/\\d+/_history/1 is not the latest version.  Latest version is Person/\\d+/_history/2"));
		}
	}

	@Test
	public void testClearInvalidTargetType() {
		myEmpiProviderR4.updateLink(myPersonId, myPatientId, MATCH_RESULT, myRequestDetails);
		Person person = myEmpiProviderR4.updateLink(myVersionlessPersonId, myPatientId, NO_MATCH_RESULT, myRequestDetails);
		assertThat(person.getLink(), hasSize(0));
	}


	@Nonnull
	protected EmpiLink getOnlyPractitionerLink() {
		return myEmpiLinkDaoSvc.findEmpiLinkByTarget(myPractitioner).get();
	}

	@Nonnull
	protected List<EmpiLink> getPractitionerLinks() {
		return myEmpiLinkDaoSvc.findEmpiLinksByTarget(myPractitioner);
	}
}
