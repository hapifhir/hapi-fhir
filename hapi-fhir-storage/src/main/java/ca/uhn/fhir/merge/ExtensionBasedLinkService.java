// Created by claude-sonnet-4-5
package ca.uhn.fhir.merge;

import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing resource merge links using FHIR extensions.
 *
 * This implementation uses HAPI FHIR standard extensions to track "replaces" and "replaced-by"
 * relationships between resources during merge operations. It works with any resource type
 * in a version-agnostic manner.
 *
 * Extension URLs:
 * - Replaces: http://hapifhir.io/fhir/StructureDefinition/replaces
 * - Replaced-by: http://hapifhir.io/fhir/StructureDefinition/replaced-by
 */
public class ExtensionBasedLinkService implements IResourceLinkService {

	private static final Logger ourLog = LoggerFactory.getLogger(ExtensionBasedLinkService.class);

	@Override
	public void addReplacesLink(IBaseResource theTarget, IBaseReference theSourceRef) {
		IBaseExtension<?, ?> extension = ExtensionUtil.addExtension(theTarget, HapiExtensions.EXTENSION_REPLACES);
		extension.setValue(theSourceRef);
	}

	@Override
	public void addReplacedByLink(IBaseResource theSource, IBaseReference theTargetRef) {
		IBaseExtension<?, ?> extension = ExtensionUtil.addExtension(theSource, HapiExtensions.EXTENSION_REPLACED_BY);
		extension.setValue(theTargetRef);
	}

	@Override
	public List<IBaseReference> getReplacesLinks(IBaseResource theResource) {
		return getLinksWithExtensionUrl(theResource, HapiExtensions.EXTENSION_REPLACES);
	}

	@Override
	public List<IBaseReference> getReplacedByLinks(IBaseResource theResource) {
		return getLinksWithExtensionUrl(theResource, HapiExtensions.EXTENSION_REPLACED_BY);
	}

	/**
	 * Helper method to extract Reference values from extensions with a specific URL.
	 *
	 * @param theResource the resource to check
	 * @param theExtensionUrl the extension URL to filter by
	 * @return list of Reference objects from matching extensions
	 */
	private List<IBaseReference> getLinksWithExtensionUrl(IBaseResource theResource, String theExtensionUrl) {
		List<IBaseReference> references = new ArrayList<>();

		List<IBaseExtension<?, ?>> extensions =
				ExtensionUtil.getExtensionsByUrl((IBaseHasExtensions) theResource, theExtensionUrl);

		for (IBaseExtension<?, ?> extension : extensions) {
			IBase value = extension.getValue();
			if (value instanceof IBaseReference) {
				references.add((IBaseReference) value);
			}
		}

		return references;
	}
}
