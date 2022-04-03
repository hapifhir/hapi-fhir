package ca.uhn.fhir.jpa.bulk.export.api;

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

import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IBulkDataExportSvc {
	/**
	 * Deprecated - Use {@link #submitJob(BulkDataExportOptions, Boolean, RequestDetails)} instead
	 */
	@Deprecated
	JobInfo submitJob(BulkDataExportOptions theBulkDataExportOptions);

	JobInfo submitJob(BulkDataExportOptions theBulkDataExportOptions, Boolean useCache, RequestDetails theRequestDetails);

	JobInfo getJobInfoOrThrowResourceNotFound(String theJobId);

	/**
	 * Return a set of all resource types which contain search parameters which have Patient as a target.
	 */
	Set<String> getPatientCompartmentResources();

	class JobInfo {
		private String myJobId;
		private BulkExportJobStatusEnum myStatus;
		private List<FileEntry> myFiles;
		private String myRequest;
		private Date myStatusTime;
		private String myStatusMessage;

		public String getRequest() {
			return myRequest;
		}

		public void setRequest(String theRequest) {
			myRequest = theRequest;
		}

		public Date getStatusTime() {
			return myStatusTime;
		}

		public JobInfo setStatusTime(Date theStatusTime) {
			myStatusTime = theStatusTime;
			return this;
		}

		public String getJobId() {
			return myJobId;
		}

		public JobInfo setJobId(String theJobId) {
			myJobId = theJobId;
			return this;
		}

		public List<FileEntry> getFiles() {
			if (myFiles == null) {
				myFiles = new ArrayList<>();
			}
			return myFiles;

		}

		public BulkExportJobStatusEnum getStatus() {
			return myStatus;
		}

		public JobInfo setStatus(BulkExportJobStatusEnum theStatus) {
			myStatus = theStatus;
			return this;
		}

		public String getStatusMessage() {
			return myStatusMessage;
		}

		public JobInfo setStatusMessage(String theStatusMessage) {
			myStatusMessage = theStatusMessage;
			return this;
		}

		public FileEntry addFile() {
			FileEntry retVal = new FileEntry();
			getFiles().add(retVal);
			return retVal;
		}
	}


	class FileEntry {
		private String myResourceType;
		private IIdType myResourceId;

		public String getResourceType() {
			return myResourceType;
		}

		public FileEntry setResourceType(String theResourceType) {
			myResourceType = theResourceType;
			return this;
		}

		public IIdType getResourceId() {
			return myResourceId;
		}

		public FileEntry setResourceId(IIdType theResourceId) {
			myResourceId = theResourceId;
			return this;
		}
	}



}
