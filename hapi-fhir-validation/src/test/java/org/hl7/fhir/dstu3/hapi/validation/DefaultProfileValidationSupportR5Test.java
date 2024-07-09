package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultProfileValidationSupportR5Test extends BaseValidationTestWithInlineMocks {

	private static FhirContext ourCtx = FhirContext.forR5Cached();
	private DefaultProfileValidationSupport mySvc = new DefaultProfileValidationSupport(ourCtx);

	@Test
	public void testNoDuplicates() {
		List<IBaseResource> allSds = mySvc.fetchAllStructureDefinitions()
				.stream()
				.map(t->(StructureDefinition)t)
				.filter(t->t.getUrl().equals("http://hl7.org/fhir/StructureDefinition/language"))
				.collect(Collectors.toList());
		assertThat(allSds).hasSize(1);
	}

}
