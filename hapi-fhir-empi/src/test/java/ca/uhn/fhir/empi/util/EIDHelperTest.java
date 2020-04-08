package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.BaseTest;
import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.empi.config.TestEmpiConfig;
import ca.uhn.fhir.empi.rules.config.EmpiConfigImpl;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.rest.annotation.Patch;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static ca.uhn.fhir.rest.api.Constants.INTERNAL_ENTERPRISE_IDENTIFIER_SYSTEM;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

//FIXME EMPI QUESTION how do i load the appropriate context for this test??
public class EIDHelperTest extends BaseTest{
	private static FhirContext myFhirContext = FhirContext.forR4();
	private static EmpiRulesJson myEmpiRules = new EmpiRulesJson(){{setEnterpriseEIDSystem("http://some-test-system");}};
	private static IEmpiConfig myEmpiConfig = new EmpiConfigImpl().setEmpiRules(myEmpiRules);
	private static EIDHelper myEIDHelper = new EIDHelper(myFhirContext, myEmpiConfig);

	@Test
	public void testExtractionOfInternalEID() {
		Patient patient = new Patient();
		patient.addIdentifier()
			.setSystem(INTERNAL_ENTERPRISE_IDENTIFIER_SYSTEM)
			.setValue("simpletest")
			.setUse(Identifier.IdentifierUse.SECONDARY);

		Optional<CanonicalEID> externalEid = myEIDHelper.getExternalEid(patient);

		assertThat(externalEid.isPresent(), is(true));
		assertThat(externalEid.get().getValue(), is(equalTo("simpletest")));
		assertThat(externalEid.get().getSystem(), is(equalTo(INTERNAL_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(externalEid.get().getUse(), is(equalTo("secondary")));
	}

}
