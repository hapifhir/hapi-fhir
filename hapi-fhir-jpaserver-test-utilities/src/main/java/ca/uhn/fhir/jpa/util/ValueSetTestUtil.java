package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.ValueSet;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValueSetTestUtil {

	private final VersionCanonicalizer myCanonicalizer;

	public ValueSetTestUtil(FhirVersionEnum theFhirVersion) {
		myCanonicalizer = new VersionCanonicalizer(theFhirVersion);
	}

	public String extractExpansionMessage(IBaseResource theValueSet) {
		ValueSet outcome = myCanonicalizer.valueSetToCanonical(theValueSet);
		List<Extension> extensions = outcome.getMeta().getExtensionsByUrl(EXT_VALUESET_EXPANSION_MESSAGE);
		assertEquals(1, extensions.size());
		String expansionMessage = extensions.get(0).getValueAsPrimitive().getValueAsString();
		return expansionMessage;
	}

	@Nonnull
	public List<String> toCodes(IBaseResource theExpandedValueSet) {
		ValueSet outcome = myCanonicalizer.valueSetToCanonical(theExpandedValueSet);
		return outcome.getExpansion().getContains().stream().map(t -> t.getCode()).collect(Collectors.toList());
	}
}
