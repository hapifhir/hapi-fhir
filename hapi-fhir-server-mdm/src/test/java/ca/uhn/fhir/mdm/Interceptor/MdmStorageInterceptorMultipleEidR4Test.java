package ca.uhn.fhir.mdm.Interceptor;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.BaseR4Test;
import ca.uhn.fhir.mdm.interceptor.MdmStorageInterceptor;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Covers the "prevent multiple EIDs" safeguard once a resource type may be identified by more than one
 * EID system. The safeguard is scoped per system: a resource may carry one EID from each configured
 * system, but never two from the same one.
 */
// Created by claude-opus-5
class MdmStorageInterceptorMultipleEidR4Test extends BaseR4Test {

	private static final String MRN_SYSTEM = "http://example.com/mrn";
	private static final String NPI_SYSTEM = "http://example.com/npi";

	private MdmStorageInterceptor myInterceptor;
	private EIDHelper myEidHelper;

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
		mdmSettings.setPreventMultipleEids(true);

		myEidHelper = new EIDHelper(ourFhirContext, mdmSettings);
		myInterceptor = new MdmStorageInterceptor();
		myInterceptor.setFhirContextForUnitTest(ourFhirContext);
		myInterceptor.setMdmSettingsForUnitTest(mdmSettings);
		myInterceptor.setEidHelperForUnitTest(myEidHelper);
	}

	@Test
	void preventMultipleEids_patientWithOneEidPerConfiguredSystem_isAccepted() {
		Patient patient = patientWith(MRN_SYSTEM, "mrn-1", NPI_SYSTEM, "npi-9");

		create(patient);

		assertThat(myEidHelper.getExternalEid(patient)).hasSize(2);
	}

	@Test
	void preventMultipleEids_patientWithASingleEid_isAccepted() {
		Patient patient = new Patient();
		patient.addIdentifier(new Identifier().setSystem(MRN_SYSTEM).setValue("mrn-1"));

		create(patient);

		assertThat(myEidHelper.getExternalEid(patient)).hasSize(1);
	}

	@Test
	void preventMultipleEids_patientWithNoEid_isAccepted() {
		Patient patient = new Patient();

		create(patient);

		assertThat(myEidHelper.getExternalEid(patient)).isEmpty();
	}

	@Test
	void preventMultipleEids_patientWithTwoEidsFromTheSameSystem_isRejected() {
		Patient patient = patientWith(MRN_SYSTEM, "mrn-1", MRN_SYSTEM, "mrn-2");

		assertThatThrownBy(() -> create(patient))
			.isInstanceOf(ForbiddenOperationException.class)
			.hasMessageContaining(Msg.code(766))
			.hasMessageContaining(MRN_SYSTEM);
	}

	/**
	 * The guard counts identifiers, not distinct system/value pairs, so a repeated EID is rejected just as
	 * it was before the safeguard became per-system. Worth pinning separately from the differing-values
	 * case: de-duplicating first - as {@code eidMatchExists} and {@code addCanonicalEidsToGoldenResourceIfAbsent}
	 * legitimately do - would let this through while still rejecting two different values.
	 */
	@Test
	void preventMultipleEids_sameEidValueRepeatedInOneSystem_isRejected() {
		Patient patient = patientWith(MRN_SYSTEM, "mrn-1", MRN_SYSTEM, "mrn-1");

		assertThatThrownBy(() -> create(patient))
			.isInstanceOf(ForbiddenOperationException.class)
			.hasMessageContaining(Msg.code(766));
	}

	@Test
	void preventMultipleEids_patientWithOneEidPerSystemPlusADuplicate_isRejected() {
		Patient patient = patientWith(MRN_SYSTEM, "mrn-1", NPI_SYSTEM, "npi-9");
		patient.addIdentifier(new Identifier().setSystem(NPI_SYSTEM).setValue("npi-7"));

		assertThatThrownBy(() -> create(patient))
			.isInstanceOf(ForbiddenOperationException.class)
			.hasMessageContaining(Msg.code(766))
			.hasMessageContaining(NPI_SYSTEM);
	}

	private void create(Patient thePatient) {
		myInterceptor.blockManualResourceManipulationOnCreate(thePatient, new SystemRequestDetails(), null);
	}

	private Patient patientWith(
			String theFirstSystem, String theFirstValue, String theSecondSystem, String theSecondValue) {
		Patient retVal = new Patient();
		retVal.addIdentifier(new Identifier().setSystem(theFirstSystem).setValue(theFirstValue));
		retVal.addIdentifier(new Identifier().setSystem(theSecondSystem).setValue(theSecondValue));
		return retVal;
	}
}
