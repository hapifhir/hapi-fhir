package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.test.concurrency.PointcutLatch;
import ca.uhn.test.util.LogbackCaptureTestExtension;
import ch.qos.logback.classic.Logger;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

	@RegisterExtension
	LogbackCaptureTestExtension myLogCapture = new LogbackCaptureTestExtension((Logger) Logs.getMdmTroubleshootingLog());

	@Autowired
	IInterceptorService myInterceptorService;
	@Autowired
	MdmSettings myMdmSettings;

	PointcutLatch afterMdmLatch = new PointcutLatch(Pointcut.MDM_AFTER_PERSISTED_RESOURCE_CHECKED);

	public static Stream<Arguments> requestTypes() {
		ServletRequestDetails asyncSrd = mock(ServletRequestDetails.class);
		when(asyncSrd.getHeader("Prefer")).thenReturn("respond-async");
		ServletRequestDetails syncSrd = mock(ServletRequestDetails.class);

		return Stream.of(
			Arguments.of(Named.of("Asynchronous Request", asyncSrd)),
			Arguments.of(Named.of("Synchronous Request", syncSrd))
		);
	}
	@Override
	@BeforeEach
	public void before() throws Exception {
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
		myMdmSettings.setEnabled(true);
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		myInterceptorService.unregisterInterceptor(afterMdmLatch);
		myMdmSettings.setEnabled(false);
		super.after();
	}

	@ParameterizedTest
	@MethodSource("requestTypes")
	public void testBatchRunOnAllMedications(ServletRequestDetails theSyncOrAsyncRequest) throws InterruptedException {
		StringType criteria = null;
		clearMdmLinks();

		afterMdmLatch.runWithExpectedCount(1, () -> myMdmProvider.mdmBatchOnAllSourceResources(new StringType("Medication"), criteria, null, theSyncOrAsyncRequest));
		assertLinkCount(1);
	}

	@ParameterizedTest
	@MethodSource("requestTypes")
	public void testBatchRunOnAllPractitioners(ServletRequestDetails theSyncOrAsyncRequest) throws InterruptedException {
		StringType criteria = null;
		clearMdmLinks();

		afterMdmLatch.runWithExpectedCount(1, () -> myMdmProvider.mdmBatchPractitionerType(criteria, null, theSyncOrAsyncRequest));
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

	@ParameterizedTest
	@MethodSource("requestTypes")
	public void testBatchRunOnAllPatients(ServletRequestDetails theSyncOrAsyncRequest) throws InterruptedException {
		assertLinkCount(3);
		StringType criteria = null;
		clearMdmLinks();
		afterMdmLatch.runWithExpectedCount(1, () -> myMdmProvider.mdmBatchPatientType(criteria, null, theSyncOrAsyncRequest));
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

	@ParameterizedTest
	@MethodSource("requestTypes")
	public void testBatchRunOnAllTypes(ServletRequestDetails theSyncOrAsyncRequest) throws InterruptedException {
		assertLinkCount(3);
		StringType criteria = new StringType("");
		clearMdmLinks();
		afterMdmLatch.runWithExpectedCount(3, () -> {
			myMdmProvider.mdmBatchOnAllSourceResources(null, criteria, null, theSyncOrAsyncRequest);
		});
		assertLinkCount(3);
	}

	@ParameterizedTest
	@MethodSource("requestTypes")
	public void testBatchRunOnAllTypesWithInvalidCriteria(ServletRequestDetails theSyncOrAsyncRequest) {
		assertLinkCount(3);
		StringType criteria = new StringType("death-date=2020-06-01");
		clearMdmLinks();

		try {
			myMdmProvider.mdmBatchOnAllSourceResources(null, criteria , null, theSyncOrAsyncRequest);
			fail();
		} catch (InvalidRequestException e) {

			assertThat(e.getMessage(), either(
				containsString(Msg.code(2039) + "Failed to validate parameters for job"))//Async case
				.or(containsString(Msg.code(488) + "Failed to parse match URL")));// Sync case
		}
	}

	@Test
	public void testClearAndUpdateResource_WithoutMdmSubmit_LogsError() {
		// Given
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Patient janePatient2 = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(5);

		// When
		clearMdmLinks();

		updatePatientAndUpdateLinks(janePatient);
		try {
			updatePatientAndUpdateLinks(janePatient2);
		} catch (InternalErrorException e) {
			// Then
			assertLinkCount(1);
			String expectedMsg = Msg.code(2362) + "Old golden resource was null while updating MDM links with new golden resource. It is likely that a $mdm-clear was performed without a $mdm-submit. Link will not be updated.";
			assertEquals(expectedMsg, e.getMessage());
		}
	}

	@ParameterizedTest
	@MethodSource("requestTypes")
	public void testUpdateResource_WithClearAndSubmit_Succeeds(ServletRequestDetails theSyncOrAsyncRequest) throws InterruptedException {
		// Given
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Patient janePatient2 = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(5);

		// When
		clearMdmLinks();
		afterMdmLatch.runWithExpectedCount(3, () -> {
			myMdmProvider.mdmBatchPatientType(null , null, theSyncOrAsyncRequest);
		});

		// Then
		updatePatientAndUpdateLinks(janePatient);
		updatePatientAndUpdateLinks(janePatient2);
		assertLinkCount(3);
	}
}
