package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.mdm.BaseR4Test;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static ca.uhn.fhir.mdm.api.MdmConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


public class EIDHelperR4Test extends BaseR4Test {

	private static final FhirContext ourFhirContext = FhirContext.forR4();
	private static final String EXTERNAL_ID_SYSTEM_FOR_TEST = "http://testsystem.io/naming-system/mdm";
	private static final String SECOND_EXTERNAL_ID_SYSTEM_FOR_TEST = "http://testsystem.io/naming-system/mdm-two";

	private static final MdmRulesJson ourRules = new MdmRulesJson() {
		{
			addEnterpriseEIDSystem("Patient", EXTERNAL_ID_SYSTEM_FOR_TEST);
			addEnterpriseEIDSystem("Patient", SECOND_EXTERNAL_ID_SYSTEM_FOR_TEST);
			setMdmTypes(Arrays.asList(new String[] {"Patient"}));
		}
	};

	private MdmSettings myMdmSettings;

	private EIDHelper myEidHelper;

	@Override
	@BeforeEach
	public void before() {
		super.before();
		when(mySearchParamRetriever.getActiveSearchParam(eq("Patient"), eq("identifier"), any()))
			.thenReturn(new RuntimeSearchParam(null, null, "identifier", "Description", "identifier", RestSearchParameterTypeEnum.STRING, new HashSet<>(), new HashSet<>(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, null, null, null));

		myMdmSettings = new MdmSettings(new MdmRuleValidator(ourFhirContext, mySearchParamRetriever, null, null)) {
			{
				setMdmRules(ourRules);
			}
		};
		myEidHelper = new EIDHelper(ourFhirContext, myMdmSettings);
	}

	@Test
	public void testExtractionOfInternalEID() {
		Patient patient = new Patient();
		patient.addIdentifier()
			.setSystem(HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)
			.setValue("simpletest")
			.setUse(Identifier.IdentifierUse.SECONDARY);

		List<CanonicalEID> externalEid = myEidHelper.getHapiEid(patient);

		assertFalse(externalEid.isEmpty());
		assertEquals("simpletest", externalEid.get(0).getValue());
		assertEquals(HAPI_ENTERPRISE_IDENTIFIER_SYSTEM, externalEid.get(0).getSystem());
		assertEquals("secondary", externalEid.get(0).getUse());
	}

	@Test
	public void testExtractionOfExternalEID() {
		String uniqueID = "uniqueID!";

		Patient patient = new Patient();
		patient.addIdentifier()
			.setSystem(EXTERNAL_ID_SYSTEM_FOR_TEST)
			.setValue(uniqueID);

		List<CanonicalEID> externalEid = myEidHelper.getExternalEid(patient);

		assertFalse(externalEid.isEmpty());
		assertEquals(uniqueID, externalEid.get(0).getValue());
		assertEquals(EXTERNAL_ID_SYSTEM_FOR_TEST, externalEid.get(0).getSystem());
	}

	@Test
	public void testCreationOfInternalEIDGeneratesUuidEID() {

		CanonicalEID internalEid = myEidHelper.createHapiEid();

		assertEquals(HAPI_ENTERPRISE_IDENTIFIER_SYSTEM, internalEid.getSystem());
		assertThat(internalEid.getValue()).hasSize(36);
		assertNull(internalEid.getUse());
	}

	@Test
	void getExternalEid_withTwoConfiguredSystems_returnsBoth() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem(EXTERNAL_ID_SYSTEM_FOR_TEST).setValue("mrn-1");
		patient.addIdentifier().setSystem(SECOND_EXTERNAL_ID_SYSTEM_FOR_TEST).setValue("npi-9");

		List<CanonicalEID> externalEids = myEidHelper.getExternalEid(patient);

		assertThat(externalEids).extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(
				EXTERNAL_ID_SYSTEM_FOR_TEST + "|mrn-1", SECOND_EXTERNAL_ID_SYSTEM_FOR_TEST + "|npi-9");
	}

	@Test
	void getExternalEid_identifierFromUnconfiguredSystem_isIgnored() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem(EXTERNAL_ID_SYSTEM_FOR_TEST).setValue("mrn-1");
		patient.addIdentifier().setSystem("http://not-configured.example.com").setValue("other-1");

		List<CanonicalEID> externalEids = myEidHelper.getExternalEid(patient);

