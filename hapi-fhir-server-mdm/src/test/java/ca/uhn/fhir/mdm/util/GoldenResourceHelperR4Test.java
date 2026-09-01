package ca.uhn.fhir.mdm.util;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.mdm.BaseR4Test;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

// Created by claude-opus-5
class GoldenResourceHelperR4Test extends BaseR4Test {

	private static final String MRN_SYSTEM = "http://example.com/mrn";
	private static final String NPI_SYSTEM = "http://example.com/npi";
	private static final String UNRELATED_SYSTEM = "http://example.com/unrelated";

	private EIDHelper myEidHelper;
	private GoldenResourceHelper myGoldenResourceHelper;

	@Override
	@BeforeEach
	public void before() {
		super.before();
		when(mySearchParamRetriever.getActiveSearchParam(eq("Patient"), eq("identifier"), any()))
			.thenReturn(new RuntimeSearchParam(
				null, null, "identifier", "Description", "identifier", RestSearchParameterTypeEnum.STRING,
				new HashSet<>(), new HashSet<>(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE,
				null, null, null));

		// Set preventMultipleEids explicitly rather than relying on the field default: tests below depend
		// on which branch of updateGoldenResourceExternalEidFromSourceResource is taken, and that must not
		// change silently if the default ever moves.
		configure(List.of(MRN_SYSTEM, NPI_SYSTEM), false);
	}

	/**
	 * Rebuilds the helpers under test for a given EID-system configuration. Tests that need a different
	 * configuration call this first; {@link #before()} establishes the default used by the rest.
	 */
	private void configure(List<String> theEidSystems, boolean thePreventMultipleEids) {
		MdmRulesJson rules = new MdmRulesJson();
		rules.setVersion("test version");
		rules.setMdmTypes(List.of("Patient"));
		rules.addEnterpriseEIDSystems("Patient", theEidSystems);

		MdmSettings mdmSettings = new MdmSettings(
				new MdmRuleValidator(ourFhirContext, mySearchParamRetriever, myIMatcherFactory, mySimilarityFactory))
			.setMdmRules(rules);
		mdmSettings.setPreventMultipleEids(thePreventMultipleEids);
		myEidHelper = new EIDHelper(ourFhirContext, mdmSettings);
		myGoldenResourceHelper = new GoldenResourceHelper(
			ourFhirContext, mdmSettings, myEidHelper, new MdmPartitionHelper(new MessageHelper(mdmSettings, ourFhirContext), mdmSettings));
	}

	@Test
	void createGoldenResource_incomingHasEidsFromTwoSystems_clonesBoth() {
		Patient source = new Patient();
		source.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		source.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-9"));

		Patient golden = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(
			source, new MdmTransactionContext(MdmTransactionContext.OperationType.CREATE_RESOURCE), null);

		assertThat(myEidHelper.getExternalEid(golden)).extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(MRN_SYSTEM + "|mrn-1", NPI_SYSTEM + "|npi-9");
	}

	@Test
	void createGoldenResource_incomingHasEidAndNonEidIdentifiers_clonesOnlyTheEid() {
		Patient source = new Patient();
		source.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		source.addIdentifier(new Identifier().setSystem(UNRELATED_SYSTEM).setValue("other-1"));

		Patient golden = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(
			source, new MdmTransactionContext(MdmTransactionContext.OperationType.CREATE_RESOURCE), null);

		// Exactly one identifier: the EID was carried over, the unrelated one was not, and no HAPI EID was
		// minted because the resource already had an external EID.
		assertThat(golden.getIdentifier()).extracting(Identifier::getSystem).containsExactly(MRN_SYSTEM);
	}

	@Test
	void createGoldenResource_incomingHasNoEid_generatesHapiEid() {
		Patient source = new Patient();

		Patient golden = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(
			source, new MdmTransactionContext(MdmTransactionContext.OperationType.CREATE_RESOURCE), null);

		assertThat(golden.getIdentifier()).extracting(Identifier::getSystem)
			.contains(MdmConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM);
	}

	@Test
	void overwriteExternalEids_clearsOnlyTheEidSystemsBeingOverwritten() {
		Patient golden = new Patient();
		golden.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		golden.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-9"));
		golden.addIdentifier(new Identifier().setSystem(UNRELATED_SYSTEM).setValue("other-1"));

		myGoldenResourceHelper.overwriteExternalEids(golden, List.of(new CanonicalEID(MRN_SYSTEM, "mrn-2", null)));

		// The NPI survives: replacing the MRN says nothing about the resource's NPI.
		assertThat(myEidHelper.getExternalEid(golden)).extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(MRN_SYSTEM + "|mrn-2", NPI_SYSTEM + "|npi-9");
		assertThat(golden.getIdentifier()).extracting(Identifier::getSystem).contains(UNRELATED_SYSTEM);
	}

	/**
	 * With a single configured EID system, the system being overwritten is the only one there is, so the
	 * scoped clear behaves exactly as clearing everything did.
	 */
	@Test
	void overwriteExternalEids_singleEidSystem_replacesTheEid() {
		configure(List.of(MRN_SYSTEM), true);

		Patient golden = new Patient();
		golden.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		golden.addIdentifier(new Identifier().setSystem(UNRELATED_SYSTEM).setValue("other-1"));

		myGoldenResourceHelper.overwriteExternalEids(golden, List.of(new CanonicalEID(MRN_SYSTEM, "mrn-2", null)));

		assertThat(myEidHelper.getExternalEid(golden)).extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactly(MRN_SYSTEM + "|mrn-2");
		assertThat(golden.getIdentifier()).extracting(Identifier::getSystem).contains(UNRELATED_SYSTEM);
	}

	/**
	 * A source resource that shares one EID with its matched Golden Resource must still contribute
	 * the EIDs it carries from EID systems that Golden Resource has no EID in at all.
	 */
	@Test
	void updateGoldenResourceExternalEid_preventMultipleEids_incomingCarriesEidFromASystemTheGoldenLacks_addsIt() {
		configure(List.of(MRN_SYSTEM, NPI_SYSTEM), true);

		Patient golden = new Patient();
		golden.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));

