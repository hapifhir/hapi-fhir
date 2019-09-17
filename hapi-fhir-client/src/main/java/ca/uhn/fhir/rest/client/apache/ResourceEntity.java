package ca.uhn.fhir.rest.client.apache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.nio.charset.UnsupportedCharsetException;

/**
 * Apache HttpClient request content entity where the body is a FHIR resource, that will
 * be encoded as JSON by default
 */
public class ResourceEntity extends StringEntity {

	public ResourceEntity(FhirContext theContext, IBaseResource theResource) throws UnsupportedCharsetException {
		super(theContext.newJsonParser().encodeResourceToString(theResource), ContentType.parse(Constants.CT_FHIR_JSON_NEW));
	}
}
