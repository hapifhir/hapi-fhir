package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.RestServerR4Helper;
import ca.uhn.fhir.test.utilities.TlsAuthenticationTestHelper;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import ch.qos.logback.classic.Logger;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider.RESP_PARAM_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class ReindexTerminologyCommandTest {

	private static final String FAILURE_MESSAGE = "FAILURE";
	private final FhirContext myContext = FhirContext.forR4();

	@Spy
	private BaseJpaSystemProvider<?, ?> myProvider = spy(new BaseJpaSystemProvider<>() {});

	@RegisterExtension
	public final RestServerR4Helper myRestServerR4Helper = RestServerR4Helper.newInitialized();
	@RegisterExtension
	public TlsAuthenticationTestHelper myTlsAuthenticationTestHelper = new TlsAuthenticationTestHelper();

	// Deliberately not registered - we manually run this later because App startup resets the logging.
	LogbackTestExtension myAppLogCapture;

	static {
		HapiSystemProperties.enableTestMode();
	}

	@BeforeEach
	public void beforeEach(){
		myRestServerR4Helper.registerProvider(myProvider);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testProviderMethodInvoked(boolean theIncludeTls) {
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		ParametersUtil.addParameterToParametersBoolean(myContext, retVal, RESP_PARAM_SUCCESS, true);
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		String[] args = myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ReindexTerminologyCommand.REINDEX_TERMINOLOGY,
				"-v", "r4"
			},
			"-t", theIncludeTls, myRestServerR4Helper
		);
		runAppWithStartupHook(args, getLoggingStartupHook());

		LogbackTestExtensionAssert.assertThat(myAppLogCapture).doesNotHaveMessage(FAILURE_MESSAGE);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testNoVersionThrows(boolean theIncludeTls) {
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		ParametersUtil.addParameterToParametersBoolean(myContext, retVal, RESP_PARAM_SUCCESS, true);
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		String[] args = myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ReindexTerminologyCommand.REINDEX_TERMINOLOGY
			},
			"-t", theIncludeTls, myRestServerR4Helper
		);
		try {
			App.main(args);
			fail();
		} catch (Error e) {
			assertThat(e.getMessage()).contains("Missing required option: v");
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
			assertThat(e.getMessage()).contains("Missing required option: t");
		}
	}


	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testHandleUnexpectedResponse(boolean theIncludeTls) {
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		String[] args = myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ReindexTerminologyCommand.REINDEX_TERMINOLOGY,
				"-v", "r4"
			},
			"-t", theIncludeTls, myRestServerR4Helper
		);
		runAppWithStartupHook(args, getLoggingStartupHook());

		LogbackTestExtensionAssert.assertThat(myAppLogCapture)
			.hasMessage(FAILURE_MESSAGE)
			.hasMessage("Internal error. Command result unknown. Check system logs for details");
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testHandleServiceError(boolean theIncludeTls) {
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		ParametersUtil.addParameterToParametersBoolean(myContext, retVal, RESP_PARAM_SUCCESS, false);
		ParametersUtil.addParameterToParametersString(myContext, retVal, "message",
			"Freetext service is not configured. Operation didn't run.");
		doReturn(retVal).when(myProvider).reindexTerminology(any());

		// to keep logging verbose.
		String[] args = myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				ReindexTerminologyCommand.REINDEX_TERMINOLOGY,
				"-v", "r4",
				"--debug" // to keep logging verbose.
			},
			"-t", theIncludeTls, myRestServerR4Helper
		);
		runAppWithStartupHook(args, getLoggingStartupHook());

		LogbackTestExtensionAssert.assertThat(myAppLogCapture)
			.hasMessage(FAILURE_MESSAGE)
			.hasMessage("Freetext service is not configured. Operation didn't run.");
	}

	static void runAppWithStartupHook(String[] args, Consumer<BaseApp> startupHook) {
		App app = new App();
		app.setStartupHook(startupHook);
		try {
			app.run(args);
		} catch (CommandFailureException e) {
			// expected
		}
	}

	/**
	 * The CLI resets Logback logging, so our log hook needs to run inside the app.
	 */
	Consumer<BaseApp> getLoggingStartupHook() {
		return (unused) -> {
			myAppLogCapture = new LogbackTestExtension((Logger) BaseApp.ourLog);
			myAppLogCapture.setUp();
		};
	}
}
