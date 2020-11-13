package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.test.concurrency.PointcutLatch;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

public class EmpiProviderBatchR4Test extends BaseLinkR4Test {

	public static final String ORGANIZATION_DUMMY = "Organization/dummy";
	protected Practitioner myPractitioner;
	protected StringType myPractitionerId;
	protected IAnyResource myGoldenPractitioner;
	protected StringType myGoldenPractitionerId;
	protected Medication myMedication;
	protected StringType myMedicationId;
	protected IAnyResource myGoldenMedication;
	protected StringType myGoldenMedicationId;


	@Autowired
	IInterceptorService myInterceptorService;
	PointcutLatch afterEmpiLatch = new PointcutLatch(Pointcut.EMPI_AFTER_PERSISTED_RESOURCE_CHECKED);


	@BeforeEach
	public void before() {
		super.before();
		myPractitioner = createPractitionerAndUpdateLinks(buildPractitionerWithNameAndId("some_pract", "some_pract_id"));
		myPractitionerId = new StringType(myPractitioner.getIdElement().getValue());
		myGoldenPractitioner = getGoldenResourceFromTargetResource(myPractitioner);
		myGoldenPractitionerId = new StringType(myGoldenPractitioner.getIdElement().getValue());

		Organization dummyOrganization = new Organization();
		dummyOrganization.setId(ORGANIZATION_DUMMY);
		myOrganizationDao.update(dummyOrganization);

		myMedication = createMedicationAndUpdateLinks(buildMedication(ORGANIZATION_DUMMY));
		myMedicationId = new StringType(myMedication.getIdElement().getValue());
		myGoldenMedication = getGoldenResourceFromTargetResource(myMedication);
		myGoldenMedicationId = new StringType(myGoldenMedication.getIdElement().getValue());


		myInterceptorService.registerAnonymousInterceptor(Pointcut.EMPI_AFTER_PERSISTED_RESOURCE_CHECKED, afterEmpiLatch);
	}

	@AfterEach
	public void after() throws IOException {
		myInterceptorService.unregisterInterceptor(afterEmpiLatch);
		super.after();
	}

	@Test
	public void testBatchRunOnAllMedications() throws InterruptedException {
		StringType criteria = null;
		myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);

		afterEmpiLatch.runWithExpectedCount(1, () -> myEmpiProviderR4.empiBatchOnAllTargets(new StringType("Medication"), criteria, null));
		assertLinkCount(1);
	}

	@Test
	public void testBatchRunOnAllPractitioners() throws InterruptedException {
		StringType criteria = null;
		myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);

		afterEmpiLatch.runWithExpectedCount(1, () -> myEmpiProviderR4.empiBatchPractitionerType(criteria, null));
		assertLinkCount(1);
	}
	@Test
	public void testBatchRunOnSpecificPractitioner() throws InterruptedException {
		myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);
		afterEmpiLatch.runWithExpectedCount(1, () -> myEmpiProviderR4.empiBatchPractitionerInstance(myPractitioner.getIdElement(), null));
		assertLinkCount(1);
	}

	@Test
	public void testBatchRunOnNonExistentSpecificPractitioner() {
		myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);
		try {
			myEmpiProviderR4.empiBatchPractitionerInstance(new IdType("Practitioner/999"), null);
			fail();
		} catch (ResourceNotFoundException e){}
	}

	@Test
	public void testBatchRunOnAllPatients() throws InterruptedException {
		assertLinkCount(2);
		StringType criteria = null;
		myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);
		afterEmpiLatch.runWithExpectedCount(1, () -> myEmpiProviderR4.empiBatchPatientType(criteria, null));
		assertLinkCount(1);
	}

	@Test
	public void testBatchRunOnSpecificPatient() throws InterruptedException {
		assertLinkCount(2);
		myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);
		afterEmpiLatch.runWithExpectedCount(1, () -> myEmpiProviderR4.empiBatchPatientInstance(myPatient.getIdElement(), null));
		assertLinkCount(1);
	}

	@Test
	public void testBatchRunOnNonExistentSpecificPatient() {
		assertLinkCount(2);
		myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);
		try {
			myEmpiProviderR4.empiBatchPatientInstance(new IdType("Patient/999"), null);
			fail();
		} catch (ResourceNotFoundException e){}
	}

	@Test
	public void testBatchRunOnAllTypes() throws InterruptedException {
		assertLinkCount(2);
		StringType criteria = new StringType("");
		myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);
		afterEmpiLatch.runWithExpectedCount(2, () -> {
			myEmpiProviderR4.empiBatchOnAllTargets(null, criteria, null);
		});
		assertLinkCount(2);
	}

	@Test
	public void testBatchRunOnAllTypesWithInvalidCriteria() {
		assertLinkCount(2);
		StringType criteria = new StringType("death-date=2020-06-01");
		myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);

		try {
			myEmpiProviderR4.empiBatchPractitionerType(criteria, null);
			fail();
		} catch(InvalidRequestException e) {
			assertThat(e.getMessage(), is(equalTo("Failed to parse match URL[death-date=2020-06-01] - Resource type Practitioner does not have a parameter with name: death-date")));
		}
	}
}
