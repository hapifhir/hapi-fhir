package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuantitySearchOrListPrefixR4Test extends BaseResourceProviderR4Test {

	@BeforeEach
	void setUp() {
		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);
	}


	@ParameterizedTest
	@EnumSource(value = NormalizedQuantitySearchLevel.class)
	void testQuantityOrSearchWithDifferentPrefixes(NormalizedQuantitySearchLevel searchLevel) {
		myStorageSettings.setNormalizedQuantitySearchLevel(searchLevel);
		setupObservations();

		List<Double> actualValues = executeSearchAndExtractValues("Observation?value-quantity=gt100,lt60");

		assertThat(actualValues)
			.as("Search result should contain values < 60 or > 100")
			.containsExactlyInAnyOrder(0.0, 45.0, 110.0, 250.0);

		assertThat(actualValues)
			.as("Search result must not include 75 (between 60 and 100)")
			.doesNotContain(75.0);
	}

	@ParameterizedTest
	@EnumSource(value = NormalizedQuantitySearchLevel.class)
	void testQuantitySearchSinglePrefixRegression(NormalizedQuantitySearchLevel searchLevel) {
		myStorageSettings.setNormalizedQuantitySearchLevel(searchLevel);
		setupObservations();

		List<Double> actualValues = executeSearchAndExtractValues("Observation?value-quantity=gt100");

		assertThat(actualValues)
			.as("Single prefix search for gt100 should return 100")
			.containsExactlyInAnyOrder(110.0, 250.0);
	}

	@ParameterizedTest
	@EnumSource(value = NormalizedQuantitySearchLevel.class)
	void testQuantitySearchAndOrCombinationRegression(NormalizedQuantitySearchLevel searchLevel) {
		myStorageSettings.setNormalizedQuantitySearchLevel(searchLevel);
		setupObservations();

		// (value > 0) AND ((value > 100) OR (value < 60))
		List<Double> actualValues = executeSearchAndExtractValues("Observation?value-quantity=gt0&value-quantity=gt100,lt60");

		assertThat(actualValues)
			.as("Combined AND + OR search should exclude 0 and 75")
			.containsExactlyInAnyOrder(45.0, 110.0, 250.0);
	}

	private List<Double> executeSearchAndExtractValues(String theUrl) {
		Bundle resultBundle = myClient
			.search()
			.byUrl(theUrl)
			.returnBundle(Bundle.class)
			.execute();

		List<Observation> observations = BundleUtil.toListOfResourcesOfType(myFhirContext, resultBundle, Observation.class);

		return observations.stream()
			.filter(Observation::hasValueQuantity)
			.map(obs -> obs.getValueQuantity().getValue().doubleValue())
			.toList();
	}

	private void setupObservations() {
		createObservationWithQuantity(0);
		createObservationWithQuantity(45);
		createObservationWithQuantity(75);
		createObservationWithQuantity(110);
		createObservationWithQuantity(250);
	}

	private void createObservationWithQuantity(double value) {
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		obs.getCode().addCoding()
			.setSystem("http://loinc.org")
			.setCode("8867-4")
			.setDisplay("Heart rate");

		Quantity quantity = new Quantity();
		quantity.setValue(BigDecimal.valueOf(value));
		quantity.setUnit("bpm");
		quantity.setSystem("http://unitsofmeasure.org");
		quantity.setCode("/min");
		obs.setValue(quantity);

		myClient.create().resource(obs).execute();
	}
}
