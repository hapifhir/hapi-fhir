package ca.uhn.fhir.jpa.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.util.FhirTerser;

public class FhirResourceDaoDstu1<T extends IResource> extends BaseFhirResourceDao<T> {

	protected List<Object> getIncludeValues(FhirTerser t, Include next, IResource nextResource, RuntimeResourceDefinition def) {
		List<Object> values;
		if ("*".equals(next.getValue())) {
			values = new ArrayList<Object>();
			values.addAll(t.getAllPopulatedChildElementsOfType(nextResource, BaseResourceReferenceDt.class));
		} else if (next.getValue().startsWith(def.getName() + ".")) {
			values = t.getValues(nextResource, next.getValue());
		} else {
			values = Collections.emptyList();
		}
		return values;
	}

	
}
