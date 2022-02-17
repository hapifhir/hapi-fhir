package ca.uhn.fhir.jpa.config.util;

import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;

public final class ValidationSupportConfigUtil {
	private ValidationSupportConfigUtil() {}

	public static CachingValidationSupport newCachingValidationSupport(JpaValidationSupportChain theJpaValidationSupportChain) {
		// Short timeout for code translation because TermConceptMappingSvcImpl has its own caching
		CachingValidationSupport.CacheTimeouts cacheTimeouts = CachingValidationSupport.CacheTimeouts.defaultValues()
			.setTranslateCodeMillis(1000);

		return new CachingValidationSupport(theJpaValidationSupportChain, cacheTimeouts);
	}
}
