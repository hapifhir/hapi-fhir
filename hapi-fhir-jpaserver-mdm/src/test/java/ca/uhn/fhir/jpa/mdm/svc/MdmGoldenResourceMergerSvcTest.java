package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMState;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.interceptor.IMdmStorageInterceptor;
import ca.uhn.fhir.mdm.model.MdmMergeGoldenResourcesParams;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.jpa.mdm.matcher.GoldenResourceMatchingAssert;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
	private Patient myTargetPatient1;
	private Patient myTargetPatient2;

	@BeforeEach
	public void before() {
		myFromGoldenPatient = createGoldenPatient();
		myToGoldenPatient = createGoldenPatient();

		myTargetPatient1 = createPatient();
		myTargetPatient2 = createPatient();

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
		assertThat(getAllGoldenPatients()).hasSize(2);
		assertThat(getAllRedirectedGoldenPatients()).isEmpty();

		Patient mergedGoldenPatient = mergeGoldenPatients();

		assertEquals(myToGoldenPatient.getIdElement(), mergedGoldenPatient.getIdElement());
		GoldenResourceMatchingAssert.assertThat(mergedGoldenPatient, myIdHelperService, myMdmLinkDaoSvc).is_MATCH_to(mergedGoldenPatient);
		assertThat(getAllGoldenPatients()).hasSize(1);
		assertThat(getAllRedirectedGoldenPatients()).hasSize(1);
	}

	private Patient mergeGoldenPatients() {
		return mergeGoldenPatientsFlip(false);
	}

	private Patient mergeGoldenPatientsFlip(boolean theFlipToAndFromGoldenResources) {
		assertEquals(0, redirectLinkCount());
		Patient from = theFlipToAndFromGoldenResources ? myToGoldenPatient : myFromGoldenPatient;
		Patient to = theFlipToAndFromGoldenResources ? myFromGoldenPatient : myToGoldenPatient;

		MdmMergeGoldenResourcesParams params = new MdmMergeGoldenResourcesParams();
		params.setFromGoldenResource(from);
		params.setToGoldenResource(to);
		params.setMdmTransactionContext(createMdmContext());
		params.setRequestDetails(new SystemRequestDetails());

		Patient retval = (Patient) myGoldenResourceMergerSvc.mergeGoldenResources(
			params
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

	@Test
	public void PG1DuplicatesPG2_mergePG2toPG1_PG2RedirectsToPG1() {
		// setup
		String inputState = """
				GP1, AUTO, POSSIBLE_DUPLICATE, GP2
			""";
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState(inputState);
		myMdmLinkHelper.setup(state);

		// test
		mergeGoldenResources(
			state.getParameter("GP2"),
			state.getParameter("GP1")
		);

		// verify
		String outputState =
			"""
					GP2, MANUAL, REDIRECT, GP1
				""";
		state.setOutputState(outputState);
		myMdmLinkHelper.validateResults(state);
	}

	@Test
	public void GP1DuplicatesGP2_mergeGP1toGP2_GP1RedirectsToGP2() {
		// setup
		String inputState = """
				GP1, AUTO, POSSIBLE_DUPLICATE, GP2
			""";
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState(inputState);
		myMdmLinkHelper.setup(state);

		// test
		mergeGoldenResources(
			state.getParameter("GP1"),
			state.getParameter("GP2")
		);

		// verify
		String outputState =
			"""
					GP1, MANUAL, REDIRECT, GP2
				""";
		state.setOutputState(outputState);
		myMdmLinkHelper.validateResults(state);
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

		MdmMergeGoldenResourcesParams params = new MdmMergeGoldenResourcesParams();
		params.setFromGoldenResource(myFromGoldenPatient);
		params.setManuallyMergedResource(manuallyMergedPatient);
		params.setToGoldenResource(myToGoldenPatient);
		params.setMdmTransactionContext(ctx);
		params.setRequestDetails(new SystemRequestDetails());
		Patient mergedSourcePatient = (Patient) myGoldenResourceMergerSvc
			.mergeGoldenResources(params);

		HumanName returnedName = mergedSourcePatient.getNameFirstRep();
		assertEquals("TestGiven TestFamily", returnedName.getNameAsSingleString());
		assertEquals(POSTAL_CODE, mergedSourcePatient.getAddressFirstRep().getPostalCode());
	}

	@Test
	public void fromLinkToNoLink() {
		createMdmLink(myFromGoldenPatient, myTargetPatient1);

		Patient mergedGoldenPatient = mergeGoldenPatients();
		List<MdmLink> links = getNonRedirectLinksByGoldenResource(mergedGoldenPatient);
		assertThat(links).hasSize(1);
		GoldenResourceMatchingAssert.assertThat(mergedGoldenPatient, myIdHelperService, myMdmLinkDaoSvc).is_POSSIBLE_MATCH_to(myTargetPatient1);
	}

	@Test
	public void fromNoLinkToLink() {
		createMdmLink(myToGoldenPatient, myTargetPatient1);

		Patient mergedSourcePatient = mergeGoldenPatients();
		List<MdmLink> links = getNonRedirectLinksByGoldenResource(mergedSourcePatient);
		assertThat(links).hasSize(1);
		GoldenResourceMatchingAssert.assertThat(mergedSourcePatient, myIdHelperService, myMdmLinkDaoSvc).is_POSSIBLE_MATCH_to(myTargetPatient1);
	}

	private Patient mergeGoldenResources(Patient theFrom, Patient theTo) {
		MdmMergeGoldenResourcesParams params = new MdmMergeGoldenResourcesParams();
		params.setFromGoldenResource(theFrom);
		params.setToGoldenResource(theTo);
		params.setMdmTransactionContext(createMdmContext());
		params.setRequestDetails(new SystemRequestDetails());
		Patient retval = (Patient) myGoldenResourceMergerSvc.mergeGoldenResources(
			params
		);
		assertEquals(1, redirectLinkCount());
		return retval;
	}

	@Test
	public void fromManualLinkOverridesAutoToLink() {
		// setup
		String inputState = """
				GP2, MANUAL, MATCH, P1
			 	GP1, AUTO, POSSIBLE_MATCH, P1   
			""";
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState(inputState);

		myMdmLinkHelper.setup(state);

		// test
		mergeGoldenResources(
			state.getParameter("GP2"), // from
			state.getParameter("GP1") // to
		);

		// verify
		String outputState = """
				GP1, MANUAL, MATCH, P1
			   GP2, MANUAL, REDIRECT, GP1
			""";
		state.setOutputState(outputState);
		myMdmLinkHelper.validateResults(state);
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
		assertThat(links).hasSize(1);
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
		assertThat(links).hasSize(1);
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

		assertResourceHasLinkCount(myToGoldenPatient, 2);
		assertResourceHasLinkCount(myFromGoldenPatient, 1);
		// TODO ENSURE PROPER LINK TYPES
		assertEquals(3, myMdmLinkDao.count());
	}

	@Test
	public void from123To1() {
		// init
		String inputState = """
				  GP1, AUTO, POSSIBLE_MATCH, P1
				  GP1, AUTO, POSSIBLE_MATCH, P2
				  GP1, AUTO, POSSIBLE_MATCH, P3
				  GP2, AUTO, POSSIBLE_MATCH, P1
			""";
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState(inputState);

		myMdmLinkHelper.setup(state);

		// test
		mergeGoldenResources(
			state.getParameter("GP1"), // from
			state.getParameter("GP2") // to
		);
		myMdmLinkHelper.logMdmLinks();

		// validate
		String outputState = """
					GP1, MANUAL, REDIRECT, GP2
					GP2, AUTO, POSSIBLE_MATCH, P1
					GP2, AUTO, POSSIBLE_MATCH, P2
					GP2, AUTO, POSSIBLE_MATCH, P3
			""";
		state.setOutputState(outputState);
		myMdmLinkHelper.validateResults(state);
	}

	private void assertResourceHasLinkCount(IBaseResource theResource, int theCount) {
		List<? extends IMdmLink> links = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theResource);
		assertThat(links).hasSize(theCount);
	}

	@Test
	public void from1To123() {
		// setup
		String inputState = """
				 GP1, AUTO, POSSIBLE_MATCH, P1
				 GP2, AUTO, POSSIBLE_MATCH, P1
				 GP2, AUTO, POSSIBLE_MATCH, P2
				 GP2, AUTO, POSSIBLE_MATCH, P3
			""";
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState(inputState);
		myMdmLinkHelper.setup(state);

		// test
		mergeGoldenResources(
			state.getParameter("GP1"),
			state.getParameter("GP2")
		);

		myMdmLinkHelper.logMdmLinks();

		// validate
		String outputState = """
				 GP1, MANUAL, REDIRECT, GP2
				 GP2, AUTO, POSSIBLE_MATCH, P1
				 GP2, AUTO, POSSIBLE_MATCH, P2
				 GP2, AUTO, POSSIBLE_MATCH, P3
			""";
		state.setOutputState(outputState);
		myMdmLinkHelper.validateResults(state);
	}

	@Test
	public void from123To123() {
		// setup
		String inputState = """
				 GP1, AUTO, POSSIBLE_MATCH, P1
			  	 GP1, AUTO, POSSIBLE_MATCH, P2
			  	 GP1, AUTO, POSSIBLE_MATCH, P3
			  	 GP2, AUTO, POSSIBLE_MATCH, P1
			  	 GP2, AUTO, POSSIBLE_MATCH, P2
			  	 GP2, AUTO, POSSIBLE_MATCH, P3
			""";
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState(inputState);
		myMdmLinkHelper.setup(state);

		// test
		mergeGoldenResources(state.getParameter("GP1"), state.getParameter("GP2"));

		// verify
		String outputState = """
				 GP1, MANUAL, REDIRECT, GP2
				 GP2, AUTO, POSSIBLE_MATCH, P1
				 GP2, AUTO, POSSIBLE_MATCH, P2
				 GP2, AUTO, POSSIBLE_MATCH, P3
			""";
		state.setOutputState(outputState);
		myMdmLinkHelper.validateResults(state);
	}

	@Test
	public void from12To23() {
		// setup
		String inputState = """
				 GP1, AUTO, POSSIBLE_MATCH, P1
				 GP1, AUTO, POSSIBLE_MATCH, P2
				 GP2, AUTO, POSSIBLE_MATCH, P2
				 GP2, AUTO, POSSIBLE_MATCH, P3
			""";
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState(inputState);

		myMdmLinkHelper.setup(state);

		// test
		mergeGoldenResources(
			state.getParameter("GP1"), // from
			state.getParameter("GP2") // to
		);
		myMdmLinkHelper.logMdmLinks();

		// validate
		String outputState = """
				GP1, MANUAL, REDIRECT, GP2
			  	GP2, AUTO, POSSIBLE_MATCH, P1
			  	GP2, AUTO, POSSIBLE_MATCH, P2
			  	GP2, AUTO, POSSIBLE_MATCH, P3
			""";
		state.setOutputState(outputState);
		myMdmLinkHelper.validateResults(state);
	}

	@Test
	public void testMergeNames() {
		myFromGoldenPatient.addName().addGiven("Jim");
		myFromGoldenPatient.getNameFirstRep().addGiven("George");
		assertThat(myFromGoldenPatient.getName()).hasSize(1);
		assertThat(myFromGoldenPatient.getName().get(0).getGiven()).hasSize(2);

		myToGoldenPatient.addName().addGiven("Jeff");
		myToGoldenPatient.getNameFirstRep().addGiven("George");
		assertThat(myToGoldenPatient.getName()).hasSize(1);
		assertThat(myToGoldenPatient.getName().get(0).getGiven()).hasSize(2);

		Patient mergedSourcePatient = mergeGoldenPatients();
		assertThat(mergedSourcePatient.getName()).hasSize(2);
		assertThat(mergedSourcePatient.getName().get(0).getGiven()).hasSize(2);
		assertThat(mergedSourcePatient.getName().get(1).getGiven()).hasSize(2);

		assertEquals("Jeff George", mergedSourcePatient.getName().get(0).getNameAsSingleString());
		assertEquals("Jim George", mergedSourcePatient.getName().get(1).getNameAsSingleString());
	}

	@Test
	public void testMergeNamesAllSame() {
		myFromGoldenPatient.addName().addGiven("Jim");
		myFromGoldenPatient.getNameFirstRep().addGiven("George");
		assertThat(myFromGoldenPatient.getName()).hasSize(1);
		assertThat(myFromGoldenPatient.getName().get(0).getGiven()).hasSize(2);

		myToGoldenPatient.addName().addGiven("Jim");
		myToGoldenPatient.getNameFirstRep().addGiven("George");
		assertThat(myToGoldenPatient.getName()).hasSize(1);
		assertThat(myToGoldenPatient.getName().get(0).getGiven()).hasSize(2);

		mergeGoldenPatients();
		assertThat(myToGoldenPatient.getName()).hasSize(1);
		assertThat(myToGoldenPatient.getName().get(0).getGiven()).hasSize(2);

		assertEquals("Jim George", myToGoldenPatient.getName().get(0).getNameAsSingleString());
	}

	@Test
	public void testMergeIdentifiers() {
		myFromGoldenPatient.addIdentifier().setValue("aaa").setSystem("SYSTEM1");
		myFromGoldenPatient.addIdentifier().setValue("bbb").setSystem("SYSTEM1");
		myFromGoldenPatient.addIdentifier().setValue("ccc").setSystem("SYSTEM2");
		assertThat(myFromGoldenPatient.getIdentifier()).hasSize(3);

		myToGoldenPatient.addIdentifier().setValue("aaa").setSystem("SYSTEM1");
		myToGoldenPatient.addIdentifier().setValue("ccc").setSystem("SYSTEM1");
		assertThat(myToGoldenPatient.getIdentifier()).hasSize(2);

		mergeGoldenPatients();

		assertThat(myToGoldenPatient.getIdentifier()).hasSize(4);

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

}
