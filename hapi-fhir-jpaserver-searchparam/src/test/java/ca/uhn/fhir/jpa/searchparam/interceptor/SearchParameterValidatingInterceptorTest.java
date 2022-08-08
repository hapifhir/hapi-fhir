package ca.uhn.fhir.jpa.searchparam.interceptor;

import ca.uhn.fhir.jpa.searchparam.submit.interceptor.SearchParamValidatingInterceptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class SearchParameterValidatingInterceptorTest {

	@InjectMocks
	SearchParamValidatingInterceptor mySearchParamValidatingInterceptor;

	@Test
	public void whenCreatingNonOverlappingSearchParam_thenSuccess(){

		fail();
	}

	@Test
	public void whenCreatingOverlappingSearchParam_thenExceptionIsThrown(){

		fail();
	}

	@Test
	public void whenPutOperationToCreateNonOverlappingSearchParam_thenSuccess(){

		fail();
	}

	@Test
	public void whenPutOperationToCreateOverlappingSearchParam_thenExceptionIsThrown(){

		fail();
	}

}
