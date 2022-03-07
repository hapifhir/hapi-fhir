package ca.uhn.fhir.jpa.bulk.export.api;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import javax.transaction.Transactional;

public interface IBulkDataExportJobSchedulingHelper {

	/**
	 * invoked via scheduled task, purges any tasks which are past their cutoff point.
	 */
	@Transactional(value = Transactional.TxType.NEVER)
	void purgeExpiredFiles();

	/**
	 * Stops all invoked jobs, and then purges them.
	 */
	@Transactional(value = Transactional.TxType.NEVER)
	void cancelAndPurgeAllJobs();

	/**
	 * Given all Bulk Export jobs that have been created since the last scheduled run, this method will
	 * start them all. This is invoked primarily via a scheduler.
	 */
	@Transactional(value = Transactional.TxType.NEVER)
	void startSubmittedJobs();
}
