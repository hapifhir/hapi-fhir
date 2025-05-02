package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class AdditionalResourcesParser {

	public static IBaseBundle bundleAdditionalResources(
			Set<String> additionalResources, PackageInstallationSpec packageInstallationSpec, FhirContext fhirContext) {
		NpmPackage npmPackage;
		try {
			npmPackage = NpmPackage.fromPackage(new ByteArrayInputStream(packageInstallationSpec.getPackageContents()));
		} catch (IOException e) {
			throw new InternalErrorException(e);
		}
		List<IBaseResource> resources = getAdditionalResources(additionalResources, npmPackage, fhirContext);

		BundleBuilder bundleBuilder = new BundleBuilder(fhirContext);
		resources.forEach(bundleBuilder::addTransactionUpdateEntry);
		return bundleBuilder.getBundle();
	}

	@NotNull
	public static List<IBaseResource> getAdditionalResources(
			Set<String> folderNames, NpmPackage npmPackage, FhirContext fhirContext) {

		List<NpmPackage.NpmPackageFolder> npmFolders = folderNames.stream()
				.map(name -> npmPackage.getFolders().get(name))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		List<IBaseResource> resources = new LinkedList<>();
		IParser parser = fhirContext.newJsonParser().setSuppressNarratives(true);

		for (NpmPackage.NpmPackageFolder folder : npmFolders) {
			List<String> fileNames;
			try {
				fileNames = folder.getTypes().values().stream()
						.flatMap(Collection::stream)
						.collect(Collectors.toList());
			} catch (IOException e) {
				throw new InternalErrorException(e.getMessage(), e);
			}

			resources.addAll(fileNames.stream()
					.map(fileName -> {
						try {
							return new String(folder.fetchFile(fileName));
						} catch (IOException e) {
							throw new InternalErrorException(e.getMessage(), e);
						}
					})
					.map(parser::parseResource)
					.collect(Collectors.toList()));
		}
		return resources;
	}
}
