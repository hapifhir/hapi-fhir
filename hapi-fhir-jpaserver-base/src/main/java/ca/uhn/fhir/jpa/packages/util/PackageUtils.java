package ca.uhn.fhir.jpa.packages.util;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class PackageUtils {
	public static List<String> DEFAULT_INSTALL_TYPES = Collections.unmodifiableList(Lists.newArrayList(
		"NamingSystem",
		"CodeSystem",
		"ValueSet",
		"StructureDefinition",
		"ConceptMap",
		"SearchParameter",
		"Subscription"
	));
}
