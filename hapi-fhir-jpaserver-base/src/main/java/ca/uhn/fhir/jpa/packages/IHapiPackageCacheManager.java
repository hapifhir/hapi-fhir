package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.cache.IPackageCacheManager;
import org.hl7.fhir.utilities.cache.NpmPackage;

import java.io.IOException;

public interface IHapiPackageCacheManager extends IPackageCacheManager {

	NpmPackage loadPackage(NpmInstallationSpec theInstallationSpec) throws IOException;

	IBaseResource loadPackageAssetByUrl(FhirVersionEnum theFhirVersion, String theCanonicalUrl);
}
