package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchTestUtil {

	private SearchTestUtil() {
	}

	public static List<IIdType> toUnqualifiedVersionlessIds(IBaseBundle theFound) {
		FhirContext ctx = FhirContext.forCached(theFound.getStructureFhirVersionEnum());

		List<IIdType> retVal = new ArrayList<>();
		for (IBaseResource next : BundleUtil.toListOfResources(ctx, theFound)) {
			if (next != null) {
				retVal.add(next.getIdElement().toUnqualifiedVersionless());
			}
		}
		return retVal;
	}

	public static List<String> toUnqualifiedVersionlessIdValues(IBaseBundle theFound) {
		return toUnqualifiedVersionlessIds(theFound)
			.stream()
			.map(t -> t.getValue())
			.collect(Collectors.toList());
	}

}
