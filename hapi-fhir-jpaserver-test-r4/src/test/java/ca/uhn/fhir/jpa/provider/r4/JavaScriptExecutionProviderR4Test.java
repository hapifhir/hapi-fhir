package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.provider.JavaScriptExecutionProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JavaScriptExecutionProviderR4Test extends BaseResourceProviderR4Test {

	private JavaScriptExecutionProvider myProvider;

	@BeforeEach
	void registerProvider() throws URISyntaxException {
		myStorageSettings.setJavaScriptExecutionScriptsDirectory(findScriptsDirectory());
		// Keep the timeout short so the infinite-loop test finishes quickly.
		myStorageSettings.setJavaScriptExecutionTimeoutSeconds(3);

		myProvider = new JavaScriptExecutionProvider(myFhirContext, myDaoRegistry, myStorageSettings);
		myServer.registerProvider(myProvider);
	}

	@AfterEach
	void unregisterProvider() {
		myServer.unregisterProvider(myProvider);

		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setJavaScriptExecutionScriptsDirectory(defaults.getJavaScriptExecutionScriptsDirectory());
		myStorageSettings.setJavaScriptExecutionTimeoutSeconds(defaults.getJavaScriptExecutionTimeoutSeconds());
	}

	private String findScriptsDirectory() throws URISyntaxException {
		return new File(Objects.requireNonNull(getClass().getClassLoader().getResource("javascript"))
						.toURI())
				.getAbsolutePath();
	}

	private Parameters callOperation(Parameters theInput) {
		return myClient.operation()
				.onServer()
				.named(ProviderConstants.OPERATION_EXECUTE_JAVASCRIPT)
				.withParameters(theInput)
				.execute();
	}

	private Parameters execute(String theScriptName, Resource... theResources) {
		Parameters input = new Parameters();
		input.addParameter().setName("script").setValue(new StringType(theScriptName));
		for (Resource resource : theResources) {
			input.addParameter().setName("resource").setResource(resource);
		}
		return callOperation(input);
	}

	private Patient newPatient(String theFamily) {
		Patient patient = new Patient();
		patient.addName().setFamily(theFamily);
		return patient;
	}

	@Test
	void transformsBothInputResources() {
		Patient patientA = newPatient("A");
		patientA.setActive(false);
		Patient patientB = newPatient("B");
		patientB.setActive(false);

		Parameters output = execute("set-active", patientA, patientB);

		List<Parameters.ParametersParameterComponent> returns = output.getParameter();
		assertThat(returns).hasSize(2);
		assertThat(returns.get(0).getName()).isEqualTo("return");
		assertThat(((Patient) returns.get(0).getResource()).getActive()).isTrue();
		assertThat(((Patient) returns.get(1).getResource()).getActive()).isTrue();
	}

	@Test
	void mergesTwoResourcesIntoOne() {
		// A script can read both inputs (input[0], input[1]) and emit a single combined resource.
		Parameters output = execute("merge", newPatient("First"), newPatient("Second"));

		assertThat(output.getParameter()).hasSize(1);
		Patient merged = (Patient) output.getParameter().get(0).getResource();
		assertThat(merged.getName().get(0).getFamily()).isEqualTo("First");
		assertThat(merged.getName().get(1).getFamily()).isEqualTo("Second");
	}

	@Test
	void acceptsASingleObjectResult() {
		Parameters output = execute("set-gender", newPatient("A"));

		assertThat(output.getParameter()).hasSize(1);
		Patient result = (Patient) output.getParameter().get(0).getResource();
		assertThat(result.getGender()).isEqualTo(Enumerations.AdministrativeGender.FEMALE);
	}

	@Test
	void acceptsScriptNameWithJsSuffix() {
		Parameters output = execute("set-gender.js", newPatient("A"));
		assertThat(output.getParameter()).hasSize(1);
	}

	@Test
	void canSynthesizeANewResource() {
		Parameters output = execute("synthesize", newPatient("A"));

		assertThat(output.getParameter()).hasSize(1);
		Observation observation =
				(Observation) output.getParameter().get(0).getResource();
		assertThat(observation.getStatus()).isEqualTo(Observation.ObservationStatus.FINAL);
	}

	@Test
	void emptyResultProducesEmptyParameters() {
		Parameters output = execute("empty", newPatient("A"));
		assertThat(output.getParameter()).isEmpty();
	}

	@Test
	void resolvesLiteralReferencesBeforeExecution() {
		// Store a resource on the server, then reference it (rather than inlining it).
		IIdType id = createPatient(withFamily("Stored"), withActiveFalse()).toUnqualifiedVersionless();

		Parameters input = new Parameters();
		input.addParameter().setName("script").setValue(new StringType("set-active"));
		input.addParameter().setName("reference").setValue(new Reference(id.getValue()));

		Parameters output = callOperation(input);

		assertThat(output.getParameter()).hasSize(1);
		Patient result = (Patient) output.getParameter().get(0).getResource();
		assertThat(result.getNameFirstRep().getFamily()).isEqualTo("Stored");
		assertThat(result.getActive()).isTrue();
	}

	@Test
	void mixesInlineResourcesAndReferences() {
		IIdType id = createPatient(withFamily("FromServer")).toUnqualifiedVersionless();

		Parameters input = new Parameters();
		input.addParameter().setName("script").setValue(new StringType("merge")); // uses input[0] + input[1]
		input.addParameter().setName("resource").setResource(newPatient("Inline")); // input[0]
		input.addParameter().setName("reference").setValue(new Reference(id.getValue())); // input[1]

		Parameters output = callOperation(input);

		assertThat(output.getParameter()).hasSize(1);
		Patient merged = (Patient) output.getParameter().get(0).getResource();
		assertThat(merged.getName().get(0).getFamily()).isEqualTo("Inline");
		assertThat(merged.getName().get(1).getFamily()).isEqualTo("FromServer");
	}

	@Test
	void unresolvableReferenceIsRejected() {
		Parameters input = new Parameters();
		input.addParameter().setName("script").setValue(new StringType("set-active"));
		input.addParameter().setName("reference").setValue(new Reference("Patient/does-not-exist"));

		assertThatThrownBy(() -> callOperation(input)).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void acceptsZeroResources() {
		// 'input' is simply an empty array; a script can ignore it and synthesize output.
		Parameters output = execute("synthesize");
		assertThat(output.getParameter()).hasSize(1);
		assertThat(output.getParameter().get(0).getResource()).isInstanceOf(Observation.class);
	}

	@Test
	void scriptErrorIsReportedAsBadRequest() {
		assertThatThrownBy(() -> execute("error")).isInstanceOf(InvalidRequestException.class);
	}

	@Test
	void nonResourceResultIsRejected() {
		assertThatThrownBy(() -> execute("non-resource")).isInstanceOf(InvalidRequestException.class);
	}

	@Test
	@Timeout(20)
	void longRunningScriptIsStoppedByTimeout() {
		// The 3s timeout configured above must fire and abort the infinite loop, well within
		// this test's own 20s budget.
		assertThatThrownBy(() -> execute("infinite-loop"))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("timed out");
	}

	@Test
	void unknownScriptIsRejected() {
		assertThatThrownBy(() -> execute("does-not-exist")).isInstanceOf(InvalidRequestException.class);
	}

	@Test
	void pathTraversalIsRejected() {
		assertThatThrownBy(() -> execute("../application")).isInstanceOf(InvalidRequestException.class);
	}

	@Test
	void javaAccessIsBlockedBySandbox() {
		// The sandbox denies all host access, so Java.type(...) must fail rather than
		// giving the script a handle on the host JVM.
		assertThatThrownBy(() -> execute("java-access")).isInstanceOf(InvalidRequestException.class);
	}

	@Test
	void unconfiguredScriptsDirectoryIsRejected() {
		myStorageSettings.setJavaScriptExecutionScriptsDirectory(null);

		assertThatThrownBy(() -> execute("set-active"))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("not configured");
	}
}
