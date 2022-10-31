package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static ca.uhn.fhir.jpa.provider.BaseJpaProvider.endRequest;
import static ca.uhn.fhir.jpa.provider.BaseJpaProvider.startRequest;

public class ProcessMessageProvider {

	@Autowired
	private IFhirSystemDao<?, ?> mySystemDao;

	/**
	 * $process-message
	 */
	@Description("Accept a FHIR Message Bundle for processing")
	@Operation(name = JpaConstants.OPERATION_PROCESS_MESSAGE, idempotent = false)
	public IBaseBundle processMessage(
		HttpServletRequest theServletRequest,
		RequestDetails theRequestDetails,

		@OperationParam(name = "content", min = 1, max = 1, typeName = "Bundle")
		@Description(shortDefinition = "The message to process (or, if using asynchronous messaging, it may be a response message to accept)")
		IBaseBundle theMessageToProcess
	) {

		startRequest(theServletRequest);
		try {
			return mySystemDao.processMessage(theRequestDetails, theMessageToProcess);
		} finally {
			endRequest(theServletRequest);
		}

	}

}
