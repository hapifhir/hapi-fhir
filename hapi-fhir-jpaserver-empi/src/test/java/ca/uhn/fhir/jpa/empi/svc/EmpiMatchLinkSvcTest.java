package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.util.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.rest.api.Constants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.in;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class EmpiMatchLinkSvcTest extends BaseEmpiR4Test {
	private static final Logger ourLog = getLogger(EmpiMatchLinkSvcTest.class);

	@Autowired
	IEmpiLinkDao myEmpiLinkDao;
	@Autowired
	IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	private EIDHelper myEidHelper;

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
		assertLinkCount(1);

		Patient patient2 = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(2);

		assertThat(patient1, is(samePersonAs(patient2)));
	}

	@Test
	public void testWhenMatchOccursOnPersonThatHasBeenManuallyNOMATCHedThatItIsBlocked() {
		Patient originalJane = createPatientAndUpdateLinks(buildJanePatient());
		IBundleProvider search = myPersonDao.search(new SearchParameterMap());
		IBaseResource janePerson = search.getResources(0,1).get(0);

		//Create a manual NO_MATCH between janePerson and unmatchedJane.
		Patient unmatchedJane= createPatient(buildJanePatient());
		myEmpiLinkSvc.updateLink(janePerson, unmatchedJane, EmpiMatchResultEnum.NO_MATCH, EmpiLinkSourceEnum.MANUAL);

		//rerun EMPI rules against unmatchedJane.
		myEmpiMatchLinkSvc.updateEmpiLinksForPatient(unmatchedJane);

		assertThat(unmatchedJane, is(not(samePersonAs(janePerson))));
		assertThat(unmatchedJane, is(not(linkedTo(originalJane))));
	}

	@Test
	public void testWhenPOSSIBLE_MATCHOccursOnPersonThatHasBeenManuallyNOMATCHedThatItIsBlocked() {
		Patient originalJane = createPatientAndUpdateLinks(buildJanePatient());
		IBundleProvider search = myPersonDao.search(new SearchParameterMap());
		IBaseResource janePerson = search.getResources(0, 1).get(0);

		Patient unmatchedPatient = createPatient(buildJanePatient());

		//This simulates an admin specifically saying that unmatchedPatient does NOT match janePerson.
		myEmpiLinkSvc.updateLink(janePerson, unmatchedPatient, EmpiMatchResultEnum.NO_MATCH, EmpiLinkSourceEnum.MANUAL);
		//TODO change this so that it will only partially match.

		//Now normally, when we run update links, it should link to janePerson. However, this manual NO_MATCH link
		//should cause a whole new Person to be created.
		myEmpiMatchLinkSvc.updateEmpiLinksForPatient(unmatchedPatient);

		assertThat(unmatchedPatient, is(not(samePersonAs(janePerson))));
		assertThat(unmatchedPatient, is(not(linkedTo(originalJane))));
	}

	@Test
	public void testWhenPatientIsCreatedWithEIDThatItPropagatesToNewPersons() {
		String sampleEID = "sample-eid";
		Patient janePatient = addExternalEID(buildJanePatient(), sampleEID);
		janePatient = createPatientAndUpdateLinks(janePatient);

		Optional<EmpiLink> empiLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(janePatient.getIdElement().getIdPartAsLong());
		assertThat(empiLink.isPresent(), is(true));

		Person person = getPersonFromEmpiLink(empiLink.get());
		Optional<CanonicalEID> externalEid = myEidHelper.getExternalEid(person);

		assertThat(externalEid.get().getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(externalEid.get().getValue(), is(equalTo(sampleEID)));
	}

	@Test
	public void testWhenPatientIsCreatedWithoutAnEIDThePersonGetsAutomaticallyAssignedOne() {
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		EmpiLink empiLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(patient.getIdElement().getIdPartAsLong()).get();

		Person person = getPersonFromEmpiLink(empiLink);
		Identifier identifierFirstRep = person.getIdentifierFirstRep();
		assertThat(identifierFirstRep.getSystem(), is(equalTo(HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(identifierFirstRep.getValue(), is(notNullValue()));
	}

	@Test
	public void testPatientAttributesAreCopiedOverWhenPersonIsCreatedFromPatient() {
		Patient patient = createPatientAndUpdateLinks(buildPatientWithNameIdAndBirthday("Gary", "GARY_ID", new Date()));;

		Optional<EmpiLink> empiLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(patient.getIdElement().getIdPartAsLong());
		Person read = getPersonFromEmpiLink(empiLink.get());

		assertThat(read.getNameFirstRep().getFamily(), is(equalTo(patient.getNameFirstRep().getFamily())));
		assertThat(read.getNameFirstRep().getGivenAsSingleString(), is(equalTo(patient.getNameFirstRep().getGivenAsSingleString())));
		assertThat(read.getBirthDateElement().toHumanDisplay(), is(equalTo(patient.getBirthDateElement().toHumanDisplay())));
		assertThat(read.getTelecomFirstRep().getValue(), is(equalTo(patient.getTelecomFirstRep().getValue())));
		assertThat(read.getPhoto().getData(), is(equalTo(patient.getPhotoFirstRep().getData())));
		assertThat(read.getGender(), is(equalTo(patient.getGender())));
	}


	@Test
	public void testPatientMatchingAnotherPatientLinksToSamePerson() {
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Patient sameJanePatient = createPatientAndUpdateLinks(buildJanePatient());
		assertThat(janePatient, is(samePersonAs(sameJanePatient)));
	}


	@Test
	public void testIncomingPatientWithEIDThatMatchesPersonWithHapiEidAddsExternalEidToPerson(){
		// Existing Person with system-assigned EID found linked from matched Patient.  incoming Patient has EID.  Replace Person system-assigned EID with Patient EID.
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());

		Person janePerson = getPersonFromResource(patient);
		Optional<CanonicalEID> hapiEid = myEidHelper.getHapiEid(janePerson);
		String foundHapiEid = hapiEid.get().getValue();

		Patient janePatient= addExternalEID(buildJanePatient(), "12345");
		createPatientAndUpdateLinks(janePatient);

		//We want to make sure the patients were linked to the same person.
		assertThat(patient, is(samePersonAs(janePatient)));

		Person person = getPersonFromResource(patient);

		List<Identifier> identifier = person.getIdentifier();

		//The collision should have kept the old identifier
		Identifier firstIdentifier = identifier.get(0);
		assertThat(firstIdentifier.getSystem(), is(equalTo(HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(firstIdentifier.getValue(), is(equalTo(foundHapiEid)));

		//The collision should have added a new identifier with the external system.
		Identifier secondIdentifier = identifier.get(1);
		assertThat(secondIdentifier.getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(secondIdentifier.getValue(), is(equalTo("12345")));
	}

	@Test
	public void testIncomingPatientWithEidMatchesAnotherPatientWithSameEIDAreLinked() {
		Patient patient1 = addExternalEID(buildJanePatient(), "uniqueid");
		createPatientAndUpdateLinks(patient1);

		Patient patient2 = addExternalEID(buildPaulPatient(), "uniqueid");
		createPatientAndUpdateLinks(patient2);

		assertThat(patient1, is(samePersonAs(patient2)));
	}

	@Test
	public void testDuplicatePersonLinkIsCreatedWhenAnIncomingPatientArrivesWithEIDThatMatchesAnotherEIDPatient() {

		Patient patient1 = addExternalEID(buildJanePatient(), "eid-1");
		patient1 = createPatientAndUpdateLinks(patient1);

		Patient patient2 = addExternalEID(buildJanePatient(), "eid-2");
		patient2 = createPatientAndUpdateLinks(patient2);

		List<EmpiLink> possibleDuplicates = myEmpiLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates, hasSize(1));

		Person person1 = getPersonFromResource(patient1);
		Person person2 = getPersonFromResource(patient2);
		List<Long> duplicatePids = Arrays.asList(myResourceTableHelper.getPidOrNull(person1), myResourceTableHelper.getPidOrNull(person2));

		assertThat(possibleDuplicates.get(0).getPersonPid(), is(in(duplicatePids)));
		assertThat(possibleDuplicates.get(0).getTargetPid(), is(in(duplicatePids)));
	}

	@Test
	public void testPatientWithNoEmpiTagIsNotMatched() {
		// Patient with "no-empi" tag is not matched
		Patient janePatient = buildJanePatient();
		janePatient.getMeta().addTag(SYSTEM_EMPI_MANAGED, CODE_NO_EMPI_MANAGED, "Don't EMPI on me!");
		createPatientAndUpdateLinks(janePatient);
		assertLinkCount(0);
	}

}
