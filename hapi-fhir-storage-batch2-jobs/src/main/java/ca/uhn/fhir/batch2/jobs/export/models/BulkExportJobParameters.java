package ca.uhn.fhir.batch2.jobs.export.models;

import ca.uhn.fhir.jpa.util.JsonDateDeserializer;
import ca.uhn.fhir.jpa.util.JsonDateSerializer;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

public class BulkExportJobParameters implements IModelJson {

	// list of resource types to export
	@JsonProperty("resourceTypes")
	private List<String> myResourceTypes;

	/**
	 * The start date from when we should start
	 * doing the export. (end date is assumed to be "now")
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@JsonProperty("since")
	private Date myStartDate;

	@JsonProperty("filters")
	private List<String> myFilters;

	@JsonProperty("outputFormat")
	private String myOutputFormat;

	// TODO - move enum
	@JsonProperty("exportStyle")
	private BulkDataExportOptions.ExportStyle myExportStyle;

	// could be null
	@JsonProperty("groupId")
	private String myGroupId;
}
