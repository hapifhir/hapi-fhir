package org.hl7.fhir.r4.utils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.utils.FhirPathEngineTest;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
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

import static org.assertj.core.api.Assertions.assertThat;

public class FhirPathEngineR4Test extends BaseValidationTestWithInlineMocks {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirPathEngineTest.class);
	private static FHIRPathEngine ourEngine;

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

		value = ourCtx.newFhirPath().evaluate(o, "Observation.specimen", Base.class);
		assertThat(value).hasSize(1);
		value = ourCtx.newFhirPath().evaluate(o, "Observation.specimen.resolve()", Base.class);
		assertThat(value).hasSize(1);


		value = ourCtx.newFhirPath().evaluate(o, "Observation.specimen.resolve().receivedTime", Base.class);
		assertThat(value).hasSize(1);
		assertThat(((DateTimeType) value.get(0)).getValueAsString()).isEqualTo("2011-01-01");
	}

	@Test
	public void testComponentCode() {
		String path = "(Observation.component.value.ofType(FHIR.Quantity)) ";

		Observation o1 = new Observation();
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code1").setValue(200));
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code2")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code2").setValue(200));

		List<Base> outcome = ourCtx.newFhirPath().evaluate(o1, path, Base.class);
		assertThat(outcome).hasSize(2);

	}


	@Test
	public void testAs() {
		Observation obs = new Observation();
		obs.setValue(new StringType("FOO"));

		// Allow for bad casing on primitive type names - this is a common mistake and
		// even some 4.0.1 SPs use it

		List<Base> value = ourCtx.newFhirPath().evaluate(obs, "Observation.value.as(string)", Base.class);
		assertThat(value).hasSize(1);
		assertThat(((StringType) value.get(0)).getValue()).isEqualTo("FOO");

		value = ourCtx.newFhirPath().evaluate(obs, "Observation.value.as(FHIR.string)", Base.class);
		assertThat(value).hasSize(1);
		assertThat(((StringType) value.get(0)).getValue()).isEqualTo("FOO");

		value = ourCtx.newFhirPath().evaluate(obs, "Observation.value.as(String)", Base.class);
		assertThat(value).hasSize(1);
		assertThat(((StringType) value.get(0)).getValue()).isEqualTo("FOO");

		value = ourCtx.newFhirPath().evaluate(obs, "Observation.value.as(FHIR.String)", Base.class);
		assertThat(value).hasSize(1);
		assertThat(((StringType) value.get(0)).getValue()).isEqualTo("FOO");
	}
	
	@Test
	public void testExistsWithNoValue() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType());
		List<Base> eval = ourCtx.newFhirPath().evaluate(patient, "Patient.deceased.exists()", Base.class);
		ourLog.info(eval.toString());
		assertThat(((BooleanType) eval.get(0)).getValue()).isFalse();
	}

	@Test
	public void testApproxEquivalent() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType());
		testEquivalent(patient, "@2012-04-15 ~ @2012-04-15", true);
		testEquivalent(patient, "@2012-04-15 ~ @2012-04-15T10:00:00", false);
	}

	@Test
	public void testApproxNotEquivalent() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType());
		testEquivalent(patient, "@2012-04-15 !~ @2012-04-15", false);
		testEquivalent(patient, "@2012-04-15 !~ @2012-04-15T10:00:00", true);
	}


	private void testEquivalent(Patient thePatient, String theExpression, boolean theExpected) throws FHIRException {
		List<Base> eval = ourCtx.newFhirPath().evaluate(thePatient, theExpression, Base.class);
		assertThat(((BooleanType) eval.get(0)).getValue()).isEqualTo(theExpected);
	}

	@Test
	public void testExistsWithValue() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType(false));
		List<Base> eval = ourCtx.newFhirPath().evaluate(patient, "Patient.deceased.exists()", Base.class);
		ourLog.info(eval.toString());
		assertThat(((BooleanType) eval.get(0)).getValue()).isTrue();
	}

	@Test
	public void testConcatenation() throws FHIRException {
		String exp = "Patient.name.family & '.'";

		Patient p = new Patient();
		p.addName().setFamily("TEST");
		String result = ourCtx.newFhirPath().evaluate(p, exp, IPrimitiveType.class).get(0).getValueAsString();
		assertThat(result).isEqualTo("TEST.");
	}

	@Test
	public void testStringCompare() throws FHIRException {
		String exp = "element.first().path.startsWith(%resource.type().name) and element.tail().all(path.startsWith(%resource.type().name & '.'))";

		StructureDefinition sd = new StructureDefinition();
		StructureDefinition.StructureDefinitionDifferentialComponent diff = sd.getDifferential();

		diff.addElement().setPath("Patient.name");


		Patient p = new Patient();
		p.addName().setFamily("TEST");
		List<Base> result = ourEngine.evaluate(null, p, null, diff, exp);
		ourLog.info(result.toString());
		assertThat(((BooleanType) result.get(0)).booleanValue()).isTrue();
	}

	@Test
	public void testQuestionnaireResponseExpression() {

		QuestionnaireResponse qr = new QuestionnaireResponse();
		QuestionnaireResponse.QuestionnaireResponseItemComponent parent = qr.addItem().setLinkId("PARENT");
		QuestionnaireResponse.QuestionnaireResponseItemComponent child = parent.addItem().setLinkId("CHILD");
		child.addAnswer().setValue(new DateTimeType("2019-01-01"));

		String path = "QuestionnaireResponse.item.where(linkId = 'PARENT').item.where(linkId = 'CHILD').answer.value.as(FHIR.dateTime)";
		List<Base> answer = ourCtx.newFhirPath().evaluate(qr, path, Base.class);
		assertThat(((DateTimeType) answer.get(0)).getValueAsString()).isEqualTo("2019-01-01");

	}


	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() {
		ourEngine = new FHIRPathEngine(new HapiWorkerContext(ourCtx, ourCtx.getValidationSupport()));
	}

}
