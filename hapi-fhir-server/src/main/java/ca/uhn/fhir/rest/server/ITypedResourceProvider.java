package ca.uhn.fhir.rest.server;

import java.lang.reflect.ParameterizedType;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.server.IResourceProvider;

public interface ITypedResourceProvider<T extends IBase> extends IResourceProvider {

	@SuppressWarnings({ "unchecked" })
	@Override
	default Class<? extends IBaseResource> getResourceType() {
		ParameterizedType type = (ParameterizedType) getClass().getGenericInterfaces()[0];
		return (Class<? extends IBaseResource>) type.getActualTypeArguments()[0];
	}
}