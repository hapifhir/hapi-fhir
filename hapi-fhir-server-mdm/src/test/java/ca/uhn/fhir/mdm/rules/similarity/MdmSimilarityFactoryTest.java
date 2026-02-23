package ca.uhn.fhir.mdm.rules.similarity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
