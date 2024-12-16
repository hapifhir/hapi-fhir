package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.support.IValidationSupport;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @deprecated This should no longer be used, caching functionality is provided by {@link ValidationSupportChain}
 */
@Deprecated(since = "8.0.0", forRemoval = true)
public class CachingValidationSupport extends BaseValidationSupportWrapper implements IValidationSupport {

	private final boolean myIsEnabledValidationForCodingsLogicalAnd;

	/**
	 * Constructor with default timeouts
	 *
	 * @param theWrap The validation support module to wrap
	 */
	public CachingValidationSupport(IValidationSupport theWrap) {
		this(theWrap, CacheTimeouts.defaultValues(), false);
	}

	public CachingValidationSupport(IValidationSupport theWrap, boolean theIsEnabledValidationForCodingsLogicalAnd) {
		this(theWrap, CacheTimeouts.defaultValues(), theIsEnabledValidationForCodingsLogicalAnd);
	}

	public CachingValidationSupport(IValidationSupport theWrap, CacheTimeouts theCacheTimeouts) {
		this(theWrap, theCacheTimeouts, false);
	}

	/**
	 * Constructor with configurable timeouts
	 *
	 * @param theWrap          The validation support module to wrap
	 * @param theCacheTimeouts The timeouts to use
	 */
	public CachingValidationSupport(
			IValidationSupport theWrap,
			CacheTimeouts theCacheTimeouts,
			boolean theIsEnabledValidationForCodingsLogicalAnd) {
		super(theWrap.getFhirContext(), theWrap);
		myIsEnabledValidationForCodingsLogicalAnd = theIsEnabledValidationForCodingsLogicalAnd;
	}

	/**
	 * @since 5.4.0
	 * @deprecated
	 */
	@Deprecated
	public static class CacheTimeouts {

		public CacheTimeouts setExpandValueSetMillis(long theExpandValueSetMillis) {
			// nothing
			return this;
		}

		public CacheTimeouts setTranslateCodeMillis(long theTranslateCodeMillis) {
			// nothing
			return this;
		}

		public CacheTimeouts setLookupCodeMillis(long theLookupCodeMillis) {
			// nothibng
			return this;
		}

		public CacheTimeouts setValidateCodeMillis(long theValidateCodeMillis) {
			// nothing
			return this;
		}

		public CacheTimeouts setMiscMillis(long theMiscMillis) {
			// nothing
			return this;
		}

		public static CacheTimeouts defaultValues() {
			return new CacheTimeouts()
					.setLookupCodeMillis(10 * DateUtils.MILLIS_PER_MINUTE)
					.setExpandValueSetMillis(DateUtils.MILLIS_PER_MINUTE)
					.setTranslateCodeMillis(10 * DateUtils.MILLIS_PER_MINUTE)
					.setValidateCodeMillis(10 * DateUtils.MILLIS_PER_MINUTE)
					.setMiscMillis(10 * DateUtils.MILLIS_PER_MINUTE);
		}
	}

	@Override
	public boolean isCodeableConceptValidationSuccessfulIfNotAllCodingsAreValid() {
		return myIsEnabledValidationForCodingsLogicalAnd;
	}
}
