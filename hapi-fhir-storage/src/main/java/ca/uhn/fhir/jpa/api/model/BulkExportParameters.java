package ca.uhn.fhir.jpa.api.model;

import ca.uhn.fhir.jpa.api.model.RunJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;

public class BulkExportParameters extends RunJobParameters {

	private List<String> myResourceTypes;

	private Date myStartDate;

	private List<String> myFilters;

	private BulkDataExportOptions.ExportStyle myExportStyle;

	private String myGroupId;

	private String myOutputFormat;

	// This is the ID of the db resource that tracks
	// the bulkexportjob. See BulkExportJobEntity
	private String myJobId;

	public BulkExportParameters(@Nonnull String theJobDefinitionId) {
		super(theJobDefinitionId);
	}

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

	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
	}

	public String getOutputFormat() {
		return myOutputFormat;
	}

	public void setOutputFormat(String theOutputFormat) {
		myOutputFormat = theOutputFormat;
	}
}
