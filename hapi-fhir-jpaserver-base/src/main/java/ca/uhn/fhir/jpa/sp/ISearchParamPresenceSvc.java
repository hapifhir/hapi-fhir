package ca.uhn.fhir.jpa.sp;

import java.util.Map;

import ca.uhn.fhir.jpa.entity.ResourceTable;

public interface ISearchParamPresenceSvc {

	void updatePresence(ResourceTable theResource, Map<String, Boolean> theParamNameToPresence);

	void flushCachesForUnitTest();

}
