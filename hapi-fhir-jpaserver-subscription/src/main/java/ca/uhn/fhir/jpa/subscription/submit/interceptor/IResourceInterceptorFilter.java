package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nullable;

public interface IResourceInterceptorFilter {
	boolean canSubmitResource(@Nullable IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequest);
}
