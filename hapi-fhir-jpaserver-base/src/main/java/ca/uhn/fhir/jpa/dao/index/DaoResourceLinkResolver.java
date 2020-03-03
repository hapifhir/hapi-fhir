package ca.uhn.fhir.jpa.dao.index;

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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.IResourceLinkResolver;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.Optional;

@Service
public class DaoResourceLinkResolver implements IResourceLinkResolver {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DaoResourceLinkResolver.class);
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private DaoRegistry myDaoRegistry;

	@Override
	public ResourceTable findTargetResource(RuntimeSearchParam theNextSpDef, String theNextPathsUnsplit, IIdType theNextId, String theTypeString, Class<? extends IBaseResource> theType, IBaseReference theReference, RequestDetails theRequest) {
		ResourceTable target;
		ResourcePersistentId valueOf;
		String idPart = theNextId.getIdPart();
		try {
			valueOf = myIdHelperService.translateForcedIdToPid(theTypeString, idPart, theRequest);
			ourLog.trace("Translated {}/{} to resource PID {}", theType, idPart, valueOf);
		} catch (ResourceNotFoundException e) {

			Optional<ResourcePersistentId> pidOpt = createPlaceholderTargetIfConfiguredToDoSo(theType, theReference, idPart);
			if (!pidOpt.isPresent()) {

				if (myDaoConfig.isEnforceReferentialIntegrityOnWrite() == false) {
					return null;
				}

				RuntimeResourceDefinition missingResourceDef = myContext.getResourceDefinition(theType);
				String resName = missingResourceDef.getName();
				throw new InvalidRequestException("Resource " + resName + "/" + idPart + " not found, specified in path: " + theNextPathsUnsplit);

			}

			valueOf = pidOpt.get();
		}

		target = myEntityManager.find(ResourceTable.class, valueOf.getIdAsLong());
		RuntimeResourceDefinition targetResourceDef = myContext.getResourceDefinition(theType);
		if (target == null) {
			String resName = targetResourceDef.getName();
			throw new InvalidRequestException("Resource " + resName + "/" + idPart + " not found, specified in path: " + theNextPathsUnsplit);
		}

		ourLog.trace("Resource PID {} is of type {}", valueOf, target.getResourceType());
		if (!theTypeString.equals(target.getResourceType())) {
			ourLog.error("Resource {} with PID {} was not of type {}", target.getIdDt().getValue(), target.getId(), theTypeString);
			throw new UnprocessableEntityException(
				"Resource contains reference to " + theNextId.getValue() + " but resource with ID " + theNextId.getIdPart() + " is actually of type " + target.getResourceType());
		}

		if (target.getDeleted() != null) {
			String resName = targetResourceDef.getName();
			throw new InvalidRequestException("Resource " + resName + "/" + idPart + " is deleted, specified in path: " + theNextPathsUnsplit);
		}

		if (!theNextSpDef.hasTargets() && theNextSpDef.getTargets().contains(theTypeString)) {
			return null;
		}
		return target;
	}

	/**
	 * @param theIdToAssignToPlaceholder If specified, the placeholder resource created will be given a specific ID
	 */
	public <T extends IBaseResource> Optional<ResourcePersistentId> createPlaceholderTargetIfConfiguredToDoSo(Class<T> theType, IBaseReference theReference, @Nullable String theIdToAssignToPlaceholder) {
		ResourcePersistentId valueOf = null;

		if (myDaoConfig.isAutoCreatePlaceholderReferenceTargets()) {
			RuntimeResourceDefinition missingResourceDef = myContext.getResourceDefinition(theType);
			String resName = missingResourceDef.getName();

			@SuppressWarnings("unchecked")
			T newResource = (T) missingResourceDef.newInstance();

			IFhirResourceDao<T> placeholderResourceDao = myDaoRegistry.getResourceDao(theType);
			ourLog.debug("Automatically creating empty placeholder resource: {}", newResource.getIdElement().getValue());

			if (myDaoConfig.isPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets()) {
				tryToCopyIdentifierFromReferenceToTargetResource(theReference, missingResourceDef, newResource);
			}

			if (theIdToAssignToPlaceholder != null) {
				newResource.setId(resName + "/" + theIdToAssignToPlaceholder);
				valueOf = placeholderResourceDao.update(newResource).getEntity().getPersistentId();
			} else {
				valueOf = placeholderResourceDao.create(newResource).getEntity().getPersistentId();
			}
		}

		return Optional.ofNullable(valueOf);
	}

	private <T extends IBaseResource> void tryToCopyIdentifierFromReferenceToTargetResource(IBaseReference theSourceReference, RuntimeResourceDefinition theTargetResourceDef, T theTargetResource) {
		boolean referenceHasIdentifier = theSourceReference.hasIdentifier();
		if (referenceHasIdentifier) {
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
	}

	@Override
	public void validateTypeOrThrowException(Class<? extends IBaseResource> theType) {
		myDaoRegistry.getDaoOrThrowException(theType);
	}

}
