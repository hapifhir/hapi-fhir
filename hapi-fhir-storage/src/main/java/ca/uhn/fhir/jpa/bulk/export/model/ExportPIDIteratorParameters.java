/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.bulk.export.model;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ExportPIDIteratorParameters {
	/**
	 * The primary resource type of interest
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
	private String myInstanceId;

	private String myChunkId;
	/**
	 * The export style
	 */
	private BulkExportJobParameters.ExportStyle myExportStyle;
	/**
	 * the group id
	 */
	private String myGroupId;
	/**
	 * For group export - whether or not to expand mdm
	 */
	private boolean myExpandMdm;
	/**
	 * The patient id
	 */
	private List<String> myPatientIds;
	/**
	 * The partition id
	 */
	private RequestPartitionId myPartitionId;

	/**
	 * The list of resource types to recurse on.
	 * This should always have at least one resource in it (the resource being requested)!
	 */
	private List<String> myRequestedResourceTypes;

	public String getChunkId() {
		return myChunkId;
	}

	public void setChunkId(String theChunkId) {
		myChunkId = theChunkId;
	}

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

	public String getInstanceId() {
		return myInstanceId;
	}

	public void setInstanceId(String theInstanceId) {
		myInstanceId = theInstanceId;
	}

	public BulkExportJobParameters.ExportStyle getExportStyle() {
		return myExportStyle;
	}

	public void setExportStyle(BulkExportJobParameters.ExportStyle theExportStyle) {
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

	public List<String> getPatientIds() {
		return myPatientIds;
	}

	public void setPatientIds(List<String> thePatientIds) {
		myPatientIds = thePatientIds;
	}

	public RequestPartitionId getPartitionIdOrAllPartitions() {
		if (myPartitionId != null) {
			return myPartitionId;
		} else {
			return RequestPartitionId.allPartitions();
		}
	}

	public void setPartitionId(RequestPartitionId thePartitionId) {
		myPartitionId = thePartitionId;
	}

	public List<String> getRequestedResourceTypes() {
		if (myRequestedResourceTypes == null) {
			myRequestedResourceTypes = new ArrayList<>();
			if (!isBlank(myResourceType)) {
				myRequestedResourceTypes.add(myResourceType);
			}
		}
		return myRequestedResourceTypes;
	}

	public void setRequestedResourceTypes(List<String> theRequestedResourceTypes) {
		myRequestedResourceTypes = theRequestedResourceTypes;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
