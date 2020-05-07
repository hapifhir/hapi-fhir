package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.util.FhirTerser;
import com.google.gson.Gson;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.hl7.fhir.utilities.cache.PackageCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IgInstallerSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(IgInstallerSvc.class);

	boolean enabled = true;

	@Autowired
	private FhirContext fhirContext;
	@Autowired
	private DaoRegistry daoRegistry;
	@Autowired
	private IValidationSupport validationSupport;

	private PackageCacheManager packageCacheManager;

	private String[] SUPPORTED_RESOURCE_TYPES = new String[]
		{ "NamingSystem",
			"CodeSystem",
			"ValueSet",
			"StructureDefinition",
			"ConceptMap",
			"SearchParameter",
			"Subscription" };

	private SearchUtilities searchUtilities;

	@PostConstruct
	public void initialize() {
		switch (fhirContext.getVersion().getVersion()) {
			case R5: {
				searchUtilities = SearchUtilitiesImpl.forR5();
				break;
			}
			case R4: {
				searchUtilities = SearchUtilitiesImpl.forR4();
				break;
			}
			case DSTU3: {
				searchUtilities = SearchUtilitiesImpl.forDstu3();
				break;
			}
			default: {
				ourLog.info("IG installation not supported for version: {}", fhirContext.getVersion().getVersion());
				enabled = false;
			}
		}
		try {
			packageCacheManager = new PackageCacheManager(true, 1);
		} catch (IOException e) {
			ourLog.error("Unable to initialize PackageCacheManager: {}", e);
			enabled = false;
		}
	}

	/**
	 * Loads and installs an IG tarball (with its dependencies) from the specified url.
	 *
	 * Installs the IG by persisting instances of the following types of resources:
	 *
	 * - NamingSystem, CodeSystem, ValueSet, StructureDefinition (with snapshots),
	 *   ConceptMap, SearchParameter, Subscription
	 *
	 * Creates the resources if non-existent, updates them otherwise.
	 *
	 * @param url of IG tarball
	 * @throws ImplementationGuideInstallationException if installation fails
	 */
	public void install(String url) throws ImplementationGuideInstallationException {
		if (enabled) {
			try  {
				install(NpmPackage.fromPackage(toInputStream(url)));
			} catch (IOException e) {
				ourLog.error("Could not load implementation guide from URL {}", url, e);
			}
		}
	}

	private InputStream toInputStream(String url) throws IOException {
		URL u = new URL(url);
		URLConnection c = u.openConnection();
		return c.getInputStream();
	}

	/**
	 * Loads and installs an IG from a file on disk or the Simplifier repo using
	 * the {@link PackageCacheManager}.
	 *
	 * Installs the IG by persisting instances of the following types of resources:
	 *
	 * - NamingSystem, CodeSystem, ValueSet, StructureDefinition (with snapshots),
	 *   ConceptMap, SearchParameter, Subscription
	 *
	 * Creates the resources if non-existent, updates them otherwise.
	 *
	 * @param id of the package, or name of folder in filesystem
	 * @param version of package, or path to folder in filesystem
	 * @throws ImplementationGuideInstallationException if installation fails
	 */
	public void install(String id, String version) throws ImplementationGuideInstallationException {
		if (enabled) {
			try {
				install(packageCacheManager.loadPackage(id, version));
			} catch (IOException e) {
				ourLog.error("Could not load implementation guide from packages.fhir.org or " +
					"file on disk using ID {} and version {}", id, version, e);
			}
		}
	}

	/**
	 * Installs a package and its dependencies.
	 *
	 * Fails fast if one of its dependencies could not be installed.
	 *
	 * @throws ImplementationGuideInstallationException if installation fails
	 */
	private void install(NpmPackage npmPackage) throws ImplementationGuideInstallationException {
		String name = npmPackage.getNpm().get("name").getAsString();
		String version = npmPackage.getNpm().get("version").getAsString();
		String fhirVersion = npmPackage.fhirVersion();
		String currentFhirVersion = fhirContext.getVersion().getVersion().getFhirVersionString();

		assertFhirVersionsAreCompatible(fhirVersion, currentFhirVersion);

		fetchAndInstallDependencies(npmPackage);

		ourLog.info("Installing package: {}#{}", name, version);

		for (String type : SUPPORTED_RESOURCE_TYPES) {
			Collection<IBaseResource> resources = parseResourcesOfType(type, npmPackage);

			ourLog.info(String.format("creating or updating %s resources of type %s", resources.size(), type));

			resources.stream()
				.map(r -> isStructureDefinitionWithoutSnapshot(r) ? generateSnapshot(r) : r)
				.forEach(r -> createOrUpdate(r));
		}
		ourLog.info(String.format("Finished installation of package: %s#%s", name, version));
	}

	private void fetchAndInstallDependencies(NpmPackage npmPackage) throws ImplementationGuideInstallationException {
		if (npmPackage.getNpm().has("dependencies")) {
			Map<String, String> dependencies = new Gson().fromJson(npmPackage.getNpm().get("dependencies"), HashMap.class);
			for (Map.Entry<String, String> d : dependencies.entrySet()) {
				String id = d.getKey();
				String ver = d.getValue();
				if (id.startsWith("hl7.fhir")) {
					continue; // todo : which packages to ignore?
				}
				if (packageCacheManager == null) {
					throw new ImplementationGuideInstallationException(String.format(
						"Cannot install dependency %s#%s due to PacketCacheManager initialization error", id, ver));
				}
				try {
					// resolve in local cache or on packages.fhir.org
					NpmPackage dependency = packageCacheManager.loadPackage(id, ver);
					// recursive call to install dependencies of a package before
					// installing the package
					fetchAndInstallDependencies(dependency);
					install(dependency);
				} catch (IOException e) {
					throw new ImplementationGuideInstallationException(String.format(
						"Cannot resolve dependency %s#%s", id, ver), e);
				}
			}
		}
	}

	/**
	 * Asserts if package FHIR version is compatible with current FHIR version
	 * by using semantic versioning rules.
	 */
	private void assertFhirVersionsAreCompatible(String fhirVersion, String currentFhirVersion)
		throws ImplementationGuideInstallationException {

		boolean compatible = fhirVersion.charAt(0) == currentFhirVersion.charAt(0) &&
			currentFhirVersion.compareTo(fhirVersion) >= 0;
		if (!compatible) {
			throw new ImplementationGuideInstallationException(String.format(
				"Cannot install implementation guide: FHIR versions mismatch (expected <=%s, package uses %s)",
				currentFhirVersion, fhirVersion));
		}
	}

	/**
	 * ============================= Utility methods ===============================
	 */

	private Collection<IBaseResource> parseResourcesOfType(String type, NpmPackage pkg) {
		if (!pkg.getFolders().containsKey("package")) {
			return Collections.EMPTY_LIST;
		}
		ArrayList<IBaseResource> resources = new ArrayList<>();
		for (String file : pkg.getFolders().get("package").listFiles()) {
			if (file.contains(type)) {
				try {
					byte[] content = pkg.getFolders().get("package").fetchFile(file);
					resources.add(fhirContext.newJsonParser().parseResource(new String(content)));
				} catch (IOException e) {
					ourLog.error("Cannot install resource of type {}: Could not fetch file {}", type, file);
				}
			}
		}
		return resources;
	}

	/**
	 * Create a resource or update it, if its already existing.
	 */
	private void createOrUpdate(IBaseResource resource) {
		IFhirResourceDao dao = daoRegistry.getResourceDao(resource.getClass());
		IBundleProvider searchResult = dao.search(searchUtilities.createSearchParameterMapFor(resource));
		if (searchResult.isEmpty()) {
			dao.create(resource);
		} else {
			IBaseResource existingResource = searchUtilities.verifySearchResultFor(resource, searchResult);
			if (existingResource != null) {
				resource.setId(existingResource.getIdElement().getValue());
				dao.update(resource);
			}
		}
	}

	private boolean isStructureDefinitionWithoutSnapshot(IBaseResource r) {
		FhirTerser terser = fhirContext.newTerser();
		return r.getClass().getSimpleName().equals("StructureDefinition") &&
			terser.getSingleValueOrNull(r, "snapshot") == null;
	}

	private IBaseResource generateSnapshot(IBaseResource sd) {
		try {
			return validationSupport.generateSnapshot(validationSupport, sd, null, null, null);
		} catch (Exception e) {
			throw new ImplementationGuideInstallationException(String.format(
				"Failure when generating snapshot of StructureDefinition: %s", sd.getIdElement()), e);
		}
	}
}
