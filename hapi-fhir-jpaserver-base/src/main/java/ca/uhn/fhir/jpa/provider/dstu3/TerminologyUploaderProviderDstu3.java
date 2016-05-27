package ca.uhn.fhir.jpa.provider.dstu3;

import static org.apache.commons.lang3.StringUtils.defaultString;

import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.UriType;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.jpa.provider.BaseJpaProvider;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc.UploadStatistics;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class TerminologyUploaderProviderDstu3 extends BaseJpaProvider {

	@Autowired
	private IHapiTerminologyLoaderSvc myTerminologyLoaderSvc;
	
	//@formatter:off
	@Operation(name = "$upload-external-code-system", idempotent = false, returnParameters= {
		@OperationParam(name="conceptCount", type=IntegerType.class, min=1)
	})
	public Parameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name="url", min=1) UriType theUrl,
			@OperationParam(name="package", min=1) Attachment thePackage,
			RequestDetails theRequestDetails 
			) {
		//@formatter:on
		
		startRequest(theServletRequest);
		try {
			if (thePackage == null || thePackage.getData() == null || thePackage.getData().length == 0) {
				throw new InvalidRequestException("Missing mandatory 'package' parameter, or package had no data");
			}
			
			byte[] data = thePackage.getData();
			String url = theUrl != null ? theUrl.getValueAsString() : null;
			url = defaultString(url);

			UploadStatistics stats;
			if (IHapiTerminologyLoaderSvc.SCT_URL.equals(url)) {
				stats = myTerminologyLoaderSvc.loadSnomedCt(data, theRequestDetails);
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
