package ca.uhn.fhir.jpa.provider.dstu3;

/*-
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

import ca.uhn.fhir.jpa.provider.BaseTerminologyUploaderProvider;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class TerminologyUploaderProviderDstu3 extends BaseTerminologyUploaderProvider {

	@Operation(name = UPLOAD_EXTERNAL_CODE_SYSTEM, idempotent = false, returnParameters = {
		@OperationParam(name = "conceptCount", type = IntegerType.class, min = 1)
	})
	public Parameters uploadExternalCodeSystem(
		HttpServletRequest theServletRequest,
		@OperationParam(name = "url", min = 1) StringParam theCodeSystemUrl,
		@OperationParam(name = "localfile", min = 1, max = OperationParam.MAX_UNLIMITED) List<StringType> theLocalFile,
		@OperationParam(name = "package", min = 0, max = OperationParam.MAX_UNLIMITED) List<Attachment> thePackage,
		RequestDetails theRequestDetails
	) {
		try {
			List<org.hl7.fhir.r4.model.StringType> localFile = null;
			if (theLocalFile != null) {
				localFile = new ArrayList<>();
				for (StringType next : theLocalFile) {
					localFile.add(VersionConvertor_30_40.convertString(next));
				}
			}
			List<org.hl7.fhir.r4.model.Attachment> pkg = null;
			if (thePackage!=null){
				pkg = new ArrayList<>();
				for (Attachment next : thePackage) {
					pkg.add(VersionConvertor_30_40.convertAttachment(next));
				}
			}
			org.hl7.fhir.r4.model.Parameters retValR4 = handleUploadExternalCodeSystem(theServletRequest, theCodeSystemUrl, localFile, pkg, theRequestDetails);
			return VersionConvertor_30_40.convertParameters(retValR4);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
	}
}
