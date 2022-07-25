package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider;
import ca.uhn.fhir.test.utilities.RestServerR4Helper;
import ca.uhn.fhir.test.utilities.TlsAuthenticationTestHelper;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider.RESP_PARAM_SUCCESS;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class ReindexTerminologyCommandTest {

	private final FhirContext myContext = FhirContext.forR4();

	@Spy
	private BaseJpaSystemProvider<?, ?> myProvider = spy(new BaseJpaSystemProvider<>());

	@RegisterExtension
	public final RestServerR4Helper myRestServerR4Helper = new RestServerR4Helper(true);
	@RegisterExtension
	public TlsAuthenticationTestHelper myTlsAuthenticationTestHelper = new TlsAuthenticationTestHelper();


	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	static {
		System.setProperty("test", "true");
	}

	@BeforeEach
	public void beforeEach(){
		myRestServerR4Helper.registerProvider(myProvider);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testProviderMethodInvoked(boolean theIncludeTls) {
		System.setOut(new PrintStream(outputStreamCaptor));
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		ParametersUtil.addParameterToParametersBoolean(myContext, retVal, RESP_PARAM_SUCCESS, true);
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ReindexTerminologyCommand.REINDEX_TERMINOLOGY,
				"-v", "r4"
			},
			"-t", theIncludeTls, myRestServerR4Helper
		));

		assertThat(outputStreamCaptor.toString().trim(),
			outputStreamCaptor.toString().trim(), containsString("<valueBoolean value=\"true\"/>"));
	}


	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testNoVersionThrows(boolean theIncludeTls) {
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		ParametersUtil.addParameterToParametersBoolean(myContext, retVal, RESP_PARAM_SUCCESS, true);
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		try {
			App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
				new String[]{
					ReindexTerminologyCommand.REINDEX_TERMINOLOGY
				},
				"-t", theIncludeTls, myRestServerR4Helper
			));
			fail();
		} catch (Error e) {
			assertThat(e.getMessage(), containsString("Missing required option: v"));
		}
	}


	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testNoTargetThrows(boolean theIncludeTls) {
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		ParametersUtil.addParameterToParametersBoolean(myContext, retVal, RESP_PARAM_SUCCESS, true);
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		try {
			App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
				new String[]{
					ReindexTerminologyCommand.REINDEX_TERMINOLOGY,
					"-v", "r4"
				},
				null, theIncludeTls, myRestServerR4Helper
			));
			fail();
		} catch (Error e) {
			assertThat(e.getMessage(), containsString("Missing required option: t"));
		}
	}


	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testHandleUnexpectedResponse(boolean theIncludeTls) {
		System.setOut(new PrintStream(outputStreamCaptor));
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ReindexTerminologyCommand.REINDEX_TERMINOLOGY,
				"-v", "r4"
			},
			"-t", theIncludeTls, myRestServerR4Helper
		));

		assertThat(outputStreamCaptor.toString().trim(),
			outputStreamCaptor.toString().trim(), containsString("<valueBoolean value=\"false\"/>"));
		assertThat(outputStreamCaptor.toString().trim(),
			outputStreamCaptor.toString().trim(), containsString("<valueString value=\"Internal error. " +
				"Command result unknown. Check system logs for details\"/>"));

	}


	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testHandleServiceError(boolean theIncludeTls) {
		System.setOut(new PrintStream(outputStreamCaptor));
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		ParametersUtil.addParameterToParametersBoolean(myContext, retVal, RESP_PARAM_SUCCESS, false);
		ParametersUtil.addParameterToParametersString(myContext, retVal, "message",
			"Freetext service is not configured. Operation didn't run.");
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ReindexTerminologyCommand.REINDEX_TERMINOLOGY,
				"-v", "r4"
			},
			"-t", theIncludeTls, myRestServerR4Helper
		));

		assertThat(outputStreamCaptor.toString().trim(),
			outputStreamCaptor.toString().trim(), containsString("<valueBoolean value=\"false\"/>"));
		assertThat(outputStreamCaptor.toString().trim(),
			outputStreamCaptor.toString().trim(), containsString("<valueString value=\"Freetext service is not configured. Operation didn't run.\"/>"));
	}

}
