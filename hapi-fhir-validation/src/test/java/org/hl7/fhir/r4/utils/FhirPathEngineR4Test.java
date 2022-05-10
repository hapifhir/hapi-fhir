package org.hl7.fhir.r4.utils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.utils.FhirPathEngineTest;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Specimen;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirPathEngineR4Test {

	private static FhirContext ourCtx = FhirContext.forR4();
	private static FHIRPathEngine ourEngine;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirPathEngineTest.class);

	@Test
	public void testCrossResourceBoundaries() throws FHIRException {
		Specimen specimen = new Specimen();
		specimen.setReceivedTimeElement(new DateTimeType("2011-01-01"));

		Observation o = new Observation();
		o.setId("O1");
		o.setStatus(Observation.ObservationStatus.FINAL);
		o.setSpecimen(new Reference(specimen));

		IParser p = ourCtx.newJsonParser();
		o = (Observation) p.parseResource(p.encodeResourceToString(o));

		List<Base> value;

//		value = ourEngine.evaluate(o, "Observation.specimen");
//		assertEquals(1, value.size());
		value = ourEngine.evaluate(o, "Observation.specimen.resolve()");
		assertEquals(1, value.size());


		value = ourEngine.evaluate(o, "Observation.specimen.resolve().receivedTime");
		assertEquals(1, value.size());
		assertEquals("2011-01-01", ((DateTimeType) value.get(0)).getValueAsString());
	}

	@Test
	public void testComponentCode() {
		String path = "(Observation.component.value as Quantity) ";

		Observation o1 = new Observation();
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code1").setValue(200));
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code2")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code2").setValue(200));

		List<Base> outcme = ourEngine.evaluate(o1, path);
		assertEquals(2, outcme.size());

	}


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
	public void testApproxEquivalent() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType());
		testEquivalent(patient, "@2012-04-15 ~ @2012-04-15",true);
		testEquivalent(patient, "@2012-04-15 ~ @2012-04-15T10:00:00",true);
	}

	@Test
	public void testApproxNotEquivalent() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType());
		testEquivalent(patient, "@2012-04-15 !~ @2012-04-15",false);
		testEquivalent(patient, "@2012-04-15 !~ @2012-04-15T10:00:00",false);
	}


	private void testEquivalent(Patient thePatient, String theExpression, boolean theExpected) throws FHIRException {
		List<Base> eval = ourEngine.evaluate(thePatient, theExpression);
		assertEquals(theExpected, ((BooleanType)eval.get(0)).getValue());
	}

	@Test
	public void testExistsWithValue() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType(false));
		List<Base> eval = ourEngine.evaluate(patient, "Patient.deceased.exists()");
		ourLog.info(eval.toString());
		assertTrue(((BooleanType)eval.get(0)).getValue());
	}

	@Test
	public void testConcatenation() throws FHIRException {
		String exp = "Patient.name.family & '.'";

		Patient p = new Patient();
		p.addName().setFamily("TEST");
		String result = ourEngine.evaluateToString(p, exp);
		assertEquals("TEST.", result);
	}

	@Test
	public void testStringCompare() throws FHIRException {
		String exp = "element.first().path.startsWith(%resource.type) and element.tail().all(path.startsWith(%resource.type&'.'))";

		StructureDefinition sd = new StructureDefinition();
		StructureDefinition.StructureDefinitionDifferentialComponent diff = sd.getDifferential();

		diff.addElement().setPath("Patient.name");


		Patient p = new Patient();
		p.addName().setFamily("TEST");
		List<Base> result = ourEngine.evaluate(null, p, null, diff, exp);
		ourLog.info(result.toString());
		assertEquals(true, ((BooleanType)result.get(0)).booleanValue());
	}

	@Test
	public void testQuestionnaireResponseExpression() {

		QuestionnaireResponse qr = new QuestionnaireResponse();
		QuestionnaireResponse.QuestionnaireResponseItemComponent parent = qr.addItem().setLinkId("PARENT");
		QuestionnaireResponse.QuestionnaireResponseItemComponent child = parent.addItem().setLinkId("CHILD");
		child.addAnswer().setValue(new DateTimeType("2019-01-01"));

		List<Base> answer = ourEngine.evaluate(qr, "QuestionnaireResponse.item.where(linkId = 'PARENT').item.where(linkId = 'CHILD').answer.value.as(DateTime)");
		assertEquals("2019-01-01", ((DateTimeType)answer.get(0)).getValueAsString());

	}


	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() {
		ourEngine = new FHIRPathEngine(new HapiWorkerContext(ourCtx, new DefaultProfileValidationSupport(ourCtx)));
	}

}