		Patient source = new Patient();
		source.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		source.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-9"));

		myGoldenResourceHelper.updateGoldenResourceExternalEidFromSourceResource(
			golden, source, new MdmTransactionContext(MdmTransactionContext.OperationType.UPDATE_RESOURCE));

		assertThat(myEidHelper.getExternalEid(golden))
			.extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(MRN_SYSTEM + "|mrn-1", NPI_SYSTEM + "|npi-9");
	}

	/**
	 * The safeguard is per EID system, so an incoming EID that contradicts the Golden Resource within a
	 * system it already uses must not be applied - the resulting Golden Resource would carry two EIDs of
	 * one system and be rejected on its next write. The drop is recorded in the transaction log rather
	 * than discarded silently.
	 */
	@Test
	void updateGoldenResourceExternalEid_preventMultipleEids_incomingConflictsWithinAnOccupiedSystem_addsNothingAndRecordsIt() {
		configure(List.of(MRN_SYSTEM, NPI_SYSTEM), true);

		Patient golden = new Patient();
		golden.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		golden.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-7"));

		Patient source = new Patient();
		source.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		source.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-9"));

		MdmTransactionContext context = new MdmTransactionContext(
			TransactionLogMessages.createNew(), MdmTransactionContext.OperationType.UPDATE_RESOURCE);
		myGoldenResourceHelper.updateGoldenResourceExternalEidFromSourceResource(golden, source, context);

		assertThat(myEidHelper.getExternalEid(golden))
			.extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(MRN_SYSTEM + "|mrn-1", NPI_SYSTEM + "|npi-7");
		assertThat(context.getTransactionLogMessages().getValues())
			.anySatisfy(message -> assertThat(message).contains("npi-9").contains("npi-7"));
	}

	/**
	 * Two EIDs from one absent system must contribute only the first: applying both would leave the
	 * Golden Resource holding two EIDs of that system.
	 */
	@Test
	void updateGoldenResourceExternalEid_preventMultipleEids_incomingCarriesTwoEidsFromAnAbsentSystem_addsOnlyTheFirst() {
		configure(List.of(MRN_SYSTEM, NPI_SYSTEM), true);

		Patient golden = new Patient();
		golden.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));

		Patient source = new Patient();
		source.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		source.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-8"));
		source.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-9"));

		myGoldenResourceHelper.updateGoldenResourceExternalEidFromSourceResource(
			golden, source, new MdmTransactionContext(MdmTransactionContext.OperationType.UPDATE_RESOURCE));

		assertThat(myEidHelper.getExternalEid(golden))
			.extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(MRN_SYSTEM + "|mrn-1", NPI_SYSTEM + "|npi-8");
	}

	/**
	 * The single-EID-system no-op, at unit level: the Golden Resource already carries an EID in the only
	 * configured system, so there is never anything to add.
	 */
	@Test
	void updateGoldenResourceExternalEid_preventMultipleEids_singleEidSystem_addsNothing() {
		configure(List.of(MRN_SYSTEM), true);

		Patient golden = new Patient();
		golden.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));

		Patient source = new Patient();
		source.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));

		myGoldenResourceHelper.updateGoldenResourceExternalEidFromSourceResource(
			golden, source, new MdmTransactionContext(MdmTransactionContext.OperationType.UPDATE_RESOURCE));

		assertThat(myEidHelper.getExternalEid(golden))
			.extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactly(MRN_SYSTEM + "|mrn-1");
	}

	/**
	 * The single-system no-op must not depend on the "prevent multiple EIDs" safeguard ever having held:
	 * resources written while it was off, or ingested without passing through the storage interceptor, can
	 * carry several EIDs of one system. Those must still be left exactly as they are.
	 */
	@Test
	void updateGoldenResourceExternalEid_preventMultipleEids_singleEidSystemWithLegacyMultipleEids_addsNothing() {
		configure(List.of(MRN_SYSTEM), true);

		Patient golden = new Patient();
		golden.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		golden.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-2"));

		Patient source = new Patient();
		source.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		source.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-3"));

		myGoldenResourceHelper.updateGoldenResourceExternalEidFromSourceResource(
			golden, source, new MdmTransactionContext(MdmTransactionContext.OperationType.UPDATE_RESOURCE));

		assertThat(myEidHelper.getExternalEid(golden))
			.extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(MRN_SYSTEM + "|mrn-1", MRN_SYSTEM + "|mrn-2");
	}

	/**
	 * Identifier.use differs routinely between a source resource and its golden clone, so an EID that
	 * differs only in use must not be added a second time - and the golden resource must keep its own
	 * use rather than having the source's written over it.
	 */
	@Test
	void updateGoldenResourceExternalEid_eidDifferingOnlyInUse_isNotDuplicatedAndKeepsTheGoldenUse() {
		Patient golden = new Patient();
		golden.addIdentifier(new Identifier()
			.setSystem(MRN_SYSTEM)
			.setValue("mrn-1")
			.setUse(Identifier.IdentifierUse.OFFICIAL));

		Patient source = new Patient();
		source.addIdentifier(new Identifier()
			.setSystem(MRN_SYSTEM)
			.setValue("mrn-1")
			.setUse(Identifier.IdentifierUse.SECONDARY));

		myGoldenResourceHelper.updateGoldenResourceExternalEidFromSourceResource(
			golden, source, new MdmTransactionContext(MdmTransactionContext.OperationType.UPDATE_RESOURCE));

		List<CanonicalEID> goldenEids = myEidHelper.getExternalEid(golden);
		assertThat(goldenEids).hasSize(1);
		assertThat(goldenEids.get(0).getSystemAndValueKey()).isEqualTo(MRN_SYSTEM + "|mrn-1");
		// The surviving EID is the golden resource's own, not the source's copy written over it.
		assertThat(goldenEids.get(0).getUse()).isEqualTo("official");
	}

	/**
	 * {@code isPotentialDuplicate} is only consulted once MDM has already decided the two resources match
	 * on demographics, so it asks whether their enterprise identifiers <em>contradict</em> that: two
	 * non-overlapping EIDs mean the records are flagged for a steward rather than merged.
	 * <p>
	 * An MRN and an NPI that happen to read alike are not the same identifier, so the contradiction
	 * stands. Comparing on value alone would have merged two unrelated patients on a coincidence.
	 * </p>
	 */
	@Test
	void isPotentialDuplicate_eidValuesCollideAcrossSystems_flagsPossibleDuplicate() {
		Patient golden = new Patient();
		golden.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("123"));

		Patient incoming = new Patient();
		incoming.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("123"));

		assertThat(myGoldenResourceHelper.isPotentialDuplicate(golden, incoming)).isTrue();
	}

	/**
	 * A genuinely shared EID means the identifiers agree, so there is nothing for a steward to resolve -
	 * even though the incoming resource also carries an EID the golden resource has never seen.
	 */
	@Test
	void isPotentialDuplicate_eidSharedInOneSystem_doesNotFlagPossibleDuplicate() {
		Patient golden = new Patient();
		golden.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));

		Patient incoming = new Patient();
		incoming.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		incoming.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-9"));

		assertThat(myGoldenResourceHelper.isPotentialDuplicate(golden, incoming)).isFalse();
	}
}
