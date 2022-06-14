package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_CODE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_CODE_NORM;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_SYSTEM;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE_NORM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FreetextSortPropertyFilterQuantityTest {

	private final FreetextSortPropertyFilterQuantity tested = new FreetextSortPropertyFilterQuantity();

	private final List<String> NORMALIZED_PROPS = Collections.singletonList("nsp.value-quantity.quantity." + QTY_VALUE_NORM);
	private final List<String> NOT_NORMALIZED_PROPS = Collections.singletonList("nsp.value-quantity.quantity." + QTY_VALUE);
	private final List<String> OTHER_PROPS = List.of(
		"nsp.value-quantity.quantity." + QTY_CODE,
		"nsp.value-quantity.quantity." + QTY_SYSTEM,
		"nsp.value-quantity.quantity." + QTY_CODE_NORM
	);

	@Test
	void acceptsQuantity() {
		assertTrue(tested.accepts(RestSearchParameterTypeEnum.QUANTITY));
	}

	@Test
	void returnsNormalizedWhenPresent() {
		List<String> allPropertyKinds = new ArrayList<>(NORMALIZED_PROPS);
		allPropertyKinds.addAll(NOT_NORMALIZED_PROPS);
		allPropertyKinds.addAll(OTHER_PROPS);

		List<String> filteredList = tested.filter(allPropertyKinds);

		assertEquals(NORMALIZED_PROPS, filteredList);
	}

	@Test
	void returnsNotNormalizeIfPresentWhenNormalizedNotPresent() {
		List<String> allPropertyKinds = new ArrayList<>(NOT_NORMALIZED_PROPS);
		allPropertyKinds.addAll(OTHER_PROPS);

		List<String> filteredList = tested.filter(allPropertyKinds);

		assertEquals(NOT_NORMALIZED_PROPS, filteredList);
	}

	@Test
	void returnsEmptyWhenNeitherNormalizedNorNotNormalizedPresent() {
		List<String> allPropertyKinds = new ArrayList<>(OTHER_PROPS);

		List<String> filteredList = tested.filter(allPropertyKinds);

		assertTrue(filteredList.isEmpty());
	}

}
