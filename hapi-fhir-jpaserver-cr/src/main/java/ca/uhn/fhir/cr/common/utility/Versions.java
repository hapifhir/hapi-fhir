package ca.uhn.fhir.cr.common.utility;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class provides utilities for handling multiple business versions of FHIR
 * Resources.
 */
public class Versions {
	private Versions() {
	}

	/**
	 * This function compares two versions using semantic versioning.
	 * 
	 * @param version1 the first version to compare
	 * @param version2 the second version to compare
	 * @return 0 if versions are equal, 1 if version1 is greater than version2, and
	 *         -1 otherwise
	 */
	public static int compareVersions(String version1, String version2) {
		// Treat null as MAX VERSION
		if (version1 == null && version2 == null) {
			return 0;
		}

		if (version1 != null && version2 == null) {
			return -1;
		}

		if (version1 == null) {
			return 1;
		}

		String[] string1Vals = version1.split("\\.");
		String[] string2Vals = version2.split("\\.");

		int length = Math.max(string1Vals.length, string2Vals.length);

		for (int i = 0; i < length; i++) {
			Integer v1 = i < string1Vals.length ? Integer.parseInt(string1Vals[i]) : 0;
			Integer v2 = i < string2Vals.length ? Integer.parseInt(string2Vals[i]) : 0;

			// Making sure Version1 bigger than version2
			if (v1 > v2) {
				return 1;
			}
			// Making sure Version1 smaller than version2
			else if (v1 < v2) {
				return -1;
			}
		}

		// Both are equal
		return 0;
	}

	/***
	 * Given a list of FHIR Resources that have the same name, choose the one with
	 * the matching version.
	 * 
	 * @param <ResourceType> an IBaseResource type
	 * @param theResources   a list of Resources to select from
	 * @param theVersion     the version of the Resource to select
	 * @param theGetVersion  a function to access version information for the
	 *                       ResourceType
	 * @return the Resource with a matching version, or the highest version
	 *         otherwise.
	 */
	public static <ResourceType extends IBaseResource> ResourceType selectByVersion(List<ResourceType> theResources,
			String theVersion,
			Function<ResourceType, String> theGetVersion) {
		checkNotNull(theResources);
		checkNotNull(theGetVersion);

		ResourceType library = null;
		ResourceType maxVersion = null;
		for (ResourceType l : theResources) {
			String currentVersion = theGetVersion.apply(l);
			if (theVersion == null && currentVersion == null || theVersion != null && theVersion.equals(currentVersion)) {
				library = l;
			}

			if (maxVersion == null || compareVersions(currentVersion, theGetVersion.apply(maxVersion)) >= 0) {
				maxVersion = l;
			}
		}

		// If we were not given a version, return the highest found
		if ((theVersion == null || library == null) && maxVersion != null) {
			return maxVersion;
		}

		return library;
	}
}
