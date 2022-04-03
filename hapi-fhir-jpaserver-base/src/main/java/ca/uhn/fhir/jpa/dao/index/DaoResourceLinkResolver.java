package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.IResourceLinkResolver;
import ca.uhn.fhir.mdm.util.CanonicalIdentifier;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.TerserUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DaoResourceLinkResolver implements IResourceLinkResolver {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DaoResourceLinkResolver.class);
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private IIdHelperService myIdHelperService;
	@Autowired
	private DaoRegistry myDaoRegistry;

	@Override
	public IResourceLookup findTargetResource(@Nonnull RequestPartitionId theRequestPartitionId, RuntimeSearchParam theSearchParam, String theSourcePath, IIdType theSourceResourceId, String theResourceType, Class<? extends IBaseResource> theType, IBaseReference theReference, RequestDetails theRequest, TransactionDetails theTransactionDetails) {
		ResourcePersistentId persistentId = null;
		if (theTransactionDetails != null) {
			ResourcePersistentId resolvedResourceId = theTransactionDetails.getResolvedResourceId(theSourceResourceId);
			if (resolvedResourceId != null && resolvedResourceId.getIdAsLong() != null && resolvedResourceId.getAssociatedResourceId() != null) {
				persistentId = resolvedResourceId;
			}
		}

		IResourceLookup resolvedResource;
		String idPart = theSourceResourceId.getIdPart();
		try {
			if (persistentId == null) {
				resolvedResource = myIdHelperService.resolveResourceIdentity(theRequestPartitionId, theResourceType, idPart);
				ourLog.trace("Translated {}/{} to resource PID {}", theType, idPart, resolvedResource);
			} else {
				resolvedResource = new ResourceLookupPersistentIdWrapper(persistentId);
			}
		} catch (ResourceNotFoundException e) {

			Optional<ResourceTable> createdTableOpt = createPlaceholderTargetIfConfiguredToDoSo(theType, theReference, idPart, theRequest, theTransactionDetails);
			if (!createdTableOpt.isPresent()) {

				if (myDaoConfig.isEnforceReferentialIntegrityOnWrite() == false) {
					return null;
				}

				RuntimeResourceDefinition missingResourceDef = myContext.getResourceDefinition(theType);
				String resName = missingResourceDef.getName();
				throw new InvalidRequestException(Msg.code(1094) + "Resource " + resName + "/" + idPart + " not found, specified in path: " + theSourcePath);

			}
			resolvedResource = createdTableOpt.get();
		}

		ourLog.trace("Resolved resource of type {} as PID: {}", resolvedResource.getResourceType(), resolvedResource.getResourceId());
		if (!theResourceType.equals(resolvedResource.getResourceType())) {
			ourLog.error("Resource with PID {} was of type {} and wanted {}", resolvedResource.getResourceId(), theResourceType, resolvedResource.getResourceType());
			throw new UnprocessableEntityException(Msg.code(1095) + "Resource contains reference to unknown resource ID " + theSourceResourceId.getValue());
		}

		if (resolvedResource.getDeleted() != null) {
			String resName = resolvedResource.getResourceType();
			throw new InvalidRequestException(Msg.code(1096) + "Resource " + resName + "/" + idPart + " is deleted, specified in path: " + theSourcePath);
		}

		if (persistentId == null) {
			persistentId = new ResourcePersistentId(resolvedResource.getResourceId());
			persistentId.setAssociatedResourceId(theSourceResourceId);
			theTransactionDetails.addResolvedResourceId(theSourceResourceId, persistentId);
		}

		if (!theSearchParam.hasTargets() && theSearchParam.getTargets().contains(theResourceType)) {
			return null;
		}

		return resolvedResource;
	}

	/**
	 * @param theIdToAssignToPlaceholder If specified, the placeholder resource created will be given a specific ID
	 */
	public <T extends IBaseResource> Optional<ResourceTable> createPlaceholderTargetIfConfiguredToDoSo(Class<T> theType, IBaseReference theReference, @Nullable String theIdToAssignToPlaceholder, RequestDetails theRequest, TransactionDetails theTransactionDetails) {
		ResourceTable valueOf = null;

		if (myDaoConfig.isAutoCreatePlaceholderReferenceTargets()) {
			RuntimeResourceDefinition missingResourceDef = myContext.getResourceDefinition(theType);
			String resName = missingResourceDef.getName();

			@SuppressWarnings("unchecked")
			T newResource = (T) missingResourceDef.newInstance();

			tryToAddPlaceholderExtensionToResource(newResource);

			IFhirResourceDao<T> placeholderResourceDao = myDaoRegistry.getResourceDao(theType);
			ourLog.debug("Automatically creating empty placeholder resource: {}", newResource.getIdElement().getValue());

			if (myDaoConfig.isPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets()) {
				tryToCopyIdentifierFromReferenceToTargetResource(theReference, missingResourceDef, newResource);
			}

			if (theIdToAssignToPlaceholder != null) {
				newResource.setId(resName + "/" + theIdToAssignToPlaceholder);
				valueOf = ((ResourceTable) placeholderResourceDao.update(newResource, theRequest).getEntity());
			} else {
				valueOf = ((ResourceTable) placeholderResourceDao.create(newResource, theRequest).getEntity());
			}

			ResourcePersistentId persistentId = new ResourcePersistentId(valueOf.getResourceId(), 1L);
			persistentId.setAssociatedResourceId(valueOf.getIdType(myContext));
			theTransactionDetails.addResolvedResourceId(persistentId.getAssociatedResourceId(), persistentId);
		}

		return Optional.ofNullable(valueOf);
	}

	private <T extends IBaseResource> void tryToAddPlaceholderExtensionToResource(T newResource) {
		if (newResource instanceof IBaseHasExtensions) {
			IBaseExtension<?, ?> extension = ((IBaseHasExtensions) newResource).addExtension();
			extension.setUrl(HapiExtensions.EXT_RESOURCE_PLACEHOLDER);
			extension.setValue(myContext.getPrimitiveBoolean(true));
		}
	}

	private <T extends IBaseResource> void tryToCopyIdentifierFromReferenceToTargetResource(IBaseReference theSourceReference, RuntimeResourceDefinition theTargetResourceDef, T theTargetResource) {
//		boolean referenceHasIdentifier = theSourceReference.hasIdentifier();
		CanonicalIdentifier referenceMatchUrlIdentifier = extractIdentifierFromUrl(theSourceReference.getReferenceElement().getValue());
		CanonicalIdentifier referenceIdentifier = extractIdentifierReference(theSourceReference);

		if (referenceIdentifier == null && referenceMatchUrlIdentifier != null) {
			addMatchUrlIdentifierToTargetResource(theTargetResourceDef, theTargetResource, referenceMatchUrlIdentifier);
		} else if (referenceIdentifier != null && referenceMatchUrlIdentifier == null) {
			addSubjectIdentifierToTargetResource(theSourceReference, theTargetResourceDef, theTargetResource);
		} else if (referenceIdentifier != null && referenceMatchUrlIdentifier != null) {
			if (referenceIdentifier.equals(referenceMatchUrlIdentifier)) {
				addSubjectIdentifierToTargetResource(theSourceReference, theTargetResourceDef, theTargetResource);
			} else {
				addSubjectIdentifierToTargetResource(theSourceReference, theTargetResourceDef, theTargetResource);
				addMatchUrlIdentifierToTargetResource(theTargetResourceDef, theTargetResource, referenceMatchUrlIdentifier);
			}
		}
	}

	private <T extends IBaseResource> void addSubjectIdentifierToTargetResource(IBaseReference theSourceReference, RuntimeResourceDefinition theTargetResourceDef, T theTargetResource) {
		BaseRuntimeChildDefinition targetIdentifier = theTargetResourceDef.getChildByName("identifier");
		if (targetIdentifier != null) {
			BaseRuntimeElementDefinition<?> identifierElement = targetIdentifier.getChildByName("identifier");
			String identifierElementName = identifierElement.getName();
			boolean targetHasIdentifierElement = identifierElementName.equals("Identifier");
			if (targetHasIdentifierElement) {

				BaseRuntimeElementCompositeDefinition<?> referenceElement = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(theSourceReference.getClass());
				BaseRuntimeChildDefinition referenceIdentifierChild = referenceElement.getChildByName("identifier");
				Optional<IBase> identifierOpt = referenceIdentifierChild.getAccessor().getFirstValueOrNull(theSourceReference);
				identifierOpt.ifPresent(theIBase -> targetIdentifier.getMutator().addValue(theTargetResource, theIBase));
			}
		}
	}

	private <T extends IBaseResource> void addMatchUrlIdentifierToTargetResource(RuntimeResourceDefinition theTargetResourceDef, T theTargetResource, CanonicalIdentifier referenceMatchUrlIdentifier) {
		BaseRuntimeChildDefinition identifierDefinition = theTargetResourceDef.getChildByName("identifier");
		IBase identifierIBase = identifierDefinition.getChildByName("identifier").newInstance(identifierDefinition.getInstanceConstructorArguments());
		IBase systemIBase = TerserUtil.newElement(myContext, "uri", referenceMatchUrlIdentifier.getSystemElement().getValueAsString());
		IBase valueIBase = TerserUtil.newElement(myContext, "string", referenceMatchUrlIdentifier.getValueElement().getValueAsString());
		//Set system in the IBase Identifier

		BaseRuntimeElementDefinition<?> elementDefinition = myContext.getElementDefinition(identifierIBase.getClass());

		BaseRuntimeChildDefinition systemDefinition = elementDefinition.getChildByName("system");
		systemDefinition.getMutator().setValue(identifierIBase, systemIBase);

		BaseRuntimeChildDefinition valueDefinition = elementDefinition.getChildByName("value");
		valueDefinition.getMutator().setValue(identifierIBase, valueIBase);

		//Set Value in the IBase identifier
		identifierDefinition.getMutator().addValue(theTargetResource, identifierIBase);
	}

	private CanonicalIdentifier extractIdentifierReference(IBaseReference theSourceReference) {
		Optional<IBase> identifier = myContext.newFhirPath().evaluateFirst(theSourceReference, "identifier", IBase.class);
		if (!identifier.isPresent()) {
			return null;
		} else {
			CanonicalIdentifier canonicalIdentifier = new CanonicalIdentifier();
			Optional<IPrimitiveType> system = myContext.newFhirPath().evaluateFirst(identifier.get(), "system", IPrimitiveType.class);
			Optional<IPrimitiveType> value = myContext.newFhirPath().evaluateFirst(identifier.get(), "value", IPrimitiveType.class);

			system.ifPresent(theIPrimitiveType -> canonicalIdentifier.setSystem(theIPrimitiveType.getValueAsString()));
			value.ifPresent(theIPrimitiveType -> canonicalIdentifier.setValue(theIPrimitiveType.getValueAsString()));
			return canonicalIdentifier;
		}
	}

	/**
	 * Extracts the first available identifier from the URL part
	 *
	 * @param theValue Part of the URL to extract identifiers from
	 * @return Returns the first available identifier in the canonical form or null if URL contains no identifier param
	 * @throws IllegalArgumentException IllegalArgumentException is thrown in case identifier parameter can not be split using <code>system|value</code> pattern.
	 */
	protected CanonicalIdentifier extractIdentifierFromUrl(String theValue) {
		int identifierIndex = theValue.indexOf("identifier=");
		if (identifierIndex == -1) {
			return null;
		}

		List<NameValuePair> params = URLEncodedUtils.parse(theValue.substring(identifierIndex), StandardCharsets.UTF_8, '&', ';');
		Optional<NameValuePair> idOptional = params.stream().filter(p -> p.getName().equals("identifier")).findFirst();
		if (!idOptional.isPresent()) {
			return null;
		}

		NameValuePair id = idOptional.get();
		String identifierString = id.getValue();
		String[] split = identifierString.split("\\|");
		if (split.length != 2) {
			throw new IllegalArgumentException(Msg.code(1097) + "Can't create a placeholder reference with identifier " + theValue + ". It is not a valid identifier");
		}

		CanonicalIdentifier identifier = new CanonicalIdentifier();
		identifier.setSystem(split[0]);
		identifier.setValue(split[1]);
		return identifier;
	}

	@Override
	public void validateTypeOrThrowException(Class<? extends IBaseResource> theType) {
		myDaoRegistry.getDaoOrThrowException(theType);
	}

	private static class ResourceLookupPersistentIdWrapper implements IResourceLookup {
		private final ResourcePersistentId myPersistentId;

		public ResourceLookupPersistentIdWrapper(ResourcePersistentId thePersistentId) {
			myPersistentId = thePersistentId;
		}

		@Override
		public String getResourceType() {
			return myPersistentId.getAssociatedResourceId().getResourceType();
		}

		@Override
		public Long getResourceId() {
			return myPersistentId.getIdAsLong();
		}

		@Override
		public Date getDeleted() {
			return null;
		}
	}
}
