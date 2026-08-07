package ca.uhn.fhir.jpa.packages.loader;

import java.util.List;

public interface IPackageUrlAllowListProvider {
	/**
	 * whitelist of remote url prefixes to allow for package import.
	 * These are urls that are prefixed with http: or https:
	 */
	List<String> getRemotePrefixes();

	/**
	 * whitelist of local url prefixes to allow for package import.
	 * These are urls that are prefixed with file: or classpath:
	 */
	List<String> getLocalPrefixes();
}
