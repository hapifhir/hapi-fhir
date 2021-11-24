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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
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

		assertThat(externalEid.isEmpty(), is(false));
		assertThat(externalEid.get(0).getValue(), is(equalTo("simpletest")));
		assertThat(externalEid.get(0).getSystem(), is(equalTo(HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(externalEid.get(0).getUse(), is(equalTo("secondary")));
	}

	@Test
	public void testExtractionOfExternalEID() {
		String uniqueID = "uniqueID!";

		Patient patient = new Patient();
		patient.addIdentifier()
			.setSystem(EXTERNAL_ID_SYSTEM_FOR_TEST)
			.setValue(uniqueID);

		List<CanonicalEID> externalEid = myEidHelper.getExternalEid(patient);

		assertThat(externalEid.isEmpty(), is(false));
		assertThat(externalEid.get(0).getValue(), is(equalTo(uniqueID)));
		assertThat(externalEid.get(0).getSystem(), is(equalTo(EXTERNAL_ID_SYSTEM_FOR_TEST)));
	}

	@Test
	public void testCreationOfInternalEIDGeneratesUuidEID() {

		CanonicalEID internalEid = myEidHelper.createHapiEid();

		assertThat(internalEid.getSystem(), is(equalTo(HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(internalEid.getValue().length(), is(equalTo(36)));
		assertThat(internalEid.getUse(), is(nullValue()));
	}
}
