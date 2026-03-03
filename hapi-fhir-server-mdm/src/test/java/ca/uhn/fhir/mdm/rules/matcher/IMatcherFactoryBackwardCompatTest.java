package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.nickname.NicknameSvc;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-opus-4-6
class IMatcherFactoryBackwardCompatTest {

	/**
	 * Simulates a legacy external implementation that only overrides the old
	 * enum-based method {@code getFieldMatcherForMatchType(MatchTypeEnum)}.
	 */
	private static final IMdmFieldMatcher LEGACY_STRING_MATCHER = (left, right, params) -> true;

	private final IMatcherFactory myLegacyFactory = new IMatcherFactory() {
		@Override
		@SuppressWarnings("all")
		public IMdmFieldMatcher getFieldMatcherForMatchType(MatchTypeEnum theMatchType) {
			if (theMatchType == MatchTypeEnum.STRING) {
				return LEGACY_STRING_MATCHER;
			}
			return null;
		}
	};

	@Test
	void legacyImpl_getFieldMatcherForName_throwsUnsupportedOperationException() {
		// A legacy impl that only overrides the deprecated enum method does not
		// support string-based lookup — the default throws UnsupportedOperationException.
		assertThatThrownBy(() -> myLegacyFactory.getFieldMatcherForName("STRING"))
			.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void legacyImpl_register_throwsUnsupportedOperationException() {
		IMdmFieldMatcher matcher = (left, right, params) -> true;
		assertThatThrownBy(() -> myLegacyFactory.register("CUSTOM", matcher))
			.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void legacyImpl_getRegisteredNames_returnsEmptySet() {
		assertThat(myLegacyFactory.getRegisteredNames()).isEmpty();
	}

	@Test
	void modernImpl_deprecatedGetFieldMatcherForMatchType_delegatesToGetFieldMatcherForName() {
		// MdmMatcherFactory (a modern impl) overrides getFieldMatcherForName;
		// the deprecated getFieldMatcherForMatchType default should delegate to it.
		MdmMatcherFactory modernFactory = new MdmMatcherFactory(FhirContext.forR4(), new NicknameSvc());
		@SuppressWarnings("all")
		IMdmFieldMatcher matcher = modernFactory.getFieldMatcherForMatchType(MatchTypeEnum.STRING);
		assertThat(matcher)
			.isNotNull()
			.isSameAs(modernFactory.getFieldMatcherForName("STRING"));
	}
}
