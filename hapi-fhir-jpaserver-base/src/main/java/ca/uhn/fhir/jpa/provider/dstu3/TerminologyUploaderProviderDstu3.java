package ca.uhn.fhir.jpa.provider.dstu3;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.LockableFileWriter;
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
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyUploaderProviderDstu3.class);

	@Autowired
	private IHapiTerminologyLoaderSvc myTerminologyLoaderSvc;
	
	//@formatter:off
	@Operation(name = "$upload-external-code-system", idempotent = false, returnParameters= {
		@OperationParam(name="conceptCount", type=IntegerType.class, min=1)
	})
	public Parameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name="url", min=1) UriType theUrl,
			@OperationParam(name="package", min=0) Attachment thePackage,
			@OperationParam(name="localfile", min=0) StringType theLocalFile,
			RequestDetails theRequestDetails 
			) {
		//@formatter:on
		
		startRequest(theServletRequest);
		try {
			byte[] data;
			if (theLocalFile != null && isNotBlank(theLocalFile.getValue())) {
				ourLog.info("Reading in local file: {}", theLocalFile.getValue());
				try {
					data = IOUtils.toByteArray(new FileInputStream(theLocalFile.getValue()));
				} catch (IOException e) {
					throw new InternalErrorException(e);
				}
			} else if (thePackage == null || thePackage.getData() == null || thePackage.getData().length == 0) {
				throw new InvalidRequestException("No 'localfile' or 'package' parameter, or package had no data");
			} else {
				data = thePackage.getData();
			}
			
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
