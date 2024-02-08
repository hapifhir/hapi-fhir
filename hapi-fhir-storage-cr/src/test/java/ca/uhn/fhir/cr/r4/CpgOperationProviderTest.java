package ca.uhn.fhir.cr.r4;


import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.opencds.cqf.fhir.utility.r4.Parameters.booleanPart;
import static org.opencds.cqf.fhir.utility.r4.Parameters.canonicalPart;
import static org.opencds.cqf.fhir.utility.r4.Parameters.parameters;
import static org.opencds.cqf.fhir.utility.r4.Parameters.part;
import static org.opencds.cqf.fhir.utility.r4.Parameters.datePart;
import static org.opencds.cqf.fhir.utility.r4.Parameters.stringPart;

public class CpgOperationProviderTest extends BaseCrR4TestServer{
	@BeforeEach
	void setup() {
		var reqDeets = setupRequestDetails();
		loadResource(Library.class, "SimpleR4Library.json", reqDeets);
		loadResource(Patient.class, "SimplePatient.json", reqDeets);
		loadResource(Observation.class, "SimpleObservation.json", reqDeets);
		loadResource(Condition.class, "SimpleCondition.json", reqDeets);
	}

	@Test
	void cqlExecutionProvider_testSimpleDate() {
		// execute cql expression on date interval
		Parameters params = parameters(stringPart("expression", "Interval[Today() - 2 years, Today())"));
		Parameters results = runCqlExecution(params);
		assertThat(results.getParameter("return").getValue() instanceof Period).isTrue();
	}

	@Test
	void cqlExecutionProvider_testSimpleArithmetic() {
		// execute simple cql expression
		Parameters params = parameters(stringPart("expression", "5 * 5"));
		Parameters results = runCqlExecution(params);
		assertThat(results.getParameter("return").getValue() instanceof IntegerType).isTrue();
		assertThat(((IntegerType) results.getParameter("return").getValue()).asStringValue()).isEqualTo("25");
	}

	@Test
	void evaluateLibraryProvider_testLibraryWithSubject() {
		// evaluate library resource for a subject
		var params = new Parameters();
		params.addParameter("subject", new StringType("Patient/SimplePatient"));

		Parameters report = runEvaluateLibrary(params, "SimpleR4Library");

		assertThat(report).isNotNull();
		assertThat(report.hasParameter("Initial Population")).isTrue();
		assertThat(((BooleanType) report.getParameter("Initial Population").getValue()).booleanValue()).isTrue();
		assertThat(report.hasParameter("Numerator")).isTrue();
		assertThat(((BooleanType) report.getParameter("Numerator").getValue()).booleanValue()).isTrue();
		assertThat(report.hasParameter("Denominator")).isTrue();
		assertThat(((BooleanType) report.getParameter("Denominator").getValue()).booleanValue()).isTrue();
	}

	@Test
	void evaluateLibraryProvider_testSimpleExpression() {
		// evaluate expression for subject from specified library resource
		var params = new Parameters();
		params.addParameter("subject", new StringType("Patient/SimplePatient"));
		params.addParameter("expression", "Numerator");

		Parameters report = runEvaluateLibrary(params, "SimpleR4Library");
		assertThat(report).isNotNull();
		assertThat(report.hasParameter("Numerator")).isTrue();
		assertThat(((BooleanType) report.getParameter("Numerator").getValue()).booleanValue()).isTrue();
	}

	@Test
	void cqlExecutionProvider_testReferencedLibrary() {
		// execute cql expression from referenced library on subject
		Parameters libraryParameter = parameters(
			canonicalPart("url", ourClient.getServerBase() + "/Library/SimpleR4Library|0.0.1"),
			stringPart("name", "SimpleR4Library"));
		Parameters params = parameters(
			stringPart("subject", "SimplePatient"),
			part("library", libraryParameter),
			stringPart("expression", "SimpleR4Library.simpleBooleanExpression"));

		Parameters results = runCqlExecution(params);
		assertThat(results.getParameter("return").getValue() instanceof BooleanType).isTrue();
		assertThat(((BooleanType) results.getParameter("return").getValue()).booleanValue()).isTrue();
	}

