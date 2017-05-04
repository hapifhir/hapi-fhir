package ca.uhn.fhir.jpa.sp;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.jpa.dao.data.ISearchParamDao;
import ca.uhn.fhir.jpa.dao.data.ISearchParamPresentDao;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.SearchParam;
import ca.uhn.fhir.jpa.entity.SearchParamPresent;

public class SearchParamPresenceSvcImpl implements ISearchParamPresenceSvc {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParamPresenceSvcImpl.class);

	private Map<Pair<String, String>, SearchParam> myResourceTypeToSearchParamToEntity = new ConcurrentHashMap<Pair<String, String>, SearchParam>();

	@Autowired
	private ISearchParamDao mySearchParamDao;

	@Autowired
	private ISearchParamPresentDao mySearchParamPresentDao;

	@Override
	public void updatePresence(ResourceTable theResource, Map<String, Boolean> theParamNameToPresence) {

		Map<String, Boolean> presenceMap = new HashMap<String, Boolean>(theParamNameToPresence);
		List<SearchParamPresent> entitiesToSave = new ArrayList<SearchParamPresent>();
		List<SearchParamPresent> entitiesToDelete = new ArrayList<SearchParamPresent>();

		Collection<SearchParamPresent> existing;
		existing = mySearchParamPresentDao.findAllForResource(theResource);

		for (SearchParamPresent nextExistingEntity : existing) {
			String nextSearchParamName = nextExistingEntity.getSearchParam().getParamName();
			Boolean existingValue = presenceMap.remove(nextSearchParamName);
			if (existingValue == null) {
				entitiesToDelete.add(nextExistingEntity);
			} else if (existingValue.booleanValue() == nextExistingEntity.isPresent()) {
				ourLog.trace("No change for search param {}", nextSearchParamName);
			} else {
				nextExistingEntity.setPresent(existingValue);
				entitiesToSave.add(nextExistingEntity);
			}
		}

		for (Entry<String, Boolean> next : presenceMap.entrySet()) {
			String resourceType = theResource.getResourceType();
			String paramName = next.getKey();
			Pair<String, String> key = Pair.of(resourceType, paramName);

			SearchParam searchParam = myResourceTypeToSearchParamToEntity.get(key);
			if (searchParam == null) {
				searchParam = mySearchParamDao.findForResource(resourceType, paramName);
				if (searchParam != null) {
					myResourceTypeToSearchParamToEntity.put(key, searchParam);
				} else {
					searchParam = new SearchParam();
					searchParam.setResourceName(resourceType);
					searchParam.setParamName(paramName);
					searchParam = mySearchParamDao.saveAndFlush(searchParam);
					ourLog.info("Added search param {} with pid {}", paramName, searchParam.getId());
					// Don't add the newly saved entity to the map in case the save fails
				}
			}

			SearchParamPresent present = new SearchParamPresent();
			present.setResource(theResource);
			present.setSearchParam(searchParam);
			present.setPresent(next.getValue());
			entitiesToSave.add(present);

		}

		mySearchParamPresentDao.deleteInBatch(entitiesToDelete);
		mySearchParamPresentDao.save(entitiesToSave);

	}

	@Override
	public void flushCachesForUnitTest() {
		myResourceTypeToSearchParamToEntity.clear();
	}

}
