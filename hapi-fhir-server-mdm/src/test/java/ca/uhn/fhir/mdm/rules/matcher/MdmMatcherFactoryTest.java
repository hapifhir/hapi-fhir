package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.nickname.NicknameSvc;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-opus-4-6
class MdmMatcherFactoryTest {

	private MdmMatcherFactory myFactory;

	@BeforeEach
	void setUp() {
		myFactory = new MdmMatcherFactory(FhirContext.forR4(), new NicknameSvc());
	}

	@Test
	void getFieldMatcherForName_builtInMatcher_returnsMatcher() {
		for (MatchTypeEnum matchType : MatchTypeEnum.values()) {
			IMdmFieldMatcher matcher = myFactory.getFieldMatcherForName(matchType.name());
			assertThat(matcher).as("Matcher for " + matchType.name()).isNotNull();
		}
	}

	@Test
	void getFieldMatcherForName_unknownName_returnsNull() {
		assertThat(myFactory.getFieldMatcherForName("UNKNOWN_ALGORITHM")).isNull();
	}

	@Test
	void register_customMatcher_isRetrievableByName() {
		IMdmFieldMatcher customMatcher = (theLeftBase, theRightBase, theParams) -> true;
		myFactory.register("CUSTOM_MATCHER", customMatcher);

		assertThat(myFactory.getFieldMatcherForName("CUSTOM_MATCHER")).isSameAs(customMatcher);
		assertThat(myFactory.getRegisteredNames()).contains("CUSTOM_MATCHER");
	}

	@Test
	void register_duplicateName_throwsException() {
		IMdmFieldMatcher customMatcher = (theLeftBase, theRightBase, theParams) -> true;
		myFactory.register("CUSTOM", customMatcher);

		assertThatThrownBy(() -> myFactory.register("CUSTOM", customMatcher))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("already registered");
	}

	@Test
	void register_builtInName_throwsException() {
		IMdmFieldMatcher customMatcher = (theLeftBase, theRightBase, theParams) -> true;

		assertThatThrownBy(() -> myFactory.register("STRING", customMatcher))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("already registered");
	}

	@Test
	void getRegisteredNames_containsAllBuiltInMatchers() {
		assertThat(myFactory.getRegisteredNames()).containsAll(
			java.util.Arrays.stream(MatchTypeEnum.values())
				.map(MatchTypeEnum::name)
				.toList()
		);
	}
}
