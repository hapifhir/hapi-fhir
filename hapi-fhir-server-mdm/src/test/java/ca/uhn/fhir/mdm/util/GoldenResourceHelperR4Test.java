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

		MdmRulesJson rules = new MdmRulesJson();
		rules.setVersion("test version");
		rules.setMdmTypes(List.of("Patient"));
		rules.addEnterpriseEIDSystems("Patient", List.of(MRN_SYSTEM, NPI_SYSTEM));

		MdmSettings mdmSettings = new MdmSettings(
				new MdmRuleValidator(ourFhirContext, mySearchParamRetriever, myIMatcherFactory, mySimilarityFactory))
			.setMdmRules(rules);
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
	void overwriteExternalEids_clearsEidsFromEveryConfiguredSystem() {
		Patient golden = new Patient();
		golden.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));
		golden.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-9"));
		golden.addIdentifier(new Identifier().setSystem(UNRELATED_SYSTEM).setValue("other-1"));

		myGoldenResourceHelper.overwriteExternalEids(golden, List.of(new CanonicalEID(MRN_SYSTEM, "mrn-2", null)));

		assertThat(myEidHelper.getExternalEid(golden)).extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactly(MRN_SYSTEM + "|mrn-2");
		assertThat(golden.getIdentifier()).extracting(Identifier::getSystem).contains(UNRELATED_SYSTEM);
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
