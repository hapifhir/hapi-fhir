package ca.uhn.fhir.ctx;

import ca.uhn.fhir.context.BaseRuntimeChildDatatypeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirContextDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirContextDstu2Test.class);

	private static FhirContext ourCtx = FhirContext.forDstu2();

	@Test
	public void testScanInvalid() {
		try {
			FhirContext ctx = FhirContext.forDstu2();
			ctx.getResourceDefinition("InvalidResource");
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1684) + "Unknown resource name \"InvalidResource\" (this name is not known in FHIR version \"DSTU2\")", e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testAutoDetectVersion() {
		FhirContext ctx = new FhirContext();
		assertEquals(FhirVersionEnum.DSTU2, ctx.getVersion().getVersion());
	}

	@Test
	public void testQueryBoundCode() {
		RuntimeResourceDefinition patientType = ourCtx.getResourceDefinition(Patient.class);
		String childName = "gender";
		BaseRuntimeChildDatatypeDefinition genderChild = (BaseRuntimeChildDatatypeDefinition) patientType.getChildByName(childName);
		ourLog.trace(genderChild.getClass().getName());

		assertEquals(AdministrativeGenderEnum.class, genderChild.getBoundEnumType());
	}

	@Test
	public void testQueryBoundCodeableConcept() {
		RuntimeResourceDefinition patientType = ourCtx.getResourceDefinition(Patient.class);
		String childName = "maritalStatus";
		BaseRuntimeChildDatatypeDefinition genderChild = (BaseRuntimeChildDatatypeDefinition) patientType.getChildByName(childName);
		ourLog.trace(genderChild.getClass().getName());

		assertEquals(MaritalStatusCodesEnum.class, genderChild.getBoundEnumType());
	}

	@Test
	public void testPossibleToUseModelWhileScanIsRunning() throws InterruptedException {
		FhirContext fhirContext = FhirContext.forDstu2();
		final IParser iParser = fhirContext.newJsonParser();
		final String valueSetResource = "{\"resourceType\":\"ValueSet\",\"id\":\"test-value-set\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2017-03-03T15:52:39.374+01:00\",\"profile\":[\"http://test.com/TestValueset\"],\"tag\":[{\"code\":\"user|Role|Org\",\"display\":\"UPDATER_USER_NAME\"}]},\"url\":\"http://test.com/TestValueset\",\"status\":\"active\",\"compose\":{\"id\":\"ea56e586-432f-4af3-8d04-888bc1154875\",\"include\":[{\"id\":\"474107a6-fec7-4f15-aac4-f1e3b94f32ce\",\"system\":\"http://test.com/TestValueset\"}]}}";
		final String valueSetResource2 = "{\"resourceType\":\"ValueSet\",\"id\":\"test-value-set-2\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2017-03-08T10:53:36.394+01:00\",\"profile\":[\"http://test.com/TestValueset2\"],\"tag\":[{\"code\":\"user|Role|Org\",\"display\":\"UPDATER_USER_NAME\"}]},\"url\":\"http://test.com/TestValueset2\",\"status\":\"active\",\"compose\":{\"id\":\"c64d1c2a-a64a-49ab-aac4-5cc269844b01\",\"include\":[{\"id\":\"5a32346d-af1e-40f0-a787-4390a58b56aa\",\"system\":\"http://test.com/TestValueset2\"}]}}";

		List<Runnable> runnables = new ArrayList<Runnable>();
		for (int i = 0; i < 1000; i++) {
			runnables.add(new Runnable() {
				@Override
				public void run() {
					parseStringResourceMultipleTimes(iParser, valueSetResource);
					parseStringResourceMultipleTimes(iParser, valueSetResource2);
				}
			});
		}
		assertConcurrent("Unable to encode resources multithreaded", runnables, 1000);
	}

	@Disabled
	@Test
	public void testPossibleToUseModelWhileScanIsRunning2() throws InterruptedException {
		FhirContext fhirContext = FhirContext.forDstu2();
		final IParser iParser = fhirContext.newJsonParser();
		final String medicationOrderResource = "{\"resourceType\":\"MedicationOrder\",\"id\":\"941da8dc-7cc5-4dc3-ab28-9b783d6e09ca\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2017-03-08T14:53:06.098+01:00\",\"profile\":[\"http://test.com/p/TestMedicationOrder\"],\"tag\":[{\"code\":\"user|Role|1b1fbde4-fe78-11e6-bc64-92361f002671\",\"display\":\"UPDATER_USER_NAME\"}]},\"contained\":[{\"resourceType\":\"Medication\",\"id\":\"1\",\"meta\":{\"profile\":[\"http://test.com/p/TestMedication\"]},\"code\":{\"coding\":[{\"system\":\"LOCAL_DRUG_NAME\",\"display\":\"Ipren\"},{\"system\":\"LOCAL_DRUG_ATC\"},{\"system\":\"LOCAL_DRUG_INGREDIENT\"}]},\"product\":{\"id\":\"b4ff5fcc-8b22-4123-bac6-19085c4efd7b\",\"extension\":[{\"url\":\"http://test.com/x/strengthQuantity\",\"valueQuantity\":{\"value\":200.0,\"unit\":\"mg\",\"code\":\"dose_MG\"}}],\"form\":{\"coding\":[{\"system\":\"http://test.com/cs/medicationOrderDrugForm-valueSet\",\"code\":\"form_TAB\"}]}}}],\"extension\":[{\"url\":\"http://test.com/p/TestMedicationOrder/isExternal\",\"valueBoolean\":false},{\"url\":\"http://test.com/x/TestMedicationOrder/isPrivate\",\"valueBoolean\":false},{\"url\":\"http://test.com/x/medication-order-route\",\"valueCode\":\"route_OR\"},{\"url\":\"http://test.com/x/TestMedicationOrder/startDate\",\"valueDateTime\":\"2017-03-08T14:52:07+01:00\"},{\"url\":\"http://test.com/x/medication-order-unit\",\"valueCode\":\"dosage_tablet\"}],\"dateWritten\":\"2017-03-08T14:53:07+01:00\",\"status\":\"active\",\"dateEnded\":\"3000-01-01T00:00:00+01:00\",\"patient\":{\"reference\":\"Patient/2cb3bac7-f307-4ad7-832c-9141888a9a11\"},\"reasonCodeableConcept\":{\"text\":\"Mod smerter\"},\"medicationReference\":{\"reference\":\"#1\"},\"dosageInstruction\":[{\"id\":\"1810f1a4-72d0-4fa3-ba96-9dd0f4148ee5\",\"extension\":[{\"url\":\"http://test.com/x/scheduled-dosage-repetition-type\",\"extension\":[{\"url\":\"#length\",\"valuePositiveInt\":1}]},{\"url\":\"http://test.com/x/scheduled-dosage-day\",\"extension\":[{\"url\":\"http://test.com/x/DosageInstruction/scheduledDays/dayNumber\",\"valuePositiveInt\":1},{\"url\":\"http://test.com/x/DosageInstruction/scheduledDays/dosageSchedule\",\"extension\":[{\"url\":\"http://test.com/x/DosageInstruction/scheduledDays/dosageSchedule/quantity\",\"valueQuantity\":{\"value\":2.0}},{\"url\":\"http://test.com/x/DosageInstruction/scheduledDays/dosageSchedule/time\",\"valueTime\":\"08:00\"}]}]},{\"url\":\"http://test.com/x/medication-order-type\",\"valueCode\":\"scheduled\"}],\"timing\":{\"repeat\":{\"boundsPeriod\":{\"start\":\"2017-03-08T00:00:00+01:00\"}}}}]}\n";
		final String medicationOrderResource2 = "{\"resourceType\":\"MedicationOrder\",\"id\":\"4225fea5-189a-4f00-b8c8-b801623bd319\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2017-03-08T14:51:59.948+01:00\",\"profile\":[\"http://test.com/p/TestMedicationOrder\"],\"tag\":[{\"code\":\"user|Role|1b1fbde4-fe78-11e6-bc64-92361f002671\",\"display\":\"UPDATER_USER_NAME\"}]},\"contained\":[{\"resourceType\":\"Medication\",\"id\":\"1\",\"meta\":{\"profile\":[\"http://test.com/p/TestMedication\"]},\"code\":{\"coding\":[{\"system\":\"LOCAL_DRUG_NAME\",\"display\":\"Pamol\"},{\"system\":\"LOCAL_DRUG_ATC\"},{\"system\":\"LOCAL_DRUG_INGREDIENT\"}]},\"product\":{\"id\":\"f283ef1b-bd39-444f-ac55-14b61d9048b2\",\"extension\":[{\"url\":\"http://test.com/x/strengthQuantity\",\"valueQuantity\":{\"value\":500.0,\"unit\":\"mg\",\"code\":\"dose_MG\"}}],\"form\":{\"coding\":[{\"system\":\"http://test.com/cs/medicationOrderDrugForm-valueSet\",\"code\":\"form_TAB\"}]}}}],\"extension\":[{\"url\":\"http://test.com/p/TestMedicationOrder/isExternal\",\"valueBoolean\":false},{\"url\":\"http://test.com/x/TestMedicationOrder/isPrivate\",\"valueBoolean\":false},{\"url\":\"http://test.com/x/medication-order-route\",\"valueCode\":\"route_OR\"},{\"url\":\"http://test.com/x/TestMedicationOrder/startDate\",\"valueDateTime\":\"2017-03-08T14:51:20+01:00\"},{\"url\":\"http://test.com/x/medication-order-unit\",\"valueCode\":\"dosage_tablet\"}],\"dateWritten\":\"2017-03-08T14:51:57+01:00\",\"status\":\"active\",\"dateEnded\":\"3000-01-01T00:00:00+01:00\",\"patient\":{\"reference\":\"Patient/2cb3bac7-f307-4ad7-832c-9141888a9a11\"},\"reasonCodeableConcept\":{\"text\":\"Smerter\"},\"medicationReference\":{\"reference\":\"#1\"},\"dosageInstruction\":[{\"id\":\"c32a7d82-4126-4bce-91d0-28beba15a20a\",\"extension\":[{\"url\":\"http://test.com/x/scheduled-dosage-repetition-type\",\"extension\":[{\"url\":\"#length\",\"valuePositiveInt\":1}]},{\"url\":\"http://test.com/x/scheduled-dosage-day\",\"extension\":[{\"url\":\"http://test.com/x/DosageInstruction/scheduledDays/dayNumber\",\"valuePositiveInt\":1},{\"url\":\"http://test.com/x/DosageInstruction/scheduledDays/dosageSchedule\",\"extension\":[{\"url\":\"http://test.com/x/DosageInstruction/scheduledDays/dosageSchedule/quantity\",\"valueQuantity\":{\"value\":4.0}},{\"url\":\"http://test.com/x/DosageInstruction/scheduledDays/dosageSchedule/time\",\"valueTime\":\"08:00\"}]}]},{\"url\":\"http://test.com/x/medication-order-type\",\"valueCode\":\"scheduled\"}],\"timing\":{\"repeat\":{\"boundsPeriod\":{\"start\":\"2017-03-08T00:00:00+01:00\"}}}}]}";

		List<Runnable> runnables = new ArrayList<Runnable>();
		for (int i = 0; i < 1000; i++) {
			runnables.add(new Runnable() {
				@Override
				public void run() {
					parseStringResourceMultipleTimesWithClass(iParser, medicationOrderResource);
					parseStringResourceMultipleTimesWithClass(iParser, medicationOrderResource2);
				}
			});
		}
		assertConcurrent("Unable to encode resources multithreaded", runnables, 1000);
	}

	private void parseStringResourceMultipleTimesWithClass(IParser iParser, String medicationOrderResource) {
		iParser.parseResource(MedicationOrder.class, medicationOrderResource);
		iParser.parseResource(MedicationOrder.class, medicationOrderResource);
		iParser.parseResource(MedicationOrder.class, medicationOrderResource);
		iParser.parseResource(MedicationOrder.class, medicationOrderResource);
		IBaseResource iBaseResource2 = iParser.parseResource(MedicationOrder.class, medicationOrderResource);
		iParser.encodeResourceToString(iBaseResource2);
	}

	private void parseStringResourceMultipleTimes(IParser iParser, String valueSetResource2) {
		iParser.parseResource(valueSetResource2);
		iParser.parseResource(valueSetResource2);
		iParser.parseResource(valueSetResource2);
		iParser.parseResource(valueSetResource2);
		IBaseResource iBaseResource2 = iParser.parseResource(valueSetResource2);
		iParser.encodeResourceToString(iBaseResource2);
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	//Source: https://github.com/junit-team/junit4/wiki/multithreaded-code-and-concurrency
	public static void assertConcurrent(final String message, final List<? extends Runnable> runnables, final int maxTimeoutSeconds) throws InterruptedException {
		final int numThreads = runnables.size();
		final List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<Throwable>());
		final ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
		try {
			final CountDownLatch allExecutorThreadsReady = new CountDownLatch(numThreads);
			final CountDownLatch afterInitBlocker = new CountDownLatch(1);
			final CountDownLatch allDone = new CountDownLatch(numThreads);
			for (final Runnable submittedTestRunnable : runnables) {
				threadPool.submit(new Runnable() {
					@Override
					public void run() {
						allExecutorThreadsReady.countDown();
						try {
							afterInitBlocker.await();
							submittedTestRunnable.run();
						} catch (final Throwable e) {
							ourLog.error("Exception", e);
							exceptions.add(e);
						} finally {
							allDone.countDown();
						}
					}
				});
			}
			// wait until all threads are ready
			assertTrue(allExecutorThreadsReady.await(runnables.size() * 10L, TimeUnit.MILLISECONDS), "Timeout initializing threads! Perform long lasting initializations before passing runnables to assertConcurrent");
			// start all test runners
			afterInitBlocker.countDown();
			assertTrue(allDone.await(maxTimeoutSeconds, TimeUnit.SECONDS), message + " timeout! More than" + maxTimeoutSeconds + "seconds");
		} finally {
			threadPool.shutdownNow();
		}
		assertTrue(exceptions.isEmpty(), message + "failed with exception(s)" + exceptions);
	}
}
