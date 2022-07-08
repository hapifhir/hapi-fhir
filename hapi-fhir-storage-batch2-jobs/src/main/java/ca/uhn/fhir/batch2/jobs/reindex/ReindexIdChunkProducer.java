package ca.uhn.fhir.batch2.jobs.reindex;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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

import ca.uhn.fhir.batch2.jobs.step.IIdChunkProducer;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;

public class ReindexIdChunkProducer implements IIdChunkProducer<ReindexChunkRangeJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexIdChunkProducer.class);
	private final IResourceReindexSvc myResourceReindexSvc;

	public ReindexIdChunkProducer(IResourceReindexSvc theResourceReindexSvc) {
		myResourceReindexSvc = theResourceReindexSvc;
	}

	@Override
	public IResourcePidList fetchResourceIdsPage(Date theNextStart, Date theEnd, @Nonnull Integer thePageSize, @Nullable RequestPartitionId theRequestPartitionId, ReindexChunkRangeJson theData) {
		String url = theData.getUrl();

		ourLog.info("Fetching resource ID chunk for URL {} - Range {} - {}", url, theNextStart, theEnd);
		return myResourceReindexSvc.fetchResourceIdsPage(theNextStart, theEnd, thePageSize, theRequestPartitionId, url);
	}
}
