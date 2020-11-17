package ca.uhn.fhir.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.empi.BaseR4Test;
import ca.uhn.fhir.empi.model.CanonicalEID;
import ca.uhn.fhir.empi.rules.config.EmpiRuleValidator;
import ca.uhn.fhir.empi.rules.config.EmpiSettings;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static ca.uhn.fhir.empi.api.EmpiConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;


public class EIDHelperR4Test extends BaseR4Test {

	private static final FhirContext ourFhirContext = FhirContext.forR4();
	private static final String EXTERNAL_ID_SYSTEM_FOR_TEST = "http://testsystem.io/naming-system/empi";

	private static final EmpiRulesJson ourRules = new EmpiRulesJson() {
		{
			setEnterpriseEIDSystem(EXTERNAL_ID_SYSTEM_FOR_TEST);
			setMdmTypes(Arrays.asList(new String[] {"Patient"}));
		}
	};

	private EmpiSettings myEmpiSettings;

	private EIDHelper myEidHelper;

	@BeforeEach
	public void before() {
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "identifier"))
			.thenReturn(new RuntimeSearchParam(
				"identifier", "Description", "identifier", RestSearchParameterTypeEnum.STRING,
				new HashSet<>(), new HashSet<>(), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE
			));

		myEmpiSettings = new EmpiSettings(new EmpiRuleValidator(ourFhirContext, mySearchParamRetriever)) {
			{
				setEmpiRules(ourRules);
			}
		};
		myEidHelper = new EIDHelper(ourFhirContext, myEmpiSettings);
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
