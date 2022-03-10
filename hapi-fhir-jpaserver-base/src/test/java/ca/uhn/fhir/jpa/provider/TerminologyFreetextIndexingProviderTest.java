package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.term.TermReadSvcR4;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TerminologyFreetextIndexingProviderTest {

	private final FhirContext  myContext = FhirContext.forR4();

	@Mock private ITermReadSvc myTermReadSvc;
	@Mock private HttpServletRequest myServletRequest;
	@Mock private SystemRequestDetails myRequestDetails;

	@InjectMocks
	private BaseJpaSystemProvider<?, ?> testedProvider;



	@Test
	public void testNoSearchEnabledThrows() {
		ReflectionTestUtils.setField(testedProvider, "myTermReadSvc", new TermReadSvcR4());
		InternalErrorException thrown = assertThrows(
			InternalErrorException.class,
			() -> testedProvider.reindexTerminology(myRequestDetails)
		);

		assertThat(thrown.getMessage(), containsString("Freetext search service is not configured"));
	}


	@Test
	void providerTest() throws InterruptedException {
		BaseJpaSystemProvider<?, ?> spiedProvider = spy(testedProvider);
		when(spiedProvider.getContext()).thenReturn(myContext);

		IBaseParameters retVal = spiedProvider.reindexTerminology(myRequestDetails);

		verify(myTermReadSvc, Mockito.times(1)).reindexTerminology();
		List<String> values = ParametersUtil.getNamedParameterValuesAsString(
			myContext, retVal, BaseJpaSystemProvider.RESP_PARAM_SUCCESS);
		assertEquals(1, values.size());
		assertEquals("true", values.get(0));

	}


}
