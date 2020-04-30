package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import com.google.common.annotations.VisibleForTesting;
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

	private boolean enabled = true;
	private FhirContext fhirContext;
	private DaoConfig daoConfig;
	private DaoRegistry daoRegistry;
	private PackageCacheManager packageCacheManager;

	private static final String[] RESOURCE_TYPES = new String[]
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
	public IgInstaller(FhirContext fhirContext, DaoConfig daoConfig, DaoRegistry daoRegistry) {
		this.fhirContext = fhirContext;
		this.daoConfig = daoConfig;
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
			ourLog.warn("Unable to initialize PackageCacheManager: {}", e);
			enabled = false;
		}
	}

	/**
	 * Listens for the {@link ContextStartedEvent} and installs an IG specified
	 * by the fields getMyImplementationGuideURL, getMyImplementationGuideID and
	 * getMyImplementationGuideVersion in {@link DaoConfig}.
	 *
	 * See the specification of {@link DaoConfig}
	 */
	@EventListener(ContextStartedEvent.class)
	public void run() throws IOException {
		if (!enabled) { return; }
		String url = daoConfig.getMyImplementationGuideURL();
		String id = daoConfig.getMyImplementationGuideID();
		String ver = daoConfig.getMyImplementationGuideVersion();
		NpmPackage ig = null;

		if (url != null) {
			if (id != null) {
				ourLog.warn("Only one of myImplementationGuideURL and myImplementationGuideID " +
					"should be set. Using {} to fetch implementation guide", url);
			}
			ig = loadIg(url);
		} else if (id != null) {
			ig = loadIg(id, ver);
		}
		if (ig != null) {
			install(ig);
		}
	}

	/**
	 * Loads a package from the WWW.
	 */
	private NpmPackage loadIg(String url) throws IOException {
		return NpmPackage.fromPackage(toInputStream(url));
	}

	private InputStream toInputStream(String url) throws IOException {
		URL u = new URL(url);
		URLConnection c = u.openConnection();
		return c.getInputStream();
	}

	/**
	 * Loads a package from a file on disk or the Simplifier repo using the
	 * {@link PackageCacheManager}.
	 */
	private NpmPackage loadIg(String id, String ver) throws IOException {
		if (packageCacheManager != null) {
			return packageCacheManager.loadPackage(id, ver);
		}
		return null;
	}

	/**
	 * Installs a FHIR package and its dependencies by persists instances of the following
	 * types of resources to the db in the given order:
	 *
	 * - NamingSystem, CodeSystem, ValueSet, StructureDefinition (with snapshots),
	 *   ConceptMap, SearchParameter, Subscription
	 *
	 * Creates the resources if non-existent, updates them otherwise.
	 */
	private void install(NpmPackage npmPackage) throws IOException {
		String name = npmPackage.getNpm().get("name").getAsString();
		String version = npmPackage.getNpm().get("version").getAsString();
		String packageFhirVersion = npmPackage.fhirVersion();
		if (!isCompatibleWithCurrentFhirVersion(packageFhirVersion)) {
			ourLog.warn("Cannot install package {}#{}, FHIR versions mismatch (expected <={}, package uses {})",
				name, version, fhirContext.getVersion().getVersion().getFhirVersionString(), packageFhirVersion);
			return;
		}
		fetchAndInstallDependencies(npmPackage);

		ourLog.info("Installing package: {}#{}", name, version);
		for (String type : RESOURCE_TYPES) {
			Collection<IBaseResource> resources = parseResourcesOfType(type, npmPackage);
			if (type.equals("StructureDefinition")) {
				resources = snapshotGenerator.generateFrom(resources);
			}
			ourLog.info(String.format("creating or updating %s resources of type %s", resources.size(), type));

			resources.stream().forEach(r -> createOrUpdate(r));
		}
		ourLog.info(String.format("Finished installation of package: %s#%s", name, version));
	}

	private void fetchAndInstallDependencies(NpmPackage npmPackage) throws IOException {
		if (npmPackage.getNpm().has("dependencies")) {
			Map<String, String> dependencies = new Gson().fromJson(npmPackage.getNpm().get("dependencies"), HashMap.class);
			for (Map.Entry<String, String> d : dependencies.entrySet()) {
				String id = d.getKey();
				String ver = d.getValue();
				if (id.startsWith("hl7.fhir")) {
					continue; // todo : which packages to ignore?
				}
				if (packageCacheManager == null) {
					String name = npmPackage.getNpm().get("name").getAsString();
					ourLog.warn("Cannot install dependency {}#{} from package {}", id, ver, name);
					return;
				}
				NpmPackage dependency = packageCacheManager.loadPackage(id, ver); // resolve in local cache or on packages.fhir.org
				fetchAndInstallDependencies(dependency);                          // recursive call to install children before parent
				install(dependency);
			}
		}
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
	private Collection<IBaseResource> parseResourcesOfType(String type, NpmPackage pkg)
		throws IOException {

		if (!pkg.getFolders().containsKey("package")) {
			return Collections.EMPTY_LIST;
		}
		ArrayList<IBaseResource> res = new ArrayList<>();
		for (String file : pkg.getFolders().get("package").listFiles()) {
			if (file.startsWith(type)) {
				byte[] content = pkg.getFolders().get("package").fetchFile(file);
				IBaseResource r = fhirContext.newJsonParser().parseResource(new String(content));
				res.add(r);
			}
		}
		return res;
	}
}
