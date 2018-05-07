package ca.uhn.fhir.context;

import ca.uhn.fhir.rest.client.MyPatientWithExtensions;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class FhirContextDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirContextDstu3Test.class);

	private static FhirContext ourCtx = FhirContext.forDstu3();

	@SuppressWarnings("deprecation")
	@Test
	public void testAutoDetectVersion() {
		FhirContext ctx = new FhirContext();
		assertEquals(FhirVersionEnum.DSTU3, ctx.getVersion().getVersion());
	}

	@Test
	public void testCustomTypeDoesntBecomeDefault() {
		FhirContext ctx = FhirContext.forDstu3();

		MyPatientWithExtensions pt = new MyPatientWithExtensions();
		pt.addName().addGiven("FOO");
		ctx.newXmlParser().encodeResourceToString(pt);

		assertEquals(Patient.class, ctx.getResourceDefinition("Patient").getImplementingClass());
	}

	/**
	 * See #344
	 */
	@Test
	public void testGetElementDefinitionCaseInsensitive() {
		assertEquals(Reference.class, ourCtx.getElementDefinition("reference").getImplementingClass());
		assertEquals(Reference.class, ourCtx.getElementDefinition("Reference").getImplementingClass());
		assertEquals(Reference.class, ourCtx.getElementDefinition("REFerence").getImplementingClass());
	}

	/**
	 * See #344
	 */
	@Test
	public void testGetResourceDefinitionCaseInsensitive() {
		assertEquals(Patient.class, ourCtx.getResourceDefinition("patient").getImplementingClass());
		assertEquals(Patient.class, ourCtx.getResourceDefinition("Patient").getImplementingClass());
		assertEquals(Patient.class, ourCtx.getResourceDefinition("PATient").getImplementingClass());
		assertEquals(StructureDefinition.class, ourCtx.getResourceDefinition("structuredefinition").getImplementingClass());
	}

	@Test
	public void testInitialisationThreadSafety() {
		final FhirContext ctx = FhirContext.forDstu3();

		final int numThreads = 40;
		final List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<Throwable>());
		final ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
		try {
			final CountDownLatch threadsReady = new CountDownLatch(numThreads);
			final CountDownLatch threadsFinished = new CountDownLatch(numThreads);

			for (int i = 0; i < numThreads; i++) {
				threadPool.submit(
					new Runnable() {
						public void run() {
							threadsReady.countDown();
							try {
								threadsReady.await();
								RuntimeResourceDefinition def = ctx.getResourceDefinition("patient");
								ourLog.info(def.toString());
								assertNotNull(def);
							} catch (final Exception e) {
								exceptions.add(e);
							}
							threadsFinished.countDown();
						}
					}
				);
			}

			threadsFinished.await();
		} catch (final InterruptedException e) {
			exceptions.add(e);
		} finally {
			threadPool.shutdownNow();
		}

		assertTrue("failed with exception(s): " + exceptions, exceptions.isEmpty());
	}

	/**
	 * See #794
	 */
	@Test
	public void testInitializeThreadSafety2() throws InterruptedException {
		final FhirContext dstu3FhirContext = FhirContext.forDstu3();

		final AtomicInteger count = new AtomicInteger();

		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					OperationOutcomeUtil.newInstance(dstu3FhirContext);
					ourLog.info("Have finished {}", count.incrementAndGet());
				}
			}).start();
		}

		TestUtil.waitForSize(10, count);

	}

	@Test
	public void testQueryBoundCode() {
		RuntimeResourceDefinition patientType = ourCtx.getResourceDefinition(Patient.class);
		String childName = "gender";
		BaseRuntimeChildDatatypeDefinition genderChild = (BaseRuntimeChildDatatypeDefinition) patientType.getChildByName(childName);
		ourLog.trace(genderChild.getClass().getName());

		assertEquals(AdministrativeGender.class, genderChild.getBoundEnumType());
	}

	@Test
	public void testQueryNonBoundCode() {
		RuntimeResourceDefinition patientType = ourCtx.getResourceDefinition(Patient.class);
		String childName = "name";
		BaseRuntimeChildDatatypeDefinition genderChild = (BaseRuntimeChildDatatypeDefinition) patientType.getChildByName(childName);
		ourLog.trace(genderChild.getClass().getName());

		assertEquals(null, genderChild.getBoundEnumType());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
