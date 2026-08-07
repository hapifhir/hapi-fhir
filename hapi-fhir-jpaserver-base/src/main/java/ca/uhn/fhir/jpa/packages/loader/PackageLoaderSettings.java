package ca.uhn.fhir.jpa.packages.loader;

import com.google.common.collect.ImmutableList;
import jakarta.annotation.Nonnull;

import java.util.List;

public class PackageLoaderSettings {

	// allow all
	public static final String WILDCARD = "*";

	public static PackageLoaderSettings restricted(
			@Nonnull List<String> theRemotePrefixes, @Nonnull List<String> theLocalPrefixes) {
		return new PackageLoaderSettings(theRemotePrefixes, theLocalPrefixes);
	}

	public static PackageLoaderSettings unrestricted() {
		return new PackageLoaderSettings(List.of(WILDCARD), List.of(WILDCARD));
	}

	/**
	 * A list of url (prefixes) that are allowed
	 * for packageloader.
	 */
	private final List<String> myRemotePrefixes;

	/**
	 * A list of prefixes allowed (for file/classpath: 'urls')
	 */
	private final List<String> myLocalPrefixes;

	private PackageLoaderSettings(List<String> theRemotePrefixes, List<String> theLocalPrefixes) {
		myRemotePrefixes = ImmutableList.copyOf(theRemotePrefixes);
		myLocalPrefixes = ImmutableList.copyOf(theLocalPrefixes);
	}

	public List<String> getRemotePrefixes() {
		return myRemotePrefixes;
	}

	public List<String> getLocalPrefixes() {
		return myLocalPrefixes;
	}
}
