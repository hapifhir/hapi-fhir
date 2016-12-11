package org.hl7.fhir.dstu3.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.HapiWorkerContext;
import org.hl7.fhir.dstu3.model.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;

public class FhirPathEngineTest {

	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static FHIRPathEngine ourEngine;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirPathEngineTest.class);

	@Test
	public void testAs() throws Exception {
		Observation obs = new Observation();
		obs.setValue(new StringType("FOO"));
		
		List<Base> value = ourEngine.evaluate(obs, "Observation.value.as(String)");
		assertEquals(1, value.size());
		assertEquals("FOO", ((StringType)value.get(0)).getValue());
	}
	
	@Test
	public void testExistsWithNoValue() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType());
		List<Base> eval = ourEngine.evaluate(patient, "Patient.deceased.exists()");
		ourLog.info(eval.toString());
		assertFalse(((BooleanType)eval.get(0)).getValue());
	}

	@Test
	public void testExistsWithValue() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType(false));
		List<Base> eval = ourEngine.evaluate(patient, "Patient.deceased.exists()");
		ourLog.info(eval.toString());
		assertTrue(((BooleanType)eval.get(0)).getValue());
	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() {
		ourEngine = new FHIRPathEngine(new HapiWorkerContext(ourCtx, new DefaultProfileValidationSupport()));
	}

}
