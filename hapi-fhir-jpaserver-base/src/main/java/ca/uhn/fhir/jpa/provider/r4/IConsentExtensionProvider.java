package ca.uhn.fhir.jpa.provider.r4;

import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.Collections;

public interface IConsentExtensionProvider {

	/**
	 * Takes a Consent resource and returns a collection of Extensions that will
	 * be added to the base resource.
	 *
	 * @param theConsentResource - the consent resource
	 * @return - a collection of resources (or an empty collection if none).
	 */
	default Collection<IBaseExtension> getConsentExtension(IBaseResource theConsentResource) {
		return Collections.emptyList();
	};
}
