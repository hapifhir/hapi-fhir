package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

@Service
public class AdditionalResourcesParser {

	public static IBaseBundle bundleAdditionalResources(
			Set<String> additionalResources, PackageInstallationSpec packageInstallationSpec, FhirContext fhirContext) {
		NpmPackage npmPackage;
		try {
			npmPackage = NpmPackage.fromPackage(new ByteArrayInputStream(packageInstallationSpec.getPackageContents()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		var resources = getAdditionalResources(additionalResources, npmPackage, fhirContext);

		var bundleBuilder = new BundleBuilder(fhirContext);
		resources.forEach(bundleBuilder::addTransactionUpdateEntry);
		return bundleBuilder.getBundle();
	}

	@NotNull
	public static List<IBaseResource> getAdditionalResources(
			Set<String> folderNames, NpmPackage npmPackage, FhirContext fhirContext) {

		var npmFolders = folderNames.stream()
				.map(name -> npmPackage.getFolders().get(name))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		var resources = new LinkedList<IBaseResource>();
		for (var folder : npmFolders) {
			List<String> fileNames;
			try {
				fileNames = folder.getTypes().values().stream()
						.flatMap(Collection::stream)
						.collect(Collectors.toList());
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}

			resources.addAll(fileNames.stream()
					.map(fileName -> {
						try {
							return new String(folder.fetchFile(fileName));
						} catch (IOException e) {
							throw new RuntimeException(e.getMessage(), e);
						}
					})
					.map(fhirContext.newJsonParser().setSuppressNarratives(true)::parseResource)
					.collect(Collectors.toList()));
		}
		return resources;
	}
}
