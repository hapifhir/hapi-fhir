package ca.uhn.fhir.jpa.sp;

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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchParamPresentDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresentEntity;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class SearchParamPresenceSvcImpl implements ISearchParamPresenceSvc {

	@Autowired
	private ISearchParamPresentDao mySearchParamPresentDao;

	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired
	private DaoConfig myDaoConfig;

	@VisibleForTesting
	public void setDaoConfig(DaoConfig theDaoConfig) {
		myDaoConfig = theDaoConfig;
	}

	@Override
	public AddRemoveCount updatePresence(ResourceTable theResource, Map<String, Boolean> theParamNameToPresence) {
		AddRemoveCount retVal = new AddRemoveCount();
		if (myDaoConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.DISABLED) {
			return retVal;
		}

		Map<String, Boolean> presenceMap = new HashMap<>(theParamNameToPresence);

		// Find existing entries
		Collection<SearchParamPresentEntity> existing = theResource.getSearchParamPresents();
		Map<Long, SearchParamPresentEntity> existingHashToPresence = new HashMap<>();
		for (SearchParamPresentEntity nextExistingEntity : existing) {
			existingHashToPresence.put(nextExistingEntity.getHashPresence(), nextExistingEntity);
		}

		// Find newly wanted set of entries
		Map<Long, SearchParamPresentEntity> newHashToPresence = new HashMap<>();
		for (Entry<String, Boolean> next : presenceMap.entrySet()) {
			String paramName = next.getKey();

			SearchParamPresentEntity present = new SearchParamPresentEntity();
			present.setPartitionSettings(myPartitionSettings);
			present.setResource(theResource);
			present.setParamName(paramName);
			present.setPresent(next.getValue());
			present.setPartitionId(theResource.getPartitionId());
			present.calculateHashes();

			newHashToPresence.put(present.getHashPresence(), present);
		}

		// Delete any that should be deleted
		List<SearchParamPresentEntity> toDelete = new ArrayList<>();
		for (Entry<Long, SearchParamPresentEntity> nextEntry : existingHashToPresence.entrySet()) {
			if (newHashToPresence.containsKey(nextEntry.getKey()) == false) {
				toDelete.add(nextEntry.getValue());
			}
		}
		// Add any that should be added
		List<SearchParamPresentEntity> toAdd = new ArrayList<>();
		for (Entry<Long, SearchParamPresentEntity> nextEntry : newHashToPresence.entrySet()) {
			if (existingHashToPresence.containsKey(nextEntry.getKey()) == false) {
				toAdd.add(nextEntry.getValue());
			}
		}

		// Try to reuse any entities we can
		while (toDelete.size() > 0 && toAdd.size() > 0) {
			SearchParamPresentEntity nextToDelete = toDelete.remove(toDelete.size() - 1);
			SearchParamPresentEntity nextToAdd = toAdd.remove(toAdd.size() - 1);
			nextToDelete.updateValues(nextToAdd);
			mySearchParamPresentDao.save(nextToDelete);
			retVal.addToAddCount(1);
			retVal.addToRemoveCount(1);
		}

		mySearchParamPresentDao.deleteAll(toDelete);
		retVal.addToRemoveCount(toDelete.size());

		mySearchParamPresentDao.saveAll(toAdd);
		retVal.addToRemoveCount(toAdd.size());

		return retVal;
	}

}
