package ca.uhn.fhir.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.FhirTerser;

public class FhirTerserTest {

	@Test
	public void testGetAllPopulatedChildElementsOfType() {

		Patient p = new Patient();
		p.setGender(AdministrativeGenderCodesEnum.M);
		p.addIdentifier().setSystem("urn:foo");
		p.addAddress().addLine("Line1");
		p.addAddress().addLine("Line2");
		p.addName().addFamily("Line3");

		FhirTerser t = new FhirContext().newTerser();
		List<StringDt> strings = t.getAllPopulatedChildElementsOfType(p, StringDt.class);

		assertEquals(3, strings.size());
		assertThat(strings, containsInAnyOrder(new StringDt("Line1"), new StringDt("Line2"), new StringDt("Line3")));

	}

}
