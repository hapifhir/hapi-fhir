package ca.uhn.fhir.cr.r4;


import ca.uhn.fhir.cr.r4.cqlexecution.CqlExecutionOperationProvider;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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
		var reqDeets = setupRequestDetails();
		loadResource(Library.class, "SimpleR4Library.json", reqDeets);
		loadResource(Patient.class, "SimplePatient.json", reqDeets);
		loadResource(Observation.class, "SimpleObservation.json", reqDeets);
		loadResource(Condition.class, "SimpleCondition.json", reqDeets);
	}
	public Parameters runCqlExecution(Parameters parameters){

		var results = ourClient.operation().onServer()
			.named("$cql")
			.withParameters(parameters)
			.execute();
		return results;
	}
	@Test
	void testSimpleDateCqlExecutionProvider() {
		Parameters params = parameters(stringPart("expression", "Interval[Today() - 2 years, Today())"));
		Parameters results = runCqlExecution(params);
		assertTrue(results.getParameter("return").getValue() instanceof Period);
	}

	@Test
	void testSimpleArithmeticCqlExecutionProvider() {
		Parameters params = parameters(stringPart("expression", "5 * 5"));
		Parameters results = runCqlExecution(params);
		assertTrue(results.getParameter("return").getValue() instanceof IntegerType);
		assertEquals("25", ((IntegerType) results.getParameter("return").getValue()).asStringValue());
	}
	@Test
	void testReferencedLibraryCqlExecutionProvider() {

		var test = ourClient.read().resource(Library.class).withId("SimpleR4Library").execute();
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
			canonicalPart("url", this.ourClient.getServerBase() + "/Library/SimpleR4Library"),
			stringPart("name", "SimpleR4Library"));
		//var data = loadBundle(Bundle.class,"SimpleDataBundle.json");
		var data = (Bundle) readResource("SimpleDataBundle.json");
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
			canonicalPart("url", ourClient.getServerBase() + "/Library/SimpleR4Library"),
			stringPart("name", "SimpleR4Library"));
		var data = (Bundle) readResource("SimpleDataBundle.json");
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
	void testCqlExecutionProviderExpression() {
		Parameters libraryParameter = parameters(
			canonicalPart("url", ourClient.getServerBase() + "/Library/SimpleR4Library"),
			stringPart("name", "SimpleR4Library"));
		Parameters params = parameters(
			stringPart("subject", "SimplePatient"),
			part("library", libraryParameter),
			stringPart("expression", "SimpleR4Library.\"Numerator\"")
			);

		Parameters results = runCqlExecution(params);

		assertFalse(results.isEmpty());
		assertEquals(1, results.getParameter().size());
		assertTrue(results.getParameter("return").getValue() instanceof BooleanType);
		assertTrue(((BooleanType) results.getParameter("returnf").getValue()).booleanValue());
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
