package ca.uhn.fhir.parser;

import ca.uhn.fhir.parser.path.EncodeContextPath;
import jakarta.annotation.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ParserUtil {

	/** Non instantiable */
	private ParserUtil() {}

	public static @Nullable Set<String> determineApplicableResourceTypesForTerserPaths(@Nullable List<EncodeContextPath> encodeElements) {
		Set<String> encodeElementsAppliesToResourceTypes = null;
		if (encodeElements != null) {
			encodeElementsAppliesToResourceTypes = new HashSet<>();
			for (String next : encodeElements.stream()
					.map(t -> t.getPath().get(0).getName())
					.collect(Collectors.toList())) {
				if (next.startsWith("*")) {
					encodeElementsAppliesToResourceTypes = null;
					break;
				}
				int dotIdx = next.indexOf('.');
				if (dotIdx == -1) {
					encodeElementsAppliesToResourceTypes.add(next);
				} else {
					encodeElementsAppliesToResourceTypes.add(next.substring(0, dotIdx));
				}
			}
		}
		return encodeElementsAppliesToResourceTypes;
	}
}
