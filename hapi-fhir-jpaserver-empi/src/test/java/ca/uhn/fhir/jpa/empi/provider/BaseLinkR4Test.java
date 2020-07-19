package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;

import javax.annotation.Nonnull;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class BaseLinkR4Test extends BaseProviderR4Test {
	protected static final StringType NO_MATCH_RESULT = new StringType(EmpiMatchResultEnum.NO_MATCH.name());
	protected static final StringType MATCH_RESULT = new StringType(EmpiMatchResultEnum.MATCH.name());
	protected static final StringType POSSIBLE_MATCH_RESULT = new StringType(EmpiMatchResultEnum.POSSIBLE_MATCH.name());
	protected static final StringType POSSIBLE_DUPLICATE_RESULT = new StringType(EmpiMatchResultEnum.POSSIBLE_DUPLICATE.name());

	protected Patient myPatient;
	protected Person myPerson;
	protected EmpiLink myLink;
	protected StringType myPatientId;
	protected StringType myPersonId;
	protected StringType myVersionlessPersonId;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		myPatient = createPatientAndUpdateLinks(new Patient());
		myPatientId = new StringType(myPatient.getIdElement().getValue());

		myPerson = getPersonFromTarget(myPatient);
		myPersonId = new StringType(myPerson.getIdElement().getValue());
		myVersionlessPersonId = new StringType(myPerson.getIdElement().toVersionless().getValue());

		myLink = getOnlyPatientLink();
		// Tests require our initial link to be a POSSIBLE_MATCH
		myLink.setMatchResult(EmpiMatchResultEnum.POSSIBLE_MATCH);
		saveLink(myLink);
		assertEquals(EmpiLinkSourceEnum.AUTO, myLink.getLinkSource());
	}

	@Nonnull
	protected EmpiLink getOnlyPatientLink() {
		return myEmpiLinkDaoSvc.findEmpiLinkByTarget(myPatient).get();
	}


	@Nonnull
	protected List<EmpiLink> getPatientLinks() {
		return myEmpiLinkDaoSvc.findEmpiLinksByTarget(myPatient);
	}

}
