/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.validation.SearchParameterDaoValidator;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import ca.uhn.fhir.jpa.packages.loader.PackageResourceParsingSvc;
import ca.uhn.fhir.jpa.packages.util.PackageUtils;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistryController;
import ca.uhn.fhir.jpa.searchparam.util.SearchParameterHelper;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ca.uhn.fhir.util.MetaUtil;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.TerserUtil;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.utilities.npm.IPackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static ca.uhn.fhir.jpa.packages.PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL;
import static ca.uhn.fhir.jpa.packages.PackageInstallerSvcImpl.InstallResultEnum.CREATED;
import static ca.uhn.fhir.jpa.packages.PackageInstallerSvcImpl.InstallResultEnum.SKIPPED;
import static ca.uhn.fhir.jpa.packages.PackageInstallerSvcImpl.InstallResultEnum.UPDATED;
import static ca.uhn.fhir.jpa.packages.util.PackageUtils.DEFAULT_INSTALL_TYPES;
import static ca.uhn.fhir.util.SearchParameterUtil.getBaseAsStrings;

/**
 * @since 5.1.0
 */
public class PackageInstallerSvcImpl implements IPackageInstallerSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(PackageInstallerSvcImpl.class);
	private static final String OUR_PIPE_CHARACTER = "|";

	// Created by Claude Opus 4.6
	enum InstallResultEnum {
		SKIPPED("Skipped"),
		CREATED("Created"),
		UPDATED("Updated");

		private final String myDisplayName;

		InstallResultEnum(String theDisplayName) {
			myDisplayName = theDisplayName;
		}

		@Override
		public String toString() {
			return myDisplayName;
		}
	}

	private boolean myEnabled = true;

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

	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	private CommonCodeSystemsTerminologyService myCommonCodeSystemsTerminologyService;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	/**
	 * Constructor
	 */
	public PackageInstallerSvcImpl() {
		super();
	}

	@PostConstruct
	public void initialize() {
		myCommonCodeSystemsTerminologyService = new CommonCodeSystemsTerminologyService(myFhirContext);
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
				myEnabled = false;
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
		if (myEnabled) {
			try {

				logIfPackageAlreadyInstalled(theInstallationSpec);

				NpmPackage npmPackage = myPackageCacheManager.installPackage(theInstallationSpec);
				if (npmPackage == null) {
					throw new IOException(Msg.code(1284) + "Package " + theInstallationSpec.getName() + "#"
							+ theInstallationSpec.getVersion() + " not found");
				}

				retVal.getMessage().addAll(NpmPackageUtils.getProcessingMessages(npmPackage));

				if (theInstallationSpec.isFetchDependencies()) {
					fetchAndInstallDependencies(npmPackage, theInstallationSpec, retVal);
				}

				if (theInstallationSpec.getInstallMode() == STORE_AND_INSTALL
						|| theInstallationSpec.getInstallMode()
								== PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY) {
					installPackage(npmPackage, theInstallationSpec, retVal);

					if (theInstallationSpec.getInstallMode() == PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY) {
						retVal.getMessage()
								.add(
										"Resources have been successfully installed. This is INSTALL only, so there will be no NPM packages persisted.");
					}

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

	private void logIfPackageAlreadyInstalled(PackageInstallationSpec theInstallationSpec) {
		boolean exists = myTxService.withRequest(createRequestDetails()).execute(() -> {
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
	}

	/**
	 * Installs a package and its dependencies.
	 * <p>
	 * Fails fast if one of its dependencies could not be installed.
	 *
	 * @throws ImplementationGuideInstallationException if installation fails
	 */
	@Override
	public void installPackage(
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
		Map<String, EnumMap<InstallResultEnum, Integer>> installCounts = new HashMap<>();

		for (String type : installTypes) {

			Collection<IBaseResource> resources = myPackageResourceParsingSvc.parseResourcesOfType(type, npmPackage);

			for (IBaseResource next : resources) {
				try {
					next = isStructureDefinitionWithoutSnapshot(next) ? generateSnapshot(next) : next;
					InstallResultEnum result = install(next, theInstallationSpec, theOutcome);
					installCounts
							.computeIfAbsent(type, k -> new EnumMap<>(InstallResultEnum.class))
							.merge(result, 1, Integer::sum);
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

		if (theInstallationSpec.isDryRun()) {
			theOutcome.getMessage().add("Dry-run complete. No resources were stored.");

			for (Map.Entry<String, List<String>> set :
					theOutcome.getAddedResourceTypeToUniqueIdentifier().entrySet()) {
				String resourceType = set.getKey();
				List<String> resourceIdentifiers = set.getValue();
				theOutcome
						.getMessage()
						.add(String.format(
								"%s: %d new resource(s) would be created.", resourceType, resourceIdentifiers.size()));
			}
			for (Map.Entry<String, List<String>> set :
					theOutcome.getReplacedResourceToUniqueIdentifier().entrySet()) {
				String resourceType = set.getKey();
				List<String> resourceIdentifiers = set.getValue();
				for (String identifier : resourceIdentifiers) {
					theOutcome
							.getMessage()
							.add(String.format(
									"Resource of type %s with unique identifier %s would be overwritten.",
									resourceType, identifier));
				}
			}
		}
		ourLog.info("Finished installation of package {}#{}:", name, version);

		installCounts.forEach((type, counts) -> {
			counts.forEach((result, count) -> ourLog.info("-- {} {} resources of type {}", result, count, type));
		});
	}

	/**
	 * Starts an asynchronous batch job to install a package asynchronously as a background process
	 * @param theInstallationSpec the specification defining the package to install
	 * @return the instance id of the job, needed for polling for updates
	 */
	@Override
	public String installAsynchronously(PackageInstallationSpec theInstallationSpec) {
		if (!myEnabled) {
			ourLog.info(
					"Package installation is not supported for FHIR version {}",
					myFhirContext.getVersion().getVersion());

			return null;
		}

		logIfPackageAlreadyInstalled(theInstallationSpec);

		PackageInstallationJobParameters parameters = new PackageInstallationJobParameters();
		parameters.setInstallationSpec(theInstallationSpec);
		JobInstanceStartRequest startRequest =
				new JobInstanceStartRequest(Batch2JobDefinitionConstants.INSTALL_PACKAGE, parameters);
		Batch2JobStartResponse response = myJobCoordinator.startInstance(createRequestDetails(), startRequest);
		return response.getInstanceId();
	}

	@Override
	public PackageInstallationStatusJson checkInstallationStatus(String theJobId) {
		JobInstance jobInstance = myJobCoordinator.getInstance(theJobId);

		PackageInstallationStatusJson status = new PackageInstallationStatusJson();
		status.setJobId(theJobId);
		status.setStatus(jobInstance.getStatus().name());
		status.setProgress(jobInstance.getProgress());
		status.setCurrentStep(jobInstance.getCurrentGatedStepId());
		status.setStartTime(jobInstance.getStartTime());
		status.setOutcome(jobInstance.getReport());

		return status;
	}

	private void fetchAndInstallDependencies(
			NpmPackage npmPackage, PackageInstallationSpec theInstallationSpec, PackageInstallOutcomeJson theOutcome)
			throws ImplementationGuideInstallationException {
		List<PackageUtils.DependentPackage> dependentPackages =
				PackageUtils.extractDependentPackages(npmPackage, theInstallationSpec, theOutcome);

		for (PackageUtils.DependentPackage nextPackage : dependentPackages) {
			try {
				if (theInstallationSpec.isDryRun()) {
					theOutcome
							.getMessage()
							.add(String.format(
									"Installation would install %s:%s", nextPackage.name(), nextPackage.version()));
				} else {

					// resolve in local cache or on packages.fhir.org
					NpmPackage dependency =
							myPackageCacheManager.loadPackage(nextPackage.name(), nextPackage.version());

					// If the dependency's FHIR version is incompatible with the server,
					// attempt to load a version-specific variant (e.g., {id}.r4 for R4 servers)
					dependency = substituteVersionSpecificPackageIfNeeded(
							dependency, nextPackage.name(), nextPackage.version());

					// recursive call to install dependencies of a package before
					// installing the package
					fetchAndInstallDependencies(dependency, theInstallationSpec, theOutcome);

					if (theInstallationSpec.getInstallMode()
							== PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL) {
						installPackage(dependency, theInstallationSpec, theOutcome);
					}
				}
			} catch (IOException e) {
				throw new ImplementationGuideInstallationException(
						Msg.code(1287)
								+ String.format(
										"Cannot resolve dependency %s#%s", nextPackage.name(), nextPackage.version()),
						e);
			}
		}
	}

	/**
	 * Checks if a dependency package's FHIR version is compatible with the server's FHIR version.
	 * If incompatible, attempts to load a version-specific variant by appending a FHIR version
	 * suffix (e.g., ".r4" for R4/R4B servers, ".r5" for R5, ".r3" for DSTU3).
	 * <p>
	 * This handles cross-version packages like {@code hl7.fhir.uv.extensions} which declare
	 * FHIR version 5.0.0 but have R4-specific counterparts like {@code hl7.fhir.uv.extensions.r4}.
	 *
	 * @param theDependency the loaded dependency package
	 * @param theId the package ID
	 * @param theVersion the package version
	 * @return the original package if compatible, or the version-specific variant if found
	 */
	@Override
	public NpmPackage substituteVersionSpecificPackageIfNeeded(
			NpmPackage theDependency, String theId, String theVersion) {
		String dependencyFhirVersion = theDependency.fhirVersion();
		String serverFhirVersion = myFhirContext.getVersion().getVersion().getFhirVersionString();

		if (areFhirVersionsCompatible(dependencyFhirVersion, serverFhirVersion)) {
			return theDependency;
		}

		FhirVersionEnum serverVersionEnum = FhirVersionEnum.forVersionString(serverFhirVersion);
		if (serverVersionEnum == null) {
			return theDependency;
		}

		String suffix = getVersionSpecificSuffix(serverVersionEnum);
		if (suffix == null) {
			return theDependency;
		}

		String variantId = theId + suffix;
		ourLog.warn(
				"Dependency {}#{} declares FHIR version {} which is incompatible with server FHIR version {}. "
						+ "Attempting to load version-specific variant: {}#{}",
				theId,
				theVersion,
				dependencyFhirVersion,
				serverFhirVersion,
				variantId,
				theVersion);

		try {
			NpmPackage variant = myPackageCacheManager.loadPackage(variantId, theVersion);
			ourLog.info(
					"Successfully substituted {}#{} with version-specific variant {}#{}",
					theId,
					theVersion,
					variantId,
					theVersion);
			return variant;
		} catch (IOException e) {
			ourLog.warn(
					"Could not find version-specific variant {}#{}. "
							+ "Proceeding with original package {}#{} which may fail version compatibility checks.",
					variantId,
					theVersion,
					theId,
					theVersion);
			return theDependency;
		}
	}

	/**
	 * Returns the version-specific package suffix for the given FHIR version,
	 * or {@code null} if no suffix mapping exists.
	 */
	private static String getVersionSpecificSuffix(FhirVersionEnum theVersion) {
		switch (theVersion) {
			case R4:
			case R4B:
				return ".r4";
			case R5:
				return ".r5";
			case DSTU3:
				return ".r3";
			default:
				return null;
		}
	}

	/**
	 * Asserts if package FHIR version is compatible with current FHIR version
	 * by using semantic versioning rules.
	 */
	protected void assertFhirVersionsAreCompatible(String fhirVersion, String currentFhirVersion)
			throws ImplementationGuideInstallationException {

		if (!areFhirVersionsCompatible(fhirVersion, currentFhirVersion)) {
			throw new ImplementationGuideInstallationException(Msg.code(1288)
					+ String.format(
							"Cannot install implementation guide: FHIR versions mismatch (expected <=%s, package uses %s)",
							currentFhirVersion, fhirVersion));
		}
	}

	/**
	 * Checks whether two FHIR version strings are compatible. Versions are compatible if they
	 * resolve to the same {@link FhirVersionEnum}, or if both are in the R4 family (R4 or R4B).
	 *
	 * @param theFhirVersion the package's FHIR version string (e.g., "4.0.1", "4.3.0")
	 * @param theCurrentFhirVersion the server's FHIR version string
	 * @return {@code true} if the versions are compatible
	 */
	private static boolean areFhirVersionsCompatible(String theFhirVersion, String theCurrentFhirVersion) {
		FhirVersionEnum fhirVersionEnum = FhirVersionEnum.forVersionString(theFhirVersion);
		FhirVersionEnum currentFhirVersionEnum = FhirVersionEnum.forVersionString(theCurrentFhirVersion);
		if (fhirVersionEnum == null || currentFhirVersionEnum == null) {
			return false;
		}
		if (fhirVersionEnum.equals(currentFhirVersionEnum)) {
			return true;
		}
		boolean bothR4Family = (fhirVersionEnum == FhirVersionEnum.R4 || fhirVersionEnum == FhirVersionEnum.R4B)
				&& (currentFhirVersionEnum == FhirVersionEnum.R4 || currentFhirVersionEnum == FhirVersionEnum.R4B);
		return bothR4Family;
	}

	/**
	 * ============================= Utility methods ===============================
	 */
	@VisibleForTesting
	InstallResultEnum install(
			IBaseResource theResource,
			PackageInstallationSpec theInstallationSpec,
			PackageInstallOutcomeJson theOutcome) {

		if (!validForUpload(theResource)) {
			return SKIPPED;
		}

		SearchParameterMap map = createSearchParameterMapFor(theResource, theInstallationSpec);
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.fhirType());
		IBundleProvider searchResult = searchResource(dao, map);

		String resourceQuery = map.toNormalizedQueryString();
		if (!searchResult.isEmpty() && !theInstallationSpec.isReloadExisting()) {
			ourLog.info(
					"Skipping update of existing resource matching {} since `PackageInstallationSpec.reloadExisting=false",
					resourceQuery);
			return SKIPPED;
		}

		IBaseResource existingResource =
				findExistingResource(dao, theResource, searchResult).orElse(null);

		if (existingResource != null
				&& isContentNotPresentCodeSystem(existingResource)
				&& !theInstallationSpec.isOverwriteContentNotPresentCodeSystems()) {
			ourLog.info(
					"Skipping update of CodeSystem with content=not-present matching {} since `PackageInstallationSpec.overwriteContentNotPresentCodeSystems=false",
					resourceQuery);
			return SKIPPED;
		}

		if (!searchResult.isEmpty()) {
			ourLog.info("Updating existing resource matching {}", resourceQuery);
		}

		if (theInstallationSpec.isDryRun()) {
			constructDryRunReport(theResource, existingResource, map, theOutcome);
			return SKIPPED;
		}

		InstallResultEnum result = installResource(dao, theResource, existingResource, theInstallationSpec);
		if (result != SKIPPED) {
			theOutcome.incrementResourcesInstalled(myFhirContext.getResourceType(theResource));
		}
		return result;
	}

	// Generated by Claude Opus 4.6
	private Optional<IBaseResource> findExistingResource(
			IFhirResourceDao<?> theDao, IBaseResource theResource, IBundleProvider theSearchResult) {
		if (!theSearchResult.isEmpty()) {
			return theSearchResult.getAllResources().stream().findFirst();
		}
		if ("CodeSystem".equals(theResource.fhirType())) {
			Optional<IBaseResource> fromTermLayer = findExistingCodeSystemViaTermLayer(theDao, theResource);
			fromTermLayer.ifPresent(resource -> ourLog.info(
					"FHIR search index missed existing CodeSystem but term layer found it at {}; routing to update",
					resource.getIdElement().toUnqualifiedVersionless()));
			return fromTermLayer;
		}
		// TODO: a dedicated (resourceType, url, version) -> PID registry (like TRM_CODESYSTEM_VER for CodeSystem)
		//  would let us fall back for all canonical resource types, not just CodeSystem
		return Optional.empty();
	}

	// Generated by Claude Opus 4.6
	// Fallback lookup via term tables when the FHIR search index is stale/misconfigured (avoids HAPI-0848)
	private Optional<IBaseResource> findExistingCodeSystemViaTermLayer(
			IFhirResourceDao<?> theDao, IBaseResource theResource) {
		String url = extractStringValueOrEmpty(theResource, "url");
		if (url.isEmpty()) {
			return Optional.empty();
		}
		String version = extractStringValueOrEmpty(theResource, "version", () -> null);
		return myTxService
				.withRequest(createRequestDetails())
				.execute(() -> myTermCodeSystemStorageSvc.findExistingCodeSystemResourcePid(url, version))
				.map(pid -> theDao.readByPid(JpaPid.fromId(pid)));
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

	protected InstallResultEnum installResource(
			IFhirResourceDao theDao,
			IBaseResource theResource,
			IBaseResource theExistingResource,
			@Nonnull PackageInstallationSpec thePackageInstallationSpec) {

		prefixNumericIdIfNeeded(theResource);
		setPackageMetaSource(theResource, thePackageInstallationSpec);

		if (theExistingResource == null) {
			boolean created = createNewResource(theDao, theResource, thePackageInstallationSpec);
			return created ? CREATED : SKIPPED;
		} else {
			boolean updated = updateExistingResource(theDao, theResource, theExistingResource);
			return updated ? UPDATED : SKIPPED;
		}
	}

	private boolean constructDryRunReport(
			IBaseResource theResource,
			IBaseResource theExistingResource,
			SearchParameterMap theSpMap,
			PackageInstallOutcomeJson theOutcome) {
		String resourceType = theResource.fhirType();
		RuntimeResourceDefinition rtDefinition = myFhirContext.getResourceDefinition(resourceType);
		List<RuntimeSearchParam> sps = rtDefinition.getSearchParams();
		StringBuilder uniqueIdentifierSpBuilder = new StringBuilder();
		for (Map.Entry<String, List<List<IQueryParameterType>>> spSet : theSpMap.entrySet()) {
			String key = spSet.getKey();

			// TODO - will we ever see nothing? or a throw?
			RuntimeSearchParam runtimeSearchParameter = sps.stream()
					.filter(sp -> sp.getName().equals(key))
					.findFirst()
					.orElseThrow();

			String value = extractStringValueOrEmpty(theResource, runtimeSearchParameter.getPath());
			if (value.isEmpty()) {
				/*
				 * we don't expect to see this because we constructed this search paramter map out of the
				 * existing fields already and we're deconstructing it.
				 */
				throw new UnprocessableEntityException(
						Msg.code(2872) + " No unique value found at " + runtimeSearchParameter.getPath());
			}
			uniqueIdentifierSpBuilder.append(value);
		}

		// dry run
		if (theExistingResource != null) {
			theOutcome.addExistingResource(resourceType, uniqueIdentifierSpBuilder.toString());
		} else {
			// a resource we'd add
			theOutcome.addResourceTypeToBeAdded(resourceType, uniqueIdentifierSpBuilder.toString());
		}
		return false;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private boolean createNewResource(
			IFhirResourceDao theDao,
			IBaseResource theResource,
			@Nonnull PackageInstallationSpec thePackageInstallationSpec) {

		if (allowMultipleVersionsForResource(theResource, thePackageInstallationSpec)) {
			// Use a server-assigned ID to prevent FHIR ID conflicts for multiple versions of
			// Conformance/Canonical resources (e.g. StructureDefinition.version),
			// which is helpful for validation against versioned profiles.
			// (Note: This is not to be confused with meta.versionId)
			theResource.setId(new IdDt());
		}

		if (theResource.getIdElement().isEmpty()) {
			ourLog.debug("Installing resource with server-assigned id");
			theDao.create(theResource, createRequestDetails());
			return true;
		} else {
			ourLog.debug("Installing resource {} with client-assigned ID", theResource.getIdElement());
			DaoMethodOutcome outcome = updateResource(theDao, theResource);
			return outcome != null && !outcome.isNop();
		}
	}

	private boolean updateExistingResource(
			IFhirResourceDao theDao, IBaseResource theResource, IBaseResource theExistingResource) {
		if (isSearchParameter(theResource)) {
			return updateExistingSearchParameter(theDao, theResource, theExistingResource);
		} else {
			return updateExistingNonSearchParameter(theDao, theResource, theExistingResource);
		}
	}

	private boolean updateExistingNonSearchParameter(
			IFhirResourceDao theDao, IBaseResource theResource, IBaseResource theExistingResource) {
		// An existing resource is found,
		// update this resource and force-use the old ID
		ourLog.debug(
				"Existing resource {} will be overridden with installed resource {}",
				theExistingResource.getIdElement(),
				theResource.getIdElement());
		theResource.setId(theExistingResource.getIdElement().toUnqualifiedVersionless());
		DaoMethodOutcome outcome = updateResource(theDao, theResource);
		return outcome != null && !outcome.isNop();
	}

	private boolean updateExistingSearchParameter(
			IFhirResourceDao theDao, IBaseResource theResource, IBaseResource theExistingResource) {
		boolean wasBaseSplitAndExistingUpdated =
				updateExistingSearchParameterBaseIfNecessary(theDao, theResource, theExistingResource);

		if (wasBaseSplitAndExistingUpdated) {
			// The incoming SP has a different ID and its base set is different from the existing SP.
			// The existing SP was narrowed to retain only the base types NOT covered by the incoming SP.
			// Now create the incoming SP to cover its specified base types.
			// Use client-assigned ID if provided, otherwise server-assigned.
			if (theResource.getIdElement().isEmpty()) {
				ourLog.info(
						"Creating new SearchParameter with server-assigned ID after base split (no client-assigned ID in package)");
				theDao.create(theResource, createRequestDetails());
				return true;
			} else {
				ourLog.debug("Creating new SearchParameter {} after base split", theResource.getIdElement());
				DaoMethodOutcome outcome = updateResource(theDao, theResource);
				return outcome != null && !outcome.isNop();
			}
		} else {
			ourLog.debug("Overwriting existing SearchParameter {}", theExistingResource.getIdElement());
			theResource.setId(theExistingResource.getIdElement().toUnqualifiedVersionless());
			DaoMethodOutcome outcome = updateResource(theDao, theResource);
			return outcome != null && !outcome.isNop();
		}
	}

	private boolean isSearchParameter(IBaseResource theResource) {
		return theResource.fhirType().equals(ResourceType.SearchParameter.name());
	}

	/**
	 * Returns true if the given resource is a CodeSystem with content=not-present.
	 * Such CodeSystems store their concepts in terminology tables rather than inline,
	 * and should not be overwritten by IG packages by default.
	 */
	private boolean isContentNotPresentCodeSystem(IBaseResource theResource) {
		if (!theResource.fhirType().equals(ResourceType.CodeSystem.name())) {
			return false;
		}
		String content = extractStringValueOrEmpty(theResource, "content");
		return "not-present".equals(content);
	}

	private boolean allowMultipleVersionsForResource(
			IBaseResource theResource, PackageInstallationSpec thePackageInstallationSpec) {
		// For Search parameters don't allow multi version
		if (isSearchParameter(theResource)) {
			return false;
		}
		return thePackageInstallationSpec.getVersionPolicy() == PackageInstallationSpec.VersionPolicyEnum.MULTI_VERSION;
	}

	private void setPackageMetaSource(IBaseResource theResource, PackageInstallationSpec thePackageInstallationSpec) {
		if (thePackageInstallationSpec != null) {
			String metaSourceUrl =
					thePackageInstallationSpec.getName() + OUR_PIPE_CHARACTER + thePackageInstallationSpec.getVersion();
			MetaUtil.setSource(myFhirContext, theResource, metaSourceUrl);
		}
	}

	private void prefixNumericIdIfNeeded(IBaseResource theResource) {
		IIdType id = theResource.getIdElement();
		if (!id.isEmpty() && id.isIdPartValidLong()) {
			String newIdPart = "npm-" + id.getIdPart();
			id.setParts(id.getBaseUrl(), id.getResourceType(), newIdPart, id.getVersionIdPart());
		}
	}

	/*
	 * This function helps preserve the resource types in the base of an existing SP when an overriding SP's base
	 * differs from the existing base.
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
	private boolean updateExistingSearchParameterBaseIfNecessary(
			IFhirResourceDao theDao, IBaseResource theResource, IBaseResource theExistingResource) {
		if (!isSearchParameter(theResource)) {
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
		Collection<String> remainingBaseList = new HashSet<>(SearchParameterUtil.expandBaseWhenNeeded(
				myFhirContext, getBaseAsStrings(myFhirContext, theExistingResource)));
		remainingBaseList.removeAll(
				SearchParameterUtil.expandBaseWhenNeeded(myFhirContext, getBaseAsStrings(myFhirContext, theResource)));
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

	@VisibleForTesting
	RequestDetails createRequestDetails() {
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		if (myPartitionSettings.isPartitioningEnabled()) {
			requestDetails.setRequestPartitionId(myPartitionSettings.getDefaultRequestPartitionId());
		}
		return requestDetails;
	}

	boolean validForUpload(IBaseResource theResource) {
		String resourceType = myFhirContext.getResourceType(theResource);
		String resourceId = theResource.getIdElement().getValue();
		if ("SearchParameter".equals(resourceType) && !isValidSearchParameter(theResource)) {
			ourLog.warn("Skipping installation of resource {} because it is an invalid SearchParameter.", resourceId);
			return false;
		}

		if ("CodeSystem".equals(resourceType) && isEmbeddedCodeSystem(theResource)) {
			ourLog.warn("Skipping installation of resource {} because it is a common CodeSystem.", resourceId);
			return false;
		}

		if ("ValueSet".equals(resourceType) && isEmbeddedValueSet(theResource)) {
			ourLog.warn("Skipping installation of resource {} because it is a common ValueSet.", resourceId);
			return false;
		}

		if (!hasValidId(theResource)) {
			ourLog.warn("Skipping installation of resource {} because of an invalid FHIR ID.", resourceId);
			return false;
		}

		if (!isValidResourceStatusForPackageUpload(theResource)) {
			String status = extractStringValueOrEmpty(theResource, "status");
			ourLog.warn(
					"Skipping installation of resource {} because of an invalid resource status {}. Resource status validation setting is enabled.",
					resourceId,
					status);
			return false;
		}

		return true;
	}

	private boolean hasValidId(IBaseResource theResource) {
		IIdType resourceId = theResource.getIdElement();
		return !resourceId.hasIdPart() || resourceId.isIdPartValid();
	}

	private boolean isEmbeddedValueSet(IBaseResource theResource) {
		org.hl7.fhir.r4.model.ValueSet valueSet = myVersionCanonicalizer.valueSetToCanonical(theResource);
		if (!valueSet.hasUrl()) return false;
		return myCommonCodeSystemsTerminologyService.isValueSetSupported(null, valueSet.getUrl());
	}

	private boolean isEmbeddedCodeSystem(IBaseResource theResource) {

		org.hl7.fhir.r4.model.CodeSystem codeSystem = myVersionCanonicalizer.codeSystemToCanonical(theResource);
		if (!codeSystem.hasUrl()) return false;
		return myCommonCodeSystemsTerminologyService.isCodeSystemSupported(null, codeSystem.getUrl());
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
		// TODO: there is a bug here - see testValidForUpload_statusElementDefinedButNeverSet_returnsTrue
		// Resource has no status field, or an explicitly null one
		if (!statusTypes.get(0).hasValue() || statusTypes.get(0).getValue() == null) {
			return false;
		}
		final String statusValue = statusTypes.get(0).getValueAsString();
		// Resource has a status, and we need to check based on type
		return switch (theResource.fhirType()) {
			case "Subscription" -> statusValue.equals("requested");
			case "DocumentReference", "Communication" -> !statusValue.equals("?");
			default -> statusValue.equals("active");
		};
	}

	private boolean isStructureDefinitionWithoutSnapshot(IBaseResource r) {
		if (!("StructureDefinition".equals(r.fhirType()))) {
			return false;
		}
		String kind = extractStringValueOrEmpty(r, "kind");
		if (!kind.isEmpty() && !(kind.equals("logical"))) {
			return extractValue(r, "snapshot") == null;
		}
		return false;
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

	private SearchParameterMap createSearchParameterMapFor(IBaseResource theResource, PackageInstallationSpec theSpec) {
		String resourceType = theResource.fhirType();
		if ("NamingSystem".equals(resourceType)) {
			String uniqueId = extractUniqueIdFromNamingSystem(theResource);
			return SearchParameterMap.newSynchronous().add("value", new StringParam(uniqueId).setExact(true));
		} else if ("Subscription".equals(resourceType)) {
			String id = extractId(theResource);
			return SearchParameterMap.newSynchronous().add("_id", new TokenParam(id));
		} else if ("SearchParameter".equals(resourceType)) {
			return buildSearchParameterMapForSearchParameter(theResource);
		} else if (hasValue(theResource, "url")) {
			SearchParameterMap retVal = SearchParameterMap.newSynchronous();
			retVal.add("url", new UriParam(extractStringValueOrEmpty(theResource, "url")));
			// If multiple versions are allowed, include version in search to avoid overwriting
			if (allowMultipleVersionsForResource(theResource, theSpec)) {
				String version = extractStringValueOrEmpty(theResource, "version");
				if (!version.isEmpty()) {
					retVal.add("version", new TokenParam(version));
				}
			}
			// Always sort by _pid DESC for deterministic results.
			// This is particularly important in SINGLE_VERSION mode when multiple versions
			// already exist (e.g., user switched from MULTI_VERSION to SINGLE_VERSION) - we want
			// to consistently update the most recently created resource.
			// Note: _pid sorts by internal RES_ID (database sequence), not the
			// client-visible FHIR ID (_id), ensuring true creation-order sorting
			// regardless of whether resources have server-assigned or client-assigned IDs.
			retVal.setSort(new SortSpec(Constants.PARAM_PID, SortOrderEnum.DESC));
			return retVal;
		} else {
			TokenParam identifierToken = extractIdentifier(theResource);
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

		if (hasValue(theResource, "url")) {
			String url = extractStringValueOrEmpty(theResource, "url");
			return SearchParameterMap.newSynchronous().add("url", new UriParam(url));
		} else {
			TokenParam identifierToken = extractIdentifier(theResource);
			return SearchParameterMap.newSynchronous().add("identifier", identifierToken);
		}
	}

	private String extractId(IBaseResource theResource) {
		return myFhirContext
				.newTerser()
				.getSinglePrimitiveValue(theResource, "id")
				.orElseThrow(() -> new UnsupportedOperationException(Msg.code(2929) + theResource.fhirType()
						+ " resources in a package must have an id to be loaded by the package installer."));
	}

	private String extractUniqueIdFromNamingSystem(IBaseResource theResource) {
		IBase uniqueIdComponent = (IBase) extractValue(theResource, "uniqueId");
		if (uniqueIdComponent == null) {
			throw new ImplementationGuideInstallationException(
					Msg.code(1291) + "NamingSystem does not have uniqueId component.");
		}
		return extractStringValueOrEmpty(uniqueIdComponent, "value");
	}

	private TokenParam extractIdentifier(IBaseResource theResource) {
		Identifier identifier = myFhirContext
				.newTerser()
				.getSingleValue(theResource, "identifier", Identifier.class)
				.orElseThrow(
						() -> new ImplementationGuideInstallationException(
								Msg.code(1292)
										+ "Resources in a package must have a url or identifier to be loaded by the package installer."));
		return new TokenParam(identifier.getSystem(), identifier.getValue());
	}

	private Object extractValue(IBase theResource, String thePath) {
		return myFhirContext.newTerser().getSingleValueOrNull(theResource, thePath);
	}

	private String extractStringValueOrEmpty(IBase theResource, String theElementName) {
		return extractStringValueOrEmpty(theResource, theElementName, () -> "");
	}

	private String extractStringValueOrEmpty(
			IBase theResource, String theElementName, Supplier<String> theDefaultSupplier) {
		return myFhirContext
				.newTerser()
				.getSinglePrimitiveValue(theResource, theElementName)
				.orElseGet(theDefaultSupplier);
	}

	private boolean hasValue(IBaseResource theResource, String theElementName) {
		return TerserUtil.hasValues(myFhirContext, theResource, theElementName);
	}

	@VisibleForTesting
	void setFhirContextForUnitTest(FhirContext theCtx) {
		myFhirContext = theCtx;
	}
}
