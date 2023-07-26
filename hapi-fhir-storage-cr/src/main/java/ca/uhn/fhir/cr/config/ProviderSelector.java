package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProviderSelector {
	private final FhirContext myFhirContext;
	private final Map<FhirVersionEnum, List<Class<?>>> myProviderMap;
	public ProviderSelector(FhirContext theFhirContext, Map<FhirVersionEnum, List<Class<?>>> theProviderMap) {
		myFhirContext = theFhirContext;
		myProviderMap = theProviderMap;
	}
	public List<Class<?>> getProviderType() {
		return myProviderMap.get(myFhirContext.getVersion().getVersion());
	}
}
