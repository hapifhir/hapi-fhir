/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.extractor.IResourceLinkResolver;
import ca.uhn.fhir.jpa.searchparam.extractor.PathAndRef;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.storage.interceptor.AutoCreatePlaceholderReferenceTargetRequest;
import ca.uhn.fhir.storage.interceptor.AutoCreatePlaceholderReferenceTargetResponse;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.TerserUtil;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DaoResourceLinkResolver<T extends IResourcePersistentId<?>> implements IResourceLinkResolver {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DaoResourceLinkResolver.class);

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private FhirContext myContext;

	@Autowired
	private IIdHelperService<T> myIdHelperService;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IHapiTransactionService myTransactionService;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Override
	public IResourceLookup findTargetResource(
			@Nonnull RequestPartitionId theRequestPartitionId,
			String theSourceResourceName,
			PathAndRef thePathAndRef,
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails) {

		IBaseReference targetReference = thePathAndRef.getRef();
		String sourcePath = thePathAndRef.getPath();

		IIdType targetResourceId = targetReference.getReferenceElement();
		if (targetResourceId.isEmpty() && targetReference.getResource() != null) {
			targetResourceId = targetReference.getResource().getIdElement();
		}

		String resourceType = targetResourceId.getResourceType();
		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(resourceType);
		Class<? extends IBaseResource> type = resourceDef.getImplementingClass();

		T persistentId = null;
		if (theTransactionDetails != null) {
			T resolvedResourceId = (T) theTransactionDetails.getResolvedResourceId(targetResourceId);
			if (resolvedResourceId != null
					&& resolvedResourceId.getId() != null
					&& resolvedResourceId.getAssociatedResourceId() != null) {
				persistentId = resolvedResourceId;
			}
		}

		IResourceLookup<?> resolvedResource;
		String idPart = targetResourceId.getIdPart();
		try {
			if (persistentId == null) {

				// If we previously looked up the ID, and it was not found, don't bother
				// looking it up again
				if (theTransactionDetails != null
						&& theTransactionDetails.hasNullResolvedResourceId(targetResourceId)) {
					throw new ResourceNotFoundException(Msg.code(2602));
				}

				resolvedResource = myIdHelperService.resolveResourceIdentity(
						theRequestPartitionId,
						resourceType,
						idPart,
						ResolveIdentityMode.excludeDeleted().noCacheUnlessDeletesDisabled());
				ourLog.trace("Translated {}/{} to resource PID {}", type, idPart, resolvedResource);
			} else {
				resolvedResource = new ResourceLookupPersistentIdWrapper<>(persistentId);
			}
		} catch (ResourceNotFoundException e) {

			Optional<IBasePersistedResource> createdTableOpt = createPlaceholderTargetIfConfiguredToDoSo(
					type, targetReference, idPart, theRequest, theTransactionDetails);
			if (!createdTableOpt.isPresent()) {

				if (!myStorageSettings.isEnforceReferentialIntegrityOnWrite()) {
					return null;
				}

				RuntimeResourceDefinition missingResourceDef = myContext.getResourceDefinition(type);
				String resName = missingResourceDef.getName();

				// Check if this was a deleted resource
				try {
					resolvedResource = myIdHelperService.resolveResourceIdentity(
							theRequestPartitionId,
							resourceType,
							idPart,
							ResolveIdentityMode.includeDeleted().noCacheUnlessDeletesDisabled());
					handleDeletedTarget(resourceType, idPart, sourcePath);
				} catch (ResourceNotFoundException e2) {
					resolvedResource = null;
				}

				if (resolvedResource == null) {
					throw new InvalidRequestException(Msg.code(1094) + "Resource " + resName + "/" + idPart
							+ " not found, specified in path: " + sourcePath);
				}
			}

			resolvedResource = createdTableOpt.get();
		}

		ourLog.trace(
				"Resolved resource of type {} as PID: {}",
				resolvedResource.getResourceType(),
				resolvedResource.getPersistentId());
		if (!validateResolvedResourceOrThrow(resourceType, resolvedResource, targetResourceId, idPart, sourcePath)) {
			return null;
		}

		if (persistentId == null) {
			Object id = resolvedResource.getPersistentId().getId();
			Integer partitionId = null;
			if (resolvedResource.getPartitionId() != null) {
				partitionId = resolvedResource.getPartitionId().getPartitionId();
			}
			persistentId = myIdHelperService.newPid(id, partitionId);
			persistentId.setAssociatedResourceId(targetResourceId);
			if (theTransactionDetails != null) {
				theTransactionDetails.addResolvedResourceId(targetResourceId, persistentId);
			}
		}

		return resolvedResource;
	}

	/**
	 * Validates the resolved resource.
	 * If 'Enforce Referential Integrity on Write' is enabled:
	 * Throws <code>UnprocessableEntityException</code> when resource types do not match
	 * Throws <code>InvalidRequestException</code> when the resolved resource was deleted
	 * <p>
	 * Otherwise, return false when resource types do not match or resource was deleted
	 * and return true if the resolved resource is valid.
	 */
	private boolean validateResolvedResourceOrThrow(
			String resourceType,
			IResourceLookup resolvedResource,
			IIdType targetResourceId,
			String idPart,
			String sourcePath) {
		if (!resourceType.equals(resolvedResource.getResourceType())) {
			ourLog.error(
					"Resource with PID {} was of type {} and wanted {}",
					resolvedResource.getPersistentId(),
					resourceType,
					resolvedResource.getResourceType());
			if (!myStorageSettings.isEnforceReferentialIntegrityOnWrite()) {
				return false;
			}
			throw new UnprocessableEntityException(Msg.code(1095)
					+ "Resource contains reference to unknown resource ID " + targetResourceId.getValue());
		}

		if (resolvedResource.getDeleted() != null) {
			return handleDeletedTarget(resolvedResource.getResourceType(), idPart, sourcePath);
		}
		return true;
	}

	private boolean handleDeletedTarget(String resType, String idPart, String sourcePath) {
		if (!myStorageSettings.isEnforceReferentialIntegrityOnWrite()) {
			return false;
		}
		String resName = resType;
		throw new InvalidRequestException(Msg.code(1096) + "Resource " + resName + "/" + idPart
				+ " is deleted, specified in path: " + sourcePath);
	}

	@Nullable
	@Override
	public IBaseResource loadTargetResource(
			@Nonnull RequestPartitionId theRequestPartitionId,
			String theSourceResourceName,
			PathAndRef thePathAndRef,
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails) {
		return myTransactionService
				.withRequest(theRequest)
				.withTransactionDetails(theTransactionDetails)
				.withRequestPartitionId(theRequestPartitionId)
				.execute(() -> {
					IIdType targetId = thePathAndRef.getRef().getReferenceElement();
					IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(targetId.getResourceType());
					return dao.read(targetId, theRequest);
				});
	}

	/**
	 * @param theIdToAssignToPlaceholder If specified, the placeholder resource created will be given a specific ID
	 */
	public <T extends IBaseResource> Optional<IBasePersistedResource> createPlaceholderTargetIfConfiguredToDoSo(
			Class<T> theType,
			IBaseReference theReference,
			@Nullable String theIdToAssignToPlaceholder,
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails) {
		IBasePersistedResource placeholderEntity = null;

		if (myStorageSettings.isAutoCreatePlaceholderReferenceTargets()) {
			RuntimeResourceDefinition missingResourceDef = myContext.getResourceDefinition(theType);
			String resName = missingResourceDef.getName();

			@SuppressWarnings("unchecked")
			T newResource = (T) missingResourceDef.newInstance();

			tryToAddPlaceholderExtensionToResource(newResource);

			IFhirResourceDao<T> placeholderResourceDao = myDaoRegistry.getResourceDao(theType);
			ourLog.debug(
					"Automatically creating empty placeholder resource: {}",
					newResource.getIdElement().getValue());

			boolean urlIdentifiersCopied = false;
			if (myStorageSettings.isPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets()) {
				urlIdentifiersCopied =
						tryToCopyIdentifierFromReferenceToTargetResource(theReference, missingResourceDef, newResource);
			}

			if (isBlank(theIdToAssignToPlaceholder) && !urlIdentifiersCopied) {
				String msg = myContext
						.getLocalizer()
						.getMessage(
								BaseStorageDao.class,
								"invalidMatchUrlCantUseForAutoCreatePlaceholder",
								theReference.getReferenceElement().getValue());
				throw new ResourceNotFoundException(Msg.code(2746) + msg);
			}

			if (theIdToAssignToPlaceholder != null) {
				if (theTransactionDetails != null) {
					String existingId = newResource.getIdElement().getValue();
					theTransactionDetails.addRollbackUndoAction(() -> newResource.setId(existingId));
				}
				newResource.setId(resName + "/" + theIdToAssignToPlaceholder);
			}

			// Interceptor: STORAGE_PRE_AUTO_CREATE_PLACEHOLDER_REFERENCE
			IInterceptorBroadcaster interceptorBroadcaster =
					CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequest);
			if (interceptorBroadcaster.hasHooks(Pointcut.STORAGE_PRE_AUTO_CREATE_PLACEHOLDER_REFERENCE)) {
				AutoCreatePlaceholderReferenceTargetRequest request =
						new AutoCreatePlaceholderReferenceTargetRequest(newResource);
				HookParams params = new HookParams()
						.add(AutoCreatePlaceholderReferenceTargetRequest.class, request)
						.add(RequestDetails.class, theRequest)
						.addIfMatchesType(ServletRequestDetails.class, theRequest);
				AutoCreatePlaceholderReferenceTargetResponse response =
						(AutoCreatePlaceholderReferenceTargetResponse) interceptorBroadcaster.callHooksAndReturnObject(
								Pointcut.STORAGE_PRE_AUTO_CREATE_PLACEHOLDER_REFERENCE, params);
				if (response != null) {
					if (response.isDoNotCreateTarget()) {
						return Optional.empty();
					}
				}

				// Sanity check: Make sure that interceptors haven't changed the ID
				if (theIdToAssignToPlaceholder != null) {
					Validate.isTrue(
							theIdToAssignToPlaceholder.equals(
									newResource.getIdElement().getIdPart()),
							"Interceptors must not modify the ID of auto-created placeholder reference targets");
				} else {
					Validate.isTrue(
							isBlank(newResource.getIdElement().getIdPart()),
							"Interceptors must not modify the ID of auto-created placeholder reference targets");
				}
			}

			if (theIdToAssignToPlaceholder != null) {
				placeholderEntity = placeholderResourceDao
						.update(newResource, null, true, false, theRequest, theTransactionDetails)
						.getEntity();
			} else {
				placeholderEntity =
						placeholderResourceDao.create(newResource, theRequest).getEntity();
			}

			verifyPlaceholderCanBeCreated(theType, theIdToAssignToPlaceholder, theReference, placeholderEntity);

			IResourcePersistentId persistentId = placeholderEntity.getPersistentId();
			persistentId = myIdHelperService.newPid(persistentId.getId());
			persistentId.setAssociatedResourceId(placeholderEntity.getIdDt());
			theTransactionDetails.addResolvedResourceId(persistentId.getAssociatedResourceId(), persistentId);
			theTransactionDetails.addAutoCreatedPlaceholderResource(newResource.getIdElement());
		}

		return Optional.ofNullable(placeholderEntity);
	}

	/**
	 * Subclasses may override
	 */
	protected void verifyPlaceholderCanBeCreated(
			Class<? extends IBaseResource> theType,
			String theIdToAssignToPlaceholder,
			IBaseReference theReference,
			IBasePersistedResource theStoredEntity) {}

	private <T extends IBaseResource> void tryToAddPlaceholderExtensionToResource(T newResource) {
		if (newResource instanceof IBaseHasExtensions) {
			IBaseExtension<?, ?> extension = ((IBaseHasExtensions) newResource).addExtension();
			extension.setUrl(HapiExtensions.EXT_RESOURCE_PLACEHOLDER);
			extension.setValue(myContext.newPrimitiveBoolean(true));
		}
	}

	/**
	 * This method returns false if the reference contained a conditional reference, but
	 * the reference couldn't be resolved into one or more identifiers (and only one or
	 * more identifiers) according to the rules in {@link #extractIdentifierFromUrl(String)}.
	 */
	private <T extends IBaseResource> boolean tryToCopyIdentifierFromReferenceToTargetResource(
			IBaseReference theSourceReference, RuntimeResourceDefinition theTargetResourceDef, T theTargetResource) {
		String urlValue = theSourceReference.getReferenceElement().getValue();
		List<CanonicalIdentifier> referenceMatchUrlIdentifiers;
		if (urlValue.contains("?")) {
			referenceMatchUrlIdentifiers = extractIdentifierFromUrl(urlValue);
			for (CanonicalIdentifier identifier : referenceMatchUrlIdentifiers) {
				addMatchUrlIdentifierToTargetResource(theTargetResourceDef, theTargetResource, identifier);
			}

			if (referenceMatchUrlIdentifiers.isEmpty()) {
				return false;
			}

		} else {
			referenceMatchUrlIdentifiers = List.of();
		}

		CanonicalIdentifier referenceIdentifier = extractIdentifierReference(theSourceReference);
		if (referenceIdentifier != null && !referenceMatchUrlIdentifiers.contains(referenceIdentifier)) {
			addSubjectIdentifierToTargetResource(theSourceReference, theTargetResourceDef, theTargetResource);
		}

		return true;
	}

	private <T extends IBaseResource> void addSubjectIdentifierToTargetResource(
			IBaseReference theSourceReference, RuntimeResourceDefinition theTargetResourceDef, T theTargetResource) {
		BaseRuntimeChildDefinition targetIdentifier = theTargetResourceDef.getChildByName("identifier");
		if (targetIdentifier != null) {
			BaseRuntimeElementDefinition<?> identifierElement = targetIdentifier.getChildByName("identifier");
			String identifierElementName = identifierElement.getName();
			boolean targetHasIdentifierElement = identifierElementName.equals("Identifier");
			if (targetHasIdentifierElement) {

				BaseRuntimeElementCompositeDefinition<?> referenceElement = (BaseRuntimeElementCompositeDefinition<?>)
						myContext.getElementDefinition(theSourceReference.getClass());
				BaseRuntimeChildDefinition referenceIdentifierChild = referenceElement.getChildByName("identifier");
				Optional<IBase> identifierOpt =
						referenceIdentifierChild.getAccessor().getFirstValueOrNull(theSourceReference);
				identifierOpt.ifPresent(
						theIBase -> targetIdentifier.getMutator().addValue(theTargetResource, theIBase));
			}
		}
	}

	private <T extends IBaseResource> void addMatchUrlIdentifierToTargetResource(
			RuntimeResourceDefinition theTargetResourceDef,
			T theTargetResource,
			CanonicalIdentifier referenceMatchUrlIdentifier) {
		BaseRuntimeChildDefinition identifierDefinition = theTargetResourceDef.getChildByName("identifier");
		IBase identifierIBase = identifierDefinition
				.getChildByName("identifier")
				.newInstance(identifierDefinition.getInstanceConstructorArguments());
		IBase systemIBase = TerserUtil.newElement(
				myContext, "uri", referenceMatchUrlIdentifier.getSystemElement().getValueAsString());
		IBase valueIBase = TerserUtil.newElement(
				myContext,
				"string",
				referenceMatchUrlIdentifier.getValueElement().getValueAsString());
		// Set system in the IBase Identifier

		BaseRuntimeElementDefinition<?> elementDefinition = myContext.getElementDefinition(identifierIBase.getClass());

		BaseRuntimeChildDefinition systemDefinition = elementDefinition.getChildByName("system");
		systemDefinition.getMutator().setValue(identifierIBase, systemIBase);

		BaseRuntimeChildDefinition valueDefinition = elementDefinition.getChildByName("value");
		valueDefinition.getMutator().setValue(identifierIBase, valueIBase);

		// Set Value in the IBase identifier
		identifierDefinition.getMutator().addValue(theTargetResource, identifierIBase);
	}

	private CanonicalIdentifier extractIdentifierReference(IBaseReference theSourceReference) {
		Optional<IBase> identifier =
				myContext.newFhirPath().evaluateFirst(theSourceReference, "identifier", IBase.class);
		if (!identifier.isPresent()) {
			return null;
		} else {
			CanonicalIdentifier canonicalIdentifier = new CanonicalIdentifier();
			Optional<IPrimitiveType> system =
					myContext.newFhirPath().evaluateFirst(identifier.get(), "system", IPrimitiveType.class);
			Optional<IPrimitiveType> value =
					myContext.newFhirPath().evaluateFirst(identifier.get(), "value", IPrimitiveType.class);

			system.ifPresent(theIPrimitiveType -> canonicalIdentifier.setSystem(theIPrimitiveType.getValueAsString()));
			value.ifPresent(theIPrimitiveType -> canonicalIdentifier.setValue(theIPrimitiveType.getValueAsString()));
			return canonicalIdentifier;
		}
	}

	/**
	 * Extracts the identifier(s) from a query URL. This method is quite strict, as it is intended only for
	 * use when creating {@link StorageSettings#isAutoCreatePlaceholderReferenceTargets() auto-created placeholder reference targets}.
	 * As such, it will:
	 * <ul>
	 *     <li>Ignore any <code>_tag:not</code> parameters</li>
	 *     <li>Add an entry to the returned list for each <code>identifier</code> parameter containing a single valid value</li>
	 *     <li>Return an empty list if any <code>identifier</code> parameters have modifiers or multiple OR values in a single parameter instance</li>
	 *     <li>Return an empty list if any other parameters are found</li>
	 * </ul>
	 *
	 * @param theValue Part of the URL to extract identifiers from
	 */
	protected List<CanonicalIdentifier> extractIdentifierFromUrl(String theValue) {
		Map<String, String[]> parsedQuery = UrlUtil.parseQueryString(theValue);

		ArrayList<CanonicalIdentifier> retVal = new ArrayList<>(2);

		for (String paramName : parsedQuery.keySet()) {
			switch (paramName) {
				case "identifier" -> {
					String[] values = parsedQuery.get(paramName);
					for (String value : values) {
						QualifiedParamList paramList =
								QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null, value);
						if (paramList.size() > 1) {
							return List.of();
						} else if (!paramList.isEmpty()) {
							TokenParam tokenParam = new TokenParam();
							tokenParam.setValueAsQueryToken(myContext, "identifier", null, paramList.get(0));
							if (isNotBlank(tokenParam.getSystem()) || isNotBlank(tokenParam.getValue())) {
								CanonicalIdentifier identifier = new CanonicalIdentifier();
								identifier.setSystem(tokenParam.getSystem());
								identifier.setValue(tokenParam.getValue());
								retVal.add(identifier);
							}
						}
					}
				}
				case Constants.PARAM_TAG + Constants.PARAMQUALIFIER_TOKEN_NOT -> {
					// We ignore _tag:not expressions since any auto-created placeholder
					// won't have tags so they will match this parameter
				}
				default -> {
					return List.of();
				}
			}
		}

		return retVal;
	}

	@Override
	public void validateTypeOrThrowException(Class<? extends IBaseResource> theType) {
		myDaoRegistry.getDaoOrThrow(theType);
	}

	private static class ResourceLookupPersistentIdWrapper<P extends IResourcePersistentId> implements IResourceLookup {
		private final P myPersistentId;

		public ResourceLookupPersistentIdWrapper(P thePersistentId) {
			myPersistentId = thePersistentId;
		}

		@Override
		public String getResourceType() {
			return myPersistentId.getAssociatedResourceId().getResourceType();
		}

		@Override
		public String getFhirId() {
			return myPersistentId.getAssociatedResourceId().getIdPart();
		}

		@Override
		public Date getDeleted() {
			return null;
		}

		@Override
		public P getPersistentId() {
			return myPersistentId;
		}

		@Override
		public PartitionablePartitionId getPartitionId() {
			return new PartitionablePartitionId(myPersistentId.getPartitionId(), null);
		}
	}
}
