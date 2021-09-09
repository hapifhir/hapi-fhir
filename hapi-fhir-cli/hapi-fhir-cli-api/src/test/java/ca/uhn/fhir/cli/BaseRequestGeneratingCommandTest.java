package ca.uhn.fhir.cli;

import ca.uhn.fhir.cli.BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions;
import com.google.common.collect.Lists;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.cli.BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions.BASIC_AUTH;
import static ca.uhn.fhir.cli.BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions.HEADER_PASSTHROUGH;
import static ca.uhn.fhir.cli.BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions.VERSION;
import static ca.uhn.fhir.cli.BaseRequestGeneratingCommand.BaseRequestGeneratingCommandOptions.values;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseRequestGeneratingCommandTest {

	private final BaseRequestGeneratingCommand tested = new BaseRequestGeneratingCommandChild();

	private final List<BaseRequestGeneratingCommandOptions> allOptions =
		Arrays.asList(values());

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

	@ParameterizedTest(name = "Excluding {0}")
	@EnumSource(value = BaseRequestGeneratingCommandOptions.class,
		names = {"VERSION", "BASE_URL", "BASIC_AUTH", "VERBOSE_LOGGING", "HEADER_PASSTHROUGH"})
	void getSomeOptionsExcludingOne(BaseRequestGeneratingCommandOptions excludedOption) {
		Collection<BaseRequestGeneratingCommandOptions> excludeOptions = Collections.singleton(excludedOption);

		Options options = tested.getSomeOptions(excludeOptions);

		// BASIC_AUTH exclusion excludes 2 options
		int expectedSize = excludedOption == BASIC_AUTH ? 4 : 5;
		assertEquals(expectedSize, options.getOptions().size());

		assertFalse(options.hasShortOption(getOptionForExcludedOption(excludedOption)));
		if (excludedOption == BASIC_AUTH) {
			assertFalse(options.hasLongOption(BaseCommand.BEARER_TOKEN_PARAM_LONGOPT));
		}

		Arrays.stream(values())
			.filter(excludeOptValue -> ! excludeOptValue.equals(excludedOption))
			.forEach(excludeOptValue -> {
				assertTrue(options.hasShortOption(getOptionForExcludedOption(excludeOptValue)));
				// BASIC_AUTH option carries additional BEARER_TOKEN option
				if (excludedOption != BASIC_AUTH) {
					assertTrue(options.hasLongOption(BaseCommand.BEARER_TOKEN_PARAM_LONGOPT));
				}
			});
	}


	private String getOptionForExcludedOption(BaseRequestGeneratingCommandOptions excludeOption) {
		switch (excludeOption) {
			case VERSION:
				return BaseCommand.FHIR_VERSION_PARAM;

			case BASE_URL:
				return BaseCommand.BASE_URL_PARAM;

			case BASIC_AUTH:
				return BaseCommand.BASIC_AUTH_PARAM;

			case VERBOSE_LOGGING:
				return BaseCommand.VERBOSE_LOGGING_PARAM;

			case HEADER_PASSTHROUGH:
				return BaseRequestGeneratingCommand.HEADER_PASSTHROUGH;
		}
		throw new InvalidParameterException("unexpected exclude option " + excludeOption);
	}

	@Test
	void getSomeOptionsExcludeTwo() {
		Options options = tested.getSomeOptions(Lists.newArrayList(VERSION, HEADER_PASSTHROUGH));

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
