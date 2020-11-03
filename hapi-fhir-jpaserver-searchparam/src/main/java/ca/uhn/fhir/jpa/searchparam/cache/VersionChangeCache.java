package ca.uhn.fhir.jpa.searchparam.cache;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.Map;

public class VersionChangeCache {
	private final Map<IIdType, Long> myVersionMap = new HashMap<>();
}
