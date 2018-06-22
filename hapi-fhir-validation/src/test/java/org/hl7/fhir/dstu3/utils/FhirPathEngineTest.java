package org.hl7.fhir.dstu3.utils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FhirPathEngineTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirPathEngineTest.class);
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static FHIRPathEngine ourEngine;

	@Test
	public void testAs() throws Exception {
		Observation obs = new Observation();
		obs.setValue(new StringType("FOO"));

		List<Base> value = ourEngine.evaluate(obs, "Observation.value.as(String)");
		assertEquals(1, value.size());
		assertEquals("FOO", ((StringType) value.get(0)).getValue());
	}

	@Test
	public void testCrossResourceBoundaries() throws FHIRException {
		Specimen specimen = new Specimen();
		specimen.setId("#FOO");
		specimen.setReceivedTimeElement(new DateTimeType("2011-01-01"));
		Observation o = new Observation();
		o.getContained().add(specimen);

		o.setId("O1");
		o.setStatus(Observation.ObservationStatus.FINAL);
		o.setSpecimen(new Reference("#FOO"));

		List<Base> value = ourEngine.evaluate(o, "Observation.specimen.resolve().receivedTime");
		assertEquals(1, value.size());
		assertEquals("2011-01-01", ((DateTimeType) value.get(0)).getValueAsString());
	}

	@Test
	public void testExistsWithNoValue() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType());
		List<Base> eval = ourEngine.evaluate(patient, "Patient.deceased.exists()");
		ourLog.info(eval.toString());
		assertFalse(((BooleanType) eval.get(0)).getValue());
	}

	@Test
	public void testExistsWithValue() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType(false));
		List<Base> eval = ourEngine.evaluate(patient, "Patient.deceased.exists()");
		ourLog.info(eval.toString());
		assertTrue(((BooleanType) eval.get(0)).getValue());
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
