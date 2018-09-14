package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Semaphore;

public class ReindexController implements IReindexController {

	private static final Logger ourLog = LoggerFactory.getLogger(ReindexController.class);
	private final Semaphore myReindexingLock = new Semaphore(1);
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private IFhirSystemDao<?, ?> mySystemDao;
	private Long myDontReindexUntil;

	/**
	 * This method is called once per minute to perform any required re-indexing.
	 * <p>
	 * If nothing if found that requires reindexing, the query will not fire again for
	 * a longer amount of time.
	 * <p>
	 * During most passes this will just check and find that there are no resources
	 * requiring re-indexing. In that case the method just returns immediately.
	 * If the search finds that some resources require reindexing, the system will
	 * do a bunch of reindexing and then return.
	 */
	@Scheduled(fixedDelay = DateUtils.MILLIS_PER_MINUTE)
	@Transactional(propagation = Propagation.NEVER)
	@Override
	public void performReindexingPass() {
		if (myDaoConfig.isSchedulingDisabled() || myDaoConfig.isStatusBasedReindexingDisabled()) {
			return;
		}

		synchronized (this) {
			if (myDontReindexUntil != null && myDontReindexUntil > System.currentTimeMillis()) {
				return;
			}
		}

		if (!myReindexingLock.tryAcquire()) {
			ourLog.trace("Not going to reindex in parallel threads");
			return;
		}
		Integer count;
		try {
			count = mySystemDao.performReindexingPass(100);

			for (int i = 0; i < 50 && count != null && count != 0; i++) {
				count = mySystemDao.performReindexingPass(100);
				try {
					Thread.sleep(DateUtils.MILLIS_PER_SECOND);
				} catch (InterruptedException e) {
					break;
				}
			}
		} catch (Exception e) {
			ourLog.error("Failure during reindex", e);
			count = -1;
		} finally {
			myReindexingLock.release();
		}

		synchronized (this) {
			if (count == null) {
				ourLog.info("Reindex pass complete, no remaining resource to index");
				myDontReindexUntil = System.currentTimeMillis() + DateUtils.MILLIS_PER_HOUR;
			} else if (count == -1) {
				// Reindexing failed
				myDontReindexUntil = System.currentTimeMillis() + DateUtils.MILLIS_PER_HOUR;
			} else {
				ourLog.info("Reindex pass complete, {} remaining resource to index", count);
				myDontReindexUntil = null;
			}
		}

	}

	/**
	 * Calling this will cause a reindex loop to be triggered sooner that it would otherwise
	 */
	@Override
	public void requestReindex() {
		synchronized (this) {
			myDontReindexUntil = null;
		}
	}


}
