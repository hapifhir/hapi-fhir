package ca.uhn.fhir.jpa.util;

import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.ValueSet;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValueSetTestUtil {
	public static String extractExpansionMessage(ValueSet outcome) {
		List<Extension> extensions = outcome.getMeta().getExtensionsByUrl(EXT_VALUESET_EXPANSION_MESSAGE);
		assertEquals(1, extensions.size());
		String expansionMessage = extensions.get(0).getValueAsPrimitive().getValueAsString();
		return expansionMessage;
	}

	@Nonnull
	public static List<String> toCodes(ValueSet theExpandedValueSet) {
		return theExpandedValueSet.getExpansion().getContains().stream().map(t -> t.getCode()).collect(Collectors.toList());
	}
}
