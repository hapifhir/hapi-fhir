package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExtendedFulltextSearchParamRegistryTest {

	@Mock
	private FreetextSortPropertyFilterHelper mockSortPropertyFilterHelper;

	@InjectMocks
	private ExtendedFulltextSearchParamRegistry testedRegistry;

	@BeforeEach
	void setUp() {
		testedRegistry.put("nsp._tag.token.code", RestSearchParameterTypeEnum.TOKEN);
		testedRegistry.put("nsp._tag.token.system", RestSearchParameterTypeEnum.TOKEN);
		testedRegistry.put("nsp.value-quantity.quantity.code", RestSearchParameterTypeEnum.QUANTITY);
		testedRegistry.put("nsp.value-quantity.quantity.value", RestSearchParameterTypeEnum.QUANTITY);
	}

	@Test
	void getTypeAndFieldPathsCallsHelperOnceWithRightEnumTypeAndCollectedProperties() {
		List<String> expectedListParam = List.of(
			"nsp.value-quantity.quantity.code", "nsp.value-quantity.quantity.value");

		testedRegistry.getFieldPaths("value-quantity");

		verify(mockSortPropertyFilterHelper, Mockito.times(1)).
			filterProperties(RestSearchParameterTypeEnum.QUANTITY, expectedListParam);
	}
}
