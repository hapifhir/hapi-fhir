package ca.uhn.fhir.jpa.cache;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This service builds a map of resource ids to versions based on a SearchParameterMap.
 * It is used by the in-memory resource-version cache to detect when resource versions have been changed by remote processes.
 */
@Service
public class ResourceVersionSvcDaoImpl implements IResourceVersionSvc {

	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	IResourceTableDao myResourceTableDao;

	@Override
	@Nonnull
	public ResourceVersionMap getVersionMap(String theResourceName, SearchParameterMap theSearchParamMap) {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceName);

		List<Long> matchingIds = dao.searchForIds(theSearchParamMap, null).stream()
			.map(ResourcePersistentId::getIdAsLong)
			.collect(Collectors.toList());

		List<ResourceTable> allById = new ArrayList<>();
		new QueryChunker<Long>().chunk(matchingIds, t -> {
			List<ResourceTable> nextBatch = myResourceTableDao.findAllById(t);
			allById.addAll(nextBatch);
		});

		return ResourceVersionMap.fromResourceTableEntities(allById);
	}
}
