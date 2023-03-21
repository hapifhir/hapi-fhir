/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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

	@JsonProperty("patientIds")
	private List<String> myPatientIds;

	@JsonProperty("originalRequestUrl")
	private String myOriginalRequestUrl;

	/**
	 * The group id
	 */
	@JsonProperty("groupId")
	private String myGroupId;

	@JsonProperty("expandMdm")
	private boolean myExpandMdm;

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

	public List<String> getPatientIds() {
		return myPatientIds;
	}

	public void setPatientIds(List<String> thePatientIds) {
		myPatientIds = thePatientIds;
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

	private void setOriginalRequestUrl(String theOriginalRequestUrl) {
		this.myOriginalRequestUrl = theOriginalRequestUrl;
	}

	public String getOriginalRequestUrl() {
		return myOriginalRequestUrl;
	}

	public static BulkExportJobParameters createFromExportJobParameters(BulkExportParameters theParameters) {
		BulkExportJobParameters params = new BulkExportJobParameters();
		params.setResourceTypes(theParameters.getResourceTypes());
		params.setExportStyle(theParameters.getExportStyle());
		params.setFilters(theParameters.getFilters());
		params.setGroupId(theParameters.getGroupId());
		params.setOutputFormat(theParameters.getOutputFormat());
		params.setStartDate(theParameters.getStartDate());
		params.setExpandMdm(theParameters.isExpandMdm());
		params.setPatientIds(theParameters.getPatientIds());
		params.setOriginalRequestUrl(theParameters.getOriginalRequestUrl());
		return params;
	}

}
