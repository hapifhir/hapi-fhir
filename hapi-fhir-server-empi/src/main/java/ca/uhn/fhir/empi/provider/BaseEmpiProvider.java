package ca.uhn.fhir.empi.provider;

import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public abstract class BaseEmpiProvider {

	protected IAnyResource getPersonFromId(String theId, String theParamName) {
		IdDt personId = new IdDt(theId);
		if (!"Person".equals(personId.getResourceType()) ||
			personId.getIdPart() == null) {
			throw new InvalidRequestException(theParamName + " must have form Person/<id> where <id> is the id of the person");
		}
		return loadPerson(personId);
	}

	protected abstract IAnyResource loadPerson(IdDt thePersonId);

	protected void validateMergeParameters(IPrimitiveType<String> thePersonIdToDelete, IPrimitiveType<String> thePersonIdToKeep) {
		if (thePersonIdToDelete == null) {
			throw new InvalidRequestException("personToDelete cannot be null");
		}
		if (thePersonIdToKeep == null) {
			throw new InvalidRequestException("personToKeep cannot be null");
		}
		if (thePersonIdToDelete.getValue().equals(thePersonIdToKeep.getValue())) {
			throw new InvalidRequestException("personIdToDelete must be different from personToKeep");
		}
 	}
	protected EmpiTransactionContext createEmpiContext(RequestDetails theRequestDetails) {
		TransactionLogMessages transactionLogMessages = TransactionLogMessages.createFromTransactionGuid(theRequestDetails.getTransactionGuid());
		return new EmpiTransactionContext(transactionLogMessages, EmpiTransactionContext.OperationType.MERGE_PERSONS);
	}
}
