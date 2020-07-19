package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderEmpiRunR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderEmpiRunR4Test.class);
	private IIdType myOneVersionPatientId;
	private IIdType myTwoVersionPatientId;
	private IIdType myDeletedPatientId;


	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		Patient p = new Patient();
		p.setId("PT-ONEVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		p.addIdentifier().setSystem("foo").setValue("bar");
		p.addName().setFamily("FAM");
		myOneVersionPatientId = myPatientDao.update(p).getId();

		p = new Patient();
		p.setId("PT-TWOVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		myTwoVersionPatientId = myPatientDao.update(p).getId();
		p.setActive(false);
		myTwoVersionPatientId = myPatientDao.update(p).getId();

		p = new Patient();
		p.setId("PT-DELETED");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		myDeletedPatientId = myPatientDao.update(p).getId();
		myDeletedPatientId = myPatientDao.delete(myDeletedPatientId).getId();
	}

	@Test
	public void testWithEmpiDisabled() {
		myDaoConfig.setExpungeEnabled(new DaoConfig().isExpungeEnabled());

		Parameters input = new Parameters();
		input.addParameter()
			.setName(ProviderConstants.EMPI_BATCH_RUN_CRITERIA)
			.setValue(new StringType("Patient?"));
		try {
			myClient
				.operation()
				.onType(Patient.class)
				.named("empi-batch-run")
				.withParameters(input)
				.execute();
			fail();
		} catch (MethodNotAllowedException e){
			assertEquals("HTTP 405 Method Not Allowed: $empi-batch-run is not enabled on this server", e.getMessage());
		}
	}

	@Test
	public void testEmpiBatchRunOnType() {

		Parameters input = new Parameters();
		input.addParameter()
			.setName(ProviderConstants.EMPI_BATCH_RUN_CRITERIA)
			.setValue(new StringType("Patient?name=Gary"));
		Parameters output = myClient
			.operation()
			.onType(Patient.class)
			.named("empi-batch-run")
			.withParameters(input)
			.execute();
	}

	@Test
	public void testEmpiBatchRunOnInstance() {

		Parameters input = new Parameters();
		input.addParameter()
			.setName(ProviderConstants.EMPI_BATCH_RUN_CRITERIA)
			.setValue(new StringType("Patient?name=Gary"));
		Parameters output = myClient
			.operation()
			.onType(Patient.class)
			.named("empi-batch-run")
			.withParameters(input)
			.execute();
	}
	@Test
	public void testEmpiBatchRunOnServer() {

		Parameters input = new Parameters();
		input.addParameter()
			.setName(ProviderConstants.EMPI_BATCH_RUN_CRITERIA)
			.setValue(new StringType("Patient?name=Gary"));
		Parameters output = myClient
			.operation()
			.onType(Patient.class)
			.named("empi-batch-run")
			.withParameters(input)
			.execute();
	}
}
