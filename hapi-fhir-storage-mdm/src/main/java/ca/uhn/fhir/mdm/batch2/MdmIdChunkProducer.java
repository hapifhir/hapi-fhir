/*-
 * #%L
 * hapi-fhir-storage-mdm
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.batch2;

import ca.uhn.fhir.batch2.jobs.step.IIdChunkProducer;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.svc.IGoldenResourceSearchSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import javax.annotation.Nonnull;

public class MdmIdChunkProducer implements IIdChunkProducer<MdmChunkRangeJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmIdChunkProducer.class);
	private final IGoldenResourceSearchSvc myGoldenResourceSearchSvc;

	public MdmIdChunkProducer(IGoldenResourceSearchSvc theGoldenResourceSearchSvc) {
		myGoldenResourceSearchSvc = theGoldenResourceSearchSvc;
	}

	@Override
	public IResourcePidList fetchResourceIdsPage(
			Date theNextStart,
			Date theEnd,
			@Nonnull Integer thePageSize,
			RequestPartitionId theRequestPartitionId,
			MdmChunkRangeJson theData) {
		String resourceType = theData.getResourceType();

		ourLog.info(
				"Fetching golden resource ID chunk for resource type {} - Range {} - {}",
				resourceType,
				theNextStart,
				theEnd);

		return myGoldenResourceSearchSvc.fetchGoldenResourceIdsPage(
				theNextStart, theEnd, thePageSize, theRequestPartitionId, resourceType);
	}
}
