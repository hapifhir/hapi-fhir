package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultProfileValidationSupportR5Test {

	private static FhirContext ourCtx = FhirContext.forR5Cached();
	private DefaultProfileValidationSupport mySvc = new DefaultProfileValidationSupport(ourCtx);

	@Test
	public void testNoDuplicates() {
		List<IBaseResource> allSds = mySvc.fetchAllStructureDefinitions()
				.stream()
				.map(t->(StructureDefinition)t)
				.filter(t->t.getUrl().equals("http://hl7.org/fhir/StructureDefinition/language"))
				.collect(Collectors.toList());
		assertEquals(1, allSds.size());
	}

}
