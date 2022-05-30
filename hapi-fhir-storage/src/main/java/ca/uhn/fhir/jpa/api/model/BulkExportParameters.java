package ca.uhn.fhir.jpa.api.model;

import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BulkExportParameters extends StartNewJobParameters {

	/**
	 * List of resource types to export.
	 */
	private List<String> myResourceTypes;

	/**
	 * The earliest date from which to export resources.
	 */
	private Date myStartDate;

	/**
	 * Filters are used to narrow down the resources to export.
	 * Eg:
	 * Patient/123?group=a
	 * "group=a" is a filter
	 */
	private List<String> myFilters;

	/**
	 * Export style - Patient, Group or Everything
	 */
	private BulkDataExportOptions.ExportStyle myExportStyle;

	/**
	 * Group id
	 */
	private String myGroupId;

	/**
	 * Output format.
	 * Currently unsupported (all outputs are ndjson)
	 */
	private String myOutputFormat;

	/**
	 * The bulk export job id.
	 * Job ID is used to track export work/status
	 * (including where to find the resulting binaries)
	 */
	private String myJobId;

	/**
	 * For group export;
	 * whether or not to expand mdm
	 */
	private boolean myExpandMdm;

	public boolean isExpandMdm() {
		return myExpandMdm;
	}

	public void setExpandMdm(boolean theExpandMdm) {
		myExpandMdm = theExpandMdm;
	}

	public BulkExportParameters(@Nonnull String theJobDefinitionId,
										 boolean theStartImmediatelyBoolean) {
		super(theJobDefinitionId, theStartImmediatelyBoolean);
	}

	public BulkExportParameters(@Nonnull String theJobDefinitionId) {
		this(theJobDefinitionId, true);
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
		if (myFilters == null) {
			myFilters = new ArrayList<>();
		}
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
