package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ResourceUtil {

	/**
	 * This method removes the narrative from the resource, or if the resource is a bundle, removes the narrative from
	 * all of the resources in the bundle
	 *
	 * @param theContext The fhir context
	 * @param theInput   The resource to remove the narrative from
	 */
	public static void removeNarrative(FhirContext theContext, IBaseResource theInput) {
		if (theInput instanceof IBaseBundle) {
			for (IBaseResource next : BundleUtil.toListOfResources(theContext, (IBaseBundle) theInput)) {
				removeNarrative(theContext, next);
			}
		}

		BaseRuntimeElementCompositeDefinition<?> element = theContext.getResourceDefinition(theInput.getClass());
		BaseRuntimeChildDefinition textElement = element.getChildByName("text");
		if (textElement != null) {
			textElement.getMutator().setValue(theInput, null);
		}
	}
}
