package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.mdm.api.IMdmSubmitSvc;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.test.concurrency.PointcutLatch;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;

class MdmBatchSvcImplTest extends BaseMdmR4Test {

	@Autowired
	IMdmSubmitSvc myMdmSubmitSvc;

	@Autowired
	IInterceptorService myInterceptorService;

	PointcutLatch afterMdmLatch = new PointcutLatch(Pointcut.MDM_AFTER_PERSISTED_RESOURCE_CHECKED);

	@BeforeEach
	public void before() {
		myInterceptorService.registerAnonymousInterceptor(Pointcut.MDM_AFTER_PERSISTED_RESOURCE_CHECKED, afterMdmLatch);
	}
	@AfterEach
	public void after() throws IOException {
		myInterceptorService.unregisterInterceptor(afterMdmLatch);
		afterMdmLatch.clear();
		super.after();
	}

	@Test
	public void testMdmBatchRunWorksOverMultipleTargetTypes() throws InterruptedException {

		for (int i =0; i < 10; i++) {
			createPatient(buildJanePatient());
		}

		for(int i = 0; i< 10; i++) {
			createPractitioner(buildPractitionerWithNameAndId("test", "id"));
		}

		createDummyOrganization();
		for(int i = 0; i< 10; i++) {
			createMedication(buildMedicationWithDummyOrganization());
		}

		assertLinkCount(0);

		//SUT
		afterMdmLatch.runWithExpectedCount(30, () -> myMdmSubmitSvc.submitAllSourceTypesToMdm(null));

		assertLinkCount(30);
	}

	@Test
	public void testMdmBatchOnPatientType() throws Exception {

		for (int i =0; i < 10; i++) {
			createPatient(buildPatientWithNameAndId("test", "id"));
		}

		assertLinkCount(0);

		//SUT
		afterMdmLatch.runWithExpectedCount(10, () -> myMdmSubmitSvc.submitSourceResourceTypeToMdm("Patient", null));

		assertLinkCount(10);
	}

	@Test
	public void testMdmBatchOnMedicationType() throws Exception {

		createDummyOrganization();


		for(int i = 0; i< 10; i++) {
			createMedication(buildMedicationWithDummyOrganization());
		}
		assertLinkCount(0);

		//SUT
		afterMdmLatch.runWithExpectedCount(10, () -> myMdmSubmitSvc.submitSourceResourceTypeToMdm("Medication", null));

		assertLinkCount(10);
	}

	@Test
	public void testMdmBatchOnPractitionerType() throws Exception {

		for (int i =0; i < 10; i++) {
			createPractitioner(buildPractitionerWithNameAndId("test", "id"));
		}

		assertLinkCount(0);

		//SUT
		afterMdmLatch.runWithExpectedCount(10, () -> myMdmSubmitSvc.submitAllSourceTypesToMdm(null));

		assertLinkCount(10);
	}

	@Test
	public void testMdmOnTargetTypeWithCriteria() throws InterruptedException {
		createPatient(buildPatientWithNameIdAndBirthday("gary", "gary_id", new Date()));
		createPatient(buildPatientWithNameIdAndBirthday("john", "john_id", DateUtils.addDays(new Date(), -300)));

		assertLinkCount(0);

		//SUT
		afterMdmLatch.runWithExpectedCount(1, () -> myMdmSubmitSvc.submitSourceResourceTypeToMdm("Patient", "Patient?name=gary"));

		assertLinkCount(1);
	}
}
