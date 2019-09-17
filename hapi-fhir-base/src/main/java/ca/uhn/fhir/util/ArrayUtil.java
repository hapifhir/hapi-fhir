package ca.uhn.fhir.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

public class ArrayUtil {

	/** Non instantiable */
	private ArrayUtil() {}

	/**
	 * Takes in a list like "foo, bar,, baz" and returns a set containing only ["foo", "bar", "baz"]
	 */
	public static Set<String> commaSeparatedListToCleanSet(String theValueAsString) {
		Set<String> resourceTypes;
		resourceTypes = Arrays.stream(split(theValueAsString, ","))
			.map(t->trim(t))
			.filter(t->isNotBlank(t))
			.collect(Collectors.toSet());
		return resourceTypes;
	}
}
