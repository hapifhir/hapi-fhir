/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.api.server.bulk;

import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

// TODO: JA in next ticket - We have 3 more or less identical classes representing
// bulk data job parameters: BulkDataExportOptions, BulkExportJobParameters, and BulkExportParameters
// They don't seem to serve any distinct purpose so they should be collapsed into 1
public class BulkDataExportOptions {


	public enum ExportStyle {
		PATIENT,
		GROUP,
		SYSTEM
	}

	private String myOutputFormat;
	private Set<String> myResourceTypes;
	private Date mySince;
	private Set<String> myFilters;
	private Set<String> myPostFetchFilterUrls;
	private ExportStyle myExportStyle;
	private boolean myExpandMdm;
	private IIdType myGroupId;
	private Set<IIdType> myPatientIds;

	private String myExportIdentifier;

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

	@Nonnull
	public Set<String> getResourceTypes() {
		if (myResourceTypes == null) {
			myResourceTypes = Set.of();
		}
		return myResourceTypes;
	}

	public Date getSince() {
		return mySince;
	}

	@Nonnull
	public Set<String> getFilters() {
		if (myFilters == null) {
			myFilters = Set.of();
		}
		return myFilters;
	}

	@Nonnull
	public Set<String> getPostFetchFilterUrls() {
		if (myPostFetchFilterUrls == null) {
			myPostFetchFilterUrls = Set.of();
		}
		return myPostFetchFilterUrls;
	}

	public void setPostFetchFilterUrls(Set<String> thePostFetchFilterUrls) {
		myPostFetchFilterUrls = thePostFetchFilterUrls;
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

	public Set<IIdType> getPatientIds() {
		return myPatientIds;
	}

	public void setPatientIds(Set<IIdType> thePatientIds) {
		myPatientIds = thePatientIds;
	}

	public String getExportIdentifier() {
		return myExportIdentifier;
	}

	public void setExportIdentifier(String theExportIdentifier) {
		myExportIdentifier = theExportIdentifier;
	}
}
