package ca.uhn.fhir.jpa.bulk.export.model;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import java.util.Date;
import java.util.List;

public class ExportPIDIteratorParameters {
	/**
	 * Resource type
	 */
	private String myResourceType;

	/**
	 * The earliest date from which to retrieve records
	 */
	private Date myStartDate;

	/**
	 * List of filters to be applied to the search.
	 * Eg:
	 * Patient/123?group=1
	 * "group=1" would be the filter
	 */
	private List<String> myFilters;

	/**
	 * The ID of the BatchJob.
	 * (Batch jobs are stored in Persistence, to keep track
	 * of results/status).
	 */
	private String myJobId;

	/**
	 * The export style
	 */
	private BulkDataExportOptions.ExportStyle myExportStyle;

	/**
	 * the group id
	 */
	private String myGroupId;

	/**
	 * For group export - whether or not to expand mdm
	 */
	private boolean myExpandMdm;

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
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

	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
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

	public boolean isExpandMdm() {
		return myExpandMdm;
	}

	public void setExpandMdm(boolean theExpandMdm) {
		myExpandMdm = theExpandMdm;
	}

}
