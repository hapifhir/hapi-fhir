/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.api.server.bulk;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.BaseBatchJobParameters;
import ca.uhn.fhir.rest.server.util.JsonDateDeserializer;
import ca.uhn.fhir.rest.server.util.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class BulkExportJobParameters extends BaseBatchJobParameters {

	/**
	 * List of resource types to export.
	 */
	@JsonProperty("resourceTypes")
	private List<String> myResourceTypes;

	/**
	 * The start date from when we should start
	 * doing the export. (end date is assumed to be "now")
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@JsonProperty("since")
	private Date mySince;

	@JsonProperty("exportId")
	private String myExportId;

	/**
	 * Filters are used to narrow down the resources to export.
	 * Eg:
	 * Patient/123?group=a
	 * "group=a" is a filter
	 */
	@JsonProperty("filters")
	private List<String> myFilters;

	/**
	 * URLs to be applied by the inMemoryMatcher after the SQL select
	 */
	@JsonProperty("postFetchFilterUrls")
	private List<String> myPostFetchFilterUrls;

	/**
	 * Output format.
	 * Currently unsupported (all outputs are ndjson)
	 */
	@JsonProperty("outputFormat")
	private String myOutputFormat;

	/**
	 * Export style - Patient, Group or Everything
	 */
	@JsonProperty("exportStyle")
	private ExportStyle myExportStyle;

	/**
	 * Patient id(s)
	 */
	@JsonProperty("patientIds")
	private List<String> myPatientIds = new ArrayList<>();

	/**
	 * The request which originated the request.
	 */
	@JsonProperty("originalRequestUrl")
	private String myOriginalRequestUrl;

	/**
	 * The group id
	 */
	@JsonProperty("groupId")
	private String myGroupId;

	/**
	 * For group export;
	 * whether or not to expand mdm
	 */
	@JsonProperty("expandMdm")
	private boolean myExpandMdm;

	/**
	 * The partition for the request if applicable.
	 */
	@JsonProperty("partitionId")
	private RequestPartitionId myPartitionId;

	@JsonProperty("binarySecurityContextIdentifierSystem")
	private String myBinarySecurityContextIdentifierSystem;

	@JsonProperty("binarySecurityContextIdentifierValue")
	private String myBinarySecurityContextIdentifierValue;

	public String getExportIdentifier() {
		return myExportId;
	}

	public void setExportIdentifier(String theExportId) {
		myExportId = theExportId;
	}

	public List<String> getResourceTypes() {
		if (myResourceTypes == null) {
			myResourceTypes = new ArrayList<>();
		}
		return myResourceTypes;
	}

	public void setResourceTypes(Collection<String> theResourceTypes) {
		getResourceTypes().clear();
		if (theResourceTypes != null) {
			getResourceTypes().addAll(theResourceTypes);
		}
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

	public void setFilters(Collection<String> theFilters) {
		getFilters().clear();
		if (theFilters != null) {
			getFilters().addAll(theFilters);
		}
	}

	public List<String> getPostFetchFilterUrls() {
		if (myPostFetchFilterUrls == null) {
			myPostFetchFilterUrls = new ArrayList<>();
		}
		return myPostFetchFilterUrls;
	}

	public void setPostFetchFilterUrls(Collection<String> thePostFetchFilterUrls) {
		getPostFetchFilterUrls().clear();
		if (thePostFetchFilterUrls != null) {
			getPostFetchFilterUrls().addAll(thePostFetchFilterUrls);
		}
	}

	public String getOutputFormat() {
		return myOutputFormat;
	}

	public void setOutputFormat(String theOutputFormat) {
		myOutputFormat = theOutputFormat;
	}

	public ExportStyle getExportStyle() {
		return myExportStyle;
	}

	public void setExportStyle(ExportStyle theExportStyle) {
		myExportStyle = theExportStyle;
	}

	public List<String> getPatientIds() {
		if (myPatientIds == null) {
			myPatientIds = new ArrayList<>();
		}
		return myPatientIds;
	}

	public void setPatientIds(Collection<String> thePatientIds) {
		getPatientIds().clear();
		if (thePatientIds != null) {
			getPatientIds().addAll(thePatientIds);
		}
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

	public String getOriginalRequestUrl() {
		return myOriginalRequestUrl;
	}

	public void setOriginalRequestUrl(String theOriginalRequestUrl) {
		this.myOriginalRequestUrl = theOriginalRequestUrl;
	}

	public RequestPartitionId getPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(RequestPartitionId thePartitionId) {
		this.myPartitionId = thePartitionId;
	}

	/**
	 * Sets a value to place in the generated Binary resource's
	 * Binary.securityContext.identifier
	 */
	public void setBinarySecurityContextIdentifierSystem(String theBinarySecurityContextIdentifierSystem) {
		myBinarySecurityContextIdentifierSystem = theBinarySecurityContextIdentifierSystem;
	}

	/**
	 * Sets a value to place in the generated Binary resource's
	 * Binary.securityContext.identifier
	 */
	public String getBinarySecurityContextIdentifierSystem() {
		return myBinarySecurityContextIdentifierSystem;
	}

	/**
	 * Sets a value to place in the generated Binary resource's
	 * Binary.securityContext.identifier
	 */
	public void setBinarySecurityContextIdentifierValue(String theBinarySecurityContextIdentifierValue) {
		myBinarySecurityContextIdentifierValue = theBinarySecurityContextIdentifierValue;
	}

	/**
	 * Sets a value to place in the generated Binary resource's
	 * Binary.securityContext.identifier
	 */
	public String getBinarySecurityContextIdentifierValue() {
		return myBinarySecurityContextIdentifierValue;
	}

	public enum ExportStyle {
		PATIENT,
		GROUP,
		SYSTEM
	}
}
