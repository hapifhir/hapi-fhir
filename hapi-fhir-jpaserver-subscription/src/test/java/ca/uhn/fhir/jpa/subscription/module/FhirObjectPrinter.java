package ca.uhn.fhir.jpa.subscription.module;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.function.Function;

public class FhirObjectPrinter implements Function<Object, String> {
	@Override
	public String apply(Object object) {
		if (object instanceof IBaseResource) {
			IBaseResource resource = (IBaseResource) object;
			return resource.getClass().getSimpleName() + " { " + resource.getIdElement().getValue() + " }";
		} else {
			return object.toString();
		}
	}
}
