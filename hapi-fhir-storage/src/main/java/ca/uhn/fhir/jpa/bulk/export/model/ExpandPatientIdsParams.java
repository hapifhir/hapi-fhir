package ca.uhn.fhir.jpa.bulk.export.model;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;

import java.util.Date;
import java.util.List;

public class ExpandPatientIdsParams {
	/**
	 * The Export style
	 */
	private final BulkExportJobParameters.ExportStyle myExportStyle;
	/**
	 * The group id, if available
	 */
	private String myGroupId;
	/**
	 * Patient ids, if available
	 */
	private List<String> myPatientIds;
	/**
	 * Desired partition or all partitions.
	 */
	private RequestPartitionId myRequestPartitionId;

	/**
	 * List of filters to apply
	 */
	private List<String> myFilters;

	/**
	 * The earliest date from which to retrieve records
	 */
	private Date myStartDate;

	/**
	 * The latest date to which to retrieve records
	 */
	private Date myEndDate;

	/**
	 * Whether or not to do mdm expansion
	 */
	private boolean myShouldDoMdmExpansion;

	public ExpandPatientIdsParams(BulkExportJobParameters.ExportStyle theExportStyle) {
		myExportStyle = theExportStyle;
	}

	public BulkExportJobParameters.ExportStyle getExportStyle() {
		return myExportStyle;
	}

	public String getGroupId() {
		return myGroupId;
	}

	public void setGroupId(String theGroupId) {
		myGroupId = theGroupId;
	}

	public List<String> getPatientIds() {
		return myPatientIds;
	}

	public void setPatientIds(List<String> thePatientIds) {
		myPatientIds = thePatientIds;
	}

	public RequestPartitionId getRequestPartitionId() {
		if (myRequestPartitionId != null) {
			return myRequestPartitionId;
		} else {
			return RequestPartitionId.allPartitions();
		}
	}

	public void setRequestPartitionId(RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
	}

	public boolean isShouldDoMdmExpansion() {
		return myShouldDoMdmExpansion;
	}

	public void setShouldDoMdmExpansion(boolean theShouldDoMdmExpansion) {
		myShouldDoMdmExpansion = theShouldDoMdmExpansion;
	}

	public List<String> getFilters() {
		return myFilters;
	}

	public void setFilters(List<String> theFilters) {
		myFilters = theFilters;
	}

	public Date getStartDate() {
		return myStartDate;
	}

	public void setStartDate(Date theStartDate) {
		myStartDate = theStartDate;
	}

	public Date getEndDate() {
		return myEndDate;
	}

	public void setEndDate(Date theEndDate) {
		myEndDate = theEndDate;
	}
}
