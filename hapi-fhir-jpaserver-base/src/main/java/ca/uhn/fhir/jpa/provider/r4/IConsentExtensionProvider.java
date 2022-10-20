package ca.uhn.fhir.jpa.provider.r4;

import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.Collections;

public interface IConsentExtensionProvider {

	default Collection<IBaseExtension> getConsentExtension(IBaseResource theConsentResource) {
		return Collections.emptyList();
	};
}
