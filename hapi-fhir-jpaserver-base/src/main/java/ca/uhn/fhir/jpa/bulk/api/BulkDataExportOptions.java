package ca.uhn.fhir.jpa.bulk.api;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Date;
import java.util.Set;

public class BulkDataExportOptions {
	public BulkDataExportOptions() {

	}

	public enum ExportStyle {
		PATIENT,
		GROUP,
		SYSTEM
	}
	private String myOutputFormat;
	private Set<String> myResourceTypes;
	private Date mySince;
	private Set<String> myFilters;
	private ExportStyle myExportStyle;
	private boolean myExpandMdm;
	private IIdType myGroupId;



	public void setOutputFormat(String theOutputFormat) {
		myOutputFormat = theOutputFormat;
	}

	public void setResourceTypes(Set<String> theResourceTypes) {
		myResourceTypes = theResourceTypes;
	}

	public void setSince(Date theSince) {
		mySince = theSince;
	}

	public void setFilters(Set<String> theFilters) {
		myFilters = theFilters;
	}

	public ExportStyle getExportStyle() {
		return myExportStyle;
	}

	public void setExportStyle(ExportStyle theExportStyle) {
		myExportStyle = theExportStyle;
	}

	public String getOutputFormat() {
		return myOutputFormat;
	}

	public Set<String> getResourceTypes() {
		return myResourceTypes;
	}

	public Date getSince() {
		return mySince;
	}

	public Set<String> getFilters() {
		return myFilters;
	}

	public boolean isExpandMdm() {
		return myExpandMdm;
	}

	public void setExpandMdm(boolean theExpandMdm) {
		myExpandMdm = theExpandMdm;
	}

	public IIdType getGroupId() {
		return myGroupId;
	}

	public void setGroupId(IIdType theGroupId) {
		myGroupId = theGroupId;
	}
}
