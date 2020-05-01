package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import com.google.gson.Gson;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.hl7.fhir.utilities.cache.PackageCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Service
public class IgInstaller {

	private static final Logger ourLog = LoggerFactory.getLogger(IgInstaller.class);

	boolean enabled = true;
	private FhirContext fhirContext;
	private DaoRegistry daoRegistry;
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
	private SnapshotGenerator snapshotGenerator;

	@Autowired
	public IgInstaller(FhirContext fhirContext, DaoRegistry daoRegistry) {
		this.fhirContext = fhirContext;
		this.daoRegistry = daoRegistry;

		IFhirResourceDao structureDefinitionDao = daoRegistry.getResourceDao("StructureDefinition");

		switch (fhirContext.getVersion().getVersion()) {
			case R5: {
				searchUtilities = SearchUtilitiesImpl.forR5();
				snapshotGenerator = SnapshotGeneratorImpl.forR5(structureDefinitionDao);
				break;
			}
			case R4: {
				searchUtilities = SearchUtilitiesImpl.forR4();
				snapshotGenerator = SnapshotGeneratorImpl.forR4(structureDefinitionDao);
				break;
			}
			case DSTU3: {
				searchUtilities = SearchUtilitiesImpl.forDstu3();
				snapshotGenerator = SnapshotGeneratorImpl.forDstu3(structureDefinitionDao);
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
	 * @return success of the installation
	 */
	public boolean install(String url) {
		if (enabled) {
			try  {
				return install(NpmPackage.fromPackage(toInputStream(url)));
			} catch (IOException e) {
				ourLog.error("Could not load implementation guide from URL {}", url, e);
				return false;
			}
		}
		return false;
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
	 * @return success of the installation
	 */
	public boolean install(String id, String version) {
		if (enabled) {
			try {
				return install(packageCacheManager.loadPackage(id, version));
			} catch (IOException e) {
				ourLog.error("Could not load implementation guide from packages.fhir.org or " +
					"file on disk using ID {} and version {}", id, version, e);
			}
		}
		return false;
	}

	/**
	 * Installs a package and its dependencies.
	 *
	 * Fails fast if one of its dependencies could not be installed.
	 *
	 * @return success of the installation
	 */
	private boolean install(NpmPackage npmPackage) {
		String name = npmPackage.getNpm().get("name").getAsString();
		String version = npmPackage.getNpm().get("version").getAsString();
		String packageFhirVersion = npmPackage.fhirVersion();
		if (!isCompatibleWithCurrentFhirVersion(packageFhirVersion)) {
			ourLog.warn("Cannot install package {}#{}, FHIR versions mismatch (expected <={}, package uses {})",
				name, version, fhirContext.getVersion().getVersion().getFhirVersionString(), packageFhirVersion);
			return false;
		}
		boolean success = fetchAndInstallDependencies(npmPackage);
		if (!success) {
			ourLog.error("An error occoured when installing dependencies for the package {}#{}", name, version);
			return false;
		}
		ourLog.info("Installing package: {}#{}", name, version);
		for (String type : SUPPORTED_RESOURCE_TYPES) {
			Collection<IBaseResource> resources = parseResourcesOfType(type, npmPackage);
			if (type.equals("StructureDefinition")) {
				resources = snapshotGenerator.generateFrom(resources);
			}
			ourLog.info(String.format("creating or updating %s resources of type %s", resources.size(), type));

			resources.stream().forEach(r -> createOrUpdate(r));
		}
		ourLog.info(String.format("Finished installation of package: %s#%s", name, version));
		return true;
	}

	private boolean fetchAndInstallDependencies(NpmPackage npmPackage) {
		boolean success = true;
		if (npmPackage.getNpm().has("dependencies")) {
			Map<String, String> dependencies = new Gson().fromJson(npmPackage.getNpm().get("dependencies"), HashMap.class);
			for (Map.Entry<String, String> d : dependencies.entrySet()) {
				String id = d.getKey();
				String ver = d.getValue();
				if (id.startsWith("hl7.fhir")) {
					continue; // todo : which packages to ignore?
				}
				if (packageCacheManager == null) {
					ourLog.error("Cannot install dependency {}#{} due to PacketCacheManager initialization error", id, ver);
					return false;
				}
				try {
					// resolve in local cache or on packages.fhir.org
					NpmPackage dependency = packageCacheManager.loadPackage(id, ver);
					// recursive call to install dependencies of a package before
					// installing the package
					success &= fetchAndInstallDependencies(dependency);
					if (success) {
						success &= install(dependency);
					}
				} catch (IOException e) {
					ourLog.error("Cannot install dependency {}#{}", id, ver);
					return false;
				}
			}
		}
		return success;
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

	/**
	 * Returns true if the package version is compatible with the FHIR version used by
	 * the server according to semantic versioning.
	 */
	private boolean isCompatibleWithCurrentFhirVersion(String fhirVersion) {
		String currentVersion = fhirContext.getVersion().getVersion().getFhirVersionString();
		return fhirVersion.charAt(0) == currentVersion.charAt(0) &&
			currentVersion.compareTo(fhirVersion) >= 0;
	}

	/**
	 * Utility method
	 */
	private Collection<IBaseResource> parseResourcesOfType(String type, NpmPackage pkg) {
		if (!pkg.getFolders().containsKey("package")) {
			return Collections.EMPTY_LIST;
		}
		ArrayList<IBaseResource> res = new ArrayList<>();
		for (String file : pkg.getFolders().get("package").listFiles()) {
			if (file.startsWith(type)) {
				try {
					byte[] content = pkg.getFolders().get("package").fetchFile(file);
					IBaseResource r = fhirContext.newJsonParser().parseResource(new String(content));
					res.add(r);
				} catch (IOException e) {
					ourLog.error("Could not fetch file {}", file);
				}
			}
		}
		return res;
	}
}
