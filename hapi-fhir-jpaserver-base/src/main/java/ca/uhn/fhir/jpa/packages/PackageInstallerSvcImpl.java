package ca.uhn.fhir.jpa.packages;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.SearchParameterUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.utilities.cache.IPackageCacheManager;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @since 5.1.0
 */
public class PackageInstallerSvcImpl implements IPackageInstallerSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(PackageInstallerSvcImpl.class);
	public static List<String> DEFAULT_INSTALL_TYPES = Collections.unmodifiableList(Lists.newArrayList(
		"NamingSystem",
		"CodeSystem",
		"ValueSet",
		"StructureDefinition",
		"ConceptMap",
		"SearchParameter",
		"Subscription"
	));

	boolean enabled = true;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IValidationSupport validationSupport;
	@Autowired
	private IHapiPackageCacheManager packageCacheManager;

	/**
	 * Constructor
	 */
	public PackageInstallerSvcImpl() {
		super();
	}

	@PostConstruct
	public void initialize() {
		switch (myFhirContext.getVersion().getVersion()) {
			case R5:
			case R4:
			case DSTU3:
				break;

			case DSTU2:
			case DSTU2_HL7ORG:
			case DSTU2_1:
			default: {
				ourLog.info("IG installation not supported for version: {}", myFhirContext.getVersion().getVersion());
				enabled = false;
			}
		}
	}

	/**
	 * Loads and installs an IG from a file on disk or the Simplifier repo using
	 * the {@link IPackageCacheManager}.
	 * <p>
	 * Installs the IG by persisting instances of the following types of resources:
	 * <p>
	 * - NamingSystem, CodeSystem, ValueSet, StructureDefinition (with snapshots),
	 * ConceptMap, SearchParameter, Subscription
	 * <p>
	 * Creates the resources if non-existent, updates them otherwise.
	 *
	 * @param theInstallationSpec The details about what should be installed
	 */
	@Override
	public PackageInstallOutcomeJson install(PackageInstallationSpec theInstallationSpec) throws ImplementationGuideInstallationException {
		PackageInstallOutcomeJson retVal = new PackageInstallOutcomeJson();
		if (enabled) {
			try {
				NpmPackage npmPackage = packageCacheManager.installPackage(theInstallationSpec);
				if (npmPackage == null) {
					throw new IOException("Package not found");
				}

				retVal.getMessage().addAll(JpaPackageCache.getProcessingMessages(npmPackage));

				if (theInstallationSpec.isFetchDependencies()) {
					fetchAndInstallDependencies(npmPackage, theInstallationSpec, retVal);
				}

				if (theInstallationSpec.getInstallMode() == PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL) {
					install(npmPackage, theInstallationSpec);
				}

			} catch (IOException e) {
				throw new ImplementationGuideInstallationException("Could not load NPM package " + theInstallationSpec.getName() + "#" + theInstallationSpec.getVersion(), e);
			}
		}

		return retVal;
	}

	/**
	 * Installs a package and its dependencies.
	 * <p>
	 * Fails fast if one of its dependencies could not be installed.
	 *
	 * @throws ImplementationGuideInstallationException if installation fails
	 */
	private void install(NpmPackage npmPackage, PackageInstallationSpec theInstallationSpec) throws ImplementationGuideInstallationException {
		String name = npmPackage.getNpm().get("name").getAsString();
		String version = npmPackage.getNpm().get("version").getAsString();

		String fhirVersion = npmPackage.fhirVersion();
		String currentFhirVersion = myFhirContext.getVersion().getVersion().getFhirVersionString();
		assertFhirVersionsAreCompatible(fhirVersion, currentFhirVersion);

		List<String> installTypes;
		if (!theInstallationSpec.getInstallResourceTypes().isEmpty()) {
			installTypes = theInstallationSpec.getInstallResourceTypes();
		} else {
			installTypes = DEFAULT_INSTALL_TYPES;
		}

		ourLog.info("Installing package: {}#{}", name, version);
		int[] count = new int[installTypes.size()];

		for (int i = 0; i < installTypes.size(); i++) {
			Collection<IBaseResource> resources = parseResourcesOfType(installTypes.get(i), npmPackage);
			count[i] = resources.size();

			try {
				resources.stream()
					.map(r -> isStructureDefinitionWithoutSnapshot(r) ? generateSnapshot(r) : r)
					.forEach(r -> createOrUpdate(r));
			} catch (Exception e) {
				throw new ImplementationGuideInstallationException(String.format("Error installing IG %s#%s: %s", name, version, e.toString()), e);
			}
		}
		ourLog.info(String.format("Finished installation of package %s#%s:", name, version));

		for (int i = 0; i < count.length; i++) {
			ourLog.info(String.format("-- Created or updated %s resources of type %s", count[i], installTypes.get(i)));
		}
	}

	private void fetchAndInstallDependencies(NpmPackage npmPackage, PackageInstallationSpec theInstallationSpec, PackageInstallOutcomeJson theOutcome) throws ImplementationGuideInstallationException {
		if (npmPackage.getNpm().has("dependencies")) {
			JsonElement dependenciesElement = npmPackage.getNpm().get("dependencies");
			Map<String, String> dependencies = new Gson().fromJson(dependenciesElement, HashMap.class);
			for (Map.Entry<String, String> d : dependencies.entrySet()) {
				String id = d.getKey();
				String ver = d.getValue();
				try {
					theOutcome.getMessage().add("Package " + npmPackage.id() + "#" + npmPackage.version() + " depends on package " + id + "#" + ver);

					boolean skip = false;
					for (String next : theInstallationSpec.getDependencyExcludes()) {
						if (id.matches(next)) {
							theOutcome.getMessage().add("Not installing dependency " + id + " because it matches exclude criteria: " + next);
							skip = true;
							break;
						}
					}
					if (skip) {
						continue;
					}

					// resolve in local cache or on packages.fhir.org
					NpmPackage dependency = packageCacheManager.loadPackage(id, ver);
					// recursive call to install dependencies of a package before
					// installing the package
					fetchAndInstallDependencies(dependency, theInstallationSpec, theOutcome);

					if (theInstallationSpec.getInstallMode() == PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL) {
						install(dependency, theInstallationSpec);
					}

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

		FhirVersionEnum fhirVersionEnum = FhirVersionEnum.forVersionString(fhirVersion);
		FhirVersionEnum currentFhirVersionEnum = FhirVersionEnum.forVersionString(currentFhirVersion);
		Validate.notNull(fhirVersionEnum, "Invalid FHIR version string: %s", fhirVersion);
		Validate.notNull(currentFhirVersionEnum, "Invalid FHIR version string: %s", currentFhirVersion);
		boolean compatible = fhirVersionEnum.equals(currentFhirVersionEnum);
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
		List<String> filesForType = pkg.getFolders().get("package").getTypes().get(type);
		if (filesForType != null) {
			for (String file : filesForType) {
				try {
					byte[] content = pkg.getFolders().get("package").fetchFile(file);
					resources.add(myFhirContext.newJsonParser().parseResource(new String(content)));
				} catch (IOException e) {
					throw new InternalErrorException("Cannot install resource of type " + type + ": Could not fetch file " + file, e);
				}
			}
		}
		return resources;
	}

	/**
	 * Create a resource or update it, if its already existing.
	 */
	private void createOrUpdate(IBaseResource resource) {
		try {
			IFhirResourceDao dao = myDaoRegistry.getResourceDao(resource.getClass());
			IBundleProvider searchResult = dao.search(createSearchParameterMapFor(resource));
			if (searchResult.isEmpty()) {

				if (validForUpload(resource)) {
					dao.create(resource);
				}

			} else {
				IBaseResource existingResource = verifySearchResultFor(resource, searchResult);
				if (existingResource != null) {
					resource.setId(existingResource.getIdElement().getValue());
					dao.update(resource);
				}
			}
		} catch (BaseServerResponseException e) {
			ourLog.warn("Failed to upload resource of type {} with ID {} - Error: {}", myFhirContext.getResourceType(resource), resource.getIdElement().getValue(), e.toString());
		}
	}

	boolean validForUpload(IBaseResource theResource) {
		String resourceType = myFhirContext.getResourceType(theResource);
		if ("SearchParameter".equals(resourceType)) {

			String code = SearchParameterUtil.getCode(myFhirContext, theResource);
			if (defaultString(code).startsWith("_")) {
				return false;
			}

			String expression = SearchParameterUtil.getExpression(myFhirContext, theResource);
			if (isBlank(expression)) {
				return false;
			}

			if (SearchParameterUtil.getBaseAsStrings(myFhirContext, theResource).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	private boolean isStructureDefinitionWithoutSnapshot(IBaseResource r) {
		FhirTerser terser = myFhirContext.newTerser();
		return r.getClass().getSimpleName().equals("StructureDefinition") &&
			terser.getSingleValueOrNull(r, "snapshot") == null;
	}

	private IBaseResource generateSnapshot(IBaseResource sd) {
		try {
			return validationSupport.generateSnapshot(new ValidationSupportContext(validationSupport), sd, null, null, null);
		} catch (Exception e) {
			throw new ImplementationGuideInstallationException(String.format(
				"Failure when generating snapshot of StructureDefinition: %s", sd.getIdElement()), e);
		}
	}

	private SearchParameterMap createSearchParameterMapFor(IBaseResource resource) {
		if (resource.getClass().getSimpleName().equals("NamingSystem")) {
			String uniqueId = extractUniqeIdFromNamingSystem(resource);
			return new SearchParameterMap().add("value", new StringParam(uniqueId).setExact(true));
		} else if (resource.getClass().getSimpleName().equals("Subscription")) {
			String id = extractIdFromSubscription(resource);
			return new SearchParameterMap().add("_id", new TokenParam(id));
		} else {
			String url = extractUniqueUrlFromMetadataResouce(resource);
			return new SearchParameterMap().add("url", new UriParam(url));
		}
	}

	private IBaseResource verifySearchResultFor(IBaseResource resource, IBundleProvider searchResult) {
		FhirTerser terser = myFhirContext.newTerser();
		if (resource.getClass().getSimpleName().equals("NamingSystem")) {
			if (searchResult.size() > 1) {
				ourLog.warn("Expected 1 NamingSystem with unique ID {}, found {}. Will not attempt to update resource.",
					extractUniqeIdFromNamingSystem(resource), searchResult.size());
				return null;
			}
			return getFirstResourceFrom(searchResult);
		} else if (resource.getClass().getSimpleName().equals("Subscription")) {
			if (searchResult.size() > 1) {
				ourLog.warn("Expected 1 Subscription with ID {}, found {}. Will not attempt to update resource.",
					extractIdFromSubscription(resource), searchResult.size());
				return null;
			}
			return getFirstResourceFrom(searchResult);
		} else {
			// Resource is of type CodeSystem, ValueSet, StructureDefinition, ConceptMap or SearchParameter
			if (searchResult.size() > 1) {
				ourLog.warn("Expected 1 MetadataResource with globally unique URL {}, found {}. " +
					"Will not attempt to update resource.", extractUniqueUrlFromMetadataResouce(resource), searchResult.size());
				return null;
			}
			return getFirstResourceFrom(searchResult);
		}
	}

	private String extractUniqeIdFromNamingSystem(IBaseResource resource) {
		FhirTerser terser = myFhirContext.newTerser();
		IBase uniqueIdComponent = (IBase) terser.getSingleValueOrNull(resource, "uniqueId");
		if (uniqueIdComponent == null) {
			throw new ImplementationGuideInstallationException("NamingSystem does not have uniqueId component.");
		}
		IPrimitiveType asPrimitiveType = (IPrimitiveType) terser.getSingleValueOrNull(uniqueIdComponent, "value");
		return (String) asPrimitiveType.getValue();
	}

	private String extractIdFromSubscription(IBaseResource resource) {
		FhirTerser terser = myFhirContext.newTerser();
		IPrimitiveType asPrimitiveType = (IPrimitiveType) terser.getSingleValueOrNull(resource, "id");
		return (String) asPrimitiveType.getValue();
	}

	private String extractUniqueUrlFromMetadataResouce(IBaseResource resource) {
		FhirTerser terser = myFhirContext.newTerser();
		IPrimitiveType asPrimitiveType = (IPrimitiveType) terser.getSingleValueOrNull(resource, "url");
		return (String) asPrimitiveType.getValue();
	}

	@VisibleForTesting
	void setFhirContextForUnitTest(FhirContext theCtx) {
		myFhirContext = theCtx;
	}

	private static IBaseResource getFirstResourceFrom(IBundleProvider searchResult) {
		try {
			return searchResult.getResources(0, 0).get(0);
		} catch (IndexOutOfBoundsException e) {
			ourLog.warn("Error when extracting resource from search result " +
				"(search result should have been non-empty))", e);
			return null;
		}
	}
}
