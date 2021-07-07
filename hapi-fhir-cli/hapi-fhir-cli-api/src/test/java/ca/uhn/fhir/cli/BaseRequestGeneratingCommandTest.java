package ca.uhn.fhir.cli;

import com.google.common.collect.Lists;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseRequestGeneratingCommandTest {

	private final BaseRequestGeneratingCommand tested = new BaseRequestGeneratingCommandChild();

	private final List<BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions> allOptions =
		Arrays.asList(BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions.values());

	@Test
	void getOptions() {
		Options options = tested.getOptions();
		assertEquals(6, options.getOptions().size());
		assertTrue(options.hasShortOption(BaseCommand.FHIR_VERSION_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BASE_URL_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BASIC_AUTH_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BEARER_TOKEN_PARAM_NAME));
		assertTrue(options.hasShortOption(BaseCommand.VERBOSE_LOGGING_PARAM));
		assertTrue(options.hasShortOption(BaseRequestGeneratingCommand.HEADER_PASSTHROUGH));
	}

	@Test
	void getSomeOptionsNoVersion() {
		Options options = tested.getSomeOptions(
			Collections.singleton(BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions.VERSION));
		assertEquals(5, options.getOptions().size());
		assertTrue(options.hasShortOption(BaseCommand.BASE_URL_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BASIC_AUTH_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BEARER_TOKEN_PARAM_NAME));
		assertTrue(options.hasShortOption(BaseCommand.VERBOSE_LOGGING_PARAM));
		assertTrue(options.hasShortOption(BaseRequestGeneratingCommand.HEADER_PASSTHROUGH));
	}

	@Test
	void getSomeOptionsNoBaseUrl() {
		Options options = tested.getSomeOptions(
			Collections.singleton(BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions.BASE_URL));
		assertEquals(5, options.getOptions().size());
		assertTrue(options.hasShortOption(BaseCommand.FHIR_VERSION_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BASIC_AUTH_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BEARER_TOKEN_PARAM_NAME));
		assertTrue(options.hasShortOption(BaseCommand.VERBOSE_LOGGING_PARAM));
		assertTrue(options.hasShortOption(BaseRequestGeneratingCommand.HEADER_PASSTHROUGH));
	}

	@Test
	void getSomeOptionsNoBasicAuth() {
		Options options = tested.getSomeOptions(
			Collections.singleton(BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions.BASIC_AUTH));
		assertEquals(4, options.getOptions().size());
		assertTrue(options.hasShortOption(BaseCommand.FHIR_VERSION_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BASE_URL_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.VERBOSE_LOGGING_PARAM));
		assertTrue(options.hasShortOption(BaseRequestGeneratingCommand.HEADER_PASSTHROUGH));
	}

	@Test
	void getSomeOptionsNoVerboseLogging() {
		Options options = tested.getSomeOptions(
			Collections.singleton(BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions.VERBOSE_LOGGING));
		assertEquals(5, options.getOptions().size());
		assertTrue(options.hasShortOption(BaseCommand.FHIR_VERSION_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BASE_URL_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BASIC_AUTH_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BEARER_TOKEN_PARAM_NAME));
		assertTrue(options.hasShortOption(BaseRequestGeneratingCommand.HEADER_PASSTHROUGH));
	}

	@Test
	void getSomeOptionsNoHeaderPassthrough() {
		Options options = tested.getSomeOptions(
			Collections.singleton(BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions.HEADER_PASSTHROUGH));
		assertEquals(5, options.getOptions().size());
		assertTrue(options.hasShortOption(BaseCommand.FHIR_VERSION_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BASE_URL_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BASIC_AUTH_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BEARER_TOKEN_PARAM_NAME));
		assertTrue(options.hasShortOption(BaseCommand.VERBOSE_LOGGING_PARAM));
	}

	@Test
	void getSomeOptionsExcludeTwo() {
		Options options = tested.getSomeOptions(Lists.newArrayList(
				BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions.VERSION,
				BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions.HEADER_PASSTHROUGH));
		assertEquals(4, options.getOptions().size());
		assertTrue(options.hasShortOption(BaseCommand.BASE_URL_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BASIC_AUTH_PARAM));
		assertTrue(options.hasShortOption(BaseCommand.BEARER_TOKEN_PARAM_NAME));
		assertTrue(options.hasShortOption(BaseCommand.VERBOSE_LOGGING_PARAM));
	}


	private static class BaseRequestGeneratingCommandChild extends BaseRequestGeneratingCommand {

		@Override
		public String getCommandDescription() { return null; }

		@Override
		public String getCommandName() { return null; }

		@Override
		public void run(CommandLine theCommandLine) { }
	}
}
