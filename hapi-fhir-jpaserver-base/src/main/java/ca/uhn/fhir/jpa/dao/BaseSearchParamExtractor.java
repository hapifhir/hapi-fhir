package ca.uhn.fhir.jpa.dao;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.util.FhirTerser;

public class BaseSearchParamExtractor {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseSearchParamExtractor.class);
	private FhirContext myContext;
	
	public BaseSearchParamExtractor(FhirContext theContext) {
		myContext = theContext;
	}

	protected FhirContext getContext() {
		return myContext;
	}

	protected List<Object> extractValues(String thePaths, IResource theResource) {
		List<Object> values = new ArrayList<Object>();
		String[] nextPathsSplit = thePaths.split("\\|");
		FhirTerser t = myContext.newTerser();
		for (String nextPath : nextPathsSplit) {
			String nextPathTrimmed = nextPath.trim();
			try {
				values.addAll(t.getValues(theResource, nextPathTrimmed));
			} catch (Exception e) {
				RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
				ourLog.warn("Failed to index values from path[{}] in resource type[{}]: ", nextPathTrimmed, def.getName(), e.toString());
			}
		}
		return values;
	}

	
}
