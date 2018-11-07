package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.ISearchParamRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class SearchParamProvider {
	@Autowired
	ISearchParamRegistry mySearchParamRegistry;

	public RuntimeSearchParam getSearchParamByName(RuntimeResourceDefinition theResourceDef, String theParamName) {
		Map<String, RuntimeSearchParam> params = mySearchParamRegistry.getActiveSearchParams(theResourceDef.getName());
		return params.get(theParamName);
	}

	public Collection<RuntimeSearchParam> getSearchParamsByResourceType(RuntimeResourceDefinition theResourceDef) {
		return mySearchParamRegistry.getActiveSearchParams(theResourceDef.getName()).values();
	}


}
