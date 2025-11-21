// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
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
public class ExtensionBasedLinkService {

	private static final Logger ourLog = LoggerFactory.getLogger(ExtensionBasedLinkService.class);

	private final FhirContext myFhirContext;

	public ExtensionBasedLinkService(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	/**
	 * Add a "replaces" link from target to source.
	 * Creates an extension with URL http://hapifhir.io/fhir/StructureDefinition/replaces
	 * and a Reference value pointing to the source resource.
	 *
	 * @param theTarget the resource that replaces another
	 * @param theSourceRef reference to the resource being replaced
	 */
	public void addReplacesLink(IBaseResource theTarget, IBaseReference theSourceRef) {
		ourLog.debug("Adding replaces link to resource {} for source {}",
			theTarget.getIdElement().getValue(), theSourceRef.getReferenceElement().getValue());

		ExtensionUtil.addExtension(
			myFhirContext,
			theTarget,
			HapiExtensions.EXTENSION_REPLACES,
			"Reference",
			theSourceRef
		);
	}

	/**
	 * Add a "replaced-by" link from source to target.
	 * Creates an extension with URL http://hapifhir.io/fhir/StructureDefinition/replaced-by
	 * and a Reference value pointing to the target resource.
	 *
	 * @param theSource the resource that was replaced
	 * @param theTargetRef reference to the replacement resource
	 */
	public void addReplacedByLink(IBaseResource theSource, IBaseReference theTargetRef) {
		ourLog.debug("Adding replaced-by link to resource {} for target {}",
			theSource.getIdElement().getValue(), theTargetRef.getReferenceElement().getValue());

		ExtensionUtil.addExtension(
			myFhirContext,
			theSource,
			HapiExtensions.EXTENSION_REPLACED_BY,
			"Reference",
			theTargetRef
		);
	}

	/**
	 * Get all "replaces" links from a resource.
	 *
	 * @param theResource the resource to check
	 * @return list of references to resources that this resource replaces
	 */
	public List<IBaseReference> getReplacesLinks(IBaseResource theResource) {
		return getLinksWithExtensionUrl(theResource, HapiExtensions.EXTENSION_REPLACES);
	}

	/**
	 * Get all "replaced-by" links from a resource.
	 *
	 * @param theResource the resource to check
	 * @return list of references to resources that replaced this resource
	 */
	public List<IBaseReference> getReplacedByLinks(IBaseResource theResource) {
		return getLinksWithExtensionUrl(theResource, HapiExtensions.EXTENSION_REPLACED_BY);
	}

	/**
	 * Check if resource has any "replaced-by" links.
	 * Used to determine if resource was already merged.
	 *
	 * @param theResource the resource to check
	 * @return true if resource has been replaced
	 */
	public boolean hasReplacedByLink(IBaseResource theResource) {
		List<IBaseReference> links = getReplacedByLinks(theResource);
		return !links.isEmpty();
	}

	/**
	 * Check if resource has a specific "replaces" link to target.
	 * Used to validate result resource has correct link to source.
	 * Compares IDs without version for flexibility.
	 *
	 * @param theResource the resource to check
	 * @param theTargetId the target resource ID to look for
	 * @return true if resource has a replaces link to the target
	 */
	public boolean hasReplacesLinkTo(IBaseResource theResource, IIdType theTargetId) {
		List<IBaseReference> replacesLinks = getReplacesLinks(theResource);

		String targetIdValue = theTargetId.toUnqualifiedVersionless().getValue();

		for (IBaseReference link : replacesLinks) {
			IIdType linkRefElement = link.getReferenceElement();
			String linkIdValue = linkRefElement.toUnqualifiedVersionless().getValue();

			if (targetIdValue.equals(linkIdValue)) {
				return true;
			}
		}

		return false;
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

		List<IBaseExtension<?, ?>> extensions = ExtensionUtil.getExtensionsByUrl(
			(IBase) theResource,
			theExtensionUrl
		);

		for (IBaseExtension<?, ?> extension : extensions) {
			IBase value = extension.getValue();
			if (value instanceof IBaseReference) {
				references.add((IBaseReference) value);
			} else {
				ourLog.warn("Extension {} has non-Reference value type: {}",
					theExtensionUrl, value != null ? value.getClass().getName() : "null");
			}
		}

		return references;
	}
}
