package ca.uhn.fhir.jpa.packages.loader;

public class PackageLoaderSettings {

	private final PackageUrlAllowList myPackageUrlAllowList;

	public PackageLoaderSettings(PackageUrlAllowList theAllowList) {
		myPackageUrlAllowList = theAllowList;
	}

	public PackageUrlAllowList getPackageUrlAllowList() {
		return myPackageUrlAllowList;
	}
}
