package ca.uhn.fhir.jpa.bulk;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.hl7.fhir.instance.model.api.IIdType;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IBulkDataExportSvc {
	void buildExportFiles();

	@Transactional(value = Transactional.TxType.NEVER)
	void purgeExpiredFiles();

	JobInfo submitJob(String theOutputFormat, Set<String> theResourceTypes, Date theSince, Set<String> theFilters);

	JobInfo getJobStatusOrThrowResourceNotFound(String theJobId);

	void cancelAndPurgeAllJobs();

	class JobInfo {
		private String myJobId;
		private BulkJobStatusEnum myStatus;
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

		public BulkJobStatusEnum getStatus() {
			return myStatus;
		}

		public JobInfo setStatus(BulkJobStatusEnum theStatus) {
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
