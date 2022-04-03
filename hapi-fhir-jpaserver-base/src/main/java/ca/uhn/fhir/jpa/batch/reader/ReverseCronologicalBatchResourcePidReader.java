package ca.uhn.fhir.jpa.batch.reader;

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

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class ReverseCronologicalBatchResourcePidReader extends BaseReverseCronologicalBatchPidReader {
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private BatchResourceSearcher myBatchResourceSearcher;

	@Override
	protected Set<Long> getNextPidBatch(ResourceSearch resourceSearch) {
		Set<Long> retval = new LinkedHashSet<>();
		addDateCountAndSortToSearch(resourceSearch);

		// Perform the search
		Integer batchSize = getBatchSize();
		IResultIterator resultIter = myBatchResourceSearcher.performSearch(resourceSearch, batchSize);
		Set<Long> alreadySeenPids = getAlreadySeenPids();

		do {
			List<Long> pids = resultIter.getNextResultBatch(batchSize).stream().map(ResourcePersistentId::getIdAsLong).collect(Collectors.toList());
			retval.addAll(pids);
			retval.removeAll(alreadySeenPids);
		} while (retval.size() < batchSize && resultIter.hasNext());

		return retval;
	}

	@Override
	protected void setDateFromPidFunction(ResourceSearch resourceSearch) {
		final IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceSearch.getResourceName());

		setDateExtractorFunction(pid -> {
			IBaseResource oldestResource = dao.readByPid(new ResourcePersistentId(pid));
			return oldestResource.getMeta().getLastUpdated();
		});
	}
}
