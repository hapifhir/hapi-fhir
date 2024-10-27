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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;


public class EIDHelperR4Test extends BaseR4Test {

	private static final FhirContext ourFhirContext = FhirContext.forR4();
	private static final String EXTERNAL_ID_SYSTEM_FOR_TEST = "http://testsystem.io/naming-system/mdm";

	private static final MdmRulesJson ourRules = new MdmRulesJson() {
		{
			addEnterpriseEIDSystem("Patient", EXTERNAL_ID_SYSTEM_FOR_TEST);
			setMdmTypes(Arrays.asList(new String[] {"Patient"}));
		}
	};

	private MdmSettings myMdmSettings;

	private EIDHelper myEidHelper;

	@BeforeEach
	public void before() {
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "identifier"))
			.thenReturn(new RuntimeSearchParam(null, null, "identifier", "Description", "identifier", RestSearchParameterTypeEnum.STRING, new HashSet<>(), new HashSet<>(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, null, null, null));

		myMdmSettings = new MdmSettings(new MdmRuleValidator(ourFhirContext, mySearchParamRetriever)) {
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

		assertEquals(false, externalEid.isEmpty());
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

		assertEquals(false, externalEid.isEmpty());
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
}
