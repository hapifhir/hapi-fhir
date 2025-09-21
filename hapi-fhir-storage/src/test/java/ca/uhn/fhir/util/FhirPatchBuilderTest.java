package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.patch.FhirPatch;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FhirPatchBuilderTest {

	private final FhirContext myContext	= FhirContext.forR4Cached();
	private FhirPatchBuilder myBuilder;
	private FhirPatch myPatchSvc;

	@BeforeEach
	void before() {
		myBuilder = new FhirPatchBuilder(myContext);
		myPatchSvc = new FhirPatch(myContext);
	}

	@Test
	void testAdd() {
		myBuilder
			.add()
			.path("Patient")
			.name("birthDate")
			.value(new DateType("2020-01-02"));
		IBaseParameters patch = myBuilder.build();

		Patient patient = new Patient();
		FhirPatch.PatchOutcome outcome = myPatchSvc.apply(patient, patch);
		assertThat(outcome.getErrors()).isEmpty();

		assertEquals("2020-01-02", patient.getBirthDateElement().getValueAsString());
	}

	@Test
	void testInsert() {
		myBuilder
			.insert()
			.path("Patient")
			.name("birthDate")
			.value(new DateTimeType("2020-01-02"));

		String expected = """
			<Parameters xmlns="http://hl7.org/fhir">
			   <parameter>
			      <name value="operation"/>
			      <part>
			         <name value="type"/>
			         <valueString value="add"/>
			      </part>
			      <part>
			         <name value="path"/>
			         <valueString value="Patient"/>
			      </part>
			      <part>
			         <name value="name"/>
			         <valueString value="birthDate"/>
			      </part>
			      <part>
			         <name value="value"/>
			         <valueDateTime value="2020-01-02"/>
			      </part>
			   </parameter>
			</Parameters>""";
		String actual = myContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(myBuilder.build());
		assertEquals(expected, actual);
	}




}
