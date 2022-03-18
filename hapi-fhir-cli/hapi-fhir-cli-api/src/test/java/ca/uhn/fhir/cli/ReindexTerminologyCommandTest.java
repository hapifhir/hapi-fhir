package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider.RESP_PARAM_SUCCESS;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class ReindexTerminologyCommandTest {

	private final FhirContext myContext = FhirContext.forR4();

	@Spy
	private BaseJpaSystemProvider<?, ?> myProvider = spy(new BaseJpaSystemProvider<>());

	@RegisterExtension
	public final RestfulServerExtension myRestfulServerExtension =
		new RestfulServerExtension(myContext, myProvider);


	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	static {
		System.setProperty("test", "true");
	}


	@Test
	public void testProviderMethodInvoked() {
		System.setOut(new PrintStream(outputStreamCaptor));
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		ParametersUtil.addParameterToParametersBoolean(myContext, retVal, RESP_PARAM_SUCCESS, true);
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		App.main(new String[]{
			ReindexTerminologyCommand.REINDEX_TERMINOLOGY,
			"-v", "r4",
			"-t", myRestfulServerExtension.getBaseUrl()
		});

		assertThat(outputStreamCaptor.toString().trim(),
			outputStreamCaptor.toString().trim(), containsString("<valueBoolean value=\"true\"/>"));
	}


	@Test
	public void testNoVersionThrows() {
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		ParametersUtil.addParameterToParametersBoolean(myContext, retVal, RESP_PARAM_SUCCESS, true);
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		Error thrown = assertThrows(Error.class, () ->
			App.main(new String[]{
				ReindexTerminologyCommand.REINDEX_TERMINOLOGY,
				"-t", myRestfulServerExtension.getBaseUrl()
			})
		);
		assertThat(thrown.getMessage(), containsString("Missing required option: v"));
	}


	@Test
	public void testNoTargetThrows() {
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		ParametersUtil.addParameterToParametersBoolean(myContext, retVal, RESP_PARAM_SUCCESS, true);
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		Error thrown = assertThrows(Error.class, () ->
			App.main(new String[]{ReindexTerminologyCommand.REINDEX_TERMINOLOGY, "-v", "r4"})
		);
		assertThat(thrown.getMessage(), containsString("Missing required option: t"));
	}


	@Test
	public void testHandleUnexpectedResponse() {
		System.setOut(new PrintStream(outputStreamCaptor));
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		App.main(new String[]{
			ReindexTerminologyCommand.REINDEX_TERMINOLOGY,
			"-v", "r4",
			"-t", myRestfulServerExtension.getBaseUrl()
		});

		assertThat(outputStreamCaptor.toString().trim(),
			outputStreamCaptor.toString().trim(), containsString("<valueBoolean value=\"false\"/>"));
		assertThat(outputStreamCaptor.toString().trim(),
			outputStreamCaptor.toString().trim(), containsString("<valueString value=\"Internal error. " +
				"Command result unknown. Check system logs for details\"/>"));

	}


	@Test
	public void testHandleServiceError() {
		System.setOut(new PrintStream(outputStreamCaptor));
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		ParametersUtil.addParameterToParametersBoolean(myContext, retVal, RESP_PARAM_SUCCESS, false);
		ParametersUtil.addParameterToParametersString(myContext, retVal, "message",
			"Freetext service is not configured. Operation didn't run.");
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		App.main(new String[]{
			ReindexTerminologyCommand.REINDEX_TERMINOLOGY,
			"-v", "r4",
			"-t", myRestfulServerExtension.getBaseUrl()
		});

		assertThat(outputStreamCaptor.toString().trim(),
			outputStreamCaptor.toString().trim(), containsString("<valueBoolean value=\"false\"/>"));
		assertThat(outputStreamCaptor.toString().trim(),
			outputStreamCaptor.toString().trim(), containsString("<valueString value=\"Freetext service is not configured. Operation didn't run.\"/>"));
	}

}
