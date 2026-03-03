package ca.uhn.fhir.mdm.rules.similarity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-opus-4-6
class MdmSimilarityFactoryTest {

	private MdmSimilarityFactory myFactory;

	@BeforeEach
	void setUp() {
		myFactory = new MdmSimilarityFactory();
	}

	@Test
	void getSimilarityForName_builtInSimilarity_returnsSimilarity() {
		for (MdmSimilarityEnum enumConstant : MdmSimilarityEnum.values()) {
			IMdmFieldSimilarity similarity = myFactory.getSimilarityForName(enumConstant.name());
			assertThat(similarity).as("Similarity for " + enumConstant.name()).isNotNull();
		}
	}

	@Test
	void getSimilarityForName_unknownName_returnsNull() {
		assertThat(myFactory.getSimilarityForName("UNKNOWN_ALGORITHM")).isNull();
	}

	@Test
	void register_customSimilarity_isRetrievableByName() {
		IMdmFieldSimilarity customSimilarity =
			(theFhirContext, theLeftBase, theRightBase, theExact) -> 0.5;
		myFactory.register("CUSTOM_SIMILARITY", customSimilarity);

		assertThat(myFactory.getSimilarityForName("CUSTOM_SIMILARITY")).isSameAs(customSimilarity);
		assertThat(myFactory.getRegisteredNames()).contains("CUSTOM_SIMILARITY");
	}

	@Test
	void register_duplicateName_throwsException() {
		IMdmFieldSimilarity customSimilarity =
			(theFhirContext, theLeftBase, theRightBase, theExact) -> 0.5;
		myFactory.register("CUSTOM", customSimilarity);

		assertThatThrownBy(() -> myFactory.register("CUSTOM", customSimilarity))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("already registered");
	}

	@Test
	void register_builtInName_throwsException() {
		IMdmFieldSimilarity customSimilarity =
			(theFhirContext, theLeftBase, theRightBase, theExact) -> 0.5;

		assertThatThrownBy(() -> myFactory.register("JARO_WINKLER", customSimilarity))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("already registered");
	}

	@Test
	void getRegisteredNames_containsAllBuiltInSimilarities() {
		assertThat(myFactory.getRegisteredNames()).containsAll(
			java.util.Arrays.stream(MdmSimilarityEnum.values())
				.map(MdmSimilarityEnum::name)
				.toList()
		);
	}

	@Test
	void getRegisteredNames_returnsImmutableCopy() {
		Set<String> names = myFactory.getRegisteredNames();

		assertThatThrownBy(() -> names.add("SHOULD_FAIL"))
			.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void unregister_customSimilarity_removesFromFactory() {
		IMdmFieldSimilarity custom =
			(theFhirContext, theLeftBase, theRightBase, theExact) -> 0.5;
		myFactory.register("CUSTOM_TO_REMOVE", custom);
		assertThat(myFactory.getSimilarityForName("CUSTOM_TO_REMOVE")).isNotNull();

		myFactory.unregister("CUSTOM_TO_REMOVE");

		assertThat(myFactory.getSimilarityForName("CUSTOM_TO_REMOVE")).isNull();
		assertThat(myFactory.getRegisteredNames()).doesNotContain("CUSTOM_TO_REMOVE");
	}

	@Test
	void unregister_builtInSimilarity_throwsException() {
		assertThatThrownBy(() -> myFactory.unregister("JARO_WINKLER"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Cannot unregister built-in similarity");
	}

	@Test
	void unregister_unknownName_doesNotThrow() {
		myFactory.unregister("DOES_NOT_EXIST");
	}

	@Test
	void register_afterUnregister_succeeds() {
		IMdmFieldSimilarity sim1 =
			(theFhirContext, theLeftBase, theRightBase, theExact) -> 0.5;
		IMdmFieldSimilarity sim2 =
			(theFhirContext, theLeftBase, theRightBase, theExact) -> 0.9;
		myFactory.register("REREGISTERABLE", sim1);

		myFactory.unregister("REREGISTERABLE");
		myFactory.register("REREGISTERABLE", sim2);

		assertThat(myFactory.getSimilarityForName("REREGISTERABLE")).isSameAs(sim2);
	}

	@Test
	void register_viaProvider_isRetrievableByName() {
		IMdmFieldSimilarity similarityImpl =
			(theFhirContext, theLeftBase, theRightBase, theExact) -> 0.75;
		IMdmFieldSimilarityProvider provider = new IMdmFieldSimilarityProvider() {
			@Override
			public String getName() {
				return "PROVIDER_SIMILARITY";
			}

			@Override
			public IMdmFieldSimilarity getSimilarity() {
				return similarityImpl;
			}
		};

		myFactory.register(provider.getName(), provider.getSimilarity());

		assertThat(myFactory.getSimilarityForName("PROVIDER_SIMILARITY")).isSameAs(similarityImpl);
		assertThat(myFactory.getRegisteredNames()).contains("PROVIDER_SIMILARITY");
	}
}
