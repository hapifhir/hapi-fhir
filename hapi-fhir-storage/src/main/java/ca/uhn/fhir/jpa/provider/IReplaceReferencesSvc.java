package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

/**
 * Contract for service which replaces references
 */
public interface IReplaceReferencesSvc {

	IBaseParameters replaceReferences(String theSourceRefId, String theTargetRefId, RequestDetails theRequest);

	List<? extends IBaseResource> findReferencingResourceIds(IIdType theSourceRefIdParam, RequestDetails theRequest);
}
