package ca.uhn.fhir.jpa.term.api;

/*
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

import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * This service handles bulk loading concepts into the terminology service concept tables
 * using any of several predefined input formats
 */
public interface ITermLoaderSvc {

	String IMGTHLA_URI = "http://www.ebi.ac.uk/ipd/imgt/hla";
	String LOINC_URI = "http://loinc.org";
	String SCT_URI = "http://snomed.info/sct";
	String ICD10CM_URI = "http://hl7.org/fhir/sid/icd-10-cm";
	String IEEE_11073_10101_URI = "urn:iso:std:iso:11073:10101";

	UploadStatistics loadImgthla(List<FileDescriptor> theFiles, RequestDetails theRequestDetails);

	UploadStatistics loadLoinc(List<FileDescriptor> theFiles, RequestDetails theRequestDetails);

	UploadStatistics loadSnomedCt(List<FileDescriptor> theFiles, RequestDetails theRequestDetails);

	UploadStatistics loadIcd10cm(List<FileDescriptor> theFiles, RequestDetails theRequestDetails);

	UploadStatistics loadCustom(String theSystem, List<FileDescriptor> theFiles, RequestDetails theRequestDetails);

	UploadStatistics loadDeltaAdd(String theSystem, List<FileDescriptor> theFiles, RequestDetails theRequestDetails);

	UploadStatistics loadDeltaRemove(String theSystem, List<FileDescriptor> theFiles, RequestDetails theRequestDetails);

	interface FileDescriptor {

		String getFilename();

		InputStream getInputStream();

	}

	class ByteArrayFileDescriptor implements FileDescriptor {

		private final String myNextUrl;
		private final byte[] myNextData;

		public ByteArrayFileDescriptor(String theNextUrl, byte[] theNextData) {
			myNextUrl = theNextUrl;
			myNextData = theNextData;
		}

		@Override
		public String getFilename() {
			return myNextUrl;
		}

		@Override
		public InputStream getInputStream() {
			return new ByteArrayInputStream(myNextData);
		}
	}

}
