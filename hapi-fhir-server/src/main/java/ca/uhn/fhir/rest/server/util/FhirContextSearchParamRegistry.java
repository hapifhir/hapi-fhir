package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FhirContextSearchParamRegistry implements ISearchParamRegistry {


	private final List<RuntimeSearchParam> myExtraSearchParams = new ArrayList<>();
	private final FhirContext myCtx;
	private final ConcurrentHashMap<String, Map<String, RuntimeSearchParam>>

	/**
	 * Constructor
	 */
	public FhirContextSearchParamRegistry(@Nonnull FhirContext theCtx) {
		Validate.notNull(theCtx, "theCtx must not be null");
		myCtx = theCtx;
	}

	@Override
	public void forceRefresh() {
		// nothing
	}

	@Override
	public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
		return getActiveSearchParams(theResourceName).get(theParamName);
	}

	@Override
	public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
		Map<String, RuntimeSearchParam> sps = new HashMap<>();
		RuntimeResourceDefinition nextResDef = myCtx.getResourceDefinition(theResourceName);
		for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
			sps.put(nextSp.getName(), nextSp);
		}

		for (RuntimeSearchParam next : myExtraSearchParams) {
			sps.put(next.getName(), next);
		}

		return sps;
	}

	public void addSearchParam(RuntimeSearchParam theSearchParam) {
		myExtraSearchParams.add(theSearchParam);
	}

	@Override
	public List<RuntimeSearchParam> getActiveComboSearchParams(String theResourceName, Set<String> theParamNames) {
		throw new UnsupportedOperationException();
	}

	@Nullable
	@Override
	public RuntimeSearchParam getActiveSearchParamByUrl(String theUrl) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<RuntimeSearchParam> getActiveComboSearchParams(String theResourceName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void requestRefresh() {
		// nothing
	}

	@Override
	public void setPhoneticEncoder(IPhoneticEncoder thePhoneticEncoder) {
		// nothing
	}
}
