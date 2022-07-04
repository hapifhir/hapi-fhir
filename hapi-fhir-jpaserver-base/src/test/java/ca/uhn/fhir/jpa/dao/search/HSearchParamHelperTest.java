package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HSearchParamHelperTest {

	@InjectMocks
	private HSearchParamHelper<?> testedHelper = new HSearchParamHelperToken();

	@Mock private SearchParamRegistryImpl mySearchParamRegistry;
	@Mock private ResourceSearchParams mockedResourceSearchParams;
	@Mock private RuntimeSearchParam mockedRuntimeSearchParam;


	@Test
	void testChildRegisteredWithParent() {
//		when(mySearchParamRegistry.getActiveSearchParams("Observation")).thenReturn(mockedResourceSearchParams);
//		when(mockedResourceSearchParams.get("_security")).thenReturn(mockedRuntimeSearchParam);
//		when(mockedRuntimeSearchParam.getParamType()).thenReturn(RestSearchParameterTypeEnum.TOKEN);
//
//		HSearchParamHelper<?> typedHelper = testedHelper.getTypeHelper("Observation", "_security");
//
//		assertEquals( RestSearchParameterTypeEnum.TOKEN, typedHelper.getType() );

	}
}
