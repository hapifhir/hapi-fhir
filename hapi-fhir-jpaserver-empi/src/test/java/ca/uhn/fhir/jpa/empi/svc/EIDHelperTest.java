package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.empi.rules.config.EmpiConfigImpl;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.util.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
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
import java.util.UUID;

import static ca.uhn.fhir.rest.api.Constants.INTERNAL_ENTERPRISE_IDENTIFIER_SYSTEM;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.springframework.util.StringUtils.hasLength;


public class EIDHelperTest extends BaseEmpiR4Test {
	@Autowired
	private EIDHelper myEIDHelper;
	@Autowired
	private IEmpiConfig myEmpiConfig;

	@Test
	public void testExtractionOfInternalEID() {
		Patient patient = new Patient();
		patient.addIdentifier()
			.setSystem(INTERNAL_ENTERPRISE_IDENTIFIER_SYSTEM)
			.setValue("simpletest")
			.setUse(Identifier.IdentifierUse.SECONDARY);

		Optional<CanonicalEID> externalEid = myEIDHelper.getInternalEid(patient);

		assertThat(externalEid.isPresent(), is(true));
		assertThat(externalEid.get().getValue(), is(equalTo("simpletest")));
		assertThat(externalEid.get().getSystem(), is(equalTo(INTERNAL_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(externalEid.get().getUse(), is(equalTo("secondary")));
	}

	@Test
	public void testExtractionOfExternalEID() {
		String uniqueID = "uniqueID!";

		Patient patient = new Patient();
		patient.addIdentifier()
			.setSystem(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())
			.setValue(uniqueID)
			.setUse(Identifier.IdentifierUse.OFFICIAL);

		Optional<CanonicalEID> externalEid = myEIDHelper.getExternalEid(patient);

		assertThat(externalEid.isPresent(), is(true));
		assertThat(externalEid.get().getValue(), is(equalTo(uniqueID)));
		assertThat(externalEid.get().getSystem(), is(equalTo(INTERNAL_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(externalEid.get().getUse(), is(equalTo("official")));
	}

	@Test
	public void testCreationOfInternalEIDGeneratesUuidEID() {

		CanonicalEID internalEid = myEIDHelper.createInternalEid();

		assertThat(internalEid.getSystem(), is(equalTo(INTERNAL_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(internalEid.getValue().length(), is(equalTo(36)));
		assertThat(internalEid.getUse(), is(equalTo("secondary")));
	}

}
