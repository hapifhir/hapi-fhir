package ca.uhn.fhir.jpa.bulk.job;

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
}
