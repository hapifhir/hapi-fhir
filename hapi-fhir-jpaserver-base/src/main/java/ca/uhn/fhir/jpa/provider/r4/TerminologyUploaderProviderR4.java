package ca.uhn.fhir.jpa.provider.r4;

/*-
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

import ca.uhn.fhir.jpa.provider.BaseTerminologyUploaderProvider;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class TerminologyUploaderProviderR4 extends BaseTerminologyUploaderProvider {

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
		return handleUploadExternalCodeSystem(theServletRequest, theCodeSystemUrl, theLocalFile, thePackage, theRequestDetails);
	}
}
