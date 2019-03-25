package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.IResourceLinkResolver;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Service
public class DaoResourceLinkResolver implements IResourceLinkResolver {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DaoResourceLinkResolver.class);

	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private DaoRegistry myDaoRegistry;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Override
	public ResourceTable findTargetResource(RuntimeSearchParam theNextSpDef, String theNextPathsUnsplit, IIdType theNextId, String theTypeString, Class<? extends IBaseResource> theType, String theId) {
		ResourceTable target;
		Long valueOf;
		try {
			valueOf = myIdHelperService.translateForcedIdToPid(theTypeString, theId);
			ourLog.trace("Translated {}/{} to resource PID {}", theType, theId, valueOf);
		} catch (ResourceNotFoundException e) {
			if (myDaoConfig.isEnforceReferentialIntegrityOnWrite() == false) {
				return null;
			}
			RuntimeResourceDefinition missingResourceDef = myContext.getResourceDefinition(theType);
			String resName = missingResourceDef.getName();

			if (myDaoConfig.isAutoCreatePlaceholderReferenceTargets()) {
				IBaseResource newResource = missingResourceDef.newInstance();
				newResource.setId(resName + "/" + theId);
				IFhirResourceDao<IBaseResource> placeholderResourceDao = (IFhirResourceDao<IBaseResource>) myDaoRegistry.getResourceDao(newResource.getClass());
				ourLog.debug("Automatically creating empty placeholder resource: {}", newResource.getIdElement().getValue());
				valueOf = placeholderResourceDao.update(newResource).getEntity().getId();
			} else {
				throw new InvalidRequestException("Resource " + resName + "/" + theId + " not found, specified in path: " + theNextPathsUnsplit);
			}
		}
		target = myEntityManager.find(ResourceTable.class, valueOf);
		RuntimeResourceDefinition targetResourceDef = myContext.getResourceDefinition(theType);
		if (target == null) {
			String resName = targetResourceDef.getName();
			throw new InvalidRequestException("Resource " + resName + "/" + theId + " not found, specified in path: " + theNextPathsUnsplit);
		}

		ourLog.trace("Resource PID {} is of type {}", valueOf, target.getResourceType());
		if (!theTypeString.equals(target.getResourceType())) {
			ourLog.error("Resource {} with PID {} was not of type {}", target.getIdDt().getValue(), target.getId(), theTypeString);
			throw new UnprocessableEntityException(
				"Resource contains reference to " + theNextId.getValue() + " but resource with ID " + theNextId.getIdPart() + " is actually of type " + target.getResourceType());
		}

		if (target.getDeleted() != null) {
			String resName = targetResourceDef.getName();
			throw new InvalidRequestException("Resource " + resName + "/" + theId + " is deleted, specified in path: " + theNextPathsUnsplit);
		}

		if (theNextSpDef.getTargets() != null && !theNextSpDef.getTargets().contains(theTypeString)) {
			return null;
		}
		return target;
	}

	@Override
	public void validateTypeOrThrowException(Class<? extends IBaseResource> theType) {
		myDaoRegistry.getDaoOrThrowException(theType);
	}
}
