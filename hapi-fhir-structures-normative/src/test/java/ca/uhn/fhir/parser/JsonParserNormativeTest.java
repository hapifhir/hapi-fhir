package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.model.core.HumanName;
import org.hl7.fhir.model.core.Patient;
import org.hl7.fhir.model.core.PrimitiveType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonParserNormativeTest {

	@Test
	void testParse() {
		FhirContext ctx = FhirContext.forR6();

		String input = ClasspathUtil.loadResource("r6/patient-example.json");
		Patient patient = (Patient) ctx.newJsonParser().parseResource(input);

		assertEquals("Chalmers", patient.getNameList().get(0).getFamily());
		assertThat(patient.getNameList().get(0).getGivenList().stream().map(PrimitiveType::getValue).toList()).containsExactly("Peter", "James");
		assertEquals(HumanName.NameUse.OFFICIAL, patient.getNameList().get(0).getUse());

		assertEquals("1974-12-25", patient.getBirthDateElement().getValueAsString());
		assertEquals(1, patient.getBirthDateElement().getExtension().size());
		assertEquals("http://hl7.org/fhir/StructureDefinition/patient-birthTime", patient.getBirthDateElement().getExtension().get(0).getUrl());
		assertEquals("1974-12-25T14:35:45-05:00", patient.getBirthDateElement().getExtension().get(0).getValueDateTimeType().getValueAsString());

	}


}
