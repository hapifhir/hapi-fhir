package ca.uhn.fhir.narrative2;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.BundleUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

/**
 * An instance of this class is added to the Thymeleaf context as a variable with
 * name <code>"narrativeUtil"</code> and can be accessed from narrative templates.
 *
 * @since 7.0.0
 */
public class NarrativeGeneratorTemplateUtils {

	public static final NarrativeGeneratorTemplateUtils INSTANCE = new NarrativeGeneratorTemplateUtils();

	/**
	 * Given a Bundle as input, are any entries present with a given resource type
	 */
	public boolean bundleHasEntriesWithResourceType(IBaseBundle theBaseBundle, String theResourceType) {
		FhirContext ctx = theBaseBundle.getStructureFhirVersionEnum().newContextCached();
		List<Pair<String, IBaseResource>> entryResources = BundleUtil.getBundleEntryUrlsAndResources(ctx, theBaseBundle);
		return entryResources
			.stream()
			.map(t -> t.getValue())
			.filter(t -> t != null)
			.anyMatch(t -> ctx.getResourceType(t).equals(theResourceType));
	}

}
