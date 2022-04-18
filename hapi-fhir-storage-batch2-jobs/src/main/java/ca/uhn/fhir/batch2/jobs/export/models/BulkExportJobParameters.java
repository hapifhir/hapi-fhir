package ca.uhn.fhir.batch2.jobs.export.models;

import ca.uhn.fhir.jpa.api.model.BulkExportParameters;
import ca.uhn.fhir.jpa.util.JsonDateDeserializer;
import ca.uhn.fhir.jpa.util.JsonDateSerializer;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

public class BulkExportJobParameters extends BulkExportJobBase {

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

	public List<String> getResourceTypes() {
		return myResourceTypes;
	}

	public void setResourceTypes(List<String> theResourceTypes) {
		myResourceTypes = theResourceTypes;
	}

	public Date getStartDate() {
		return myStartDate;
	}

	public void setStartDate(Date theStartDate) {
		myStartDate = theStartDate;
	}

	public List<String> getFilters() {
		return myFilters;
	}

	public void setFilters(List<String> theFilters) {
		myFilters = theFilters;
	}

	public String getOutputFormat() {
		return myOutputFormat;
	}

	public void setOutputFormat(String theOutputFormat) {
		myOutputFormat = theOutputFormat;
	}

	public BulkDataExportOptions.ExportStyle getExportStyle() {
		return myExportStyle;
	}

	public void setExportStyle(BulkDataExportOptions.ExportStyle theExportStyle) {
		myExportStyle = theExportStyle;
	}

	public String getGroupId() {
		return myGroupId;
	}

	public void setGroupId(String theGroupId) {
		myGroupId = theGroupId;
	}

	public static BulkExportJobParameters createFromExportJobParameters(BulkExportParameters theParameters) {
		BulkExportJobParameters params = new BulkExportJobParameters();
		params.setResourceTypes(params.getResourceTypes());
		params.setExportStyle(theParameters.getExportStyle());
		params.setFilters(theParameters.getFilters());
		params.setGroupId(theParameters.getGroupId());
		params.setJobId(theParameters.getJobId());
		params.setOutputFormat(theParameters.getOutputFormat());
		params.setStartDate(theParameters.getStartDate());
		return params;
	}
}
