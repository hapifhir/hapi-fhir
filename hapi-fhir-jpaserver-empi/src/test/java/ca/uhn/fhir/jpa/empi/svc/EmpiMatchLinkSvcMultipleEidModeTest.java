package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.model.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

@TestPropertySource(properties = {
	"empi.allow_multiple_eids=true"
})
public class EmpiMatchLinkSvcMultipleEidModeTest extends BaseEmpiR4Test {
	private static final Logger ourLog = getLogger(EmpiMatchLinkSvcMultipleEidModeTest.class);
	@Autowired
	IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	private EIDHelper myEidHelper;
	@Autowired
	private PersonHelper myPersonHelper;


	@Test
	public void testIncomingPatientWithEIDThatMatchesPersonWithHapiEidAddsExternalEidsToPerson() {
		// Existing Person with system-assigned EID found linked from matched Patient.  incoming Patient has EID.  Replace Person system-assigned EID with Patient EID.
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());

		Person janePerson = getPersonFromTarget(patient);
		List<CanonicalEID> hapiEid = myEidHelper.getHapiEid(janePerson);
		String foundHapiEid = hapiEid.get(0).getValue();

		Patient janePatient = buildJanePatient();
		addExternalEID(janePatient, "12345");
		addExternalEID(janePatient, "67890");
		createPatientAndUpdateLinks(janePatient);

		//We want to make sure the patients were linked to the same person.
		assertThat(patient, is(samePersonAs(janePatient)));

		Person person = getPersonFromTarget(patient);

		List<Identifier> identifier = person.getIdentifier();

		//The collision should have kept the old identifier
		Identifier firstIdentifier = identifier.get(0);
		assertThat(firstIdentifier.getSystem(), is(equalTo(EmpiConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(firstIdentifier.getValue(), is(equalTo(foundHapiEid)));

		//The collision should have added a new identifier with the external system.
		Identifier secondIdentifier = identifier.get(1);
		assertThat(secondIdentifier.getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(secondIdentifier.getValue(), is(equalTo("12345")));

		Identifier thirdIdentifier = identifier.get(1);
		assertThat(thirdIdentifier.getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(thirdIdentifier.getValue(), is(equalTo("67890")));
	}


	@Test
	public void testHavingMultipleEIDsOnIncomingPatientMatchesCorrectly() {

		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "id_1");
		addExternalEID(patient1, "id_2");
		addExternalEID(patient1, "id_3");
		addExternalEID(patient1, "id_4");
		createPatientAndUpdateLinks(patient1);

		Patient patient2 = buildPaulPatient();
		addExternalEID(patient2, "id_5");
		addExternalEID(patient2, "id_1");
		createPatientAndUpdateLinks(patient2);

		assertThat(patient1, is(samePersonAs(patient2)));
	}

	@Test
	public void testDuplicatePersonLinkIsCreatedWhenAnIncomingPatientArrivesWithEIDThatMatchesAnotherEIDPatient() {

		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "eid-1");
		addExternalEID(patient1, "eid-11");
		patient1 = createPatientAndUpdateLinks(patient1);

		Patient patient2 = buildJanePatient();
		addExternalEID(patient2, "eid-2");
		addExternalEID(patient2, "eid-22");
		patient2 = createPatientAndUpdateLinks(patient2);

		List<EmpiLink> possibleDuplicates = myEmpiLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates, hasSize(1));


		List<Long> duplicatePids = Stream.of(patient1, patient2)
			.map(this::getPersonFromTarget)
			.map(myIdHelperService::getPidOrNull)
			.collect(Collectors.toList());

		//The two Persons related to the patients should both show up in the only existing POSSIBLE_DUPLICATE EmpiLink.
		EmpiLink empiLink = possibleDuplicates.get(0);
		assertThat(empiLink.getPersonPid(), is(in(duplicatePids)));
		assertThat(empiLink.getTargetPid(), is(in(duplicatePids)));
	}

	@Test
	public void testWhenPatientEidUpdateWouldCauseALinkChangeThatDuplicatePersonIsCreatedInstead() {

		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "eid-1");
		addExternalEID(patient1, "eid-11");
		patient1 = createPatientAndUpdateLinks(patient1);

		Patient patient2 = buildPaulPatient();
		addExternalEID(patient2, "eid-2");
		addExternalEID(patient2, "eid-22");
		patient2 = createPatientAndUpdateLinks(patient2);

		Patient patient3 = buildPaulPatient();
		addExternalEID(patient3, "eid-22");

		//Now, Patient 2 and 3 are linked, and the person has 2 eids.
		assertThat(patient2, is(samePersonAs(patient3)));
		//Now lets change one of the EIDs on an incoming patient to one that matches our original patient.
		//This should create a situation in which the incoming EIDs are matched to _two_ unique patients. In this case, we want to s
		patient2.getIdentifier().clear();
		addExternalEID(patient2, "eid-11");
		addExternalEID(patient2, "eid-22");
		patient2 = updatePatientAndUpdateLinks(patient2);
		assertThat(patient2,is(possibleMatchWith(patient1)));
		assertThat(patient2,is(possibleMatchWith(patient3)));

		List<EmpiLink> possibleDuplicates = myEmpiLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates, hasSize(1));
		assertThat(patient3, is(possibleDuplicateOf(patient1)));
	}

	@Test
	public void testWhenIncomingPatientHasMultipleEidsThatTheyAreAllPersistedToPerson() {

	}
}
