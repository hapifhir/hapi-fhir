package ca.uhn.fhir.jpa.search.reindex;

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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class ResourceReindexer {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceReindexer.class);
	@Autowired
	private IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	private IForcedIdDao myForcedIdDao;
	@Autowired
	private IResourceTableDao myResourceTableDao;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired(required = false)
	private IFulltextSearchSvc myFulltextSearchSvc;

	private final FhirContext myFhirContext;

	public ResourceReindexer(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public void readAndReindexResourceByPid(Long theResourcePid) {
		ResourceTable resourceTable = myResourceTableDao.findById(theResourcePid).orElseThrow(IllegalStateException::new);
		reindexResourceEntity(resourceTable);
	}

	public void reindexResourceEntity(ResourceTable theResourceTable) {
		/*
		 * This part is because from HAPI 1.5 - 1.6 we changed the format of forced ID to be "type/id" instead of just "id"
		 */
		ForcedId forcedId = theResourceTable.getForcedId();
		if (forcedId != null) {
			if (isBlank(forcedId.getResourceType())) {
				ourLog.info("Updating resource {} forcedId type to {}", forcedId.getForcedId(), theResourceTable.getResourceType());
				forcedId.setResourceType(theResourceTable.getResourceType());
				myForcedIdDao.save(forcedId);
			}
		}

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceTable.getResourceType());
		long expectedVersion = theResourceTable.getVersion();
		IBaseResource resource = dao.readByPid(new ResourcePersistentId(theResourceTable.getId()), true);

		if (resource == null) {
			throw new InternalErrorException(Msg.code(1171) + "Could not find resource version " + theResourceTable.getIdDt().toUnqualified().getValue() + " in database");
		}

		Long actualVersion = resource.getIdElement().getVersionIdPartAsLong();
		if (actualVersion < expectedVersion) {
			ourLog.warn("Resource {} version {} does not exist, renumbering version {}", resource.getIdElement().toUnqualifiedVersionless().getValue(), resource.getIdElement().getVersionIdPart(), expectedVersion);
			myResourceHistoryTableDao.updateVersion(theResourceTable.getId(), actualVersion, expectedVersion);
		}

		doReindex(theResourceTable, resource);
	}

	@SuppressWarnings("unchecked")
	<T extends IBaseResource> void doReindex(ResourceTable theResourceTable, T theResource) {
		RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theResource.getClass());
		Class<T> resourceClass = (Class<T>) resourceDefinition.getImplementingClass();
		final IFhirResourceDao<T> dao = myDaoRegistry.getResourceDao(resourceClass);
		dao.reindex(theResource, theResourceTable);
		if (myFulltextSearchSvc != null) {
			// update the full-text index, if active.
			myFulltextSearchSvc.reindex(theResourceTable);
		}

	}
}
