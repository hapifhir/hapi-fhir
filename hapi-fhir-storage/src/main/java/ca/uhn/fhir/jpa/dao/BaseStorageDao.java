/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.LazyDaoMethodOutcome;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.cache.ResourcePersistentIdMap;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.QualifierDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.IMetaTagSorter;
import ca.uhn.fhir.util.MetaUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.InstantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseStorageDao {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseStorageDao.class);

	public static final String OO_SEVERITY_ERROR = "error";
	public static final String OO_SEVERITY_INFO = "information";
	public static final String OO_SEVERITY_WARN = "warning";
	private static final String PROCESSING_SUB_REQUEST = "BaseStorageDao.processingSubRequest";

	protected static final String MESSAGE_KEY_DELETE_RESOURCE_NOT_EXISTING = "deleteResourceNotExisting";
	protected static final String MESSAGE_KEY_DELETE_RESOURCE_ALREADY_DELETED = "deleteResourceAlreadyDeleted";

	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	protected FhirContext myFhirContext;

	@Autowired
	protected DaoRegistry myDaoRegistry;

	@Autowired
	protected IResourceVersionSvc myResourceVersionSvc;

	@Autowired
	protected JpaStorageSettings myStorageSettings;

	@Autowired
	protected IMetaTagSorter myMetaTagSorter;

	@VisibleForTesting
	public void setSearchParamRegistry(ISearchParamRegistry theSearchParamRegistry) {
		mySearchParamRegistry = theSearchParamRegistry;
	}

	@VisibleForTesting
	public void setMyMetaTagSorter(IMetaTagSorter theMetaTagSorter) {
		myMetaTagSorter = theMetaTagSorter;
	}

	/**
	 * May be overridden by subclasses to validate resources prior to storage
	 *
	 * @param theResource The resource that is about to be stored
	 * @deprecated Use {@link #preProcessResourceForStorage(IBaseResource, RequestDetails, TransactionDetails, boolean)} instead
	 */
	protected void preProcessResourceForStorage(IBaseResource theResource) {
		// nothing
	}

	/**
	 * May be overridden by subclasses to validate resources prior to storage
	 *
	 * @param theResource The resource that is about to be stored
	 * @since 5.3.0
	 */
	protected void preProcessResourceForStorage(
			IBaseResource theResource,
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails,
			boolean thePerformIndexing) {

		verifyResourceTypeIsAppropriateForDao(theResource);

		verifyResourceIdIsValid(theResource);

		verifyBundleTypeIsAppropriateForStorage(theResource);

		if (!getStorageSettings().getTreatBaseUrlsAsLocal().isEmpty()) {
			replaceAbsoluteReferencesWithRelative(theResource, myFhirContext.newTerser());
		}

		performAutoVersioning(theResource, thePerformIndexing);

		myMetaTagSorter.sort(theResource.getMeta());
	}

	/**
	 * Sanity check - Is this resource the right type for this DAO?
	 */
	private void verifyResourceTypeIsAppropriateForDao(IBaseResource theResource) {
		String type = getContext().getResourceType(theResource);
		if (getResourceName() != null && !getResourceName().equals(type)) {
			throw new InvalidRequestException(Msg.code(520)
					+ getContext()
							.getLocalizer()
							.getMessageSanitized(
									BaseStorageDao.class, "incorrectResourceType", type, getResourceName()));
		}
	}

	/**
	 * Verify that the resource ID is actually valid according to FHIR's rules
	 */
	private void verifyResourceIdIsValid(IBaseResource theResource) {
		if (theResource.getIdElement().hasIdPart()) {
			if (!theResource.getIdElement().isIdPartValid()) {
				throw new InvalidRequestException(Msg.code(521)
						+ getContext()
								.getLocalizer()
								.getMessageSanitized(
										BaseStorageDao.class,
										"failedToCreateWithInvalidId",
										theResource.getIdElement().getIdPart()));
			}
		}
	}

	/**
	 * Verify that we're not storing a Bundle with a disallowed bundle type
	 */
	private void verifyBundleTypeIsAppropriateForStorage(IBaseResource theResource) {
		if (theResource instanceof IBaseBundle) {
			Set<String> allowedBundleTypes = getStorageSettings().getBundleTypesAllowedForStorage();
			String bundleType = BundleUtil.getBundleType(getContext(), (IBaseBundle) theResource);
			bundleType = defaultString(bundleType);
			if (!allowedBundleTypes.contains(bundleType)) {
				String message = myFhirContext
						.getLocalizer()
						.getMessage(
								BaseStorageDao.class,
								"invalidBundleTypeForStorage",
								(isNotBlank(bundleType) ? bundleType : "(missing)"));
				throw new UnprocessableEntityException(Msg.code(522) + message);
			}
		}
	}

	/**
	 * Replace absolute references with relative ones if configured to do so
	 */
	private void replaceAbsoluteReferencesWithRelative(IBaseResource theResource, FhirTerser theTerser) {
		List<ResourceReferenceInfo> refs = theTerser.getAllResourceReferences(theResource);
		for (ResourceReferenceInfo nextRef : refs) {
			IIdType refId = nextRef.getResourceReference().getReferenceElement();
			if (refId != null && refId.hasBaseUrl()) {
				if (getStorageSettings().getTreatBaseUrlsAsLocal().contains(refId.getBaseUrl())) {
					IIdType newRefId = refId.toUnqualified();
					nextRef.getResourceReference().setReference(newRefId.getValue());
				}
			}
		}
	}

	/**
	 * Handle {@link JpaStorageSettings#getAutoVersionReferenceAtPaths() auto-populate-versions}
	 * <p>
	 * We only do this if thePerformIndexing is true because if it's false, that means
	 * we're in a FHIR transaction during the first phase of write operation processing,
	 * meaning that the versions of other resources may not have need updatd yet. For example
	 * we're about to store an Observation with a reference to a Patient, and that Patient
	 * is also being updated in the same transaction, during the first "no index" phase,
	 * the Patient will not yet have its version number incremented, so it would be wrong
	 * to use that value. During the second phase it is correct.
	 * <p>
	 * Also note that {@link BaseTransactionProcessor} also has code to do auto-versioning
	 * and it is the one that takes care of the placeholder IDs. Look for the other caller of
	 * {@link #extractReferencesToAutoVersion(FhirContext, StorageSettings, IBaseResource)}
	 * to find this.
	 */
	private void performAutoVersioning(IBaseResource theResource, boolean thePerformIndexing) {
		if (thePerformIndexing) {
			Set<IBaseReference> referencesToVersion =
					extractReferencesToAutoVersion(myFhirContext, myStorageSettings, theResource);
			for (IBaseReference nextReference : referencesToVersion) {
				IIdType referenceElement = nextReference.getReferenceElement();
				if (!referenceElement.hasBaseUrl()) {

					ResourcePersistentIdMap resourceVersionMap = myResourceVersionSvc.getLatestVersionIdsForResourceIds(
							RequestPartitionId.allPartitions(), Collections.singletonList(referenceElement));

					// 3 cases:
					// 1) there exists a resource in the db with some version (use this version)
					// 2) no resource exists, but we will create one (eventually). The version is 1
					// 3) no resource exists, and none will be made -> throw
					Long version;
					if (resourceVersionMap.containsKey(referenceElement)) {
						// the resource exists... latest id
						// will be the value in the IResourcePersistentId
						version = resourceVersionMap
								.getResourcePersistentId(referenceElement)
								.getVersion();
					} else if (myStorageSettings.isAutoCreatePlaceholderReferenceTargets()) {
						// if idToPID doesn't contain object
						// but autcreateplaceholders is on
						// then the version will be 1 (the first version)
						version = 1L;
					} else {
						// resource not found
						// and no autocreateplaceholders set...
						// we throw
						throw new ResourceNotFoundException(Msg.code(523) + referenceElement);
					}
					String newTargetReference =
							referenceElement.withVersion(version.toString()).getValue();
					nextReference.setReference(newTargetReference);
				}
			}
		}
	}

	protected DaoMethodOutcome toMethodOutcome(
			RequestDetails theRequest,
			@Nonnull final IBasePersistedResource theEntity,
			@Nonnull IBaseResource theResource,
			@Nullable String theMatchUrl,
			@Nonnull RestOperationTypeEnum theOperationType) {
		DaoMethodOutcome outcome = new DaoMethodOutcome();

		IResourcePersistentId persistentId = theEntity.getPersistentId();
		persistentId.setAssociatedResourceId(theResource.getIdElement());

		outcome.setPersistentId(persistentId);
		outcome.setMatchUrl(theMatchUrl);
		outcome.setOperationType(theOperationType);

		if (theEntity instanceof ResourceTable) {
			if (((ResourceTable) theEntity).isUnchangedInCurrentOperation()) {
				outcome.setNop(true);
			}
		}

		IIdType id = null;
		if (theResource.getIdElement().getValue() != null) {
			id = theResource.getIdElement();
		}
		if (id == null) {
			id = theEntity.getIdDt();
			if (getContext().getVersion().getVersion().isRi()) {
				id = getContext().getVersion().newIdType().setValue(id.getValue());
			}
		}

		outcome.setId(id);
		if (theEntity.getDeleted() == null) {
			outcome.setResource(theResource);
		}
		outcome.setEntity(theEntity);

		// Interceptor broadcast: STORAGE_PREACCESS_RESOURCES
		if (outcome.getResource() != null) {
			SimplePreResourceAccessDetails accessDetails = new SimplePreResourceAccessDetails(outcome.getResource());
			HookParams params = new HookParams()
					.add(IPreResourceAccessDetails.class, accessDetails)
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest);
			CompositeInterceptorBroadcaster.doCallHooks(
					getInterceptorBroadcaster(), theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);
			if (accessDetails.isDontReturnResourceAtIndex(0)) {
				outcome.setResource(null);
			}
		}

		// Interceptor broadcast: STORAGE_PRESHOW_RESOURCES
		// Note that this will only fire if someone actually goes to use the
		// resource in a response (it's their responsibility to call
		// outcome.fireResourceViewCallback())
		outcome.registerResourceViewCallback(() -> {
			if (outcome.getResource() != null) {
				SimplePreResourceShowDetails showDetails = new SimplePreResourceShowDetails(outcome.getResource());
				HookParams params = new HookParams()
						.add(IPreResourceShowDetails.class, showDetails)
						.add(RequestDetails.class, theRequest)
						.addIfMatchesType(ServletRequestDetails.class, theRequest);
				CompositeInterceptorBroadcaster.doCallHooks(
						getInterceptorBroadcaster(), theRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);
				outcome.setResource(showDetails.getResource(0));
			}
		});

		return outcome;
	}

	protected DaoMethodOutcome toMethodOutcomeLazy(
			RequestDetails theRequest,
			IResourcePersistentId theResourcePersistentId,
			@Nonnull final Supplier<LazyDaoMethodOutcome.EntityAndResource> theEntity,
			Supplier<IIdType> theIdSupplier) {
		LazyDaoMethodOutcome outcome = new LazyDaoMethodOutcome(theResourcePersistentId);

		outcome.setEntitySupplier(theEntity);
		outcome.setIdSupplier(theIdSupplier);
		outcome.setEntitySupplierUseCallback(() -> {
			// Interceptor broadcast: STORAGE_PREACCESS_RESOURCES
			if (outcome.getResource() != null) {
				SimplePreResourceAccessDetails accessDetails =
						new SimplePreResourceAccessDetails(outcome.getResource());
				HookParams params = new HookParams()
						.add(IPreResourceAccessDetails.class, accessDetails)
						.add(RequestDetails.class, theRequest)
						.addIfMatchesType(ServletRequestDetails.class, theRequest);
				CompositeInterceptorBroadcaster.doCallHooks(
						getInterceptorBroadcaster(), theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);
				if (accessDetails.isDontReturnResourceAtIndex(0)) {
					outcome.setResource(null);
				}
			}

			// Interceptor broadcast: STORAGE_PRESHOW_RESOURCES
			// Note that this will only fire if someone actually goes to use the
			// resource in a response (it's their responsibility to call
			// outcome.fireResourceViewCallback())
			outcome.registerResourceViewCallback(() -> {
				if (outcome.getResource() != null) {
					SimplePreResourceShowDetails showDetails = new SimplePreResourceShowDetails(outcome.getResource());
					HookParams params = new HookParams()
							.add(IPreResourceShowDetails.class, showDetails)
							.add(RequestDetails.class, theRequest)
							.addIfMatchesType(ServletRequestDetails.class, theRequest);
					CompositeInterceptorBroadcaster.doCallHooks(
							getInterceptorBroadcaster(), theRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);
					outcome.setResource(showDetails.getResource(0));
				}
			});
		});

		return outcome;
	}

	protected void doCallHooks(
			TransactionDetails theTransactionDetails,
			RequestDetails theRequestDetails,
			Pointcut thePointcut,
			HookParams theParams) {
		if (theTransactionDetails.isAcceptingDeferredInterceptorBroadcasts(thePointcut)) {
			theTransactionDetails.addDeferredInterceptorBroadcast(thePointcut, theParams);
		} else {
			CompositeInterceptorBroadcaster.doCallHooks(
					getInterceptorBroadcaster(), theRequestDetails, thePointcut, theParams);
		}
	}

	protected abstract IInterceptorBroadcaster getInterceptorBroadcaster();

	public IBaseOperationOutcome createErrorOperationOutcome(String theMessage, String theCode) {
		return createOperationOutcome(OO_SEVERITY_ERROR, theMessage, theCode);
	}

	public IBaseOperationOutcome createInfoOperationOutcome(String theMessage) {
		return createInfoOperationOutcome(theMessage, null);
	}

	public IBaseOperationOutcome createInfoOperationOutcome(
			String theMessage, @Nullable StorageResponseCodeEnum theStorageResponseCode) {
		return createOperationOutcome(OO_SEVERITY_INFO, theMessage, "informational", theStorageResponseCode);
	}

	private IBaseOperationOutcome createOperationOutcome(String theSeverity, String theMessage, String theCode) {
		return createOperationOutcome(theSeverity, theMessage, theCode, null);
	}

	protected IBaseOperationOutcome createOperationOutcome(
			String theSeverity,
			String theMessage,
			String theCode,
			@Nullable StorageResponseCodeEnum theStorageResponseCode) {
		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(getContext());
		String detailSystem = null;
		String detailCode = null;
		String detailDescription = null;
		if (theStorageResponseCode != null) {
			detailSystem = theStorageResponseCode.getSystem();
			detailCode = theStorageResponseCode.getCode();
			detailDescription = theStorageResponseCode.getDisplay();
		}
		OperationOutcomeUtil.addIssue(
				getContext(), oo, theSeverity, theMessage, null, theCode, detailSystem, detailCode, detailDescription);
		return oo;
	}

	/**
	 * Creates a base method outcome for a delete request for the provided ID.
	 * <p>
	 * Additional information may be set on the outcome.
	 *
	 * @param theResourceId - the id of the object being deleted. Eg: Patient/123
	 */
	protected DaoMethodOutcome createMethodOutcomeForResourceId(
			String theResourceId, String theMessageKey, StorageResponseCodeEnum theStorageResponseCode) {
		DaoMethodOutcome outcome = new DaoMethodOutcome();

		IIdType id = getContext().getVersion().newIdType();
		id.setValue(theResourceId);
		outcome.setId(id);

		String message = getContext().getLocalizer().getMessage(BaseStorageDao.class, theMessageKey, id);
		String severity = "information";
		String code = "informational";
		IBaseOperationOutcome oo = createOperationOutcome(severity, message, code, theStorageResponseCode);
		outcome.setOperationOutcome(oo);

		return outcome;
	}

	@Nonnull
	protected ResourceGoneException createResourceGoneException(IBasePersistedResource theResourceEntity) {
		StringBuilder b = new StringBuilder();
		b.append("Resource was deleted at ");
		b.append(new InstantType(theResourceEntity.getDeleted()).getValueAsString());
		ResourceGoneException retVal = new ResourceGoneException(b.toString());
		retVal.setResourceId(theResourceEntity.getIdDt());
		return retVal;
	}

	/**
	 * Provide the JpaStorageSettings
	 */
	protected abstract JpaStorageSettings getStorageSettings();

	/**
	 * Returns the resource type for this DAO, or null if this is a system-level DAO
	 */
	@Nullable
	protected abstract String getResourceName();

	/**
	 * Provides the FHIR context
	 */
	protected abstract FhirContext getContext();

	@Transactional(propagation = Propagation.SUPPORTS)
	public void translateRawParameters(Map<String, List<String>> theSource, SearchParameterMap theTarget) {
		if (theSource == null || theSource.isEmpty()) {
			return;
		}

		ResourceSearchParams searchParams = mySearchParamRegistry.getActiveSearchParams(getResourceName());

		Set<String> paramNames = theSource.keySet();
		for (String nextParamName : paramNames) {
			QualifierDetails qualifiedParamName = QualifierDetails.extractQualifiersFromParameterName(nextParamName);
			RuntimeSearchParam param = searchParams.get(qualifiedParamName.getParamName());
			if (param == null) {
				Collection<String> validNames =
						mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(getResourceName());
				String msg = getContext()
						.getLocalizer()
						.getMessageSanitized(
								BaseStorageDao.class,
								"invalidSearchParameter",
								qualifiedParamName.getParamName(),
								getResourceName(),
								validNames);
				throw new InvalidRequestException(Msg.code(524) + msg);
			}

			// Should not be null since the check above would have caught it
			RuntimeSearchParam paramDef =
					mySearchParamRegistry.getActiveSearchParam(getResourceName(), qualifiedParamName.getParamName());

			for (String nextValue : theSource.get(nextParamName)) {
				QualifiedParamList qualifiedParam = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(
						qualifiedParamName.getWholeQualifier(), nextValue);
				List<QualifiedParamList> paramList = Collections.singletonList(qualifiedParam);
				IQueryParameterAnd<?> parsedParam = JpaParamUtil.parseQueryParams(
						mySearchParamRegistry, getContext(), paramDef, nextParamName, paramList);
				theTarget.add(qualifiedParamName.getParamName(), parsedParam);
			}
		}
	}

	protected void populateOperationOutcomeForUpdate(
			@Nullable StopWatch theItemStopwatch,
			DaoMethodOutcome theMethodOutcome,
			String theMatchUrl,
			RestOperationTypeEnum theOperationType) {
		String msg;
		StorageResponseCodeEnum outcome;

		if (theOperationType == RestOperationTypeEnum.PATCH) {

			if (theMatchUrl != null) {
				if (theMethodOutcome.isNop()) {
					outcome = StorageResponseCodeEnum.SUCCESSFUL_CONDITIONAL_PATCH_NO_CHANGE;
					msg = getContext()
							.getLocalizer()
							.getMessageSanitized(
									BaseStorageDao.class,
									"successfulPatchConditionalNoChange",
									theMethodOutcome.getId(),
									UrlUtil.sanitizeUrlPart(theMatchUrl),
									theMethodOutcome.getId());
				} else {
					outcome = StorageResponseCodeEnum.SUCCESSFUL_CONDITIONAL_PATCH;
					msg = getContext()
							.getLocalizer()
							.getMessageSanitized(
									BaseStorageDao.class,
									"successfulPatchConditional",
									theMethodOutcome.getId(),
									UrlUtil.sanitizeUrlPart(theMatchUrl),
									theMethodOutcome.getId());
				}
			} else {
				if (theMethodOutcome.isNop()) {
					outcome = StorageResponseCodeEnum.SUCCESSFUL_PATCH_NO_CHANGE;
					msg = getContext()
							.getLocalizer()
							.getMessageSanitized(
									BaseStorageDao.class, "successfulPatchNoChange", theMethodOutcome.getId());
				} else {
					outcome = StorageResponseCodeEnum.SUCCESSFUL_PATCH;
					msg = getContext()
							.getLocalizer()
							.getMessageSanitized(BaseStorageDao.class, "successfulPatch", theMethodOutcome.getId());
				}
			}

		} else if (theOperationType == RestOperationTypeEnum.CREATE) {

			if (theMatchUrl == null) {
				outcome = StorageResponseCodeEnum.SUCCESSFUL_CREATE;
				msg = getContext()
						.getLocalizer()
						.getMessageSanitized(BaseStorageDao.class, "successfulCreate", theMethodOutcome.getId());
			} else if (theMethodOutcome.isNop()) {
				outcome = StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH;
				msg = getContext()
						.getLocalizer()
						.getMessageSanitized(
								BaseStorageDao.class,
								"successfulCreateConditionalWithMatch",
								theMethodOutcome.getId(),
								UrlUtil.sanitizeUrlPart(theMatchUrl));
			} else {
				outcome = StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH;
				msg = getContext()
						.getLocalizer()
						.getMessageSanitized(
								BaseStorageDao.class,
								"successfulCreateConditionalNoMatch",
								theMethodOutcome.getId(),
								UrlUtil.sanitizeUrlPart(theMatchUrl));
			}

		} else if (theMethodOutcome.isNop()) {

			if (theMatchUrl != null) {
				outcome = StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH_NO_CHANGE;
				msg = getContext()
						.getLocalizer()
						.getMessageSanitized(
								BaseStorageDao.class,
								"successfulUpdateConditionalNoChangeWithMatch",
								theMethodOutcome.getId(),
								theMatchUrl);
			} else {
				outcome = StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CHANGE;
				msg = getContext()
						.getLocalizer()
						.getMessageSanitized(
								BaseStorageDao.class, "successfulUpdateNoChange", theMethodOutcome.getId());
			}

		} else {

			if (theMatchUrl != null) {
				if (theMethodOutcome.getCreated() == Boolean.TRUE) {
					outcome = StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH;
					msg = getContext()
							.getLocalizer()
							.getMessageSanitized(
									BaseStorageDao.class,
									"successfulUpdateConditionalNoMatch",
									theMethodOutcome.getId());
				} else {
					outcome = StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH;
					msg = getContext()
							.getLocalizer()
							.getMessageSanitized(
									BaseStorageDao.class,
									"successfulUpdateConditionalWithMatch",
									theMethodOutcome.getId(),
									theMatchUrl);
				}
			} else if (theMethodOutcome.getCreated() == Boolean.TRUE) {
				outcome = StorageResponseCodeEnum.SUCCESSFUL_UPDATE_AS_CREATE;
				msg = getContext()
						.getLocalizer()
						.getMessageSanitized(
								BaseStorageDao.class, "successfulUpdateAsCreate", theMethodOutcome.getId());
			} else {
				outcome = StorageResponseCodeEnum.SUCCESSFUL_UPDATE;
				msg = getContext()
						.getLocalizer()
						.getMessageSanitized(BaseStorageDao.class, "successfulUpdate", theMethodOutcome.getId());
			}
		}

		if (theItemStopwatch != null) {
			String msgSuffix = getContext()
					.getLocalizer()
					.getMessageSanitized(BaseStorageDao.class, "successfulTimingSuffix", theItemStopwatch.getMillis());
			msg = msg + " " + msgSuffix;
		}

		theMethodOutcome.setOperationOutcome(createInfoOperationOutcome(msg, outcome));
		ourLog.debug(msg);
	}

	/**
	 * Extracts a list of references that should be auto-versioned.
	 *
	 * @return A set of references that should be versioned according to both storage settings
	 * 		   and auto-version reference extensions, or it may also be empty.
	 */
	@Nonnull
	public static Set<IBaseReference> extractReferencesToAutoVersion(
			FhirContext theFhirContext, StorageSettings theStorageSettings, IBaseResource theResource) {
		Set<IBaseReference> referencesToAutoVersionFromConfig =
				getReferencesToAutoVersionFromConfig(theFhirContext, theStorageSettings, theResource);

		Set<IBaseReference> referencesToAutoVersionFromExtensions =
				getReferencesToAutoVersionFromExtension(theFhirContext, theResource);

		return Stream.concat(referencesToAutoVersionFromConfig.stream(), referencesToAutoVersionFromExtensions.stream())
				.collect(Collectors.toMap(ref -> ref, ref -> ref, (oldRef, newRef) -> oldRef, IdentityHashMap::new))
				.keySet();
	}

	/**
	 * Extracts a list of references that should be auto-versioned according to
	 * <code>auto-version-references-at-path</code> extensions.
	 * @see HapiExtensions#EXTENSION_AUTO_VERSION_REFERENCES_AT_PATH
	 */
	@Nonnull
	private static Set<IBaseReference> getReferencesToAutoVersionFromExtension(
			FhirContext theFhirContext, IBaseResource theResource) {
		String resourceType = theFhirContext.getResourceType(theResource);
		Set<String> autoVersionReferencesAtPaths =
				MetaUtil.getAutoVersionReferencesAtPath(theResource.getMeta(), resourceType);

		if (!autoVersionReferencesAtPaths.isEmpty()) {
			return getReferencesWithoutVersionId(autoVersionReferencesAtPaths, theFhirContext, theResource);
		}
		return Collections.emptySet();
	}

	/**
	 * Extracts a list of references that should be auto-versioned according to storage configuration.
	 * @see StorageSettings#getAutoVersionReferenceAtPaths()
	 */
	@Nonnull
	private static Set<IBaseReference> getReferencesToAutoVersionFromConfig(
			FhirContext theFhirContext, StorageSettings theStorageSettings, IBaseResource theResource) {
		if (!theStorageSettings.getAutoVersionReferenceAtPaths().isEmpty()) {
			String resourceName = theFhirContext.getResourceType(theResource);
			Set<String> autoVersionReferencesPaths =
					theStorageSettings.getAutoVersionReferenceAtPathsByResourceType(resourceName);
			return getReferencesWithoutVersionId(autoVersionReferencesPaths, theFhirContext, theResource);
		}
		return Collections.emptySet();
	}

	private static Set<IBaseReference> getReferencesWithoutVersionId(
			Set<String> autoVersionReferencesPaths, FhirContext theFhirContext, IBaseResource theResource) {
		return autoVersionReferencesPaths.stream()
				.map(fullPath -> theFhirContext.newTerser().getValues(theResource, fullPath, IBaseReference.class))
				.flatMap(Collection::stream)
				.filter(reference -> !reference.getReferenceElement().hasVersionIdPart())
				.collect(Collectors.toMap(ref -> ref, ref -> ref, (oldRef, newRef) -> oldRef, IdentityHashMap::new))
				.keySet();
	}

	public static void clearRequestAsProcessingSubRequest(RequestDetails theRequestDetails) {
		if (theRequestDetails != null) {
			theRequestDetails.getUserData().remove(PROCESSING_SUB_REQUEST);
		}
	}

	public static void markRequestAsProcessingSubRequest(RequestDetails theRequestDetails) {
		if (theRequestDetails != null) {
			theRequestDetails.getUserData().put(PROCESSING_SUB_REQUEST, Boolean.TRUE);
		}
	}
}
