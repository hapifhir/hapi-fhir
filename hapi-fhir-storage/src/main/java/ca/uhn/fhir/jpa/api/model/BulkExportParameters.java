/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.api.model;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.models.Batch2BaseJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BulkExportParameters extends Batch2BaseJobParameters {

	/**
	 * List of resource types to export.
	 */
	private List<String> myResourceTypes;

	/**
	 * The earliest date from which to export resources.
	 */
	private Date mySince;

	/**
	 * Filters are used to narrow down the resources to export.
	 * Eg:
	 * Patient/123?group=a
	 * "group=a" is a filter
	 */
	private List<String> myFilters;

	/**
	 * URLs to be applied by the inMemoryMatcher after the SQL select
	 */
	private List<String> myPostFetchFilterUrls;

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
	 * For group export;
	 * whether or not to expand mdm
	 */
	private boolean myExpandMdm;


	/**
	 * Patient id(s)
	 */
	private List<String> myPatientIds;

	/**
	 * The request which originated the request.
	 */
	private String myOriginalRequestUrl;
	private String myExportIdentifier;



	/**
	 * The partition for the request if applicable.
	 */
	private RequestPartitionId myPartitionId;

	public boolean isExpandMdm() {
		return myExpandMdm;
	}

	public void setExpandMdm(boolean theExpandMdm) {
		myExpandMdm = theExpandMdm;
	}

	public BulkExportParameters(@Nonnull String theJobDefinitionId) {
		super(theJobDefinitionId);
	}

	public List<String> getResourceTypes() {
		return myResourceTypes;
	}

	public void setExportIdentifier(String theExportIdentifier) {
		myExportIdentifier = theExportIdentifier;
	}
	public String getExportIdentifier() {
		return myExportIdentifier;
	}

	public void setResourceTypes(List<String> theResourceTypes) {
		myResourceTypes = theResourceTypes;
	}

	public Date getSince() {
		return mySince;
	}

	public void setSince(Date theSince) {
		mySince = theSince;
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

	public List<String> getPostFetchFilterUrls() {
		if (myPostFetchFilterUrls == null) {
			myPostFetchFilterUrls = new ArrayList<>();
		}
		return myPostFetchFilterUrls;
	}

	public void setPostFetchFilterUrls(List<String> thePostFetchFilterUrls) {
		myPostFetchFilterUrls = thePostFetchFilterUrls;
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

	public String getOutputFormat() {
		return myOutputFormat;
	}

	public void setOutputFormat(String theOutputFormat) {
		myOutputFormat = theOutputFormat;
	}

	public List<String> getPatientIds() {
		return myPatientIds;
	}

	public void setPatientIds(List<String> thePatientIds) {
		myPatientIds = thePatientIds;
	}

	public String getOriginalRequestUrl() {
		return myOriginalRequestUrl;
	}

	public void setOriginalRequestUrl(String theOriginalRequestUrl) {
		myOriginalRequestUrl = theOriginalRequestUrl;
	}

	public RequestPartitionId getPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(RequestPartitionId thePartitionId) {
		myPartitionId = thePartitionId;
	}
}
