package ca.uhn.fhir.tinder.ts;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TypescriptModelExtractorTest {

	private static TsProperty property(TsInterface theInterface, String theName) {
		Optional<TsProperty> retVal = theInterface.getProperties().stream()
				.filter(t -> t.getName().equals(theName))
				.findFirst();
		assertThat(retVal).as("property '%s' on %s", theName, theInterface.getName()).isPresent();
		return retVal.get();
	}

	@Test
	void testExtractR4Patient() {
		TsModel model = new TypescriptModelExtractor(FhirContext.forR4()).extract();

		TsInterface patient = model.getInterface("Patient");
		assertThat(patient).isNotNull();
		assertThat(patient.getInterfaceName()).isEqualTo("Patient");

		// Patient extends the FHIR base hierarchy rather than inlining inherited members
		assertThat(patient.getExtendsName()).isEqualTo("DomainResource");
		assertThat(patient.getProperties())
				.extracting(TsProperty::getName)
				.doesNotContain("id", "meta", "extension", "modifierExtension", "text", "contained");

		// Each concrete resource carries a string-literal resourceType discriminator
		TsProperty resourceType = property(patient, "resourceType");
		assertThat(resourceType.isOptional()).isFalse();
		assertThat(resourceType.getRenderedType()).isEqualTo("'Patient'");

		// The base hierarchy itself is emitted and correctly chained
		assertThat(model.getInterface("Element")).isNotNull();
		assertThat(model.getInterface("BackboneElement").getExtendsName()).isEqualTo("Element");
		assertThat(model.getInterface("Resource")).isNotNull();
		assertThat(model.getInterface("DomainResource").getExtendsName()).isEqualTo("Resource");
		// Resource has no resourceType field (mirrors the Java model)
		assertThat(model.getInterface("Resource").getProperties())
				.extracting(TsProperty::getName)
				.doesNotContain("resourceType");

		// Bound code -> enum union
		TsProperty gender = property(patient, "gender");
		assertThat(gender.isOptional()).isTrue();
		assertThat(gender.isArray()).isFalse();
		assertThat(gender.getKind()).isEqualTo(TsTypeKind.ENUM);

		// Repeating complex datatype -> Array<HumanName>
		TsProperty name = property(patient, "name");
		assertThat(name.isArray()).isTrue();
		assertThat(name.getKind()).isEqualTo(TsTypeKind.INTERFACE);
		assertThat(name.getTypeName()).isEqualTo("HumanName");
		assertThat(name.getRenderedType()).isEqualTo("Array<HumanName>");

		// choice[x] expanded into one optional property per type
		TsProperty deceasedBoolean = property(patient, "deceasedBoolean");
		assertThat(deceasedBoolean.isOptional()).isTrue();
		assertThat(deceasedBoolean.getRenderedType()).isEqualTo("boolean");
		TsProperty deceasedDateTime = property(patient, "deceasedDateTime");
		assertThat(deceasedDateTime.getRenderedType()).isEqualTo("string");

		// Reference element
		TsProperty managingOrganization = property(patient, "managingOrganization");
		assertThat(managingOrganization.getKind()).isEqualTo(TsTypeKind.INTERFACE);
		assertThat(managingOrganization.getTypeName()).isEqualTo("Reference");

		// Backbone element emitted as its own interface, extending IBackboneElement
		TsInterface contact = model.getInterface("PatientContact");
		assertThat(contact).isNotNull();
		assertThat(contact.getExtendsName()).isEqualTo("BackboneElement");
		TsProperty contactProp = property(patient, "contact");
		assertThat(contactProp.getKind()).isEqualTo(TsTypeKind.INTERFACE);
		assertThat(contactProp.getTypeName()).isEqualTo("PatientContact");

		// Referenced datatypes are emitted too
		assertThat(model.hasInterface("HumanName")).isTrue();
		assertThat(model.hasInterface("Reference")).isTrue();

		// The gender enum union carries real codes
		TsEnum genderEnum = model.getEnums().stream()
				.filter(t -> t.getName().equals(gender.getTypeName()))
				.findFirst()
				.orElse(null);
		assertThat(genderEnum).isNotNull();
		assertThat(genderEnum.getCodes()).contains("male", "female");
	}

	@Test
	void testWriteR4Patient(@TempDir File theDir) throws Exception {
		TsModel model = new TypescriptModelExtractor(FhirContext.forR4()).extract();
		new TypescriptWriter().writeModel(model, theDir);

		File patientFile = new File(theDir, "Patient.ts");
		assertThat(patientFile).exists();
		String patient = Files.readString(patientFile.toPath(), StandardCharsets.UTF_8);
		assertThat(patient).contains("export interface Patient extends DomainResource {");
		assertThat(patient).contains("resourceType: 'Patient';");
		assertThat(patient).contains("name?: Array<HumanName>;");
		assertThat(patient).contains("import { HumanName } from './HumanName';");
		assertThat(patient).contains("import { DomainResource } from './DomainResource';");

		assertThat(new File(theDir, "HumanName.ts")).exists();
		assertThat(new File(theDir, "DomainResource.ts")).exists();
		assertThat(new File(theDir, "Element.ts")).exists();
		assertThat(new File(theDir, "index.ts")).exists();
		String index = Files.readString(new File(theDir, "index.ts").toPath(), StandardCharsets.UTF_8);
		assertThat(index).contains("export * from './Patient';");
	}

	@Test
	void testExtractDstu2IsVersionAgnostic() {
		TsModel model = new TypescriptModelExtractor(FhirContext.forDstu2()).extract();

		TsInterface patient = model.getInterface("Patient");
		assertThat(patient).isNotNull();
		assertThat(patient.getProperties()).isNotEmpty();
		assertThat(property(patient, "resourceType").getRenderedType()).isEqualTo("'Patient'");
		assertThat(patient.getExtendsName()).isNotNull();
		assertThat(model.getInterface("Element")).isNotNull();

		// DSTU2 binds codes via a different runtime class; confirm bound codes still produce enum unions.
		assertThat(property(patient, "gender").getKind()).isEqualTo(TsTypeKind.ENUM);
		assertThat(model.getEnums()).isNotEmpty();
	}
}
