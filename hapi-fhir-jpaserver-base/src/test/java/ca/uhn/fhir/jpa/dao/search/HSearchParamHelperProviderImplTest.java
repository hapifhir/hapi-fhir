package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HSearchParamHelperProviderImplTest {

	@InjectMocks
	private final IHSearchParamHelperProvider tested = new HSearchParamHelperProviderImpl();

	@Mock private ISearchParamRegistry mockSearchParamRegistry;
	@Mock private ResourceSearchParams mockSearchParams;
	@Mock private RuntimeSearchParam mockSearchParam;



	@Test
	void provideHelperNotCachedThenCached() {
		HSearchParamHelper.registerChildHelper(new HSearchParamHelperUri());
		when(mockSearchParamRegistry.getActiveSearchParams("Observation")).thenReturn(mockSearchParams);
		when(mockSearchParams.get("_profile")).thenReturn(mockSearchParam);
		when(mockSearchParam.getParamType()).thenReturn(RestSearchParameterTypeEnum.URI);

		// first request finds no cached helper
		HSearchParamHelper<?> providedHelper = tested.provideHelper("Observation", "_profile");

		assertTrue( providedHelper instanceof HSearchParamHelperUri );

		// second request finds cached helper (no request to mockSearchParamRegistry)
		HSearchParamHelper<?> providedHelper2 = tested.provideHelper("Observation", "_profile");

		assertEquals(providedHelper, providedHelper2);
		// only called once for the first call
		verify(mockSearchParamRegistry, times(1)).getActiveSearchParams(any());
	}


	@Test
	void findHelperNoParamNameForResourceThrows() {
		when(mockSearchParamRegistry.getActiveSearchParams("RiskAssessment")).thenReturn(mockSearchParams);
		when(mockSearchParams.get("probability")).thenReturn(null);

		InternalErrorException thrown = assertThrows(InternalErrorException.class,
			() -> tested.provideHelper("RiskAssessment", "probability"));

		assertTrue(thrown.getMessage().contains("Failed to obtain parameter type for resource"));
	}

	@Test
	void findHelperNoHelperRegisteredForParamThrows() {
		when(mockSearchParamRegistry.getActiveSearchParams("RiskAssessment")).thenReturn(mockSearchParams);
		when(mockSearchParams.get("probability")).thenReturn(mockSearchParam);
		when(mockSearchParam.getParamType()).thenReturn(RestSearchParameterTypeEnum.NUMBER);

		InternalErrorException thrown = assertThrows(InternalErrorException.class,
			() -> tested.provideHelper("RiskAssessment", "probability"));

		assertTrue(thrown.getMessage().contains("HSearchParamHelper.myTypeHelperMap doesn't contain an entry for"));
	}
}
