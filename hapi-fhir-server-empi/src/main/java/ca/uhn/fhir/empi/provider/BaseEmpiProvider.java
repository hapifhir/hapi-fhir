package ca.uhn.fhir.empi.provider;

import ca.uhn.fhir.model.primitive.IdDt;
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
}
