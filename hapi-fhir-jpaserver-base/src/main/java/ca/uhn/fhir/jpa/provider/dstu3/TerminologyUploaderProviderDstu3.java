package ca.uhn.fhir.jpa.provider.dstu3;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.jpa.provider.BaseJpaProvider;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc.UploadStatistics;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class TerminologyUploaderProviderDstu3 extends BaseJpaProvider {
	public static final String UPLOAD_EXTERNAL_CODE_SYSTEM = "$upload-external-code-system";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyUploaderProviderDstu3.class);

	@Autowired
	private IHapiTerminologyLoaderSvc myTerminologyLoaderSvc;
	
	//@formatter:off
	@Operation(name = UPLOAD_EXTERNAL_CODE_SYSTEM, idempotent = false, returnParameters= {
		@OperationParam(name="conceptCount", type=IntegerType.class, min=1)
	})
	public Parameters uploadExternalCodeSystem(
			HttpServletRequest theServletRequest,
			@OperationParam(name="url", min=1) UriType theUrl,
			@OperationParam(name="package", min=0) Attachment thePackage,
			@OperationParam(name="localfile", min=0, max=OperationParam.MAX_UNLIMITED) List<StringType> theLocalFile,
			RequestDetails theRequestDetails 
			) {
		//@formatter:on
		
		startRequest(theServletRequest);
		try {
			List<byte[]> data = new ArrayList<byte[]>();
			if (theLocalFile != null && theLocalFile.size() > 0) {
				for (StringType nextLocalFile : theLocalFile) {
					if (isNotBlank(nextLocalFile.getValue())) {
						ourLog.info("Reading in local file: {}", nextLocalFile.getValue());
						try {
							byte[] nextData = IOUtils.toByteArray(new FileInputStream(nextLocalFile.getValue()));
							data.add(nextData);
						} catch (IOException e) {
							throw new InternalErrorException(e);
						}
					}
				}
			} else if (thePackage == null || thePackage.getData() == null || thePackage.getData().length == 0) {
				throw new InvalidRequestException("No 'localfile' or 'package' parameter, or package had no data");
			} else {
				data = new ArrayList<byte[]>();
				data.add(thePackage.getData());
				thePackage.setData(null);
			}
			
			String url = theUrl != null ? theUrl.getValueAsString() : null;
			url = defaultString(url);

			UploadStatistics stats;
			if (IHapiTerminologyLoaderSvc.SCT_URL.equals(url)) {
				stats = myTerminologyLoaderSvc.loadSnomedCt((data), theRequestDetails);
			} else if (IHapiTerminologyLoaderSvc.LOINC_URL.equals(url)) {
					stats = myTerminologyLoaderSvc.loadLoinc((data), theRequestDetails);
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