	@Test
	void cqlExecutionProvider_testDataBundle() {
		// execute cql expression from library over data from bundle with no subject
		Parameters libraryParameter = parameters(
			canonicalPart("url", ourClient.getServerBase() + "/Library/SimpleR4Library"),
			stringPart("name", "SimpleR4Library"));
		//var data = loadBundle(Bundle.class,"SimpleDataBundle.json");
		var data = (Bundle) readResource("SimpleDataBundle.json");
		Parameters params = parameters(
			part("library", libraryParameter),
			stringPart("expression", "SimpleR4Library.\"observationRetrieve\""),
			part("data", data),
			booleanPart("useServerData", false));

		Parameters results =  runCqlExecution(params);
		assertThat(results.getParameter().get(0).getResource() instanceof Observation).isTrue();
	}

	@Test
	void cqlExecutionProvider_testDataBundleWithSubject() {
		// execute cql expression from library over data from bundle with subject
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
		assertThat(results.getParameter().get(0).getResource() instanceof Observation).isTrue();
	}

	@Test
	void cqlExecutionProvider_testSimpleParameters() {
		// execute inline cql date expression with input valuemv
		Parameters evaluationParams = parameters(
			datePart("%inputDate", "2019-11-01"));
		Parameters params = parameters(
			stringPart("expression", "year from %inputDate before 2020"),
			part("parameters", evaluationParams));
		Parameters results = runCqlExecution(params);
		assertThat(results.getParameter("return").getValue() instanceof BooleanType).isTrue();
		assertThat(((BooleanType) results.getParameter("return").getValue()).booleanValue()).isTrue();
	}

	@Test
	void cqlExecutionProvider_testExpression() {
		// execute cql expression from referenced library
		Parameters libraryParameter = parameters(
			canonicalPart("url", ourClient.getServerBase() + "/Library/SimpleR4Library"),
			stringPart("name", "SimpleR4Library"));
		Parameters params = parameters(
			stringPart("subject", "SimplePatient"),
			part("library", libraryParameter),
			stringPart("expression", "SimpleR4Library.\"Numerator\"")
			);

		Parameters results = runCqlExecution(params);

		assertThat(results.isEmpty()).isFalse();
		assertThat(results.getParameter().size()).isEqualTo(1);
		assertThat(results.getParameter("return").getValue() instanceof BooleanType).isTrue();
		assertThat(((BooleanType) results.getParameter("return").getValue()).booleanValue()).isTrue();
	}

	@Test
	void cqlExecutionProvider_testErrorExpression() {
		// execute invalid cql expression
		Parameters params = parameters(stringPart("expression", "Interval[1,5]"));

		Parameters results = runCqlExecution(params);

		assertThat(results.hasParameter()).isTrue();
		assertThat(results.getParameterFirstRep().hasName()).isTrue();
		assertThat(results.getParameterFirstRep().getName()).isEqualTo("evaluation error");
		assertThat(results.getParameterFirstRep().hasResource()).isTrue();
		assertThat(results.getParameterFirstRep().getResource() instanceof OperationOutcome).isTrue();
		assertThat(((OperationOutcome) results.getParameterFirstRep().getResource()).getIssueFirstRep().getDetails()
				.getText()).isEqualTo("Unsupported interval point type for FHIR conversion java.lang.Integer");
	}

	public Parameters runCqlExecution(Parameters parameters){

		return ourClient.operation().onServer()
			.named(ProviderConstants.CR_OPERATION_CQL)
			.withParameters(parameters)
			.execute();
	}
	public Parameters runEvaluateLibrary(Parameters parameters, String libraryId){

		return ourClient.operation().onInstance("Library/" + libraryId)
			.named(ProviderConstants.CR_OPERATION_EVALUATE)
			.withParameters(parameters)
			.execute();
	}
}
