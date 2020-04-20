package ca.uhn.fhir.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.BaseTest;
import ca.uhn.fhir.empi.rules.config.EmpiSettingsImpl;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.util.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;
import org.junit.Test;
import java.util.Optional;

import static ca.uhn.fhir.empi.api.Constants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


public class EIDHelperTest {

	private static final FhirContext myFhirContext = FhirContext.forR4();
	private static final String EXTERNAL_ID_SYSTEM_FOR_TEST = "http://testsystem.io/naming-system/empi";

	private static final EmpiRulesJson myRules = new EmpiRulesJson() {{
		setEnterpriseEIDSystem(EXTERNAL_ID_SYSTEM_FOR_TEST);
	}};

	private static final EmpiSettingsImpl mySettings = new EmpiSettingsImpl(){{
		setEmpiRules(myRules);
	}};

	private static final EIDHelper EID_HELPER = new EIDHelper(myFhirContext, mySettings);


	@Test
	public void testExtractionOfInternalEID() {
		Patient patient = new Patient();
		patient.addIdentifier()
			.setSystem(HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)
			.setValue("simpletest")
			.setUse(Identifier.IdentifierUse.SECONDARY);

		Optional<CanonicalEID> externalEid = EID_HELPER.getHapiEid(patient);

		assertThat(externalEid.isPresent(), is(true));
		assertThat(externalEid.get().getValue(), is(equalTo("simpletest")));
		assertThat(externalEid.get().getSystem(), is(equalTo(HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(externalEid.get().getUse(), is(equalTo("secondary")));
	}

	@Test
	public void testExtractionOfExternalEID() {
		String uniqueID = "uniqueID!";

		Patient patient = new Patient();
		patient.addIdentifier()
			.setSystem(EXTERNAL_ID_SYSTEM_FOR_TEST)
			.setValue(uniqueID);

		Optional<CanonicalEID> externalEid = EID_HELPER.getExternalEid(patient);

		assertThat(externalEid.isPresent(), is(true));
		assertThat(externalEid.get().getValue(), is(equalTo(uniqueID)));
		assertThat(externalEid.get().getSystem(), is(equalTo(EXTERNAL_ID_SYSTEM_FOR_TEST)));
	}

	@Test
	public void testCreationOfInternalEIDGeneratesUuidEID() {

		CanonicalEID internalEid = EID_HELPER.createHapiEid();

		assertThat(internalEid.getSystem(), is(equalTo(HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(internalEid.getValue().length(), is(equalTo(36)));
		assertThat(internalEid.getUse(), is(nullValue()));
	}


}
