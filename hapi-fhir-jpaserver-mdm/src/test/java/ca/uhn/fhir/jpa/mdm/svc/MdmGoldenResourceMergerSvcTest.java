package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.jpa.mdm.testmodels.MDMLinkResults;
import ca.uhn.fhir.jpa.mdm.testmodels.MDMState;
import ca.uhn.fhir.mdm.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.interceptor.IMdmStorageInterceptor;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmGoldenResourceMergerSvcTest extends BaseMdmR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmGoldenResourceMergerSvcTest.class);

	public static final String GIVEN_NAME = "Jenn";
	public static final String FAMILY_NAME = "Chan";
	public static final String POSTAL_CODE = "M6G 1B4";
	private static final String BAD_GIVEN_NAME = "Bob";
	private static final MdmMatchOutcome POSSIBLE_MATCH = new MdmMatchOutcome(null, null).setMatchResultEnum(MdmMatchResultEnum.POSSIBLE_MATCH);

	@Autowired
	IGoldenResourceMergerSvc myGoldenResourceMergerSvc;
	@Autowired
	MdmLinkHelper myMdmLinkHelper;
	@Autowired
	IMdmStorageInterceptor myMdmStorageInterceptor;
	@Autowired
	IInterceptorService myInterceptorService;

	private Patient myFromGoldenPatient;
	private Patient myToGoldenPatient;
	private Long myFromGoldenPatientPid;
	private Long myToGoldenPatientPid;
	private Patient myTargetPatient1;
	private Patient myTargetPatient2;
	private Patient myTargetPatient3;

	@BeforeEach
	public void before() {
		myFromGoldenPatient = createGoldenPatient();
		IdType fromSourcePatientId = myFromGoldenPatient.getIdElement().toUnqualifiedVersionless();
		myFromGoldenPatientPid = runInTransaction(()->myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), fromSourcePatientId)).getIdAsLong();
		myToGoldenPatient = createGoldenPatient();
		IdType toGoldenPatientId = myToGoldenPatient.getIdElement().toUnqualifiedVersionless();
		myToGoldenPatientPid = runInTransaction(()->myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), toGoldenPatientId)).getIdAsLong();

		myTargetPatient1 = createPatient();
		myTargetPatient2 = createPatient();
		myTargetPatient3 = createPatient();

		// Register the mdm storage interceptor after the creates so the delete hook is fired when we merge
		myInterceptorService.registerInterceptor(myMdmStorageInterceptor);
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		myInterceptorService.unregisterInterceptor(myMdmStorageInterceptor);
		super.after();
	}
	
	@Test
	public void emptyMerge() {
		assertEquals(2, getAllGoldenPatients().size());
		assertEquals(0, getAllRedirectedGoldenPatients().size());

		Patient mergedGoldenPatient = mergeGoldenPatients();

		assertEquals(myToGoldenPatient.getIdElement(), mergedGoldenPatient.getIdElement());
		assertThat(mergedGoldenPatient, is(sameGoldenResourceAs(mergedGoldenPatient)));
		assertEquals(1, getAllGoldenPatients().size());
		assertEquals(1, getAllRedirectedGoldenPatients().size());
	}

	private Patient mergeGoldenPatients() {
		return mergeGoldenPatientsFlip(false);
	}

	private Patient mergeGoldenPatientsFlip(boolean theFlipToAndFromGoldenResources) {
		assertEquals(0, redirectLinkCount());
		Patient from = theFlipToAndFromGoldenResources ? myToGoldenPatient : myFromGoldenPatient;
		Patient to = theFlipToAndFromGoldenResources ? myFromGoldenPatient : myToGoldenPatient;
		Patient retval = (Patient) myGoldenResourceMergerSvc.mergeGoldenResources(
			from,
			null,
			to,
			createMdmContext()
		);
		assertEquals(1, redirectLinkCount());
		return retval;
	}

	private int redirectLinkCount() {
		MdmLink mdmLink = new MdmLink().setMatchResult(MdmMatchResultEnum.REDIRECT);
		Example<MdmLink> example = Example.of(mdmLink);
		return myMdmLinkDao.findAll(example).size();
	}

	private MdmTransactionContext createMdmContext() {
		MdmTransactionContext mdmTransactionContext = new MdmTransactionContext(TransactionLogMessages.createFromTransactionGuid(UUID.randomUUID().toString()), MdmTransactionContext.OperationType.MERGE_GOLDEN_RESOURCES);
		mdmTransactionContext.setResourceType("Patient");
		return mdmTransactionContext;
	}


	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	public void mergeRemovesPossibleDuplicatesLink(boolean theFlipToAndFromResourcesBoolean) {
		/*
		 PG1 = myToGolden
		 PG2 = myFromGolden

		 for true:
		 // input
		 PG1, AUTO, POSSIBLE_DUPLICATE, PG2

		 // output
		 PG2, AUTO, REDIRECT, PG1
		 */

		// create the link
		MdmLink mdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink()
			.setGoldenResourcePersistenceId(new ResourcePersistentId(myToGoldenPatientPid))
			.setSourcePersistenceId(new ResourcePersistentId(myFromGoldenPatientPid))
			.setMdmSourceType("Patient")
			.setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE)
			.setLinkSource(MdmLinkSourceEnum.AUTO);

		saveLink(mdmLink);

		{
			List<MdmLink> foundLinks = myMdmLinkDao.findAll();
			assertEquals(1, foundLinks.size());
			assertEquals(MdmMatchResultEnum.POSSIBLE_DUPLICATE, foundLinks.get(0).getMatchResult());
		}

		myMdmLinkHelper.logMdmLinks();

		mergeGoldenPatientsFlip(theFlipToAndFromResourcesBoolean);

		{
			List<MdmLink> foundLinks = myMdmLinkDao.findAll();
			assertEquals(1, foundLinks.size());
			assertEquals(MdmMatchResultEnum.REDIRECT, foundLinks.get(0).getMatchResult());
		}
	}

	@Test
	public void fullFromEmptyTo() {
		populatePatient(myFromGoldenPatient);

		Patient mergedSourcePatient = mergeGoldenPatients();
		HumanName returnedName = mergedSourcePatient.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, mergedSourcePatient.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void emptyFromFullTo() {
		myFromGoldenPatient.getName().add(new HumanName().addGiven(BAD_GIVEN_NAME));
		populatePatient(myToGoldenPatient);
		print(myFromGoldenPatient);

		Patient mergedSourcePatient = mergeGoldenPatients();
		print(mergedSourcePatient);
		HumanName returnedName = mergedSourcePatient.getNameFirstRep();
		assertEquals(GIVEN_NAME, returnedName.getGivenAsSingleString());
		assertEquals(FAMILY_NAME, returnedName.getFamily());
		assertEquals(POSTAL_CODE, mergedSourcePatient.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void testManualOverride() {
		Patient manuallyMergedPatient = new Patient();
		populatePatient(manuallyMergedPatient);
		manuallyMergedPatient.getNameFirstRep().setFamily("TestFamily");
		manuallyMergedPatient.getNameFirstRep().getGiven().clear();
		manuallyMergedPatient.getNameFirstRep().addGiven("TestGiven");

		MdmTransactionContext ctx = createMdmContext();
		ctx.setRestOperation(MdmTransactionContext.OperationType.MANUAL_MERGE_GOLDEN_RESOURCES);
		Patient mergedSourcePatient = (Patient) myGoldenResourceMergerSvc
			.mergeGoldenResources(myFromGoldenPatient, manuallyMergedPatient, myToGoldenPatient, ctx);

		HumanName returnedName = mergedSourcePatient.getNameFirstRep();
		assertEquals("TestGiven TestFamily", returnedName.getNameAsSingleString());
		assertEquals(POSTAL_CODE, mergedSourcePatient.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void fromLinkToNoLink() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);

		Patient mergedGoldenPatient = mergeGoldenPatients();
		List<MdmLink> links = getNonRedirectLinksByGoldenResource(mergedGoldenPatient);
		assertEquals(1, links.size());
		assertThat(mergedGoldenPatient, is(possibleLinkedTo(myTargetPatient1)));
	}

	@Test
	public void fromNoLinkToLink() {
		createMdmLink(myToGoldenPatient, myTargetPatient1);

		Patient mergedSourcePatient = mergeGoldenPatients();
		List<MdmLink> links = getNonRedirectLinksByGoldenResource(mergedSourcePatient);
		assertEquals(1, links.size());
		assertThat(mergedSourcePatient, is(possibleLinkedTo(myTargetPatient1)));
	}

	private Patient mergeGoldenResources(Patient theFrom, Patient theTo) {
		Patient retval = (Patient) myGoldenResourceMergerSvc.mergeGoldenResources(
			theFrom,
			null,
			theTo,
			createMdmContext()
		);
		assertEquals(1, redirectLinkCount());
		return retval;
	}

	@Test
	public void fromManualLinkOverridesAutoToLink() {
		// setup
		String inputState = """
				PG2, MANUAL, MATCH, P1
			 	PG1, AUTO, POSSIBLE_MATCH, P1   
			""";
		String outputState = """
				PG1, MANUAL, MATCH, P1
			   PG1, MANUAL, REDIRECT, PG2
			""";
		MDMState<Patient> state = new MDMState<>();
		state.setInputState(inputState)
			.setOutputState(outputState) // We could include the patients by default - or let the system make them
//			.addParameter("PG1", myToGoldenPatient)
//			.addParameter("PG2", myFromGoldenPatient)
//			.addParameter("P1", myTargetPatient1)
		;

		initializeTest(state);

		// test
		mergeGoldenResources(
			state.getParameter("PG2"), // from
			state.getParameter("PG1") // to
		);

//		createMdmLink(myToGoldenPatient, myTargetPatient1);
//
//		// moves all links from from -> to
//		mergeGoldenPatients();

		// verify
		for (Map.Entry<String, Patient> entrySet : state.getParameterToValue().entrySet()) {
			Patient patient = entrySet.getValue();
			List<MdmLink> links = getAllMdmLinks(patient);
			state.addLinksForResource(patient, links.toArray(new MdmLink[0]));
		}

		validateResults(state);
	}

	private List<MdmLink> getAllMdmLinks(Patient theGoldenPatient) {
		return myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theGoldenPatient).stream()
			.map( link -> (MdmLink) link)
			.collect(Collectors.toList());
	}

	private List<MdmLink> getNonRedirectLinksByGoldenResource(Patient theGoldenPatient) {
		return myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theGoldenPatient).stream()
			.filter(link -> !link.isRedirect())
			.map( link -> (MdmLink) link)
			.collect(Collectors.toList());
	}

	@Test
	public void fromManualNoMatchLinkOverridesAutoToLink() {
		MdmLink fromLink = createMdmLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);

		saveLink(fromLink);

		createMdmLink(myToGoldenPatient, myTargetPatient1);

		mergeGoldenPatients();
		List<MdmLink> links = getNonRedirectLinksByGoldenResource(myToGoldenPatient);
		assertEquals(1, links.size());
		assertEquals(MdmLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(MdmMatchResultEnum.NO_MATCH, links.get(0).getMatchResult());
	}

	@Test
	public void fromManualAutoMatchLinkNoOverridesManualToLink() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);

		MdmLink toLink = createMdmLink(myToGoldenPatient, myTargetPatient1);
		toLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		toLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		saveLink(toLink);

		mergeGoldenPatients();
		List<MdmLink> links = getNonRedirectLinksByGoldenResource(myToGoldenPatient);
		assertEquals(1, links.size());
		assertEquals(MdmLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(MdmMatchResultEnum.NO_MATCH, links.get(0).getMatchResult());
	}

	@Test
	public void fromNoMatchMergeToManualMatchIsError() {
		MdmLink fromLink = createMdmLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		saveLink(fromLink);

		MdmLink toLink = createMdmLink(myToGoldenPatient, myTargetPatient1);
		toLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		toLink.setMatchResult(MdmMatchResultEnum.MATCH);
		saveLink(toLink);

		try {
			mergeGoldenPatients();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(752) + "A MANUAL NO_MATCH link may not be merged into a MANUAL MATCH link for the same target", e.getMessage());
		}
	}

	@Test
	public void fromMatchMergeToManualNoMatchIsError() {
		MdmLink fromLink = createMdmLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(MdmMatchResultEnum.MATCH);
		saveLink(fromLink);

		MdmLink toLink = createMdmLink(myToGoldenPatient, myTargetPatient1);
		toLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		toLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		saveLink(toLink);

		try {
			mergeGoldenPatients();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(752) + "A MANUAL MATCH link may not be merged into a MANUAL NO_MATCH link for the same target", e.getMessage());
		}
	}

	@Test
	public void fromNoMatchMergeToManualMatchDifferentPatientIsOk() {
		MdmLink fromLink = createMdmLink(myFromGoldenPatient, myTargetPatient1);
		fromLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		fromLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		saveLink(fromLink);

		MdmLink toLink = createMdmLink(myToGoldenPatient, myTargetPatient2);
		toLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		toLink.setMatchResult(MdmMatchResultEnum.MATCH);
		saveLink(toLink);

		mergeGoldenPatients();

		assertResourceHasLinkCount(myToGoldenPatient, 3);
		assertResourceHasLinkCount(myFromGoldenPatient, 0);
		// TODO ENSURE PROPER LINK TYPES
		assertEquals(3, myMdmLinkDao.count());
	}

	/*
	  // patients
	  PG1 = myFromGoldenPatient
	  PG2 = myToGoldenPatient
	  P1 = myTargetPatient1
	  P2 = myTargetPatient2
	  P3 = myTargetPatient3

	  // input
	  PG1, AUTO, POSSIBLE_MATCH, P1
	  PG1, AUTO, POSSIBLE_MATCH, P2
	  PG1, AUTO, POSSIBLE_MATCH, P3
	  PG2, AUTO, POSSIBLE_MATCH, P1

	  // output
		PG2, AUTO, REDIRECT, PG1
		PG2, AUTO, POSSIBLE_MATCH, P1
		PG2, AUTO, POSSIBLE_MATCH, P2
		PG2, AUTO, POSSIBLE_MATCH, P3
	 */
	@Test
	public void from123To1() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);
		createMdmLink(myFromGoldenPatient, myTargetPatient2);
		createMdmLink(myFromGoldenPatient, myTargetPatient3);
		createMdmLink(myToGoldenPatient, myTargetPatient1);

		mergeGoldenPatients();
		myMdmLinkHelper.logMdmLinks();

		assertThat(myToGoldenPatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertResourceHasAutoLinkCount(myToGoldenPatient, 3);
	}


	private void assertResourceHasLinkCount(IBaseResource theResource, int theCount) {
		List<? extends IMdmLink> links = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theResource);
		assertEquals(theCount, links.size());
	}

	/*
	 // patients
	 PG1 = myFromPatient
	 PG2 = myToPatient
	 P1 = myTarget1
	 P2 = myTarget2
	 P3 = myTarget3

	 // input
	 PG1, AUTO, POSSIBLE_MATCH, P1
	 PG2, AUTO, POSSIBLE_MATCH, P1
	 PG2, AUTO, POSSIBLE_MATCH, P2
	 PG2, AUTO, POSSIBLE_MATCH, P3

	 // output
	 PG2, MANUAL, REDIRECT, PG1
	 PG2, AUTO, POSSIBLE_MATCH, P1
	 PG2, AUTO, POSSIBLE_MATCH, P2
	 PG2, AUTO, POSSIBLE_MATCH, P3
	 */
	@Test
	public void from1To123() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);
		createMdmLink(myToGoldenPatient, myTargetPatient1);
		createMdmLink(myToGoldenPatient, myTargetPatient2);
		createMdmLink(myToGoldenPatient, myTargetPatient3);

		mergeGoldenPatients();
		myMdmLinkHelper.logMdmLinks();

		assertThat(myToGoldenPatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));
		assertResourceHasAutoLinkCount(myToGoldenPatient, 3);
	}

	private void assertResourceHasAutoLinkCount(Patient myToGoldenPatient, int theCount) {
		List<? extends IMdmLink> links = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(myToGoldenPatient);
		assertEquals(theCount, links.stream().filter(IMdmLink::isAuto).count());
	}

	/*
	// patients
    PG1 = myFromPatient
	 PG2 = myToPatient
	 P1 = myTarget1
	 P2 = myTarget2
	 P3 = myTarget3

	 // input
	 PG1, AUTO, POSSIBLE_MATCH, P1
	 PG1, AUTO, POSSIBLE_MATCH, P2
	 PG1, AUTO, POSSIBLE_MATCH, P3
	 PG2, AUTO, POSSIBLE_MATCH, P1
	 PG2, AUTO, POSSIBLE_MATCH, P2
	 PG2, AUTO, POSSIBLE_MATCH, P3

	 // output
	 PG2, MANUAL, REDIRECT, PG1
	 PG2, AUTO, POSSIBLE_MATCH, P1
	 PG2, AUTO, POSSIBLE_MATCH, P2
	 PG2, AUTO, POSSIBLE_MATCH, P3
	 */
	@Test
	public void from123To123() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);
		createMdmLink(myFromGoldenPatient, myTargetPatient2);
		createMdmLink(myFromGoldenPatient, myTargetPatient3);
		createMdmLink(myToGoldenPatient, myTargetPatient1);
		createMdmLink(myToGoldenPatient, myTargetPatient2);
		createMdmLink(myToGoldenPatient, myTargetPatient3);

		mergeGoldenPatients();

		assertThat(myToGoldenPatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));

		assertResourceHasAutoLinkCount(myToGoldenPatient, 3);
	}

	/*
	 PG1 = myFromGolden
	 PG2 = myToGolden
	 P1 = myTarget1
	 P2 = myTarget2
	 P3 = myTarget3

	 // input
	 PG1, AUTO, POSSIBLE_MATCH, P1
	 PG1, AUTO, POSSIBLE_MATCH, P2
	 PG2, AUTO, POSSIBLE_MATCH, P2
	 PG2, AUTO, POSSIBLE_MATCH, P3

	 // output
	 PG2, MANUAL, REDIRECT, PG1
	 PG2, AUTO, POSSIBLE_MATCH, P1
	 PG2, AUTO, POSSIBLE_MATCH, P2
	 PG2, AUTO, POSSIBLE_MATCH, P3
	 */
	@Test
	public void from12To23() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);
		createMdmLink(myFromGoldenPatient, myTargetPatient2);
		createMdmLink(myToGoldenPatient, myTargetPatient2);
		createMdmLink(myToGoldenPatient, myTargetPatient3);

		mergeGoldenPatients();
		myMdmLinkHelper.logMdmLinks();

		assertThat(myToGoldenPatient, is(possibleLinkedTo(myTargetPatient1, myTargetPatient2, myTargetPatient3)));

		assertResourceHasAutoLinkCount(myToGoldenPatient, 3);
	}

	@Test
	public void testMergeNames() {
		myFromGoldenPatient.addName().addGiven("Jim");
		myFromGoldenPatient.getNameFirstRep().addGiven("George");
		assertThat(myFromGoldenPatient.getName(), hasSize(1));
		assertThat(myFromGoldenPatient.getName().get(0).getGiven(), hasSize(2));

		myToGoldenPatient.addName().addGiven("Jeff");
		myToGoldenPatient.getNameFirstRep().addGiven("George");
		assertThat(myToGoldenPatient.getName(), hasSize(1));
		assertThat(myToGoldenPatient.getName().get(0).getGiven(), hasSize(2));

		Patient mergedSourcePatient = mergeGoldenPatients();
		assertThat(mergedSourcePatient.getName(), hasSize(2));
		assertThat(mergedSourcePatient.getName().get(0).getGiven(), hasSize(2));
		assertThat(mergedSourcePatient.getName().get(1).getGiven(), hasSize(2));

		assertThat(mergedSourcePatient.getName().get(0).getNameAsSingleString(), is("Jeff George"));
		assertThat(mergedSourcePatient.getName().get(1).getNameAsSingleString(), is("Jim George"));
	}

	@Test
	public void testMergeNamesAllSame() {
		myFromGoldenPatient.addName().addGiven("Jim");
		myFromGoldenPatient.getNameFirstRep().addGiven("George");
		assertThat(myFromGoldenPatient.getName(), hasSize(1));
		assertThat(myFromGoldenPatient.getName().get(0).getGiven(), hasSize(2));

		myToGoldenPatient.addName().addGiven("Jim");
		myToGoldenPatient.getNameFirstRep().addGiven("George");
		assertThat(myToGoldenPatient.getName(), hasSize(1));
		assertThat(myToGoldenPatient.getName().get(0).getGiven(), hasSize(2));

		mergeGoldenPatients();
		assertThat(myToGoldenPatient.getName(), hasSize(1));
		assertThat(myToGoldenPatient.getName().get(0).getGiven(), hasSize(2));

		assertThat(myToGoldenPatient.getName().get(0).getNameAsSingleString(), is("Jim George"));
	}

	@Test
	public void testMergeIdentifiers() {
		myFromGoldenPatient.addIdentifier().setValue("aaa").setSystem("SYSTEM1");
		myFromGoldenPatient.addIdentifier().setValue("bbb").setSystem("SYSTEM1");
		myFromGoldenPatient.addIdentifier().setValue("ccc").setSystem("SYSTEM2");
		assertThat(myFromGoldenPatient.getIdentifier(), hasSize(3));

		myToGoldenPatient.addIdentifier().setValue("aaa").setSystem("SYSTEM1");
		myToGoldenPatient.addIdentifier().setValue("ccc").setSystem("SYSTEM1");
		assertThat(myToGoldenPatient.getIdentifier(), hasSize(2));

		mergeGoldenPatients();

		assertThat(myToGoldenPatient.getIdentifier(), hasSize(4));

		assertTrue(myToGoldenPatient.getIdentifier().get(0).equalsDeep(new Identifier().setValue("aaa").setSystem("SYSTEM1")));
		assertTrue(myToGoldenPatient.getIdentifier().get(1).equalsDeep(new Identifier().setValue("ccc").setSystem("SYSTEM1")));
		assertTrue(myToGoldenPatient.getIdentifier().get(2).equalsDeep(new Identifier().setValue("bbb").setSystem("SYSTEM1")));
		assertTrue(myToGoldenPatient.getIdentifier().get(3).equalsDeep(new Identifier().setValue("ccc").setSystem("SYSTEM2")));
	}

	private MdmLink createMdmLink(Patient theSourcePatient, Patient theTargetPatient) {
		return (MdmLink) myMdmLinkDaoSvc.createOrUpdateLinkEntity(theSourcePatient, theTargetPatient, POSSIBLE_MATCH, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
	}

	private void populatePatient(Patient theSourcePatient) {
		theSourcePatient.addName(new HumanName().addGiven(GIVEN_NAME).setFamily(FAMILY_NAME));
		theSourcePatient.setGender(Enumerations.AdministrativeGender.FEMALE);
		theSourcePatient.setBirthDateElement(new DateType("1981-01-01"));
		Address address = new Address();
		address.addLine("622 College St");
		address.addLine("Suite 401");
		address.setDistrict("Little Italy");
		address.setCity("Toronto");
		address.setCountry("Canada");
		address.setPostalCode(POSTAL_CODE);
		theSourcePatient.setAddress(Collections.singletonList(address));
	}

	/**
	 * Creates all the initial links specified in the state object.
	 *
	 * These links will be returned in an MDMLinkResults object, in case
	 * they are needed.
	 */
	private MDMLinkResults initializeTest(MDMState<Patient> theState) {
		MDMLinkResults results = new MDMLinkResults();

		for (String inputState : theState.getParsedInputState()) {
			ourLog.info(inputState);
			String[] params = MDMState.parseState(inputState);

			Patient goldenResource = theState.getParameter(params[0]);
			if (goldenResource == null) {
				// if it doesn't exist, create it
				goldenResource = createPatientById(params[0]);
				theState.addParameter(params[0], goldenResource);
			}
			Patient targetResource = theState.getParameter(params[3]);
			if (targetResource == null) {
				// if it doesn't exist, create it
				targetResource = createPatientById(params[3]);
				theState.addParameter(params[3], targetResource);
			}
			MdmLinkSourceEnum matchSourceType = MdmLinkSourceEnum.valueOf(params[1]);
			MdmMatchResultEnum matchResultType = MdmMatchResultEnum.valueOf(params[2]);

			MdmMatchOutcome matchOutcome = new MdmMatchOutcome(
				null,
				null
			);
			matchOutcome.setMatchResultEnum(matchResultType);

			MdmLink link = (MdmLink) myMdmLinkDaoSvc.createOrUpdateLinkEntity(
				goldenResource,
				targetResource,
				matchOutcome,
				matchSourceType,
				createContextForCreate("Patient")
			);

			results.addResult(link);
		}

		return results;
	}

	private Patient createPatientById(String theId) {
		Patient patient = new Patient();
		patient.setActive(true); // all mdm patients must be active

		if (theId.length() >= 2 && theId.charAt(1) == 'G') {
			// golden resource
			MdmResourceUtil.setMdmManaged(patient);
		} // else: standard resource

		DaoMethodOutcome outcome = myPatientDao.create(patient);
		Patient outputPatient = (Patient) outcome.getResource();
		outputPatient.setId(outcome.getId());
		return outputPatient;
	}

	private void validateResults(MDMState<Patient> theState) {
		String[] outputStates = theState.getParsedOutputState();
		int totalExpectedLinks = outputStates.length;
		int totalActualLinks = 0;
		for (Patient p : theState.getActualOutcomeLinks().keys()) {
			totalActualLinks += theState.getActualOutcomeLinks().get(p).size();
		}
		assertEquals(totalExpectedLinks, totalActualLinks,
			String.format("Invalid number of links. Expected %d, Actual %d",
				totalExpectedLinks, totalActualLinks)
			);

		for (String state : outputStates) {
			ourLog.info(state);
			String[] params = MDMState.parseState(state);

			Patient leftSideResource = theState.getParameter(params[0]);
			Collection<MdmLink> links = theState.getActualOutcomeLinks().get(leftSideResource);
			assertFalse(links.isEmpty(), state);

			MdmLinkSourceEnum matchSourceType = MdmLinkSourceEnum.valueOf(params[1]);
			MdmMatchResultEnum matchResultType = MdmMatchResultEnum.valueOf(params[2]);

			Patient rightSideResource = theState.getParameter(params[3]);

			boolean foundLink = false;
			for (MdmLink link : links) {
				if (link.getGoldenResourcePid().longValue() == leftSideResource.getIdElement().getIdPartAsLong().longValue()
						&& link.getSourcePid().longValue() == rightSideResource.getIdElement().getIdPartAsLong().longValue()
						&& link.getMatchResult() == matchResultType
						&& link.getLinkSource() == matchSourceType
				) {
					foundLink = true;
					break;
				}
			}

			assertTrue(foundLink, String.format("State: %s - not found", state));
		}
	}
}
