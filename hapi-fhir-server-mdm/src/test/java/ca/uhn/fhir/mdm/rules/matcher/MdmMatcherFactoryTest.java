package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.nickname.NicknameSvc;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcherProvider;
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

	@Test
	void unregister_customMatcher_removesFromFactory() {
		IMdmFieldMatcher customMatcher = (theLeftBase, theRightBase, theParams) -> true;
		myFactory.register("CUSTOM_TO_REMOVE", customMatcher);
		assertThat(myFactory.getFieldMatcherForName("CUSTOM_TO_REMOVE")).isNotNull();

		myFactory.unregister("CUSTOM_TO_REMOVE");

		assertThat(myFactory.getFieldMatcherForName("CUSTOM_TO_REMOVE")).isNull();
		assertThat(myFactory.getRegisteredNames()).doesNotContain("CUSTOM_TO_REMOVE");
	}

	@Test
	void unregister_builtInMatcher_throwsException() {
		assertThatThrownBy(() -> myFactory.unregister("STRING"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Cannot unregister built-in matcher");
	}

	@Test
	void unregister_unknownName_doesNotThrow() {
		myFactory.unregister("DOES_NOT_EXIST");
	}

	@Test
	void register_afterUnregister_succeeds() {
		IMdmFieldMatcher matcher1 = (theLeftBase, theRightBase, theParams) -> true;
		IMdmFieldMatcher matcher2 = (theLeftBase, theRightBase, theParams) -> false;
		myFactory.register("REREGISTERABLE", matcher1);

		myFactory.unregister("REREGISTERABLE");
		myFactory.register("REREGISTERABLE", matcher2);

		assertThat(myFactory.getFieldMatcherForName("REREGISTERABLE")).isSameAs(matcher2);
	}

	@Test
	void register_viaProvider_isRetrievableByName() {
		IMdmFieldMatcher matcherImpl = (theLeftBase, theRightBase, theParams) -> true;
		IMdmFieldMatcherProvider provider = new IMdmFieldMatcherProvider() {
			@Override
			public String getName() {
				return "PROVIDER_MATCHER";
			}

			@Override
			public IMdmFieldMatcher getMatcher() {
				return matcherImpl;
			}
		};

		myFactory.register(provider.getName(), provider.getMatcher());

		assertThat(myFactory.getFieldMatcherForName("PROVIDER_MATCHER")).isSameAs(matcherImpl);
		assertThat(myFactory.getRegisteredNames()).contains("PROVIDER_MATCHER");
	}
}
