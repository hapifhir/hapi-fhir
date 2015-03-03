package ca.uhn.fhir.util;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;

public class FhirTerserTest {

	private static FhirContext ourCtx = new FhirContext();

	@Test
	public void testGetAllPopulatedChildElementsOfTypeDoesntDescendIntoEmbedded() {
		Patient p = new Patient();
		p.addName().addFamily("PATIENT");
		
		Bundle b = new Bundle();
		b.addEntry().setResource(p);
		b.addLink().setRelation("BUNDLE");

		FhirTerser t = ourCtx.newTerser();
		List<StringDt> strings = t.getAllPopulatedChildElementsOfType(b, StringDt.class);

		assertEquals(1, strings.size());
		assertThat(strings, containsInAnyOrder(new StringDt("BUNDLE")));

	}

	@Test
	public void testGetAllPopulatedChildElementsOfTypeDescendsIntoContained() {
		Patient p = new Patient();
		p.addName().addFamily("PATIENT");

		Organization o = new Organization();
		o.getNameElement().setValue("ORGANIZATION");
		p.getContained().getContainedResources().add(o);

		FhirTerser t = ourCtx.newTerser();
		List<StringDt> strings = t.getAllPopulatedChildElementsOfType(p, StringDt.class);

		assertEquals(2, strings.size());
		assertThat(strings, containsInAnyOrder(new StringDt("PATIENT"), new StringDt("ORGANIZATION")));

	}

}
