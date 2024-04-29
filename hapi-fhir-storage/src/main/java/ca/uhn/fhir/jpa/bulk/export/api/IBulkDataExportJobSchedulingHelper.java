/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.bulk.export.api;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface IBulkDataExportJobSchedulingHelper {

	/**
	 * invoked via scheduled task, purges any tasks which are past their cutoff point.
	 */
	@Transactional(propagation = Propagation.NEVER)
	void purgeExpiredFiles();

	/**
	 * Stops all invoked jobs, and then purges them.
	 */
	@Transactional(propagation = Propagation.NEVER)
	@Deprecated
	void cancelAndPurgeAllJobs();
}
