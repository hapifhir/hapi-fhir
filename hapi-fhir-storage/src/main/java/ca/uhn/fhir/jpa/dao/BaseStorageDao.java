package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.LazyDaoMethodOutcome;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.cache.ResourcePersistentIdMap;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.QualifierDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.InstantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseStorageDao {
	public static final String OO_SEVERITY_ERROR = "error";
	public static final String OO_SEVERITY_INFO = "information";
	public static final String OO_SEVERITY_WARN = "warning";
	private static final String PROCESSING_SUB_REQUEST = "BaseStorageDao.processingSubRequest";

	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	protected FhirContext myFhirContext;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	protected ModelConfig myModelConfig;
	@Autowired
	protected IResourceVersionSvc myResourceVersionSvc;
	@Autowired
	protected DaoConfig myDaoConfig;

	/**
	 * @see ModelConfig#getAutoVersionReferenceAtPaths()
	 */
	@Nonnull
	public static Set<IBaseReference> extractReferencesToAutoVersion(FhirContext theFhirContext, ModelConfig theModelConfig, IBaseResource theResource) {
		Map<IBaseReference, Object> references = Collections.emptyMap();
		if (!theModelConfig.getAutoVersionReferenceAtPaths().isEmpty()) {
			String resourceName = theFhirContext.getResourceType(theResource);
			for (String nextPath : theModelConfig.getAutoVersionReferenceAtPathsByResourceType(resourceName)) {
				List<IBaseReference> nextReferences = theFhirContext.newTerser().getValues(theResource, nextPath, IBaseReference.class);
				for (IBaseReference next : nextReferences) {
					if (next.getReferenceElement().hasVersionIdPart()) {
						continue;
					}
					if (references.isEmpty()) {
						references = new IdentityHashMap<>();
					}
					references.put(next, null);
				}
			}
		}
		return references.keySet();
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

	@VisibleForTesting
	public void setSearchParamRegistry(ISearchParamRegistry theSearchParamRegistry) {
		mySearchParamRegistry = theSearchParamRegistry;
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
	protected void preProcessResourceForStorage(IBaseResource theResource, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails, boolean thePerformIndexing) {

		verifyResourceTypeIsAppropriateForDao(theResource);

		verifyResourceIdIsValid(theResource);

		verifyBundleTypeIsAppropriateForStorage(theResource);

		if(!getConfig().getTreatBaseUrlsAsLocal().isEmpty()) {
			replaceAbsoluteReferencesWithRelative(theResource, myFhirContext.newTerser());
		}

		performAutoVersioning(theResource, thePerformIndexing);

	}

	/**
	 * Sanity check - Is this resource the right type for this DAO?
	 */
	private void verifyResourceTypeIsAppropriateForDao(IBaseResource theResource) {
		String type = getContext().getResourceType(theResource);
		if (getResourceName() != null && !getResourceName().equals(type)) {
			throw new InvalidRequestException(Msg.code(520) + getContext().getLocalizer().getMessageSanitized(BaseStorageDao.class, "incorrectResourceType", type, getResourceName()));
		}
	}

	/**
	 * Verify that the resource ID is actually valid according to FHIR's rules
	 */
	private void verifyResourceIdIsValid(IBaseResource theResource) {
		if (theResource.getIdElement().hasIdPart()) {
			if (!theResource.getIdElement().isIdPartValid()) {
				throw new InvalidRequestException(Msg.code(521) + getContext().getLocalizer().getMessageSanitized(BaseStorageDao.class, "failedToCreateWithInvalidId", theResource.getIdElement().getIdPart()));
			}
		}
	}

	/**
	 * Verify that we're not storing a Bundle with a disallowed bundle type
	 */
	private void verifyBundleTypeIsAppropriateForStorage(IBaseResource theResource) {
		if (theResource instanceof IBaseBundle) {
			Set<String> allowedBundleTypes = getConfig().getBundleTypesAllowedForStorage();
			String bundleType = BundleUtil.getBundleType(getContext(), (IBaseBundle) theResource);
			bundleType = defaultString(bundleType);
			if (!allowedBundleTypes.contains(bundleType)) {
				String message = myFhirContext.getLocalizer().getMessage(BaseStorageDao.class, "invalidBundleTypeForStorage", (isNotBlank(bundleType) ? bundleType : "(missing)"));
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
					if (getConfig().getTreatBaseUrlsAsLocal().contains(refId.getBaseUrl())) {
						IIdType newRefId = refId.toUnqualified();
						nextRef.getResourceReference().setReference(newRefId.getValue());
					}
				}
			}
	}

	/**
	 * Handle {@link ModelConfig#getAutoVersionReferenceAtPaths() auto-populate-versions}
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
	 * {@link #extractReferencesToAutoVersion(FhirContext, ModelConfig, IBaseResource)}
	 * to find this.
	 */
	private void performAutoVersioning(IBaseResource theResource, boolean thePerformIndexing) {
		if (thePerformIndexing) {
			Set<IBaseReference> referencesToVersion = extractReferencesToAutoVersion(myFhirContext, myModelConfig, theResource);
			for (IBaseReference nextReference : referencesToVersion) {
				IIdType referenceElement = nextReference.getReferenceElement();
				if (!referenceElement.hasBaseUrl()) {

					ResourcePersistentIdMap resourceVersionMap = myResourceVersionSvc.getLatestVersionIdsForResourceIds(RequestPartitionId.allPartitions(),
						Collections.singletonList(referenceElement)
					);

					// 3 cases:
					// 1) there exists a resource in the db with some version (use this version)
					// 2) no resource exists, but we will create one (eventually). The version is 1
					// 3) no resource exists, and none will be made -> throw
					Long version;
					if (resourceVersionMap.containsKey(referenceElement)) {
						// the resource exists... latest id
						// will be the value in the ResourcePersistentId
						version = resourceVersionMap.getResourcePersistentId(referenceElement).getVersion();
					} else if (myDaoConfig.isAutoCreatePlaceholderReferenceTargets()) {
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
					String newTargetReference = referenceElement.withVersion(version.toString()).getValue();
					nextReference.setReference(newTargetReference);
				}
			}
		}
	}

	protected DaoMethodOutcome toMethodOutcome(RequestDetails theRequest, @Nonnull final IBasePersistedResource theEntity, @Nonnull IBaseResource theResource) {
		DaoMethodOutcome outcome = new DaoMethodOutcome().setPersistentId(theEntity.getPersistentId());

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
			CompositeInterceptorBroadcaster.doCallHooks(getInterceptorBroadcaster(), theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);
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
				CompositeInterceptorBroadcaster.doCallHooks(getInterceptorBroadcaster(), theRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);
				outcome.setResource(showDetails.getResource(0));
			}
		});

		return outcome;
	}

	protected DaoMethodOutcome toMethodOutcomeLazy(RequestDetails theRequest, ResourcePersistentId theResourcePersistentId, @Nonnull final Supplier<LazyDaoMethodOutcome.EntityAndResource> theEntity, Supplier<IIdType> theIdSupplier) {
		LazyDaoMethodOutcome outcome = new LazyDaoMethodOutcome(theResourcePersistentId);

		outcome.setEntitySupplier(theEntity);
		outcome.setIdSupplier(theIdSupplier);
		outcome.setEntitySupplierUseCallback(() -> {
			// Interceptor broadcast: STORAGE_PREACCESS_RESOURCES
			if (outcome.getResource() != null) {
				SimplePreResourceAccessDetails accessDetails = new SimplePreResourceAccessDetails(outcome.getResource());
				HookParams params = new HookParams()
					.add(IPreResourceAccessDetails.class, accessDetails)
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest);
				CompositeInterceptorBroadcaster.doCallHooks(getInterceptorBroadcaster(), theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);
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
					CompositeInterceptorBroadcaster.doCallHooks(getInterceptorBroadcaster(), theRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);
					outcome.setResource(showDetails.getResource(0));
				}
			});
		});

		return outcome;
	}

	protected void doCallHooks(TransactionDetails theTransactionDetails, RequestDetails theRequestDetails, Pointcut thePointcut, HookParams theParams) {
		if (theTransactionDetails.isAcceptingDeferredInterceptorBroadcasts(thePointcut)) {
			theTransactionDetails.addDeferredInterceptorBroadcast(thePointcut, theParams);
		} else {
			CompositeInterceptorBroadcaster.doCallHooks(getInterceptorBroadcaster(), theRequestDetails, thePointcut, theParams);
		}
	}

	protected abstract IInterceptorBroadcaster getInterceptorBroadcaster();

	public IBaseOperationOutcome createErrorOperationOutcome(String theMessage, String theCode) {
		return createOperationOutcome(OO_SEVERITY_ERROR, theMessage, theCode);
	}

	public IBaseOperationOutcome createInfoOperationOutcome(String theMessage) {
		return createOperationOutcome(OO_SEVERITY_INFO, theMessage, "informational");
	}

	private IBaseOperationOutcome createOperationOutcome(String theSeverity, String theMessage, String theCode) {
		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(getContext());
		OperationOutcomeUtil.addIssue(getContext(), oo, theSeverity, theMessage, null, theCode);
		return oo;
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
	 * Provide the DaoConfig
	 */
	protected abstract DaoConfig getConfig();

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
				Collection<String> validNames = mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(getResourceName());
				String msg = getContext().getLocalizer().getMessageSanitized(BaseStorageDao.class, "invalidSearchParameter", qualifiedParamName.getParamName(), getResourceName(), validNames);
				throw new InvalidRequestException(Msg.code(524) + msg);
			}

			// Should not be null since the check above would have caught it
			RuntimeSearchParam paramDef = mySearchParamRegistry.getActiveSearchParam(getResourceName(), qualifiedParamName.getParamName());

			for (String nextValue : theSource.get(nextParamName)) {
				QualifiedParamList qualifiedParam = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(qualifiedParamName.getWholeQualifier(), nextValue);
				List<QualifiedParamList> paramList = Collections.singletonList(qualifiedParam);
				IQueryParameterAnd<?> parsedParam = JpaParamUtil.parseQueryParams(mySearchParamRegistry, getContext(), paramDef, nextParamName, paramList);
				theTarget.add(qualifiedParamName.getParamName(), parsedParam);
			}

		}
	}

	public void notifyInterceptors(RestOperationTypeEnum theOperationType, IServerInterceptor.ActionRequestDetails theRequestDetails) {
		if (theRequestDetails.getId() != null && theRequestDetails.getId().hasResourceType() && isNotBlank(theRequestDetails.getResourceType())) {
			if (theRequestDetails.getId().getResourceType().equals(theRequestDetails.getResourceType()) == false) {
				throw new InternalErrorException(Msg.code(525) + "Inconsistent server state - Resource types don't match: " + theRequestDetails.getId().getResourceType() + " / " + theRequestDetails.getResourceType());
			}
		}

		if (theRequestDetails.getUserData().get(PROCESSING_SUB_REQUEST) == Boolean.TRUE) {
			theRequestDetails.notifyIncomingRequestPreHandled(theOperationType);
		}
	}
}
