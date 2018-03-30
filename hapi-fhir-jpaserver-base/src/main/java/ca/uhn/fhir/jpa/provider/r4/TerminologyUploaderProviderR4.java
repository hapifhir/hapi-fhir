package ca.uhn.fhir.jpa.provider.r4;

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
