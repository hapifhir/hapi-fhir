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
		double total = 0.0;
		for (EmpiResourceFieldComparator fieldComparator : myFieldComparators) {
			total += fieldComparator.compare(theLeftResource, theRightResource);
		}
		return total;
	}
}
