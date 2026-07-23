/*-
 * #%L
 * HAPI-FHIR Storage MDM
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.step.IIdChunkProducer;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.svc.IGoldenResourceSearchSvc;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MDM-specific implementation of {@link IIdChunkProducer} that produces pages of golden resource PIDs
 * for MDM batch operations. This is the second step in the MDM clear batch job pipeline: it receives
 * work chunks (resource type and date range) from the first step and queries for matching golden resource
 * IDs via {@link IGoldenResourceSearchSvc}.
 */
public class MdmIdChunkProducer implements IIdChunkProducer<ChunkRangeJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmIdChunkProducer.class);
	private final IGoldenResourceSearchSvc myGoldenResourceSearchSvc;

	/**
	 * @param theGoldenResourceSearchSvc the service used to search for golden resource PIDs
	 */
	public MdmIdChunkProducer(IGoldenResourceSearchSvc theGoldenResourceSearchSvc) {
		myGoldenResourceSearchSvc = theGoldenResourceSearchSvc;
	}

	/**
	 * Fetches a stream of golden resource PIDs for the resource type and date range specified in the chunk data.
	 * The resource type is extracted from the chunk URL, falling back to the direct resource type field
	 * for backward compatibility with older chunk formats.
	 *
	 * @param theData the chunk range containing the resource type (via URL or direct field), date range, and partition ID
	 * @return a stream of golden resource PIDs matching the criteria
	 */
	@Override
	public IResourcePidStream fetchResourceIdStream(ChunkRangeJson theData) {
		String resourceType;
		String url = theData.getUrl();
		if (StringUtils.isNotBlank(url)) {
			resourceType = UrlUtil.parseUrl(url).getResourceType();
		} else {
			// job backward compatibility: resourceType was set directly on the chunk
			resourceType = theData.getResourceType();
		}

		ourLog.info(
				"Fetching golden resource ID chunk for resource type {} - Range {} - {}",
				resourceType,
				theData.getStart(),
				theData.getEnd());

		return myGoldenResourceSearchSvc.fetchGoldenResourceIdStream(
				theData.getStart(), theData.getEnd(), theData.getPartitionId(), resourceType);
	}
}
