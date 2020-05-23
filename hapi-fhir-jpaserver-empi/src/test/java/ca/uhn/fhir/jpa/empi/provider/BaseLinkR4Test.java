package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.StringType;
import org.junit.Before;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertEquals;

public abstract class BaseLinkR4Test extends BaseProviderR4Test {
	protected Patient myPatient;
	protected Person myPerson;
	protected EmpiLink myLink;
	protected StringType myPatientId;
	protected StringType myPersonId;
	protected StringType myNoMatch;
	protected StringType myPossibleMatch;
	protected StringType myPossibleDuplicate;

	@Before
	public void before() {
		super.before();

		myPatient = createPatientAndUpdateLinks(new Patient());
		myPatientId = new StringType(myPatient.getIdElement().toUnqualifiedVersionless().getValue());

		myPerson = getPersonFromTarget(myPatient);
		myPersonId = new StringType(myPerson.getIdElement().toUnqualifiedVersionless().getValue());
		myLink = getLink();
		assertEquals(EmpiLinkSourceEnum.AUTO, myLink.getLinkSource());
		assertEquals(EmpiMatchResultEnum.MATCH, myLink.getMatchResult());

		myNoMatch = new StringType(EmpiMatchResultEnum.NO_MATCH.name());
		myPossibleMatch = new StringType(EmpiMatchResultEnum.POSSIBLE_MATCH.name());
		myPossibleDuplicate = new StringType(EmpiMatchResultEnum.POSSIBLE_DUPLICATE.name());
	}

	@Nonnull
	protected EmpiLink getLink() {
		return myEmpiLinkDaoSvc.findEmpiLinkByTarget(myPatient).get();
	}
}
