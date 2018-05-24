package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;

import java.io.InputStream;
import java.util.List;

public interface IHapiTerminologyLoaderSvc {

	String LOINC_URI = "http://loinc.org";
	String SCT_URI = "http://snomed.info/sct";
	String IEEE_11073_10101_URI = "urn:iso:std:iso:11073:10101";

	UploadStatistics loadLoinc(List<FileDescriptor> theFiles, RequestDetails theRequestDetails);

	UploadStatistics loadSnomedCt(List<FileDescriptor> theFiles, RequestDetails theRequestDetails);

	interface FileDescriptor {

		String getFilename();

		InputStream getInputStream();

	}

	class UploadStatistics {
		private final int myConceptCount;
		private final IIdType myTarget;

		public UploadStatistics(int theConceptCount, IIdType theTarget) {
			myConceptCount = theConceptCount;
			myTarget = theTarget;
		}

		public int getConceptCount() {
			return myConceptCount;
		}

		public IIdType getTarget() {
			return myTarget;
		}

	}

}
