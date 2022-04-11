package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ReindexTerminologyResult;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider.RESP_PARAM_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TerminologyFreetextIndexingProviderTest {

	private final FhirContext  myContext = FhirContext.forR4();

	@Mock private ITermReadSvc myTermReadSvc;
	@Mock private SystemRequestDetails myRequestDetails;

	@InjectMocks
	private BaseJpaSystemProvider<?, ?> testedProvider = new BaseJpaSystemProvider<>();

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(testedProvider, "myContext", myContext);
	}

	@Test
	public void testNoSearchEnabled() throws InterruptedException {
		when(myTermReadSvc.reindexTerminology()).thenReturn(ReindexTerminologyResult.SEARCH_SVC_DISABLED);

		IBaseParameters retVal = testedProvider.reindexTerminology(myRequestDetails);

		assertNotNull(retVal);
		Optional<String> successValueOpt = ParametersUtil.getNamedParameterValueAsString(myContext, retVal, RESP_PARAM_SUCCESS);
		assertTrue(successValueOpt.isPresent());
		assertEquals("false", successValueOpt.get());
		Optional<String> msgOpt = ParametersUtil.getNamedParameterValueAsString(myContext, retVal, "message");
		assertTrue(msgOpt.isPresent());
		assertEquals("Freetext service is not configured. Operation didn't run.", msgOpt.get());
	}


	@Test
	void testOtherTerminologyTasksRunning() throws InterruptedException {
		when(myTermReadSvc.reindexTerminology()).thenReturn(ReindexTerminologyResult.OTHER_BATCH_TERMINOLOGY_TASKS_RUNNING);

		IBaseParameters retVal = testedProvider.reindexTerminology(myRequestDetails);

		assertNotNull(retVal);
		Optional<String> successValueOpt = ParametersUtil.getNamedParameterValueAsString(myContext, retVal, RESP_PARAM_SUCCESS);
		assertTrue(successValueOpt.isPresent());
		assertEquals("false", successValueOpt.get());
		Optional<String> msgOpt = ParametersUtil.getNamedParameterValueAsString(myContext, retVal, "message");
		assertTrue(msgOpt.isPresent());
		assertEquals("Operation was cancelled because other terminology background tasks are currently running. Try again in a few minutes.", msgOpt.get());
	}


	@Test
	void testServiceWorks() throws InterruptedException {
		when(myTermReadSvc.reindexTerminology()).thenReturn(ReindexTerminologyResult.SUCCESS);

		IBaseParameters retVal = testedProvider.reindexTerminology(myRequestDetails);

		assertNotNull(retVal);
		Optional<String> successValueOpt = ParametersUtil.getNamedParameterValueAsString(myContext, retVal, RESP_PARAM_SUCCESS);
		assertTrue(successValueOpt.isPresent());
		assertEquals("true", successValueOpt.get());
	}


	@Test
	void testServiceThrows() throws InterruptedException {
		String exceptionMsg = "some msg";
		when(myTermReadSvc.reindexTerminology()).thenThrow(new InterruptedException(exceptionMsg));

		InternalErrorException thrown = assertThrows(InternalErrorException.class,
			() -> testedProvider.reindexTerminology(myRequestDetails));

		assertTrue(thrown.getMessage().startsWith(Msg.code(2072) + "Re-creating terminology freetext indexes " +
			"failed with exception: " + exceptionMsg));
	}


}