		assertThat(externalEids).extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactly(EXTERNAL_ID_SYSTEM_FOR_TEST + "|mrn-1");
	}

	/**
	 * Guards the regression introduced when EID comparison was generalised to lists and the system
	 * comparison was dropped: an MRN and an NPI that happen to share a value are not the same EID.
	 */
	@Test
	void eidMatchExists_sameValueDifferentSystems_returnsFalse() {
		List<CanonicalEID> mrn = List.of(new CanonicalEID(EXTERNAL_ID_SYSTEM_FOR_TEST, "123", null));
		List<CanonicalEID> npi = List.of(new CanonicalEID(SECOND_EXTERNAL_ID_SYSTEM_FOR_TEST, "123", null));

		assertThat(myEidHelper.eidMatchExists(mrn, npi)).isFalse();
	}

	@Test
	void eidMatchExists_sameSystemAndValue_returnsTrue() {
		List<CanonicalEID> first = List.of(new CanonicalEID(EXTERNAL_ID_SYSTEM_FOR_TEST, "123", null));
		List<CanonicalEID> second = List.of(new CanonicalEID(EXTERNAL_ID_SYSTEM_FOR_TEST, "123", null));

		assertThat(myEidHelper.eidMatchExists(first, second)).isTrue();
	}

	/**
	 * Identifier.use routinely differs between a source resource and its golden clone, so it must not
	 * take part in the comparison.
	 */
	@Test
	void eidMatchExists_sameSystemAndValueDifferentUse_returnsTrue() {
		List<CanonicalEID> official = List.of(new CanonicalEID(EXTERNAL_ID_SYSTEM_FOR_TEST, "123", "official"));
		List<CanonicalEID> secondary = List.of(new CanonicalEID(EXTERNAL_ID_SYSTEM_FOR_TEST, "123", "secondary"));

		assertThat(myEidHelper.eidMatchExists(official, secondary)).isTrue();
	}

	@Test
	void eidMatchExists_onlyOneOfSeveralEidsOverlaps_returnsTrue() {
		List<CanonicalEID> first = List.of(
			new CanonicalEID(EXTERNAL_ID_SYSTEM_FOR_TEST, "mrn-1", null),
			new CanonicalEID(SECOND_EXTERNAL_ID_SYSTEM_FOR_TEST, "npi-9", null));
		List<CanonicalEID> second = List.of(
			new CanonicalEID(EXTERNAL_ID_SYSTEM_FOR_TEST, "mrn-1", null),
			new CanonicalEID(SECOND_EXTERNAL_ID_SYSTEM_FOR_TEST, "npi-7", null));

		assertThat(myEidHelper.eidMatchExists(first, second)).isTrue();
	}

	/**
	 * The primary EID drives the subscription message key, so it must be chosen by configured-system
	 * order rather than by the order identifiers happen to appear in the payload.
	 */
	@Test
	void getPrimaryExternalEid_returnsEidOfFirstConfiguredSystemRegardlessOfIdentifierOrder() {
		Patient identifiersInOneOrder = new Patient();
		identifiersInOneOrder.addIdentifier().setSystem(EXTERNAL_ID_SYSTEM_FOR_TEST).setValue("mrn-1");
		identifiersInOneOrder.addIdentifier().setSystem(SECOND_EXTERNAL_ID_SYSTEM_FOR_TEST).setValue("npi-9");

		Patient identifiersReversed = new Patient();
		identifiersReversed.addIdentifier().setSystem(SECOND_EXTERNAL_ID_SYSTEM_FOR_TEST).setValue("npi-9");
		identifiersReversed.addIdentifier().setSystem(EXTERNAL_ID_SYSTEM_FOR_TEST).setValue("mrn-1");

		assertThat(myEidHelper.getPrimaryExternalEid(identifiersInOneOrder))
			.map(CanonicalEID::getValue)
			.contains("mrn-1");
		assertThat(myEidHelper.getPrimaryExternalEid(identifiersReversed))
			.map(CanonicalEID::getValue)
			.contains("mrn-1");
	}

	@Test
	void getPrimaryExternalEid_resourceCarriesOnlySecondSystem_fallsBackToIt() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem(SECOND_EXTERNAL_ID_SYSTEM_FOR_TEST).setValue("npi-9");

		assertThat(myEidHelper.getPrimaryExternalEid(patient)).map(CanonicalEID::getValue).contains("npi-9");
	}

	@Test
	void getPrimaryExternalEid_resourceHasNoEid_returnsEmpty() {
		assertThat(myEidHelper.getPrimaryExternalEid(new Patient())).isEmpty();
	}
}
