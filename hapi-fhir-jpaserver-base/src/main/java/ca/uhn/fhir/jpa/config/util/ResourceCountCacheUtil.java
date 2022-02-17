package ca.uhn.fhir.jpa.config.util;

import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import org.apache.commons.lang3.time.DateUtils;

public final class ResourceCountCacheUtil {
	private ResourceCountCacheUtil() {}
	public static ResourceCountCache newResourceCountCache(IFhirSystemDao<?, ?> theSystemDao) {
		ResourceCountCache retVal = new ResourceCountCache(() -> theSystemDao.getResourceCounts());
		retVal.setCacheMillis(4 * DateUtils.MILLIS_PER_HOUR);
		return retVal;
	}
}
