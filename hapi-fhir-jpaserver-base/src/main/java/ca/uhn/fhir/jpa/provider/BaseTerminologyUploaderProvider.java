package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;

public abstract class BaseTerminologyUploaderProvider extends BaseJpaProvider {
	public static final String UPLOAD_EXTERNAL_CODE_SYSTEM = "$upload-external-code-system";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseTerminologyUploaderProvider.class);
	public static final String CONCEPT_COUNT = "conceptCount";
	public static final String TARGET = "target";

	@Autowired
	private IHapiTerminologyLoaderSvc myTerminologyLoaderSvc;

	protected Parameters handleUploadExternalCodeSystem(
		HttpServletRequest theServletRequest,
		StringParam theCodeSystemUrl,
		List<StringType> theLocalFile,
		List<Attachment> thePackage, RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);

		if (theLocalFile == null || theLocalFile.size() == 0) {
			if (thePackage == null || thePackage.size() == 0) {
				throw new InvalidRequestException("No 'localfile' or 'package' parameter, or package had no data");
			}
		}

		try {
			List<IHapiTerminologyLoaderSvc.FileDescriptor> localFiles = new ArrayList<>();
			if (theLocalFile != null && theLocalFile.size() > 0) {
				for (StringType nextLocalFile : theLocalFile) {
					if (isNotBlank(nextLocalFile.getValue())) {
						ourLog.info("Reading in local file: {}", nextLocalFile.getValue());
						File nextFile = new File(nextLocalFile.getValue());
						if (!nextFile.exists() || !nextFile.isFile()) {
							throw new InvalidRequestException("Unknown file: " + nextFile.getName());
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

			if (thePackage != null) {
				for (Attachment nextPackage : thePackage) {
					if (isBlank(nextPackage.getUrl())) {
						throw new UnprocessableEntityException("Package is missing mandatory url element");
					}

					localFiles.add(new IHapiTerminologyLoaderSvc.FileDescriptor() {
						@Override
						public String getFilename() {
							return nextPackage.getUrl();
						}

						@Override
						public InputStream getInputStream() {
							return new ByteArrayInputStream(nextPackage.getData());
						}
					});
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
			retVal.addParameter().setName(CONCEPT_COUNT).setValue(new IntegerType(stats.getConceptCount()));
			retVal.addParameter().setName(TARGET).setValue(new Reference(stats.getTarget().getValue()));
			return retVal;
		} finally {
			endRequest(theServletRequest);
		}
	}


}
