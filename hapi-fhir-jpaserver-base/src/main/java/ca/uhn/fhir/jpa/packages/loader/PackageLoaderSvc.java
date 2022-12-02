package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.npm.BasePackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PackageLoaderSvc extends BasePackageCacheManager {

	private static final Logger ourLog = LoggerFactory.getLogger(PackageLoaderSvc.class);

	public NpmPackageData loadPackageOnly(
		String thePackageId,
		String thePackageVersion
	) throws FHIRException, IOException {
		BasePackageCacheManager.InputStreamWithSrc pkg = this.loadFromPackageServer(thePackageId, thePackageVersion);
		if (pkg == null) {
			throw new ResourceNotFoundException(Msg.code(1301) + "Unable to locate package " + thePackageId + "#" + thePackageVersion);
		}

		NpmPackageData npmPackage = createNpmPackage(
			thePackageId,
			thePackageVersion == null ? pkg.version : thePackageVersion,
			pkg.url,
			pkg.stream
		);

		return npmPackage;
	}

	public NpmPackageData createNpmPackage(
		String thePackageId,
		String thePackageVersionId,
		String theSourceDesc,
		InputStream thePackageTgzInputStream
	) throws IOException {
		Validate.notBlank(thePackageId, "thePackageId must not be null");
		Validate.notBlank(thePackageVersionId, "thePackageVersionId must not be null");
		Validate.notNull(thePackageTgzInputStream, "thePackageTgzInputStream must not be null");

		byte[] bytes = IOUtils.toByteArray(thePackageTgzInputStream);

		ourLog.info("Parsing package .tar.gz ({} bytes) from {}", bytes.length, theSourceDesc);

		NpmPackage npmPackage = NpmPackage.fromPackage(new ByteArrayInputStream(bytes));

		return new NpmPackageData(
			thePackageId,
			thePackageVersionId,
			theSourceDesc,
			bytes,
			npmPackage,
			thePackageTgzInputStream
		);
	}

	@Override
	public NpmPackage loadPackageFromCacheOnly(String theS, @Nullable String theS1) throws IOException {
		return null;
	}

	@Override
	public NpmPackage addPackageToCache(String theS, String theS1, InputStream theInputStream, String theS2) throws IOException {
		return null;
	}

	@Override
	public NpmPackage loadPackage(String theS, String theS1) throws FHIRException, IOException {
		return null;
	}
}
