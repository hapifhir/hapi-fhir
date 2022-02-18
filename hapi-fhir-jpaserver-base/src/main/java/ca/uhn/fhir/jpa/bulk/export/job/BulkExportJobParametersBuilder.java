package ca.uhn.fhir.jpa.bulk.export.job;

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

import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.api.Constants;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This builder is a helper so you don't have to go lookup what job parameters are required for a bulk export job.
 *
 */
public class BulkExportJobParametersBuilder extends JobParametersBuilder {

	public BulkExportJobParametersBuilder setResourceTypes(List<String> resourceTypes) {
		String resourceTypesString = resourceTypes.stream().collect(Collectors.joining(","));
		this.addString("resourceTypes", resourceTypesString);
		return this;
	}

	public BulkExportJobParametersBuilder setSince(Date theSince) {
		this.addDate("since", theSince);
		return this;
	}

	public BulkExportJobParametersBuilder setOutputFormat(String theOutputFormat) {
		//TODO eventually we will support more types.
		theOutputFormat = Constants.CT_FHIR_NDJSON;
		this.addString("outputFormat", theOutputFormat);
		return this;
	}

	public BulkExportJobParametersBuilder setFilters(Set<String> theFilters) {
		this.addString("filters", theFilters.stream().collect(Collectors.joining(",")));
		return this;
	}

	public BulkExportJobParametersBuilder setJobUUID(String theJobUUID) {
		this.addString("jobUUID", theJobUUID);
		return this;
	}
	public BulkExportJobParametersBuilder setReadChunkSize(Long theReadChunkSize) {
		this.addLong("readChunkSize", theReadChunkSize);
		return this;
	}
	public BulkExportJobParametersBuilder setExportStyle(BulkDataExportOptions.ExportStyle theExportStyle) {
		this.addString("exportStyle", theExportStyle.name());
		return this;
	}
}
