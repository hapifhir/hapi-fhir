package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProviderSelector {
	private final FhirContext myFhirContext;
	private final Map<Class<?>, FhirVersionEnum> myProviderMap;
	public ProviderSelector(FhirContext theFhirContext, Map<Class<?>, FhirVersionEnum> theProviderMap) {
		myFhirContext = theFhirContext;
		myProviderMap = theProviderMap;
	}
	public List<Class<?>> getProviderType() {
		List<Class<?>> providerList = new ArrayList<>();
		for (var entry: myProviderMap.entrySet()) {
			var prov = entry.getKey();
			var context = entry.getValue();
			// only load matching context providers
			if (context.isEquivalentTo(myFhirContext.getVersion().getVersion())){
				providerList.add(prov);
			}
		}

		return providerList;
	}
}
