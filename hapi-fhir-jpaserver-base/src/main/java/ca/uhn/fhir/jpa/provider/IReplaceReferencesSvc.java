package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;

/**
 * Contract for service which replaces references
 */
public interface IReplaceReferencesSvc {

	IBaseParameters replaceReferences(IIdType theCurrentTargetId, IIdType theNewTargetId, RequestDetails theRequest);
}
