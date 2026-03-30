package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.npm.IPackageCacheManager;
import org.hl7.fhir.utilities.npm.PackageServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IPackageLoader extends IPackageCacheManager {

	/**
	 * Add a package server that can be used to fetch remote packages
	 * @param thePackageServer the server
	 */
	void addPackageServer(@Nonnull PackageServer thePackageServer);

	/**
	 * Provide a list of registered servers
	 * @return the list of servers
	 */
	List<PackageServer> getPackageServers();

	/**
	 * Loads the package, but won't save it anywhere.
	 * Returns the data to the caller
	 *
	 * @return - a POJO containing information about the NpmPackage, as well as it's contents
	 * 			as fetched from the server
	 * @throws IOException if the specified data source cannot be accessed
	 */
	NpmPackageData fetchPackageFromPackageSpec(PackageInstallationSpec theSpec) throws IOException;

	/**
	 * Loads the package, but won't save it anywhere.
	 * Returns the data to the caller
	 *
	 * @return - a POJO containing information about the NpmPackage, as well as it's contents
	 * 			as fetched from the server
	 * @throws IOException if the specified data source cannot be accessed
	 */
	NpmPackageData fetchPackageFromPackageSpec(String thePackageId, String thePackageVersion)
			throws FHIRException, IOException;

	/**
	 * Creates an NpmPackage data object.
	 *
	 * @param thePackageId - the id of the npm package
	 * @param thePackageVersionId - the version id of the npm package
	 * @param theSourceDesc - the installation spec description or package url
	 * @param thePackageTgzInputStream - the package contents.
	 *                                  Typically fetched from a server, but can be added directly to the package spec
	 * @return
	 * @throws IOException
	 */
	NpmPackageData createNpmPackageDataFromData(
			String thePackageId, String thePackageVersionId, String theSourceDesc, InputStream thePackageTgzInputStream)
			throws IOException;

	/**
	 * Loads the package contents as raw bytes
	 * @param thePackageUrl the location of the package
	 * @return the contents of the package as raw bytes
	 */
	byte[] loadPackageUrlContents(String thePackageUrl);
}
