/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.config.util;

import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;

public final class ValidationSupportConfigUtil {
	private ValidationSupportConfigUtil() {}

	public static CachingValidationSupport newCachingValidationSupport(
			JpaValidationSupportChain theJpaValidationSupportChain) {
		return newCachingValidationSupport(theJpaValidationSupportChain, false);
	}

	public static CachingValidationSupport newCachingValidationSupport(
			JpaValidationSupportChain theJpaValidationSupportChain,
			boolean theIsEnabledValidationForCodingsLogicalAnd) {
		// Short timeout for code translation because TermConceptMappingSvcImpl has its own caching
		CachingValidationSupport.CacheTimeouts cacheTimeouts =
				CachingValidationSupport.CacheTimeouts.defaultValues().setTranslateCodeMillis(1000);

		return new CachingValidationSupport(
				theJpaValidationSupportChain, cacheTimeouts, theIsEnabledValidationForCodingsLogicalAnd);
	}
}
