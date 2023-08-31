package ca.uhn.fhir.cr.r4;


import ca.uhn.fhir.cr.r4.cqlexecution.CqlExecutionOperationProvider;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.List;

import static org.flywaydb.core.api.configuration.S3ClientFactory.getClient;
import static org.opencds.cqf.fhir.utility.r4.Parameters.booleanPart;
import static org.opencds.cqf.fhir.utility.r4.Parameters.canonicalPart;
import static org.opencds.cqf.fhir.utility.r4.Parameters.parameters;
import static org.opencds.cqf.fhir.utility.r4.Parameters.part;
import static org.opencds.cqf.fhir.utility.r4.Parameters.datePart;
import static org.opencds.cqf.fhir.utility.r4.Parameters.stringPart;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CqlExecutionOperationProviderTest extends BaseCrR4TestServer{
	@Autowired
	CqlExecutionOperationProvider myCqlExecutionProvider;
	@BeforeEach
	void setup() throws IOException {
		loadResource("SimpleR4Library.json");
		loadResource("SimplePatient.json");
		loadResource("SimpleObservation.json");
		loadResource("SimpleCondition.json");
	}
	public Parameters runCqlExecution(Parameters parameters){

		var results = ourClient.operation().onServer()
			.named("$cql")
			.withParameters(parameters)
			.execute();
		return results;
	}
	@Test
	void testSimpleArithmeticCqlExecutionProvider() {
		loadBundle("Exm104FhirR4MeasureBundle.json");
		Parameters params = parameters(stringPart("expression", "5 * 5"));
		Parameters results = runCqlExecution(params);
		assertTrue(results.getParameter("return").getValue() instanceof IntegerType);
		assertEquals("25", ((IntegerType) results.getParameter("return").getValue()).asStringValue());
	}

	@Test
	void testReferencedLibraryCqlExecutionProvider() {
		loadBundle("Exm104FhirR4MeasureBundle.json");
		var test1 = ourClient.read().resource(Library.class).withId("library-EXM104");
		var test = ourClient.read().resource(Library.class).withId("SimpleR4Library");
		Parameters libraryParameter = parameters(
			canonicalPart("url", ourClient.getServerBase() + "/Library/SimpleR4Library|0.0.1"),
			stringPart("name", "SimpleR4Library"));
		Parameters params = parameters(
			stringPart("subject", "SimplePatient"),
			part("library", libraryParameter),
			stringPart("expression", "SimpleR4Library.simpleBooleanExpression"));

		Parameters results = runCqlExecution(params);
		assertTrue(results.getParameter("return").getValue() instanceof BooleanType);
		assertTrue(((BooleanType) results.getParameter("return").getValue()).booleanValue());
	}

	@Test
	void testDataBundleCqlExecutionProvider() throws IOException {
		Parameters libraryParameter = parameters(
			canonicalPart("url", this.ourClient.getServerBase() + "Library/SimpleR4Library"),
			stringPart("name", "SimpleR4Library"));
		Bundle data = (Bundle) loadBundle(Bundle.class,"SimpleDataBundle.json");
		Parameters params = parameters(
			part("library", libraryParameter),
			stringPart("expression", "SimpleR4Library.\"observationRetrieve\""),
			part("data", data),
			booleanPart("useServerData", false));

		Parameters results =  runCqlExecution(params);
		assertTrue(results.getParameter().get(0).getResource() instanceof Observation);
	}

	@Test
	void testDataBundleCqlExecutionProviderWithSubject() {
		Parameters libraryParameter = parameters(
			canonicalPart("url", ourClient.getServerBase() + "Library/SimpleR4Library"),
			stringPart("name", "SimpleR4Library"));
		Bundle data = (Bundle) loadBundle(Bundle.class,"SimpleDataBundle.json");
		Parameters params = parameters(
			stringPart("subject", "SimplePatient"),
			part("library", libraryParameter),
			stringPart("expression", "SimpleR4Library.\"observationRetrieve\""),
			part("data", data),
			booleanPart("useServerData", false));
		Parameters results = runCqlExecution(params);
		assertTrue(results.getParameter().get(0).getResource() instanceof Observation);
	}

	@Test
	void testSimpleParametersCqlExecutionProvider() {
		Parameters evaluationParams = parameters(
			datePart("%inputDate", "2019-11-01"));
		Parameters params = parameters(
			stringPart("expression", "year from %inputDate before 2020"),
			part("parameters", evaluationParams));
		Parameters results = runCqlExecution(params);
		assertTrue(results.getParameter("return").getValue() instanceof BooleanType);
		assertTrue(((BooleanType) results.getParameter("return").getValue()).booleanValue());
	}

	@Test
	void testCqlExecutionProviderWithContent() {
		Parameters params = parameters(
			stringPart("subject", "SimplePatient"),
			stringPart("content", "library SimpleR4Library\n" +
				"\n" +
				"using FHIR version '4.0.1'\n" +
				"\n" +
				"include FHIRHelpers version '4.0.1' called FHIRHelpers\n" +
				"\n" +
				"context Patient\n" +
				"\n" +
				"define simpleBooleanExpression: true\n" +
				"\n" +
				"define observationRetrieve: [Observation]\n" +
				"\n" +
				"define observationHasCode: not IsNull(([Observation]).code)\n" +
				"\n" +
				"define \"Initial Population\": observationHasCode\n" +
				"\n" +
				"define \"Denominator\": \"Initial Population\"\n" +
				"\n" +
				"define \"Numerator\": \"Denominator\""));

		Parameters results = runCqlExecution(params);

		assertFalse(results.isEmpty());
		assertEquals(7, results.getParameter().size());
		assertTrue(results.hasParameter("Patient"));
		assertTrue(results.getParameter().get(0).hasResource());
		assertTrue(results.getParameter().get(0).getResource() instanceof Patient);
		assertTrue(results.hasParameter("simpleBooleanExpression"));
		assertTrue(results.getParameter("simpleBooleanExpression").getValue() instanceof BooleanType);
		assertTrue(((BooleanType) results.getParameter("simpleBooleanExpression").getValue()).booleanValue());
		assertTrue(results.hasParameter("observationRetrieve"));
		assertTrue(results.getParameter().get(2).hasResource());
		assertTrue(results.getParameter().get(2).getResource() instanceof Observation);
		assertTrue(results.hasParameter("observationHasCode"));
		assertTrue(results.getParameter("observationHasCode").getValue() instanceof BooleanType);
		assertTrue(((BooleanType) results.getParameter("observationHasCode").getValue()).booleanValue());
		assertTrue(results.hasParameter("Initial Population"));
		assertTrue(results.getParameter("Initial Population").getValue() instanceof BooleanType);
		assertTrue(((BooleanType) results.getParameter("Initial Population").getValue()).booleanValue());
		assertTrue(results.hasParameter("Numerator"));
		assertTrue(results.getParameter("Numerator").getValue() instanceof BooleanType);
		assertTrue(((BooleanType) results.getParameter("Numerator").getValue()).booleanValue());
		assertTrue(results.hasParameter("Denominator"));
		assertTrue(results.getParameter("Denominator").getValue() instanceof BooleanType);
		assertTrue(((BooleanType) results.getParameter("Denominator").getValue()).booleanValue());
	}

	@Test
	void testCqlExecutionProviderWithContentAndExpression() {
		Parameters params = parameters(
			stringPart("subject", "SimplePatient"),
			stringPart("expression", "Numerator"),
			stringPart("content", "library SimpleR4Library\n" +
				"\n" +
				"using FHIR version '4.0.1'\n" +
				"\n" +
				"include FHIRHelpers version '4.0.1' called FHIRHelpers\n" +
				"\n" +
				"context Patient\n" +
				"\n" +
				"define simpleBooleanExpression: true\n" +
				"\n" +
				"define observationRetrieve: [Observation]\n" +
				"\n" +
				"define observationHasCode: not IsNull(([Observation]).code)\n" +
				"\n" +
				"define \"Initial Population\": observationHasCode\n" +
				"\n" +
				"define \"Denominator\": \"Initial Population\"\n" +
				"\n" +
				"define \"Numerator\": \"Denominator\""));

		Parameters results = runCqlExecution(params);

		assertFalse(results.isEmpty());
		assertEquals(1, results.getParameter().size());
		assertTrue(results.getParameter("Numerator").getValue() instanceof BooleanType);
		assertTrue(((BooleanType) results.getParameter("Numerator").getValue()).booleanValue());
	}

	@Test
	void testContentRetrieveWithInlineCode() {
		Parameters params = parameters(
			stringPart("subject", "SimplePatient"),
			stringPart("content", "library AsthmaTest version '1.0.0'\n" +
				"\n" +
				"using FHIR version '4.0.1'\n" +
				"\n" +
				"include FHIRHelpers version '4.0.1'\n" +
				"\n" +
				"codesystem \"SNOMED\": 'http://snomed.info/sct'\n" +
				"\n" +
				"code \"Asthma\": '195967001' from \"SNOMED\"\n" +
				"\n" +
				"context Patient\n" +
				"\n" +
				"define \"Asthma Diagnosis\":\n" +
				"    [Condition: \"Asthma\"]\n" +
				"\n" +
				"define \"Has Asthma Diagnosis\":\n" +
				"    exists(\"Asthma Diagnosis\")\n"));

		Parameters results = runCqlExecution(params);

		assertTrue(results.hasParameter());
		assertEquals(3, results.getParameter().size());
		assertTrue(results.getParameterFirstRep().hasResource());
		assertTrue(results.getParameterFirstRep().getResource() instanceof Patient);
		assertTrue(results.getParameter().get(1).hasResource());
		assertTrue(results.getParameter().get(1).getResource() instanceof Condition);
		assertTrue(results.getParameter().get(2).hasValue());
		assertTrue(((BooleanType) results.getParameter().get(2).getValue()).booleanValue());
	}

	@Test
	void testErrorExpression() {
		Parameters params = parameters(stringPart("expression", "Interval[1,5]"));
		Parameters results = runCqlExecution(params);
		assertTrue(results.hasParameter());
		assertTrue(results.getParameterFirstRep().hasName());
		assertEquals("evaluation error", results.getParameterFirstRep().getName());
		assertTrue(results.getParameterFirstRep().hasResource());
		assertTrue(results.getParameterFirstRep().getResource() instanceof OperationOutcome);
		assertEquals("Unsupported interval point type for FHIR conversion java.lang.Integer",
			((OperationOutcome) results.getParameterFirstRep().getResource()).getIssueFirstRep().getDetails()
				.getText());
	}
}
