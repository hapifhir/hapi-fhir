package ca.uhn.fhir.jpa.sp;

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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchParamPresentDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresent;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

@Service
public class SearchParamPresenceSvcImpl implements ISearchParamPresenceSvc {

	@Autowired
	private ISearchParamPresentDao mySearchParamPresentDao;

	@Autowired
	private DaoConfig myDaoConfig;

	@Override
	public AddRemoveCount updatePresence(ResourceTable theResource, Map<String, Boolean> theParamNameToPresence) {
		AddRemoveCount retVal = new AddRemoveCount();
		if (myDaoConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.DISABLED) {
			return retVal;
		}

		Map<String, Boolean> presenceMap = new HashMap<>(theParamNameToPresence);

		// Find existing entries
		Collection<SearchParamPresent> existing;
		existing = mySearchParamPresentDao.findAllForResource(theResource);
		Map<Long, SearchParamPresent> existingHashToPresence = new HashMap<>();
		for (SearchParamPresent nextExistingEntity : existing) {
			existingHashToPresence.put(nextExistingEntity.getHashPresence(), nextExistingEntity);
		}

		// Find newly wanted set of entries
		Map<Long, SearchParamPresent> newHashToPresence = new HashMap<>();
		for (Entry<String, Boolean> next : presenceMap.entrySet()) {
			String paramName = next.getKey();

			SearchParamPresent present = new SearchParamPresent();
			present.setResource(theResource);
			present.setParamName(paramName);
			present.setPresent(next.getValue());
			present.calculateHashes();

			newHashToPresence.put(present.getHashPresence(), present);
		}

		// Delete any that should be deleted
		List<SearchParamPresent> toDelete = new ArrayList<>();
		for (Entry<Long, SearchParamPresent> nextEntry : existingHashToPresence.entrySet()) {
			if (newHashToPresence.containsKey(nextEntry.getKey()) == false) {
				toDelete.add(nextEntry.getValue());
			}
		}
		mySearchParamPresentDao.deleteAll(toDelete);
		retVal.addToRemoveCount(toDelete.size());

		// Add any that should be added
		List<SearchParamPresent> toAdd = new ArrayList<>();
		for (Entry<Long, SearchParamPresent> nextEntry : newHashToPresence.entrySet()) {
			if (existingHashToPresence.containsKey(nextEntry.getKey()) == false) {
				toAdd.add(nextEntry.getValue());
			}
		}
		mySearchParamPresentDao.saveAll(toAdd);
		retVal.addToRemoveCount(toAdd.size());

		return retVal;
	}

}
