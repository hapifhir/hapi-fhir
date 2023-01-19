package ca.uhn.fhir.util;

import ca.uhn.fhir.rest.api.MethodOutcome;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Extra methods for {@link MethodOutcome}.
 */
public class MethodOutcomeUtil {

	@Nonnull
	public static Optional<String> getFirstResponseHeader(MethodOutcome theMethodOutcome, String theHeader) {
		List<String> values = theMethodOutcome.getResponseHeaders().get(theHeader);

		if (values == null || values.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(values.get(0));
		}
	}}
