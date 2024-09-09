/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.validation.SearchParameterDaoValidator;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import ca.uhn.fhir.jpa.packages.loader.PackageResourceParsingSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistryController;
import ca.uhn.fhir.jpa.searchparam.util.SearchParameterHelper;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.utilities.json.model.JsonObject;
import org.hl7.fhir.utilities.npm.IPackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.packages.util.PackageUtils.DEFAULT_INSTALL_TYPES;
import static ca.uhn.fhir.util.SearchParameterUtil.getBaseAsStrings;

/**
 * @since 5.1.0
 */
public class PackageInstallerSvcImpl implements IPackageInstallerSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(PackageInstallerSvcImpl.class);

	boolean enabled = true;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IValidationSupport validationSupport;

	@Autowired
	private IHapiPackageCacheManager myPackageCacheManager;

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private INpmPackageVersionDao myPackageVersionDao;

	@Autowired
	private ISearchParamRegistryController mySearchParamRegistryController;

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	private SearchParameterHelper mySearchParameterHelper;

	@Autowired
	private PackageResourceParsingSvc myPackageResourceParsingSvc;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private SearchParameterDaoValidator mySearchParameterDaoValidator;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

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
			case R4B:
			case R4:
			case DSTU3:
				break;

			case DSTU2:
			case DSTU2_HL7ORG:
			case DSTU2_1:
			default: {
				ourLog.info(
						"IG installation not supported for version: {}",
						myFhirContext.getVersion().getVersion());
				enabled = false;
			}
		}
	}

	@Override
	public PackageDeleteOutcomeJson uninstall(PackageInstallationSpec theInstallationSpec) {
		PackageDeleteOutcomeJson outcome =
				myPackageCacheManager.uninstallPackage(theInstallationSpec.getName(), theInstallationSpec.getVersion());
		validationSupport.invalidateCaches();
		return outcome;
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
	@SuppressWarnings("ConstantConditions")
	@Override
	public PackageInstallOutcomeJson install(PackageInstallationSpec theInstallationSpec)
			throws ImplementationGuideInstallationException {
		PackageInstallOutcomeJson retVal = new PackageInstallOutcomeJson();
		if (enabled) {
			try {

				boolean exists = myTxService
						.withSystemRequest()
						.withRequestPartitionId(RequestPartitionId.defaultPartition())
						.execute(() -> {
							Optional<NpmPackageVersionEntity> existing = myPackageVersionDao.findByPackageIdAndVersion(
									theInstallationSpec.getName(), theInstallationSpec.getVersion());
							return existing.isPresent();
						});
				if (exists) {
					ourLog.info(
							"Package {}#{} is already installed",
							theInstallationSpec.getName(),
							theInstallationSpec.getVersion());
				}

				NpmPackage npmPackage = myPackageCacheManager.installPackage(theInstallationSpec);
				if (npmPackage == null) {
					throw new IOException(Msg.code(1284) + "Package not found");
				}

				retVal.getMessage().addAll(JpaPackageCache.getProcessingMessages(npmPackage));

				if (theInstallationSpec.isFetchDependencies()) {
					fetchAndInstallDependencies(npmPackage, theInstallationSpec, retVal);
				}

				if (theInstallationSpec.getInstallMode() == PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL) {
					install(npmPackage, theInstallationSpec, retVal);

					// If any SearchParameters were installed, let's load them right away
					mySearchParamRegistryController.refreshCacheIfNecessary();
				}

				validationSupport.invalidateCaches();

			} catch (IOException e) {
				throw new ImplementationGuideInstallationException(
						Msg.code(1285) + "Could not load NPM package " + theInstallationSpec.getName() + "#"
								+ theInstallationSpec.getVersion(),
						e);
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
	private void install(
			NpmPackage npmPackage, PackageInstallationSpec theInstallationSpec, PackageInstallOutcomeJson theOutcome)
			throws ImplementationGuideInstallationException {
		String name = npmPackage.getNpm().get("name").asJsonString().getValue();
		String version = npmPackage.getNpm().get("version").asJsonString().getValue();

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
			String type = installTypes.get(i);

			Collection<IBaseResource> resources = myPackageResourceParsingSvc.parseResourcesOfType(type, npmPackage);
			count[i] = resources.size();

			for (IBaseResource next : resources) {
				try {
					next = isStructureDefinitionWithoutSnapshot(next) ? generateSnapshot(next) : next;
					install(next, theInstallationSpec, theOutcome);
				} catch (Exception e) {
					ourLog.warn(
							"Failed to upload resource of type {} with ID {} - Error: {}",
							myFhirContext.getResourceType(next),
							next.getIdElement().getValue(),
							e.toString());
					throw new ImplementationGuideInstallationException(
							Msg.code(1286) + String.format("Error installing IG %s#%s: %s", name, version, e), e);
				}
			}
		}
		ourLog.info(String.format("Finished installation of package %s#%s:", name, version));

		for (int i = 0; i < count.length; i++) {
			ourLog.info(String.format("-- Created or updated %s resources of type %s", count[i], installTypes.get(i)));
		}
	}

	private void fetchAndInstallDependencies(
			NpmPackage npmPackage, PackageInstallationSpec theInstallationSpec, PackageInstallOutcomeJson theOutcome)
			throws ImplementationGuideInstallationException {
		if (npmPackage.getNpm().has("dependencies")) {
			JsonObject dependenciesElement =
					npmPackage.getNpm().get("dependencies").asJsonObject();
			for (String id : dependenciesElement.getNames()) {
				String ver = dependenciesElement.getJsonString(id).asString();
				try {
					theOutcome
							.getMessage()
							.add("Package " + npmPackage.id() + "#" + npmPackage.version() + " depends on package " + id
									+ "#" + ver);

					boolean skip = false;
					for (String next : theInstallationSpec.getDependencyExcludes()) {
						if (id.matches(next)) {
							theOutcome
									.getMessage()
									.add("Not installing dependency " + id + " because it matches exclude criteria: "
											+ next);
							skip = true;
							break;
						}
					}
					if (skip) {
						continue;
					}

					// resolve in local cache or on packages.fhir.org
					NpmPackage dependency = myPackageCacheManager.loadPackage(id, ver);
					// recursive call to install dependencies of a package before
					// installing the package
					fetchAndInstallDependencies(dependency, theInstallationSpec, theOutcome);

					if (theInstallationSpec.getInstallMode()
							== PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL) {
						install(dependency, theInstallationSpec, theOutcome);
					}

				} catch (IOException e) {
					throw new ImplementationGuideInstallationException(
							Msg.code(1287) + String.format("Cannot resolve dependency %s#%s", id, ver), e);
				}
			}
		}
	}

	/**
	 * Asserts if package FHIR version is compatible with current FHIR version
	 * by using semantic versioning rules.
	 */
	protected void assertFhirVersionsAreCompatible(String fhirVersion, String currentFhirVersion)
			throws ImplementationGuideInstallationException {

		FhirVersionEnum fhirVersionEnum = FhirVersionEnum.forVersionString(fhirVersion);
		FhirVersionEnum currentFhirVersionEnum = FhirVersionEnum.forVersionString(currentFhirVersion);
		Validate.notNull(fhirVersionEnum, "Invalid FHIR version string: %s", fhirVersion);
		Validate.notNull(currentFhirVersionEnum, "Invalid FHIR version string: %s", currentFhirVersion);
		boolean compatible = fhirVersionEnum.equals(currentFhirVersionEnum);
		if (!compatible && fhirVersion.startsWith("R4") && currentFhirVersion.startsWith("R4")) {
			compatible = true;
		}
		if (!compatible) {
			throw new ImplementationGuideInstallationException(Msg.code(1288)
					+ String.format(
							"Cannot install implementation guide: FHIR versions mismatch (expected <=%s, package uses %s)",
							currentFhirVersion, fhirVersion));
		}
	}

	/**
	 * ============================= Utility methods ===============================
	 */
	@VisibleForTesting
	void install(
			IBaseResource theResource,
			PackageInstallationSpec theInstallationSpec,
			PackageInstallOutcomeJson theOutcome) {

		if (!validForUpload(theResource)) {
			ourLog.warn(
					"Failed to upload resource of type {} with ID {} - Error: Resource failed validation",
					theResource.fhirType(),
					theResource.getIdElement().getValue());
			return;
		}

		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		SearchParameterMap map = createSearchParameterMapFor(theResource);
		IBundleProvider searchResult = searchResource(dao, map);

		String resourceQuery = map.toNormalizedQueryString(myFhirContext);
		if (!searchResult.isEmpty() && !theInstallationSpec.isReloadExisting()) {
			ourLog.info("Skipping update of existing resource matching {}", resourceQuery);
			return;
		}
		if (!searchResult.isEmpty()) {
			ourLog.info("Updating existing resource matching {}", resourceQuery);
		}
		IBaseResource existingResource =
				!searchResult.isEmpty() ? searchResult.getResources(0, 1).get(0) : null;
		boolean isInstalled = createOrUpdateResource(dao, theResource, existingResource);
		if (isInstalled) {
			theOutcome.incrementResourcesInstalled(myFhirContext.getResourceType(theResource));
		}
	}

	private Optional<IBaseResource> readResourceById(IFhirResourceDao dao, IIdType id) {
		try {
			return Optional.ofNullable(dao.read(id.toUnqualifiedVersionless(), createRequestDetails()));

		} catch (Exception exception) {
			// ignore because we're running this query to help build the log
			ourLog.warn("Exception when trying to read resource with ID: {}, message: {}", id, exception.getMessage());
		}

		return Optional.empty();
	}

	private IBundleProvider searchResource(IFhirResourceDao theDao, SearchParameterMap theMap) {
		return theDao.search(theMap, createRequestDetails());
	}

	protected boolean createOrUpdateResource(
			IFhirResourceDao theDao, IBaseResource theResource, IBaseResource theExistingResource) {
		final IIdType id = theResource.getIdElement();

		if (theExistingResource == null && id.isEmpty()) {
			ourLog.debug("Install resource without id will be created");
			theDao.create(theResource, createRequestDetails());
			return true;
		}

		if (theExistingResource == null && !id.isEmpty() && id.isIdPartValidLong()) {
			String newIdPart = "npm-" + id.getIdPart();
			id.setParts(id.getBaseUrl(), id.getResourceType(), newIdPart, id.getVersionIdPart());
		}

		boolean isExistingUpdated = updateExistingResourceIfNecessary(theDao, theResource, theExistingResource);
		boolean shouldOverrideId = theExistingResource != null && !isExistingUpdated;

		if (shouldOverrideId) {
			ourLog.debug(
					"Existing resource {} will be overridden with installed resource {}",
					theExistingResource.getIdElement(),
					id);
			theResource.setId(theExistingResource.getIdElement().toUnqualifiedVersionless());
		} else {
			ourLog.debug("Install resource {} will be created", id);
		}

		DaoMethodOutcome outcome = updateResource(theDao, theResource);
		return outcome != null && !outcome.isNop();
	}

	/*
	 * This function helps preserve the resource types in the base of an existing SP when an overriding SP's base
	 * covers only a subset of the existing base.
	 *
	 * For example, say for an existing SP,
	 *  -  the current base is: [ResourceTypeA, ResourceTypeB]
	 *   - the new base is: [ResourceTypeB]
	 *
	 * If we were to overwrite the existing SP's base to the new base ([ResourceTypeB]) then the
	 * SP would stop working on ResourceTypeA, which would be a loss of functionality.
	 *
	 * Instead, this function updates the existing SP's base by removing the resource types that
	 * are covered by the overriding SP.
	 * In our example, this function updates the existing SP's base to [ResourceTypeA], so that the existing SP
	 * still works on ResourceTypeA, and the caller then creates a new SP that covers ResourceTypeB.
	 * https://github.com/hapifhir/hapi-fhir/issues/5366
	 */
	private boolean updateExistingResourceIfNecessary(
			IFhirResourceDao theDao, IBaseResource theResource, IBaseResource theExistingResource) {
		if (!"SearchParameter".equals(theResource.getClass().getSimpleName())) {
			return false;
		}
		if (theExistingResource == null) {
			return false;
		}
		if (theExistingResource
				.getIdElement()
				.getIdPart()
				.equals(theResource.getIdElement().getIdPart())) {
			return false;
		}
		Collection<String> remainingBaseList = new HashSet<>(getBaseAsStrings(myFhirContext, theExistingResource));
		remainingBaseList.removeAll(getBaseAsStrings(myFhirContext, theResource));
		if (remainingBaseList.isEmpty()) {
			return false;
		}
		myFhirContext
				.getResourceDefinition(theExistingResource)
				.getChildByName("base")
				.getMutator()
				.setValue(theExistingResource, null);

		for (String baseResourceName : remainingBaseList) {
			myFhirContext.newTerser().addElement(theExistingResource, "base", baseResourceName);
		}
		ourLog.info(
				"Existing SearchParameter {} will be updated with base {}",
				theExistingResource.getIdElement().getIdPart(),
				remainingBaseList);
		updateResource(theDao, theExistingResource);
		return true;
	}

	private DaoMethodOutcome updateResource(IFhirResourceDao theDao, IBaseResource theResource) {
		DaoMethodOutcome outcome = null;

		IIdType id = theResource.getIdElement();
		RequestDetails requestDetails = createRequestDetails();

		try {
			outcome = theDao.update(theResource, requestDetails);
		} catch (ResourceVersionConflictException exception) {
			final Optional<IBaseResource> optResource = readResourceById(theDao, id);

			final String existingResourceUrlOrNull = optResource
					.filter(MetadataResource.class::isInstance)
					.map(MetadataResource.class::cast)
					.map(MetadataResource::getUrl)
					.orElse(null);
			final String newResourceUrlOrNull =
					(theResource instanceof MetadataResource) ? ((MetadataResource) theResource).getUrl() : null;

			ourLog.error(
					"Version conflict error: This is possibly due to a collision between ValueSets from different IGs that are coincidentally using the same resource ID: [{}] and new resource URL: [{}], with the exisitng resource having URL: [{}].  Ignoring this update and continuing:  The first IG wins.  ",
					id.getIdPart(),
					newResourceUrlOrNull,
					existingResourceUrlOrNull,
					exception);
		}
		return outcome;
	}

	private RequestDetails createRequestDetails() {
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		if (myPartitionSettings.isPartitioningEnabled()) {
			requestDetails.setRequestPartitionId(RequestPartitionId.defaultPartition());
		}
		return requestDetails;
	}

	boolean validForUpload(IBaseResource theResource) {
		String resourceType = myFhirContext.getResourceType(theResource);
		if ("SearchParameter".equals(resourceType) && !isValidSearchParameter(theResource)) {
			// this is an invalid search parameter
			return false;
		}

		if (!isValidResourceStatusForPackageUpload(theResource)) {
			ourLog.warn(
					"Failed to validate resource of type {} with ID {} - Error: Resource status not accepted value.",
					theResource.fhirType(),
					theResource.getIdElement().getValue());
			return false;
		}

		return true;
	}

	private boolean isValidSearchParameter(IBaseResource theResource) {
		try {
			org.hl7.fhir.r5.model.SearchParameter searchParameter =
					myVersionCanonicalizer.searchParameterToCanonical(theResource);
			mySearchParameterDaoValidator.validate(searchParameter);
			return true;
		} catch (UnprocessableEntityException unprocessableEntityException) {
			ourLog.error(
					"The SearchParameter with URL {} is invalid. Validation Error: {}",
					SearchParameterUtil.getURL(myFhirContext, theResource),
					unprocessableEntityException.getMessage());
			return false;
		}
	}

	/**
	 * For resources like {@link org.hl7.fhir.r4.model.Subscription}, {@link org.hl7.fhir.r4.model.DocumentReference},
	 * and {@link org.hl7.fhir.r4.model.Communication}, the status field doesn't necessarily need to be set to 'active'
	 * for that resource to be eligible for upload via packages. For example, all {@link org.hl7.fhir.r4.model.Subscription}
	 * have a status of {@link org.hl7.fhir.r4.model.Subscription.SubscriptionStatus#REQUESTED} when they are originally
	 * inserted into the database, so we accept that value for {@link org.hl7.fhir.r4.model.Subscription} instead.
	 * Furthermore, {@link org.hl7.fhir.r4.model.DocumentReference} and {@link org.hl7.fhir.r4.model.Communication} can
	 * exist with a wide variety of values for status that include ones such as
	 * {@link org.hl7.fhir.r4.model.Communication.CommunicationStatus#ENTEREDINERROR},
	 * {@link org.hl7.fhir.r4.model.Communication.CommunicationStatus#UNKNOWN},
	 * {@link org.hl7.fhir.r4.model.DocumentReference.ReferredDocumentStatus#ENTEREDINERROR},
	 * {@link org.hl7.fhir.r4.model.DocumentReference.ReferredDocumentStatus#PRELIMINARY}, and others, which while not considered
	 * 'final' values, should still be uploaded for reference.
	 *
	 * @return {@link Boolean#TRUE} if the status value of this resource is acceptable for package upload.
	 */
	private boolean isValidResourceStatusForPackageUpload(IBaseResource theResource) {
		if (!myStorageSettings.isValidateResourceStatusForPackageUpload()) {
			return true;
		}
		List<IPrimitiveType> statusTypes =
				myFhirContext.newFhirPath().evaluate(theResource, "status", IPrimitiveType.class);
		// Resource does not have a status field
		if (statusTypes.isEmpty()) {
			return true;
		}
		// Resource has no status field, or an explicitly null one
		if (!statusTypes.get(0).hasValue() || statusTypes.get(0).getValue() == null) {
			return false;
		}
		// Resource has a status, and we need to check based on type
		switch (theResource.fhirType()) {
			case "Subscription":
				return (statusTypes.get(0).getValueAsString().equals("requested"));
			case "DocumentReference":
			case "Communication":
				return (!statusTypes.get(0).getValueAsString().equals("?"));
			default:
				return (statusTypes.get(0).getValueAsString().equals("active"));
		}
	}

	private boolean isStructureDefinitionWithoutSnapshot(IBaseResource r) {
		boolean retVal = false;
		FhirTerser terser = myFhirContext.newTerser();
		if (r.getClass().getSimpleName().equals("StructureDefinition")) {
			Optional<String> kind = terser.getSinglePrimitiveValue(r, "kind");
			if (kind.isPresent() && !(kind.get().equals("logical"))) {
				retVal = terser.getSingleValueOrNull(r, "snapshot") == null;
			}
		}
		return retVal;
	}

	private IBaseResource generateSnapshot(IBaseResource sd) {
		try {
			return validationSupport.generateSnapshot(
					new ValidationSupportContext(validationSupport), sd, null, null, null);
		} catch (Exception e) {
			throw new ImplementationGuideInstallationException(
					Msg.code(1290)
							+ String.format(
									"Failure when generating snapshot of StructureDefinition: %s", sd.getIdElement()),
					e);
		}
	}

	private SearchParameterMap createSearchParameterMapFor(IBaseResource theResource) {
		String resourceType = theResource.getClass().getSimpleName();
		if ("NamingSystem".equals(resourceType)) {
			String uniqueId = extractUniqeIdFromNamingSystem(theResource);
			return SearchParameterMap.newSynchronous().add("value", new StringParam(uniqueId).setExact(true));
		} else if ("Subscription".equals(resourceType)) {
			String id = extractSimpleValue(theResource, "id");
			return SearchParameterMap.newSynchronous().add("_id", new TokenParam(id));
		} else if ("SearchParameter".equals(resourceType)) {
			return buildSearchParameterMapForSearchParameter(theResource);
		} else if (resourceHasUrlElement(theResource)) {
			String url = extractSimpleValue(theResource, "url");
			return SearchParameterMap.newSynchronous().add("url", new UriParam(url));
		} else {
			TokenParam identifierToken = extractIdentifierFromOtherResourceTypes(theResource);
			return SearchParameterMap.newSynchronous().add("identifier", identifierToken);
		}
	}

	/**
	 * Strategy is to build a SearchParameterMap same way the SearchParamValidatingInterceptor does, to make sure that
	 * the loader search detects existing resources and routes process to 'update' path, to avoid treating it as a new
	 * upload which validator later rejects as duplicated.
	 * To achieve this, we try canonicalizing the SearchParameter first (as the validator does) and if that is not possible
	 * we cascade to building the map from 'url' or 'identifier'.
	 */
	private SearchParameterMap buildSearchParameterMapForSearchParameter(IBaseResource theResource) {
		Optional<SearchParameterMap> spmFromCanonicalized =
				mySearchParameterHelper.buildSearchParameterMapFromCanonical(theResource);
		if (spmFromCanonicalized.isPresent()) {
			return spmFromCanonicalized.get();
		}

		if (resourceHasUrlElement(theResource)) {
			String url = extractSimpleValue(theResource, "url");
			return SearchParameterMap.newSynchronous().add("url", new UriParam(url));
		} else {
			TokenParam identifierToken = extractIdentifierFromOtherResourceTypes(theResource);
			return SearchParameterMap.newSynchronous().add("identifier", identifierToken);
		}
	}

	private String extractUniqeIdFromNamingSystem(IBaseResource theResource) {
		IBase uniqueIdComponent = (IBase) extractValue(theResource, "uniqueId");
		if (uniqueIdComponent == null) {
			throw new ImplementationGuideInstallationException(
					Msg.code(1291) + "NamingSystem does not have uniqueId component.");
		}
		return extractSimpleValue(uniqueIdComponent, "value");
	}

	private TokenParam extractIdentifierFromOtherResourceTypes(IBaseResource theResource) {
		Identifier identifier = (Identifier) extractValue(theResource, "identifier");
		if (identifier != null) {
			return new TokenParam(identifier.getSystem(), identifier.getValue());
		} else {
			throw new UnsupportedOperationException(Msg.code(1292)
					+ "Resources in a package must have a url or identifier to be loaded by the package installer.");
		}
	}

	private Object extractValue(IBase theResource, String thePath) {
		return myFhirContext.newTerser().getSingleValueOrNull(theResource, thePath);
	}

	private String extractSimpleValue(IBase theResource, String thePath) {
		IPrimitiveType<?> asPrimitiveType = (IPrimitiveType<?>) extractValue(theResource, thePath);
		return (String) asPrimitiveType.getValue();
	}

	private boolean resourceHasUrlElement(IBaseResource resource) {
		BaseRuntimeElementDefinition<?> def = myFhirContext.getElementDefinition(resource.getClass());
		if (!(def instanceof BaseRuntimeElementCompositeDefinition)) {
			throw new IllegalArgumentException(Msg.code(1293) + "Resource is not a composite type: "
					+ resource.getClass().getName());
		}
		BaseRuntimeElementCompositeDefinition<?> currentDef = (BaseRuntimeElementCompositeDefinition<?>) def;
		BaseRuntimeChildDefinition nextDef = currentDef.getChildByName("url");
		return nextDef != null;
	}

	@VisibleForTesting
	void setFhirContextForUnitTest(FhirContext theCtx) {
		myFhirContext = theCtx;
	}
}
