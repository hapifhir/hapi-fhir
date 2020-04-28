package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.*;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.UriParam;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.hl7.fhir.utilities.cache.PackageCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IgInstallerForDstu3 {

	private static final Logger ourLog = LoggerFactory.getLogger(IgInstallerForDstu3.class);

	@Autowired
	private DaoConfig daoConfig;
	@Autowired
	private FhirContext fhirContext;

	private static final Class<? extends MetadataResource>[] RESOURCE_TYPES_TO_INSTALL = new Class[]
		{ CodeSystem.class, ValueSet.class, StructureDefinition.class,
			ConceptMap.class, SearchParameter.class }; // todo : NamingSystem, Subscription?

	private Map<Class<? extends MetadataResource>, IFhirResourceDao<? extends MetadataResource>> daos;

	private PackageCacheManager packageCacheManager;

	@Autowired
	public IgInstallerForDstu3(@Qualifier("myNamingSystemDaoDstu3") IFhirResourceDao namingSystemDao,
		@Qualifier("myCodeSystemDaoDstu3") IFhirResourceDaoCodeSystem codeSystemDao,
		@Qualifier("myValueSetDaoDstu3") IFhirResourceDaoValueSet valueSetDao,
		@Qualifier("myStructureDefinitionDaoDstu3") IFhirResourceDaoStructureDefinition structureDefinitionDao,
		@Qualifier("myConceptMapDaoDstu3") IFhirResourceDaoConceptMap conceptMapDao,
		@Qualifier("mySearchParameterDaoDstu3") IFhirResourceDaoSearchParameter searchParameterDao,
		@Qualifier("mySubscriptionDaoDstu3") IFhirResourceDaoSubscription subscriptionDao) throws IOException {

		daos = new HashMap<>();
		daos.put(NamingSystem.class, namingSystemDao);
		daos.put(CodeSystem.class, codeSystemDao);
		daos.put(ValueSet.class, valueSetDao);
		daos.put(StructureDefinition.class, structureDefinitionDao);
		daos.put(ConceptMap.class, conceptMapDao);
		daos.put(SearchParameter.class, searchParameterDao);
		// daos.put(Subscription.class, subscriptionDao); // todo : Subscription extends DomainResource, not MetadataResource?

		packageCacheManager = new PackageCacheManager(true, 1);
	}

//	// @EventListener(ContextStartedEvent.class)
//	public void run() throws IOException {
//		String url = daoConfig.getMyImplementationGuideURL();
//		if (url == null) {
//			ourLog.debug("No implementation guide URL set.");
//			return;
//		}
//		NpmPackage ig;
//		if (url.startsWith("http")) {
//			try {
//				ig = loadIg(url);
//			} catch (IOException e) {
//				ourLog.error("Error fetching implementation guide from URL " + url, e); // todo : error handling
//				return;
//			}
//		} else {
//
//			// todo : load from package cache/ Simplifier repo using PackageCacheManager
//			ig = null;
//
//		}
//	}

	@VisibleForTesting
	public void fetchAndInstallDependencies(NpmPackage npmPackage) throws IOException {
		if (npmPackage.getNpm().has("dependencies")) {
			Map<String, String> dependencies = new Gson().fromJson(npmPackage.getNpm().get("dependencies"), HashMap.class);
			for (Map.Entry<String, String> d : dependencies.entrySet()) {
				String id = d.getKey();
				String ver = d.getValue();
				if (id.startsWith("hl7.fhir")) {
					continue; // todo : which packages to ignore?
				}
				NpmPackage dependency = packageCacheManager.loadPackage(id, ver); // resolve in local cache or on packages.fhir.org
				fetchAndInstallDependencies(dependency);                          // recursive call to install children before parent
				install(dependency);
			}
		}
	}

	/**
	 * Persists instances of the following resource types to the db:
	 *
	 * - NamingSystem, CodeSystem, ValueSet, StructureDefinition (with snapshots), ConceptMap, SearchParameter, Subscription
	 *
	 * in this specific order.
	 *
	 * Creates the resources if non-existent, updates them otherwise.
	 */
	@VisibleForTesting
	public void install(NpmPackage npmPackage) throws IOException {
		String name = npmPackage.getNpm().get("name").toString();
		String version = npmPackage.getNpm().get("version").toString();
		ourLog.info(String.format("Installing package: %s#%s", name, version));

		for (Class<? extends MetadataResource> type : RESOURCE_TYPES_TO_INSTALL) {
			Collection<? extends MetadataResource> resources;
			if (type == StructureDefinition.class) {
				resources = generateSnapshots(npmPackage);
			} else {
				resources = fetchResourcesOfType(type, npmPackage);
			}
			ourLog.info(String.format("creating or updating %s resources of type %s", resources.size(), type.getSimpleName()));

			resources.stream().forEach(r -> createOrUpdate(r));
		}
	}

	/**
	 * Generates snapshots of StructureDefinitions that does not already have one
	 */
	@VisibleForTesting
	public Collection<StructureDefinition> generateSnapshots(NpmPackage npmPackage) throws IOException {
		return fetchResourcesOfType(StructureDefinition.class, npmPackage).stream()
			.map(sd -> sd.hasSnapshot() ? sd : getStructureDefinitionDao().generateSnapshot(sd, null, null, null))
			.map(StructureDefinition.class::cast)
			.collect(Collectors.toList());
	}

	@VisibleForTesting
	public <T extends IBaseResource> Collection<T> fetchResourcesOfType(Class<T> clazz, NpmPackage npmPackage) throws IOException {
		if (!npmPackage.getFolders().containsKey("package")) {
			return Collections.EMPTY_LIST;
		}
		ArrayList<T> res = new ArrayList<>();
		for (String file : npmPackage.getFolders().get("package").listFiles()) {
			if (file.startsWith(clazz.getSimpleName())) {
				byte[] content = npmPackage.getFolders().get("package").fetchFile(file);
				T sd = fhirContext.newJsonParser().parseResource(
					clazz, new String(content));
				res.add(sd);
			}
		}
		return res;
	}

	/**
	 * Create a resource or update it, if its already existing.
	 */
	private void createOrUpdate(MetadataResource resource) {
		IBundleProvider searchResult = daos.get(resource.getClass())
			.search(createSearchParameterMap(resource));
		if (searchResult.isEmpty()) {
			create(resource, daos.get(resource.getClass()));
		} else {
			if (searchResult.size() > 1) {
				// todo : error handling
				// ourLog.warn(String.format("expected 1 MetadataResource with globally unique URL %s, found %s"),
				// found <size>
			}
			IBaseResource existingResource = searchResult.getResources(0, 0).get(0);
			resource.setId(existingResource.getIdElement().getValue());
			update(resource, daos.get(resource.getClass()));
		}
	}

	private <T extends MetadataResource> void create(MetadataResource resource, IFhirResourceDao<T> dao) {
		dao.create((T) resource);
	}

	private <T extends MetadataResource> void update(MetadataResource resource, IFhirResourceDao<T> dao) {
		dao.update((T) resource);
	}

	/**
	 * Loads a package from the WWW or a file on the disk.
	 */
	@VisibleForTesting
	public NpmPackage loadIg(String url) throws IOException {
		return NpmPackage.fromPackage(toInputStream(url));
	}

	/**
	 * Loads a package using the {@link PackageCacheManager}.
	 */
	@VisibleForTesting
	public NpmPackage loadIg(String id, String ver) throws IOException {
		return packageCacheManager.loadPackage(id, ver);
	}

	private InputStream toInputStream(String url) throws IOException {
		URL u = new URL(url);
		URLConnection c = u.openConnection();
		return c.getInputStream();
	}

	private IFhirResourceDaoStructureDefinition<StructureDefinition> getStructureDefinitionDao() {
		return (IFhirResourceDaoStructureDefinition) daos.get(StructureDefinition.class);
	}

	/**
	 * Defines the parameters to search for existing resources on
	 */
	private SearchParameterMap createSearchParameterMap(MetadataResource resource) {
		if (resource instanceof NamingSystem) {
			return null; // todo : not implemented
		} else {
			return new SearchParameterMap().add("url", new UriParam(resource.getUrl()));
		}
	}
}
