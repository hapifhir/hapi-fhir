package ca.uhn.fhir.jpa.config.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
