package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.model.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.MATCH;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.POSSIBLE_DUPLICATE;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.POSSIBLE_MATCH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.slf4j.LoggerFactory.getLogger;

@TestPropertySource(properties = {
	"empi.prevent_multiple_eids=false"
})
public class EmpiMatchLinkSvcMultipleEidModeTest extends BaseEmpiR4Test {
	private static final Logger ourLog = getLogger(EmpiMatchLinkSvcMultipleEidModeTest.class);
	@Autowired
	private EIDHelper myEidHelper;

	@BeforeEach
	public void before() {
		super.loadEmpiSearchParameters();
	}

	@Test
	public void testIncomingPatientWithEIDThatMatchesPersonWithHapiEidAddsExternalEidsToPerson() {
		// Existing Person with system-assigned EID found linked from matched Patient.  incoming Patient has EID.  Replace Person system-assigned EID with Patient EID.
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		assertLinksMatchResult(MATCH);
		assertLinksCreatedNewResource(true);
		assertLinksMatchedByEid(false);

		IAnyResource janePerson = getSourceResourceFromTargetResource(patient);
		List<CanonicalEID> hapiEid = myEidHelper.getHapiEid(janePerson);
		String foundHapiEid = hapiEid.get(0).getValue();

		Patient janePatient = buildJanePatient();
		addExternalEID(janePatient, "12345");
		addExternalEID(janePatient, "67890");
		createPatientAndUpdateLinks(janePatient);
		assertLinksMatchResult(MATCH, MATCH);
		assertLinksCreatedNewResource(true, false);
		assertLinksMatchedByEid(false, false);

		//We want to make sure the patients were linked to the same person.
		assertThat(patient, is(sameSourceResourceAs(janePatient)));

		Patient sourcePatient = (Patient)getSourceResourceFromTargetResource(patient);

		List<Identifier> identifier = sourcePatient.getIdentifier();

		//The collision should have kept the old identifier
		Identifier firstIdentifier = identifier.get(0);
		assertThat(firstIdentifier.getSystem(), is(equalTo(EmpiConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(firstIdentifier.getValue(), is(equalTo(foundHapiEid)));

		//The collision should have added a new identifier with the external system.
		Identifier secondIdentifier = identifier.get(1);
		assertThat(secondIdentifier.getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(secondIdentifier.getValue(), is(equalTo("12345")));

		Identifier thirdIdentifier = identifier.get(2);
		assertThat(thirdIdentifier.getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(thirdIdentifier.getValue(), is(equalTo("67890")));
	}

	@Test
	// Test Case #4
	public void testHavingMultipleEIDsOnIncomingPatientMatchesCorrectly() {

		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "id_1");
		addExternalEID(patient1, "id_2");
		addExternalEID(patient1, "id_3");
		addExternalEID(patient1, "id_4");
		createPatientAndUpdateLinks(patient1);
		assertLinksMatchResult(MATCH);
		assertLinksCreatedNewResource(true);
		assertLinksMatchedByEid(false);

		Patient patient2 = buildPaulPatient();
		addExternalEID(patient2, "id_5");
		addExternalEID(patient2, "id_1");
		patient2 = createPatientAndUpdateLinks(patient2);
		assertLinksMatchResult(MATCH, MATCH);
		assertLinksCreatedNewResource(true, false);
		assertLinksMatchedByEid(false, true);

		assertThat(patient1, is(sameSourceResourceAs(patient2)));

		clearExternalEIDs(patient2);
		addExternalEID(patient2, "id_6");

		//At this point, there should be 5 EIDs on the person
		Patient personFromTarget = (Patient)getSourceResourceFromTargetResource(patient2);
		assertThat(personFromTarget.getIdentifier(), hasSize(5));

		updatePatientAndUpdateLinks(patient2);
		assertLinksMatchResult(MATCH, MATCH);
		assertLinksCreatedNewResource(true, false);
		assertLinksMatchedByEid(false, true);

		assertThat(patient1, is(sameSourceResourceAs(patient2)));

		personFromTarget = (Patient) getSourceResourceFromTargetResource(patient2);
		assertThat(personFromTarget.getIdentifier(), hasSize(6));
	}

	@Test
	public void testDuplicatePersonLinkIsCreatedWhenAnIncomingPatientArrivesWithEIDThatMatchesAnotherEIDPatient() {

		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "eid-1");
		addExternalEID(patient1, "eid-11");
		patient1 = createPatientAndUpdateLinks(patient1);
		assertLinksMatchResult(MATCH);
		assertLinksCreatedNewResource(true);
		assertLinksMatchedByEid(false);

		Patient patient2 = buildJanePatient();
		addExternalEID(patient2, "eid-2");
		addExternalEID(patient2, "eid-22");
		patient2 = createPatientAndUpdateLinks(patient2);
		assertLinksMatchResult(MATCH, MATCH, POSSIBLE_DUPLICATE);
		assertLinksCreatedNewResource(true, true, false);
		assertLinksMatchedByEid(false, false, true);

		List<EmpiLink> possibleDuplicates = myEmpiLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates, hasSize(1));

		List<Long> duplicatePids = Stream.of(patient1, patient2)
			.map(this::getSourceResourceFromTargetResource)
			.map(myIdHelperService::getPidOrNull)
			.collect(Collectors.toList());

		//The two Persons related to the patients should both show up in the only existing POSSIBLE_DUPLICATE EmpiLink.
		EmpiLink empiLink = possibleDuplicates.get(0);
		assertThat(empiLink.getSourceResourcePid(), is(in(duplicatePids)));
		assertThat(empiLink.getTargetPid(), is(in(duplicatePids)));
	}

	@Test
	// Test Case #5
	public void testWhenPatientEidUpdateWouldCauseALinkChangeThatDuplicatePersonIsCreatedInstead() {
		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "eid-1");
		addExternalEID(patient1, "eid-11");
		patient1 = createPatientAndUpdateLinks(patient1);
		assertLinksMatchResult(MATCH);
		assertLinksCreatedNewResource(true);
		assertLinksMatchedByEid(false);

		Patient patient2 = buildPaulPatient();
		addExternalEID(patient2, "eid-2");
		addExternalEID(patient2, "eid-22");
		patient2 = createPatientAndUpdateLinks(patient2);
		assertLinksMatchResult(MATCH, MATCH);
		assertLinksCreatedNewResource(true, true);
		assertLinksMatchedByEid(false, false);

		Patient patient3 = buildPaulPatient();
		addExternalEID(patient3, "eid-22");
		patient3 = createPatientAndUpdateLinks(patient3);
		assertLinksMatchResult(MATCH, MATCH, MATCH);
		assertLinksCreatedNewResource(true, true, false);
		assertLinksMatchedByEid(false, false, true);

		//Now, Patient 2 and 3 are linked, and the person has 2 eids.
		assertThat(patient2, is(sameSourceResourceAs(patient3)));

		//Now lets change one of the EIDs on the second patient to one that matches our original patient.
		//This should create a situation in which the incoming EIDs are matched to _two_ different persons. In this case, we want to
		//set them all to possible_match, and set the two persons as possible duplicates.
		patient2.getIdentifier().clear();
		addExternalEID(patient2, "eid-11");
		addExternalEID(patient2, "eid-22");
		patient2 = updatePatientAndUpdateLinks(patient2);
		logAllLinks();
		assertLinksMatchResult(MATCH, POSSIBLE_MATCH, MATCH, POSSIBLE_MATCH, POSSIBLE_DUPLICATE);
		assertLinksCreatedNewResource(true, true, false, false, false);
		assertLinksMatchedByEid(false, true, true, true, true);

		assertThat(patient2, is(not(matchedToAPerson())));
		assertThat(patient2, is(possibleMatchWith(patient1)));
		assertThat(patient2, is(possibleMatchWith(patient3)));

		List<EmpiLink> possibleDuplicates = myEmpiLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates, hasSize(1));
		assertThat(patient3, is(possibleDuplicateOf(patient1)));
	}
}
