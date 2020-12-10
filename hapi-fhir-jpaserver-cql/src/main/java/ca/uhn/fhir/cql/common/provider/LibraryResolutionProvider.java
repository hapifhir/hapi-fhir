package ca.uhn.fhir.cql.common.provider;

import java.util.function.Function;

public interface LibraryResolutionProvider<LibraryType> {
	public LibraryType resolveLibraryById(String libraryId);

	public LibraryType resolveLibraryByName(String libraryName, String libraryVersion);

	public LibraryType resolveLibraryByCanonicalUrl(String libraryUrl);


	// Hmmm... Probably need to think through this use case a bit more.
	// Should we throw an exception? Should this be a different interface?
	public void update(LibraryType library);


	// This function assumes that you're selecting from a set of libraries with the same name.
	// It returns the closest matching version, or the max version if no version is specified.
	static <LibraryType> LibraryType selectFromList(Iterable<LibraryType> libraries, String libraryVersion, Function<LibraryType, String> getVersion) {
		LibraryType library = null;
		LibraryType maxVersion = null;
		for (LibraryType l : libraries) {
			String currentVersion = getVersion.apply(l);
			if ((libraryVersion != null && currentVersion.equals(libraryVersion)) ||
				(libraryVersion == null && currentVersion == null))
			{
				library = l;
			}

			if (maxVersion == null || compareVersions(
				getVersion.apply(maxVersion),
				getVersion.apply(l) ) < 0){
				maxVersion = l;
			}
		}

		// If we were not given a version, return the highest found
		if (libraryVersion == null && maxVersion != null) {
			return maxVersion;
		}

		return library;
	}

	public static int compareVersions(String version1, String version2)
	{
		// Treat null as MAX VERSION
		if (version1 == null && version2 == null) {
			return 0;
		}

		if (version1 != null && version2 == null) {
			return -1;
		}

		if (version1 == null && version2 != null) {
			return 1;
		}

		String[] string1Vals = version1.split("\\.");
		String[] string2Vals = version2.split("\\.");

		int length = Math.max(string1Vals.length, string2Vals.length);

		for (int i = 0; i < length; i++)
		{
			Integer v1 = (i < string1Vals.length)?Integer.parseInt(string1Vals[i]):0;
			Integer v2 = (i < string2Vals.length)?Integer.parseInt(string2Vals[i]):0;

			//Making sure Version1 bigger than version2
			if (v1 > v2)
			{
				return 1;
			}
			//Making sure Version1 smaller than version2
			else if(v1 < v2)
			{
				return -1;
			}
		}

		//Both are equal
		return 0;
	}
}
