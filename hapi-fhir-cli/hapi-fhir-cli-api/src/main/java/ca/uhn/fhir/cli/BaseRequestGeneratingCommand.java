package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.AdditionalRequestHeadersInterceptor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class BaseRequestGeneratingCommand extends BaseCommand {

	public enum BaseRequestGeneratingCommandOptions {
		VERSION,
		BASE_URL,
		BASIC_AUTH,
		VERBOSE_LOGGING,
		HEADER_PASSTHROUGH
	}


	protected static final String HEADER_PASSTHROUGH = "hp";
	protected static final String HEADER_PASSTHROUGH_NAME = "header";
	protected static final String HEADER_PASSTHROUGH_LONGOPT = "header-passthrough";


	@Override
	public Options getOptions() {
		return getSomeOptions(Collections.emptySet());
	}

	/**
	 * Allows child classes to obtain a subset of the parent-defined options
	 */
	protected Options getSomeOptions(Collection<BaseRequestGeneratingCommandOptions> theExcludeOptions) {
		Options options = new Options();

		if (! theExcludeOptions.contains(BaseRequestGeneratingCommandOptions.VERSION)) {
			addFhirVersionOption(options);
		}

		if (! theExcludeOptions.contains(BaseRequestGeneratingCommandOptions.BASE_URL)) {
			addBaseUrlOption(options);
		}

		if (! theExcludeOptions.contains(BaseRequestGeneratingCommandOptions.BASIC_AUTH)) {
			addBasicAuthOption(options);
		}

		if (! theExcludeOptions.contains(BaseRequestGeneratingCommandOptions.VERBOSE_LOGGING)) {
			addVerboseLoggingOption(options);
		}

		if (! theExcludeOptions.contains(BaseRequestGeneratingCommandOptions.HEADER_PASSTHROUGH)) {
			addHeaderPassthroughOption(options);
		}

		return options;
	}


	@Override
	protected IGenericClient newClientWithBaseUrl(CommandLine theCommandLine, String theBaseUrl,
			String theBasicAuthOptionName, String theBearerTokenOptionName) throws ParseException {

		IGenericClient client = super.newClientWithBaseUrl(
			theCommandLine, theBaseUrl, theBasicAuthOptionName, theBearerTokenOptionName);
		registerHeaderPassthrough(theCommandLine, client);

		return client;
	}


	private void registerHeaderPassthrough(CommandLine theCommandLine, IGenericClient theClient) throws ParseException {
		if (theCommandLine.hasOption(HEADER_PASSTHROUGH)) {
			theClient.registerInterceptor(
				new AdditionalRequestHeadersInterceptor(
					getAndParseOptionHeadersPassthrough(theCommandLine, HEADER_PASSTHROUGH)));
		}

	}

	private void addHeaderPassthroughOption(Options theOptions) {
		addOptionalOption(theOptions, HEADER_PASSTHROUGH, HEADER_PASSTHROUGH_LONGOPT, HEADER_PASSTHROUGH_NAME,
			"If specified, this argument specifies headers to include in the generated request");
	}

	/**
	 * @return Returns the optional pass-through header name and value
	 */
	private Map<String, List<String>> getAndParseOptionHeadersPassthrough(
		CommandLine theCommandLine, String theOptionName) throws ParseException {

		if (! theCommandLine.hasOption(theOptionName)) {
			return Collections.emptyMap();
		}

		Map<String, List<String>> headersMap = new HashMap<>();
		for (String nextOptionValue: theCommandLine.getOptionValues(theOptionName)) {
			Pair<String, String> nextHeader = parseNameValueParameter(":", theOptionName, nextOptionValue);
			headersMap.compute(nextHeader.getKey(), (k, v) -> v == null ? new ArrayList<>() : v).add(nextHeader.getValue());
		}

		return headersMap;
	}

}
