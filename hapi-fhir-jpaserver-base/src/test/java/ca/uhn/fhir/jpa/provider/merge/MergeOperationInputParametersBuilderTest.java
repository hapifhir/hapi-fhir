package ca.uhn.fhir.jpa.provider.merge;

// Created by claude-sonnet-4-5

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.CanonicalIdentifier;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MergeOperationInputParametersBuilderTest {

	private static final int RESOURCE_LIMIT = 1000;
	private MergeOperationInputParametersBuilder myBuilder;

	@BeforeEach
	void setUp() {
		FhirContext fhirContext = FhirContext.forR4Cached();
		myBuilder = new MergeOperationInputParametersBuilder(fhirContext, RESOURCE_LIMIT);
	}

	@Test
	void testFromParameters_withAllParameters_success() {
		// Arrange
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/123"));
		parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/456"));
		parameters.addParameter().setName("preview").setValue(new BooleanType(true));
		parameters.addParameter().setName("delete-source").setValue(new BooleanType(true));

		Patient resultPatient = new Patient();
		resultPatient.setId("Patient/789");
		parameters.addParameter().setName("result-patient").setResource(resultPatient);

		// Act
		MergeOperationInputParameters result = myBuilder.fromParameters(parameters);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getSourceResource()).isNotNull();
		assertThat(result.getSourceResource().getReferenceElement().getValue()).isEqualTo("Patient/123");
		assertThat(result.getTargetResource()).isNotNull();
		assertThat(result.getTargetResource().getReferenceElement().getValue()).isEqualTo("Patient/456");
		assertThat(result.getPreview()).isTrue();
		assertThat(result.getDeleteSource()).isTrue();
		assertThat(result.getResultResource()).isNotNull();
		assertThat(result.getOriginalInputParameters()).isNotNull();
		assertThat(result.getResourceLimit()).isEqualTo(RESOURCE_LIMIT);
	}

	@Test
	void testFromParameters_withOnlyRequiredParameters_success() {
		// Arrange
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/123"));
		parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/456"));

		// Act
		MergeOperationInputParameters result = myBuilder.fromParameters(parameters);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getSourceResource()).isNotNull();
		assertThat(result.getTargetResource()).isNotNull();
		assertThat(result.getPreview()).isFalse(); // default is false
		assertThat(result.getDeleteSource()).isFalse(); // default is false
		assertThat(result.getResultResource()).isNull();
		assertThat(result.getOriginalInputParameters()).isNotNull();
	}

	@Test
	void testFromParameters_withIdentifiers_success() {
		// Arrange
		Parameters parameters = new Parameters();

		Identifier sourceIdentifier = new Identifier();
		sourceIdentifier.setSystem("http://example.com/mrn");
		sourceIdentifier.setValue("12345");
		parameters.addParameter().setName("source-patient-identifier").setValue(sourceIdentifier);

		Identifier targetIdentifier = new Identifier();
		targetIdentifier.setSystem("http://example.com/mrn");
		targetIdentifier.setValue("67890");
		parameters.addParameter().setName("target-patient-identifier").setValue(targetIdentifier);

		// Act
		MergeOperationInputParameters result = myBuilder.fromParameters(parameters);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getSourceIdentifiers()).isNotNull();
		assertThat(result.getSourceIdentifiers()).hasSize(1);
		CanonicalIdentifier sourceCanonical = result.getSourceIdentifiers().get(0);
		assertThat(sourceCanonical.getSystemElement().getValue()).isEqualTo("http://example.com/mrn");
		assertThat(sourceCanonical.getValueElement().getValue()).isEqualTo("12345");

		assertThat(result.getTargetIdentifiers()).isNotNull();
		assertThat(result.getTargetIdentifiers()).hasSize(1);
		CanonicalIdentifier targetCanonical = result.getTargetIdentifiers().get(0);
		assertThat(targetCanonical.getSystemElement().getValue()).isEqualTo("http://example.com/mrn");
		assertThat(targetCanonical.getValueElement().getValue()).isEqualTo("67890");
	}

	@Test
	void testFromParameters_withMultipleIdentifiers_success() {
		// Arrange
		Parameters parameters = new Parameters();

		Identifier sourceIdentifier1 = new Identifier();
		sourceIdentifier1.setSystem("http://example.com/mrn");
		sourceIdentifier1.setValue("12345");
		parameters.addParameter().setName("source-patient-identifier").setValue(sourceIdentifier1);

		Identifier sourceIdentifier2 = new Identifier();
		sourceIdentifier2.setSystem("http://example.com/ssn");
		sourceIdentifier2.setValue("999-99-9999");
		parameters.addParameter().setName("source-patient-identifier").setValue(sourceIdentifier2);

		Identifier targetIdentifier = new Identifier();
		targetIdentifier.setSystem("http://example.com/mrn");
		targetIdentifier.setValue("67890");
		parameters.addParameter().setName("target-patient-identifier").setValue(targetIdentifier);

		// Act
		MergeOperationInputParameters result = myBuilder.fromParameters(parameters);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getSourceIdentifiers()).hasSize(2);
		assertThat(result.getTargetIdentifiers()).hasSize(1);
	}

	@Test
	void testFromParameters_withReferences_success() {
		// Arrange
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/source-123"));
		parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/target-456"));

		// Act
		MergeOperationInputParameters result = myBuilder.fromParameters(parameters);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getSourceResource()).isNotNull();
		assertThat(result.getSourceResource().getReferenceElement().getValue()).isEqualTo("Patient/source-123");
		assertThat(result.getTargetResource()).isNotNull();
		assertThat(result.getTargetResource().getReferenceElement().getValue()).isEqualTo("Patient/target-456");
	}

	@Test
	void testFromParameters_withPreviewTrue_setsFlag() {
		// Arrange
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/123"));
		parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/456"));
		parameters.addParameter().setName("preview").setValue(new BooleanType(true));

		// Act
		MergeOperationInputParameters result = myBuilder.fromParameters(parameters);

		// Assert
		assertThat(result.getPreview()).isTrue();
	}

	@Test
	void testFromParameters_withPreviewFalse_setsFlag() {
		// Arrange
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/123"));
		parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/456"));
		parameters.addParameter().setName("preview").setValue(new BooleanType(false));

		// Act
		MergeOperationInputParameters result = myBuilder.fromParameters(parameters);

		// Assert
		assertThat(result.getPreview()).isFalse();
	}

	@Test
	void testFromParameters_withDeleteSourceTrue_setsFlag() {
		// Arrange
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/123"));
		parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/456"));
		parameters.addParameter().setName("delete-source").setValue(new BooleanType(true));

		// Act
		MergeOperationInputParameters result = myBuilder.fromParameters(parameters);

		// Assert
		assertThat(result.getDeleteSource()).isTrue();
	}

	@Test
	void testFromParameters_withResultPatient_storesResource() {
		// Arrange
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/123"));
		parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/456"));

		Patient resultPatient = new Patient();
		resultPatient.setId("Patient/result");
		resultPatient.addName().setFamily("ResultFamily");
		parameters.addParameter().setName("result-patient").setResource(resultPatient);

		// Act
		MergeOperationInputParameters result = myBuilder.fromParameters(parameters);

		// Assert
		assertThat(result.getResultResource()).isNotNull();
		assertThat(result.getResultResource()).isInstanceOf(Patient.class);
		Patient storedPatient = (Patient) result.getResultResource();
		assertThat(storedPatient.getIdElement().getValue()).isEqualTo("Patient/result");
		assertThat(storedPatient.getName()).hasSize(1);
		assertThat(storedPatient.getName().get(0).getFamily()).isEqualTo("ResultFamily");
	}
}
