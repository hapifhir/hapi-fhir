package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class HSearchParamHelperProviderImpl implements IHSearchParamHelperProvider {

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	private final Map<String, Map<String, HSearchParamHelper<?>>> providerCache = new ConcurrentHashMap<>();


	@Override
	public HSearchParamHelper<?> provideHelper(String theResourceTypeName, String theParamName) {
		HSearchParamHelper<?> cachedProvider = getCached(theResourceTypeName, theParamName);
		if (cachedProvider != null) { return cachedProvider; }

		HSearchParamHelper<?> helper = findHelper(theResourceTypeName, theParamName);

		cache(theResourceTypeName, theParamName, helper);
		return helper;
	}


	private void cache(String theResourceTypeName, String theParamName, HSearchParamHelper<?> theHelper) {
		providerCache .computeIfAbsent(theResourceTypeName, k -> new HashMap<>()) .put(theParamName, theHelper);   // IfAbsent(theResourceTypeName,  new HashSet<<String, HSearchParamHelper<?>>())
	}


	@NotNull
	private HSearchParamHelper<?> findHelper(String theResourceTypeName, String theParamName) {
		Optional<RestSearchParameterTypeEnum> paramTypeOpt = getParamType(theResourceTypeName, theParamName);

		if (paramTypeOpt.isEmpty()) {
			//	fixme jm: code
			throw new InternalErrorException(Msg.code(0) + "Failed to obtain parameter type for resource " +
				theResourceTypeName + " and parameter " + theParamName);
		}

		HSearchParamHelper<?> helper = HSearchParamHelper.getTypeHelperMap().get(paramTypeOpt.get());

		if (helper == null) {
			//	fixme jm: code
			throw new InternalErrorException(Msg.code(0) +
				"HSearchParamHelper.myTypeHelperMap doesn't contain an entry for " + paramTypeOpt.get());
		}
		return helper;
	}


	private HSearchParamHelper<?> getCached(String theResourceTypeName, String theParamName) {
		return providerCache.getOrDefault(theResourceTypeName, Collections.emptyMap()).get(theParamName);
	}


	public Optional<RestSearchParameterTypeEnum> getParamType(String theResourceTypeName, String theParamName) {
		ResourceSearchParams activeSearchParams = mySearchParamRegistry.getActiveSearchParams(theResourceTypeName);
		RuntimeSearchParam searchParam = activeSearchParams.get(theParamName);
		if (searchParam == null) {
			return Optional.empty();
		}

		return Optional.of(searchParam.getParamType());
	}



}
