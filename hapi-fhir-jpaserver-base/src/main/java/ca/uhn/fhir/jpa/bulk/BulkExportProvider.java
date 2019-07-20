package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class BulkExportProvider {

	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;

	/**
	 * $binary-access-read
	 */
	@Operation(name = JpaConstants.OPERATION_EXPORT, global = true, manualResponse = true, idempotent = true)
	public void export(
		@OperationParam(name = "_outputFormat", min = 0, max = 1) StringType theOutputFormat,
		@OperationParam(name = "_type", min = 0, max = 1) StringType theType,
		@OperationParam(name = "_since", min = 0, max = 1) DateTimeType theDate,
		@OperationParam(name = "_typeFilter", min = 0, max = 1) StringType theTypeFilter,
		HttpServletRequest theServletRequest
	) {

		String preferHeader = theServletRequest.getHeader(Constants.HEADER_PREFER);
		PreferHeader prefer = RestfulServerUtils.parsePreferHeader(null, preferHeader);
		if (prefer.getRespondAsync() == false) {
			throw new InvalidRequestException("Must request async processing for $export");
		}

		myBulkDataExportSvc.
		return null;

	}


}
