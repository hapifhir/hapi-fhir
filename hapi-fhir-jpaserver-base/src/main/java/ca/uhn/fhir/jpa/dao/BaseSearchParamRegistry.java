package ca.uhn.fhir.jpa.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;

public abstract class BaseSearchParamRegistry implements ISearchParamRegistry {

	private Map<String, Map<String, RuntimeSearchParam>> myBuiltInSearchParams;

	@Autowired
	private FhirContext myCtx;

	@Autowired
	private Collection<IFhirResourceDao<?>> myDaos;

	public BaseSearchParamRegistry() {
		super();
	}

	@Override
	public void forceRefresh() {
		// nothing by default
	}

	public Map<String, Map<String, RuntimeSearchParam>> getBuiltInSearchParams() {
		return myBuiltInSearchParams;
	}

	@Override
	public Map<String,RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
		Validate.notBlank(theResourceName, "theResourceName must not be blank or null");

		return myBuiltInSearchParams.get(theResourceName);
	}

	@PostConstruct
	public void postConstruct() {
		Map<String, Map<String, RuntimeSearchParam>> resourceNameToSearchParams = new HashMap<String, Map<String,RuntimeSearchParam>>();

		for (IFhirResourceDao<?> nextDao : myDaos) {
			RuntimeResourceDefinition nextResDef = myCtx.getResourceDefinition(nextDao.getResourceType());
			String nextResourceName = nextResDef.getName();
			HashMap<String, RuntimeSearchParam> nameToParam = new HashMap<String, RuntimeSearchParam>();
			resourceNameToSearchParams.put(nextResourceName, nameToParam);
			
			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				nameToParam.put(nextSp.getName(), nextSp);
			}
		}

		myBuiltInSearchParams = Collections.unmodifiableMap(resourceNameToSearchParams);
	}

}
