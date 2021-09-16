package ca.uhn.fhir.rest.server.interceptor.auth;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * This class is used in RuleBuilder, as a way to provide a compartment permission additional resource search params that
 * are to be included as "in" the given compartment. For example, if you were to populate this map with
 * [device -> ["patient"]
 * and apply it to compartment Patient/123, then any device with Patient/123 as its patient would be considered "in"
 * the compartment, despite the fact that device is technically not part of the compartment definition for patient.
 */
public class AdditionalCompartmentSearchParameters {
	private Map<String, Set<String>> myResourceTypeToParameterCodeMap;

	public AdditionalCompartmentSearchParameters() {
		myResourceTypeToParameterCodeMap = new HashMap<>();
	}

	public void addSearchParameters(@Nonnull String theResourceType, @Nonnull String... theParameterCodes) {
		Arrays.stream(theParameterCodes).forEach(code -> {
			myResourceTypeToParameterCodeMap.computeIfAbsent(theResourceType.toLowerCase(), (key) -> new HashSet<>()).add(code.toLowerCase());
		});
	}

	public Set<String> getSearchParamNamesForResourceType(@Nonnull String theResourceType) {
		return myResourceTypeToParameterCodeMap.computeIfAbsent(theResourceType.toLowerCase(), (key) -> new HashSet<>());
	}
}
