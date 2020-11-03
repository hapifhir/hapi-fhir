package ca.uhn.fhir.jpa.searchparam.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResourceVersionCacheSvc {
	/**
	 *
	 * @param map Key is versionlessUnqualified resource id
	 * @return
	 */
	public Map<IIdType, Long> getVersionLookup(SearchParameterMap map) {
		// FIXME KHS
		// Optimized database search
		return new HashMap<>();
	}
}
