package ca.uhn.fhir.jpa.provider;

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

import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc.UploadStatistics;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TerminologyUploaderProvider extends BaseJpaProvider {
	public static final String UPLOAD_EXTERNAL_CODE_SYSTEM = "$upload-external-code-system";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyUploaderProvider.class);

	@Autowired
	private IHapiTerminologyLoaderSvc myTerminologyLoaderSvc;
	
	@Operation(name = UPLOAD_EXTERNAL_CODE_SYSTEM, idempotent = false, returnParameters= {
		@OperationParam(name="conceptCount", type=IntegerType.class, min=1)
	})
	public Parameters uploadExternalCodeSystem(
			HttpServletRequest theServletRequest,
			@OperationParam(name="url", min=1) StringParam theCodeSystemUrl,
			@OperationParam(name="localfile", min=1, max=OperationParam.MAX_UNLIMITED) List<StringType> theLocalFile,
			RequestDetails theRequestDetails 
			) {

		startRequest(theServletRequest);
		try {
			List<IHapiTerminologyLoaderSvc.FileDescriptor> localFiles = new ArrayList<>();
			if (theLocalFile != null && theLocalFile.size() > 0) {
				for (StringType nextLocalFile : theLocalFile) {
					if (isNotBlank(nextLocalFile.getValue())) {
						ourLog.info("Reading in local file: {}", nextLocalFile.getValue());
						File nextFile = new File(nextLocalFile.getValue());
						if (!nextFile.exists() || nextFile.isFile()) {
							throw new InvalidRequestException("Unknown file: " +nextFile.getName());
						}
						localFiles.add(new IHapiTerminologyLoaderSvc.FileDescriptor() {
							@Override
							public String getFilename() {
								return nextFile.getAbsolutePath();
							}

							@Override
							public InputStream getInputStream() {
								try {
									return new FileInputStream(nextFile);
								} catch (FileNotFoundException theE) {
									throw new InternalErrorException(theE);
								}
							}
						});
					}
				}
			}
			
			String url = theCodeSystemUrl != null ? theCodeSystemUrl.getValue() : null;
			url = defaultString(url);

			UploadStatistics stats;
			if (IHapiTerminologyLoaderSvc.SCT_URI.equals(url)) {
				stats = myTerminologyLoaderSvc.loadSnomedCt(localFiles, theRequestDetails);
			} else if (IHapiTerminologyLoaderSvc.LOINC_URI.equals(url)) {
					stats = myTerminologyLoaderSvc.loadLoinc(localFiles, theRequestDetails);
			} else {
				throw new InvalidRequestException("Unknown URL: " + url);
			}
			
			Parameters retVal = new Parameters();
			retVal.addParameter().setName("conceptCount").setValue(new IntegerType(stats.getConceptCount()));
			return retVal;
		} finally {
			endRequest(theServletRequest);
		}
	}

	
}
