package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.empi.util.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static ca.uhn.fhir.rest.api.Constants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class EIDHelperTest extends BaseEmpiR4Test {
	@Autowired
	private EIDHelper myEIDHelper;
	@Autowired
	private IEmpiConfig myEmpiConfig;

	@Test
	public void testExtractionOfInternalEID() {
		Patient patient = new Patient();
		patient.addIdentifier()
			.setSystem(HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)
			.setValue("simpletest")
			.setUse(Identifier.IdentifierUse.SECONDARY);

		Optional<CanonicalEID> externalEid = myEIDHelper.getHapiEid(patient);

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
			.setSystem(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())
			.setValue(uniqueID)
			.setUse(Identifier.IdentifierUse.OFFICIAL);

		Optional<CanonicalEID> externalEid = myEIDHelper.getExternalEid(patient);

		assertThat(externalEid.isPresent(), is(true));
		assertThat(externalEid.get().getValue(), is(equalTo(uniqueID)));
		assertThat(externalEid.get().getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(externalEid.get().getUse(), is(equalTo("official")));
	}

	@Test
	public void testCreationOfInternalEIDGeneratesUuidEID() {

		CanonicalEID internalEid = myEIDHelper.createInternalEid();

		assertThat(internalEid.getSystem(), is(equalTo(HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(internalEid.getValue().length(), is(equalTo(36)));
		assertThat(internalEid.getUse(), is(equalTo("secondary")));
	}

}
