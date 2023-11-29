package ca.uhn.fhir.jpa.provider;

import org.junit.jupiter.params.provider.Arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CodeSystemLookupWithPropertiesUtil {
	public static final String ourCodeSystemId = "CodeSystem-Example",
			ourCodeSystemUrl = "http://example/" + ourCodeSystemId;
	public static final String ourCode = "Code-WithProperties";
	public static final String ourPropertyA = "Property-A", ourPropertyB = "Property-B";
	public static final String ourPropertyValueA = "Value-A", ourPropertyValueB = "Value-B";

	public static Stream<Arguments> parametersLookupWithProperties() {
		return Stream.of(
				arguments(List.of(ourPropertyB), List.of(ourPropertyB)),
				arguments(List.of(ourPropertyA, ourPropertyB), List.of(ourPropertyA, ourPropertyB)),
				arguments(List.of(ourPropertyB, ourPropertyA), List.of(ourPropertyB, ourPropertyA)),
				arguments(List.of(ourPropertyA, ourPropertyA), List.of(ourPropertyA, ourPropertyA)),
				arguments(List.of(ourPropertyB, "ABC"), List.of(ourPropertyB)),
				arguments(List.of("ABC", ourPropertyA), List.of(ourPropertyA)),
				arguments(List.of("ABC"), Collections.emptyList()));
	}
}
