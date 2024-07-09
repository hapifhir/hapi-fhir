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
package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @deprecated
 */
@Service
public class ResourceReindexer {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceReindexer.class);

	@Autowired
	private IResourceHistoryTableDao myResourceHistoryTableDao;

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
		ResourceTable resourceTable =
				myResourceTableDao.findById(theResourcePid).orElseThrow(IllegalStateException::new);
		reindexResourceEntity(resourceTable);
	}

	public void reindexResourceEntity(ResourceTable theResourceTable) {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceTable.getResourceType());
		long expectedVersion = theResourceTable.getVersion();
		IBaseResource resource = dao.readByPid(JpaPid.fromId(theResourceTable.getId()), true);

		if (resource == null) {
			throw new InternalErrorException(Msg.code(1171) + "Could not find resource version "
					+ theResourceTable.getIdDt().toUnqualified().getValue() + " in database");
		}

		Long actualVersion = resource.getIdElement().getVersionIdPartAsLong();
		if (actualVersion < expectedVersion) {
			ourLog.warn(
					"Resource {} version {} does not exist, renumbering version {}",
					resource.getIdElement().toUnqualifiedVersionless().getValue(),
					resource.getIdElement().getVersionIdPart(),
					expectedVersion);
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
		if (myFulltextSearchSvc != null && !myFulltextSearchSvc.isDisabled()) {
			// update the full-text index, if active.
			myFulltextSearchSvc.reindex(theResourceTable);
		}
	}
}
