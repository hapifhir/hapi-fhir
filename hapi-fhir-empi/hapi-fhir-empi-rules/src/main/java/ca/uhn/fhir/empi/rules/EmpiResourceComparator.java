package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.IEmpiComparator;
import ca.uhn.fhir.empi.rules.metric.EmpiResourceFieldComparator;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.List;

public class EmpiResourceComparator implements IEmpiComparator<IBaseResource> {
	private final EmpiRulesJson myEmpiRulesJson;
	private final List<EmpiResourceFieldComparator> myFieldComparators = new ArrayList<>();

	public EmpiResourceComparator(FhirContext theFhirContext, EmpiRulesJson theEmpiRulesJson) {
		myEmpiRulesJson = theEmpiRulesJson;
		for (EmpiMatchFieldJson matchFieldJson : myEmpiRulesJson) {
			myFieldComparators.add(new EmpiResourceFieldComparator(theFhirContext, matchFieldJson));
		}
	}

	@Override
	public double compare(IBaseResource theLeftResource, IBaseResource theRightResource) {
		long matchVector = getMatchVector(theLeftResource, theRightResource);
		return myEmpiRulesJson.getWeight(matchVector);
	}

	// FIXME KHS test
	private long getMatchVector(IBaseResource theLeftResource, IBaseResource theRightResource) {
		long retval = 0;
		for (int i = 0; i < myFieldComparators.size(); ++i) {
			EmpiResourceFieldComparator fieldComparator = myFieldComparators.get(i);
			if (fieldComparator.match(theLeftResource, theRightResource)) {
				retval |= (1 << i);
			}
		}
		return retval;
	}
}
