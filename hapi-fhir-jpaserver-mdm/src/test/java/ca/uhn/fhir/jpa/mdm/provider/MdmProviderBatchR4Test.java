package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.i18n.Msg;
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

public class MdmProviderBatchR4Test extends BaseLinkR4Test {

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
	PointcutLatch afterMdmLatch = new PointcutLatch(Pointcut.MDM_AFTER_PERSISTED_RESOURCE_CHECKED);

	@Override
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

		myInterceptorService.registerAnonymousInterceptor(Pointcut.MDM_AFTER_PERSISTED_RESOURCE_CHECKED, afterMdmLatch);
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		myInterceptorService.unregisterInterceptor(afterMdmLatch);
		super.after();
	}

	@Test
	public void testBatchRunOnAllMedications() throws InterruptedException {
		StringType criteria = null;
		clearMdmLinks();

		afterMdmLatch.runWithExpectedCount(1, () -> myMdmProvider.mdmBatchOnAllSourceResources(new StringType("Medication"), criteria, null));
		assertLinkCount(1);
	}

	@Test
	public void testBatchRunOnAllPractitioners() throws InterruptedException {
		StringType criteria = null;
		clearMdmLinks();

		afterMdmLatch.runWithExpectedCount(1, () -> myMdmProvider.mdmBatchPractitionerType(criteria, null));
		assertLinkCount(1);
	}
	@Test
	public void testBatchRunOnSpecificPractitioner() throws InterruptedException {
		clearMdmLinks();
		afterMdmLatch.runWithExpectedCount(1, () -> myMdmProvider.mdmBatchPractitionerInstance(myPractitioner.getIdElement(), null));
		assertLinkCount(1);
	}

	@Test
	public void testBatchRunOnNonExistentSpecificPractitioner() {
		clearMdmLinks();
		try {
			myMdmProvider.mdmBatchPractitionerInstance(new IdType("Practitioner/999"), null);
			fail();
		} catch (ResourceNotFoundException e) {
		}
	}

	@Test
	public void testBatchRunOnAllPatients() throws InterruptedException {
		assertLinkCount(3);
		StringType criteria = null;
		clearMdmLinks();
		afterMdmLatch.runWithExpectedCount(1, () -> myMdmProvider.mdmBatchPatientType(criteria, null));
		assertLinkCount(1);
	}

	@Test
	public void testBatchRunOnSpecificPatient() throws InterruptedException {
		assertLinkCount(3);
		clearMdmLinks();
		afterMdmLatch.runWithExpectedCount(1, () -> myMdmProvider.mdmBatchPatientInstance(myPatient.getIdElement(), null));
		assertLinkCount(1);
	}

	@Test
	public void testBatchRunOnNonExistentSpecificPatient() {
		assertLinkCount(3);
		clearMdmLinks();
		try {
			myMdmProvider.mdmBatchPatientInstance(new IdType("Patient/999"), null);
			fail();
		} catch (ResourceNotFoundException e) {
		}
	}

	@Test
	public void testBatchRunOnAllTypes() throws InterruptedException {
		assertLinkCount(3);
		StringType criteria = new StringType("");
		clearMdmLinks();
		afterMdmLatch.runWithExpectedCount(3, () -> {
			myMdmProvider.mdmBatchOnAllSourceResources(null, criteria, null);
		});
		assertLinkCount(3);
	}

	@Test
	public void testBatchRunOnAllTypesWithInvalidCriteria() {
		assertLinkCount(3);
		StringType criteria = new StringType("death-date=2020-06-01");
		clearMdmLinks();

		try {
			myMdmProvider.mdmBatchPractitionerType(criteria, null);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), is(equalTo(Msg.code(488) + "Failed to parse match URL[death-date=2020-06-01] - Resource type Practitioner does not have a parameter with name: death-date")));
		}
	}
}
