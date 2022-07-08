package ca.uhn.fhir.jpa.delete.job;

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
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.expunge.PartitionRunner;
import ca.uhn.fhir.jpa.dao.expunge.ResourceForeignKey;
import ca.uhn.fhir.jpa.dao.expunge.ResourceTableFKProvider;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Input: list of pids of resources to be deleted and expunged
 * Output: list of sql statements to be executed
 */
public class DeleteExpungeProcessor implements ItemProcessor<List<Long>, List<String>> {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeProcessor.class);

	public static final String PROCESS_NAME = "Delete Expunging";
	public static final String THREAD_PREFIX = "delete-expunge";

	@Autowired
	ResourceTableFKProvider myResourceTableFKProvider;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	IJpaIdHelperService myIdHelper;
	@Autowired
	IResourceLinkDao myResourceLinkDao;

	@Override
	public List<String> process(List<Long> thePids) throws Exception {
		validateOkToDeleteAndExpunge(thePids);

		List<String> retval = new ArrayList<>();

		String pidListString = thePids.toString().replace("[", "(").replace("]", ")");
		List<ResourceForeignKey> resourceForeignKeys = myResourceTableFKProvider.getResourceForeignKeys();

		for (ResourceForeignKey resourceForeignKey : resourceForeignKeys) {
			retval.add(deleteRecordsByColumnSql(pidListString, resourceForeignKey));
		}

		// Lastly we need to delete records from the resource table all of these other tables link to:
		ResourceForeignKey resourceTablePk = new ResourceForeignKey("HFJ_RESOURCE", "RES_ID");
		retval.add(deleteRecordsByColumnSql(pidListString, resourceTablePk));
		return retval;
	}

	public void validateOkToDeleteAndExpunge(List<Long> thePids) {
		if (!myDaoConfig.isEnforceReferentialIntegrityOnDelete()) {
			ourLog.info("Referential integrity on delete disabled.  Skipping referential integrity check.");
			return;
		}

		List<ResourcePersistentId> targetPidsAsResourceIds = ResourcePersistentId.fromLongList(thePids);
		List<ResourceLink> conflictResourceLinks = Collections.synchronizedList(new ArrayList<>());
		PartitionRunner partitionRunner = new PartitionRunner(PROCESS_NAME, THREAD_PREFIX, myDaoConfig.getExpungeBatchSize(), myDaoConfig.getExpungeThreadCount());
		Consumer<List<ResourcePersistentId>> listConsumer = someTargetPids -> findResourceLinksWithTargetPidIn(targetPidsAsResourceIds, someTargetPids, conflictResourceLinks);
		partitionRunner.runInPartitionedThreads(targetPidsAsResourceIds, listConsumer);

		if (conflictResourceLinks.isEmpty()) {
			return;
		}

		ResourceLink firstConflict = conflictResourceLinks.get(0);

		//NB-GGG: We previously instantiated these ID values from firstConflict.getSourceResource().getIdDt(), but in a situation where we
		//actually had to run delete conflict checks in multiple partitions, the executor service starts its own sessions on a per thread basis, and by the time
		//we arrive here, those sessions are closed. So instead, we resolve them from PIDs, which are eagerly loaded.
		String sourceResourceId = myIdHelper.resourceIdFromPidOrThrowException(firstConflict.getSourceResourcePid()).toVersionless().getValue();
		String targetResourceId = myIdHelper.resourceIdFromPidOrThrowException(firstConflict.getTargetResourcePid()).toVersionless().getValue();

		throw new InvalidRequestException(Msg.code(822) + "DELETE with _expunge=true failed.  Unable to delete " +
			targetResourceId + " because " + sourceResourceId + " refers to it via the path " + firstConflict.getSourcePath());
	}

	public void findResourceLinksWithTargetPidIn(List<ResourcePersistentId> theAllTargetPids, List<ResourcePersistentId> theSomeTargetPids, List<ResourceLink> theConflictResourceLinks) {
		List<Long> allTargetPidsAsLongs = ResourcePersistentId.toLongList(theAllTargetPids);
		List<Long> someTargetPidsAsLongs = ResourcePersistentId.toLongList(theSomeTargetPids);
		// We only need to find one conflict, so if we found one already in an earlier partition run, we can skip the rest of the searches
		if (theConflictResourceLinks.isEmpty()) {
			List<ResourceLink> conflictResourceLinks = myResourceLinkDao.findWithTargetPidIn(someTargetPidsAsLongs).stream()
				// Filter out resource links for which we are planning to delete the source.
				// theAllTargetPids contains a list of all the pids we are planning to delete.  So we only want
				// to consider a link to be a conflict if the source of that link is not in theAllTargetPids.
				.filter(link -> !allTargetPidsAsLongs.contains(link.getSourceResourcePid()))
				.collect(Collectors.toList());

			// We do this in two steps to avoid lock contention on this synchronized list
			theConflictResourceLinks.addAll(conflictResourceLinks);
		}
	}

	private String deleteRecordsByColumnSql(String thePidListString, ResourceForeignKey theResourceForeignKey) {
		return "DELETE FROM " + theResourceForeignKey.table + " WHERE " + theResourceForeignKey.key + " IN " + thePidListString;
	}
}
