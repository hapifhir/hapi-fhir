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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.fusesource.jansi.Ansi.ansi;

public abstract class BaseCommand implements Comparable<BaseCommand> {
	public static final String PROMPT = "PROMPT";
	protected static final String BASE_URL_PARAM = "t";
	protected static final String BASE_URL_PARAM_LONGOPT = "target";
	protected static final String BASE_URL_PARAM_NAME = "target";
	protected static final String BASE_URL_PARAM_DESC = "Base URL for the target server (e.g. \"http://example.com/fhir\").";
	protected static final String BASIC_AUTH_PARAM = "b";
	protected static final String BASIC_AUTH_PARAM_LONGOPT = "basic-auth";
	protected static final String BASIC_AUTH_PARAM_NAME = "basic-auth";
	protected static final String BASIC_AUTH_PARAM_DESC = "If specified, this parameter supplies a username and password (in the format \"username:password\") to include in an HTTP Basic Auth header. The value \"PROMPT\" may also be used to specify that an interactive prompt should request credentials from the user.";
	protected static final String BEARER_TOKEN_PARAM_LONGOPT = "bearer-token";
	protected static final String BEARER_TOKEN_PARAM_NAME = "bearer-token";
	protected static final String BEARER_TOKEN_PARAM_DESC = "If specified, this parameter supplies a Bearer Token to supply with the request. The value \"PROMPT\" may also be used to specify that an interactive prompt should request a Bearer Token from the user.";
	protected static final String FHIR_VERSION_PARAM = "v";
	protected static final String FHIR_VERSION_PARAM_LONGOPT = "fhir-version";
	protected static final String FHIR_VERSION_PARAM_NAME = "version";
	protected static final String FHIR_VERSION_PARAM_DESC = "The FHIR version being used. Valid values: ";
	protected static final String VERBOSE_LOGGING_PARAM = "l";
	protected static final String VERBOSE_LOGGING_PARAM_LONGOPT = "logging";
	protected static final String VERBOSE_LOGGING_PARAM_DESC = "If specified, verbose logging will be used.";
	protected static final int DEFAULT_THREAD_COUNT = 10;
	protected static final String THREAD_COUNT = "thread-count";

	// TODO: Don't use qualified names for loggers in HAPI CLI.
	private static final Logger ourLog = LoggerFactory.getLogger(BaseCommand.class);
	protected FhirContext myFhirCtx;

	public BaseCommand() {
		super();
	}

	protected void addBaseUrlOption(Options theOptions) {
		addRequiredOption(theOptions, BASE_URL_PARAM, BASE_URL_PARAM_LONGOPT, BASE_URL_PARAM_NAME, BASE_URL_PARAM_DESC);
	}

	protected void addBasicAuthOption(Options theOptions) {
		addOptionalOption(theOptions, BASIC_AUTH_PARAM, BASIC_AUTH_PARAM_LONGOPT, BASIC_AUTH_PARAM_NAME, BASIC_AUTH_PARAM_DESC);
		addOptionalOption(theOptions, null, BEARER_TOKEN_PARAM_LONGOPT, BEARER_TOKEN_PARAM_NAME, BEARER_TOKEN_PARAM_DESC);
	}

	protected void addThreadCountOption(Options theOptions) {
		addOptionalOption(theOptions, null, THREAD_COUNT, "count", "If specified, this argument specifies the number of worker threads used (default is " + DEFAULT_THREAD_COUNT + ")");
	}


	protected String promptUser(String thePrompt) throws ParseException {
		System.out.print(ansi().bold().fgBrightDefault());
		System.out.print(thePrompt);
		System.out.print(ansi().boldOff().fgBlack().bgDefault());
		System.out.flush();

		Console console = System.console();
		String retVal;
		if (console == null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				retVal = reader.readLine();
			} catch (IOException e) {
				throw new ParseException(Msg.code(1566) + "Failed to read input from user: " + e);
			}
		} else {
			retVal = new String(console.readPassword());
		}

		System.out.print(ansi().boldOff().fgDefault().bgDefault());

		return retVal;
	}

	protected Collection<Object> getFilterOutVersions() {
		return Sets.newHashSet(FhirVersionEnum.DSTU2_1, FhirVersionEnum.DSTU2_HL7ORG);
	}

	protected void addFhirVersionOption(Options theOptions) {
		String versions = Arrays.stream(FhirVersionEnum.values())
			.filter(t -> ! getFilterOutVersions().contains(t))
			.map(t -> t.name().toLowerCase())
			.sorted()
			.collect(Collectors.joining(", "));
		addRequiredOption(theOptions, FHIR_VERSION_PARAM, FHIR_VERSION_PARAM_LONGOPT, FHIR_VERSION_PARAM_NAME, FHIR_VERSION_PARAM_DESC + versions);
	}


	private void addOption(Options theOptions, OptionGroup theOptionGroup, boolean theRequired, String theOpt, String theLongOpt, boolean theHasArgument, String theArgumentName, String theDescription) {
		Option option = createOption(theRequired, theOpt, theLongOpt, theHasArgument, theDescription);
		if (theHasArgument && isNotBlank(theArgumentName)) {
			option.setArgName(theArgumentName);
		}

		if (isNotBlank(theOpt)) {
			if (theOptions.getOption(theOpt) != null) {
				throw new IllegalStateException(Msg.code(1567) + "Duplicate option: " + theOpt);
			}
			if (theOptionGroup != null && theOptionGroup.getOptions().stream().anyMatch(t -> theOpt.equals(t.getOpt()))) {
				throw new IllegalStateException(Msg.code(1568) + "Duplicate option: " + theOpt);
			}
		}
		if (isNotBlank(theLongOpt)) {
			if (theOptions.getOption(theLongOpt) != null) {
				throw new IllegalStateException(Msg.code(1569) + "Duplicate option: " + theLongOpt);
			}
			if (theOptionGroup != null && theOptionGroup.getOptions().stream().anyMatch(t -> theLongOpt.equals(t.getLongOpt()))) {
				throw new IllegalStateException(Msg.code(1570) + "Duplicate option: " + theOpt);
			}
		}

		if (theOptionGroup != null) {
			theOptionGroup.addOption(option);
		} else {
			theOptions.addOption(option);
		}
	}

	protected void addOptionalOption(Options theOptions, String theOpt, String theLong, boolean theTakesArgument, String theDescription) {
		addOption(theOptions, null, false, theOpt, theLong, theTakesArgument, null, theDescription);
	}

	protected void addOptionalOption(Options theOptions, String theOpt, String theLong, String theArgumentName, String theDescription) {
		addOption(theOptions, null, false, theOpt, theLong, isNotBlank(theArgumentName), theArgumentName, theDescription);
	}

	protected void addOptionalOption(Options theOptions, OptionGroup theOptionGroup, String theOpt, String theLong, String theArgumentName, String theDescription) {
		addOption(theOptions, theOptionGroup, false, theOpt, theLong, isNotBlank(theArgumentName), theArgumentName, theDescription);
	}

	protected void addRequiredOption(Options theOptions, String theOpt, String theLong, boolean theTakesArgument, String theDescription) {
		addOption(theOptions, null, true, theOpt, theLong, theTakesArgument, null, theDescription);
	}

	protected void addRequiredOption(Options theOptions, String theOpt, String theLong, String theArgumentName, String theDescription) {
		addOption(theOptions, null, true, theOpt, theLong, isNotBlank(theArgumentName), theArgumentName, theDescription);
	}

	protected void addVerboseLoggingOption(Options theOptions) {
		addOptionalOption(theOptions, VERBOSE_LOGGING_PARAM, VERBOSE_LOGGING_PARAM_LONGOPT, false, VERBOSE_LOGGING_PARAM_DESC);
	}

	/**
	 * Subclasses may override if they want, to do any cleanup they need to do.
	 */
	public void cleanup() {
		// nothing
	}

	@Override
	public int compareTo(BaseCommand theO) {
		return getCommandName().compareTo(theO.getCommandName());
	}

	private Option createOption(boolean theRequired, String theOpt, String theLong, boolean theHasArgument, String theDescription) {
		Option option = new Option(theOpt, theLong, theHasArgument, theDescription);
		option.setRequired(theRequired);
		return option;
	}

	protected Reader createReader(File theInputFile) throws IOException {
		InputStream inputStream = new FileInputStream(theInputFile);
		if (theInputFile.getName().toLowerCase().endsWith(".gz")) {
			inputStream = new GZIPInputStream(inputStream);
		}
		if (theInputFile.getName().toLowerCase().endsWith(".bz2")) {
			inputStream = new BZip2CompressorInputStream(inputStream);
		}

		return new InputStreamReader(inputStream, Charsets.UTF_8);
	}

	private void downloadFileFromInternet(CloseableHttpResponse result, File localFile) throws IOException {
		FileOutputStream buffer = FileUtils.openOutputStream(localFile);
		try {

			long maxLength = result.getEntity().getContentLength();
			long nextLog = -1;
			// ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[16384];
			while ((nRead = result.getEntity().getContent().read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
				long fileSize = FileUtils.sizeOf(localFile);
				if (fileSize > nextLog) {
					System.err.print("\r" + ansi().eraseLine());
					System.err.print(FileUtils.byteCountToDisplaySize(fileSize));
					if (maxLength > 0) {
						System.err.print(" [");
						int stars = (int) (50.0f * ((float) fileSize / (float) maxLength));
						for (int i = 0; i < stars; i++) {
							System.err.print("*");
						}
						for (int i = stars; i < 50; i++) {
							System.err.print(" ");
						}
						System.err.print("]");
					}
					System.err.flush();
					nextLog += 100000;
				}
			}
			buffer.flush();

			System.err.println();
			System.err.flush();
		} finally {
			IOUtils.closeQuietly(buffer);
		}
	}

	/**
	 * @return Returns the complete authorization header value using the "-b" option
	 */
	protected String getAndParseOptionBasicAuthHeader(CommandLine theCommandLine) throws ParseException {
		return getAndParseOptionBasicAuthHeader(theCommandLine, BASIC_AUTH_PARAM);
	}

	/**
	 * @return Returns the complete authorization header value using an arbitrary option
	 */
	protected String getAndParseOptionBasicAuthHeader(CommandLine theCommandLine, String theOptionName) throws ParseException {
		String basicAuthHeaderValue = null;
		if (theCommandLine.hasOption(theOptionName)) {
			String optionValue = theCommandLine.getOptionValue(theOptionName);
			if (PROMPT.equals(optionValue)) {
				optionValue = promptUser("Enter Basic Auth Credentials (format is \"username:password\"): ");
				optionValue = trim(optionValue);
			}

			byte[] basicAuth = optionValue.getBytes();
			String base64EncodedBasicAuth = Base64Utils.encodeToString(basicAuth);
			basicAuthHeaderValue = Constants.HEADER_AUTHORIZATION_VALPREFIX_BASIC + base64EncodedBasicAuth;
		}
		return basicAuthHeaderValue;
	}


	protected Pair<String, String> parseNameValueParameter(
			String separator, String theParamName, String theParam) throws ParseException {

		String errorMsg = "Parameter " + theParamName + " must be in the format: \"name:value\"";

		if (! theParam.contains(separator)) {
			throw new ParseException(Msg.code(1571) + errorMsg);
		}

		String[] nameValue = theParam.split(separator);
		if (nameValue.length != 2) {
			throw new ParseException(Msg.code(1572) + errorMsg);
		}

		if (StringUtils.isBlank(nameValue[0]) || StringUtils.isBlank(nameValue[1])) {
			throw new ParseException(Msg.code(1573) + errorMsg);
		}

		return Pair.of(nameValue[0], nameValue[1]);
	}


	public <T extends Enum> T getAndParseOptionEnum(CommandLine theCommandLine, String theOption, Class<T> theEnumClass, boolean theRequired, T theDefault) throws ParseException {
		String val = theCommandLine.getOptionValue(theOption);
		if (isBlank(val)) {
			if (theRequired && theDefault == null) {
				throw new ParseException(Msg.code(1574) + "Missing required option -" + theOption);
			}
			return theDefault;
		}
		try {
			return (T) Enum.valueOf(theEnumClass, val);
		} catch (Exception e) {
			throw new ParseException(Msg.code(1575) + "Invalid option \"" + val + "\" for option -" + theOption);
		}
	}

	public Integer getAndParseNonNegativeIntegerParam(CommandLine theCommandLine, String theName) throws ParseException {
		int minimum = 0;
		return doGetAndParseIntegerParam(theCommandLine, theName, minimum);
	}

	public Integer getAndParsePositiveIntegerParam(CommandLine theCommandLine, String theName) throws ParseException {
		int minimum = 1;
		return doGetAndParseIntegerParam(theCommandLine, theName, minimum);
	}

	@Nullable
	private Integer doGetAndParseIntegerParam(CommandLine theCommandLine, String theName, int minimum) throws ParseException {
		String value = theCommandLine.getOptionValue(theName);
		value = trim(value);
		if (isBlank(value)) {
			return null;
		}

		try {
			int valueInt = Integer.parseInt(value);
			if (valueInt < minimum) {
				throw new ParseException(Msg.code(1576) + "Value for argument " + theName + " must be an integer >= " + minimum + ", got: " + value);
			}
			return valueInt;
		} catch (NumberFormatException e) {
			throw new ParseException(Msg.code(1577) + "Value for argument " + theName + " must be an integer >= " + minimum + ", got: " + value);
		}
	}

	public Class<? extends IBaseBundle> getBundleTypeForFhirVersion() {
		return getFhirContext().getResourceDefinition("Bundle").getImplementingClass(IBaseBundle.class);
	}

	protected int getThreadCount(CommandLine theCommandLine) throws ParseException {
		Integer parallelismThreadCount = getAndParsePositiveIntegerParam(theCommandLine, THREAD_COUNT);
		parallelismThreadCount = ObjectUtils.defaultIfNull(parallelismThreadCount, DEFAULT_THREAD_COUNT);
		return parallelismThreadCount.intValue();
	}

	public abstract String getCommandDescription();

	public abstract String getCommandName();

	protected FhirContext getFhirContext() {
		return myFhirCtx;
	}

	public abstract Options getOptions();

	protected Collection<File> loadFile(String theSpecUrl, String theFilepath, boolean theCacheFile) throws IOException {
		String userHomeDir = System.getProperty("user.home");

		File applicationDir = new File(userHomeDir + File.separator + "." + "hapi-fhir-cli");
		FileUtils.forceMkdir(applicationDir);

		Collection<File> inputFiles;
		if (isNotBlank(theFilepath)) {
			ourLog.info("Loading from local path: {}", theFilepath);

			if (theFilepath.startsWith("~" + File.separator)) {
				theFilepath = userHomeDir + theFilepath.substring(1);
			}

			File suppliedFile = new File(FilenameUtils.normalize(theFilepath));

			if (suppliedFile.isDirectory()) {
				inputFiles = FileUtils.listFiles(suppliedFile, new String[]{"zip"}, false);
			} else {
				inputFiles = Collections.singletonList(suppliedFile);
			}

		} else {

			File cacheDir = new File(applicationDir, "cache");
			FileUtils.forceMkdir(cacheDir);

			File inputFile = new File(cacheDir, "examples-json-" + getFhirContext().getVersion().getVersion() + ".zip");

			Date cacheExpiryDate = DateUtils.addHours(new Date(), -12);

			if (!inputFile.exists() | (theCacheFile && FileUtils.isFileOlder(inputFile, cacheExpiryDate))) {

				File exampleFileDownloading = new File(cacheDir, "examples-json-" + getFhirContext().getVersion().getVersion() + ".zip.partial");

				HttpGet get = new HttpGet(theSpecUrl);
				CloseableHttpClient client = HttpClientBuilder.create().build();
				CloseableHttpResponse result = client.execute(get);

				if (result.getStatusLine().getStatusCode() != 200) {
					throw new CommandFailureException(Msg.code(1578) + "Got HTTP " + result.getStatusLine().getStatusCode() + " response code loading " + theSpecUrl);
				}

				ourLog.info("Downloading from remote url: {}", theSpecUrl);
				downloadFileFromInternet(result, exampleFileDownloading);

				FileUtils.deleteQuietly(inputFile);
				FileUtils.moveFile(exampleFileDownloading, inputFile);

				if (!theCacheFile) {
					inputFile.deleteOnExit();
				}

				ourLog.info("Successfully Loaded example pack ({})", FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(inputFile)));
				IOUtils.closeQuietly(result.getEntity().getContent());
			}

			inputFiles = Collections.singletonList(inputFile);

		}
		return inputFiles;
	}

	protected IGenericClient newClient(CommandLine theCommandLine) throws ParseException {
		return newClient(theCommandLine, BASE_URL_PARAM, BASIC_AUTH_PARAM, BEARER_TOKEN_PARAM_LONGOPT);
	}

	protected IGenericClient newClient(CommandLine theCommandLine, String theBaseUrlParamName, String theBasicAuthOptionName, String theBearerTokenOptionName) throws ParseException {
		String baseUrl = theCommandLine.getOptionValue(theBaseUrlParamName);
		if (isBlank(baseUrl)) {
			throw new ParseException(Msg.code(1579) + "No target server (-" + BASE_URL_PARAM + ") specified.");
		} else if (!baseUrl.startsWith("http") && !baseUrl.startsWith("file")) {
			throw new ParseException(Msg.code(1580) + "Invalid target server specified, must begin with 'http' or 'file'.");
		}

		return newClientWithBaseUrl(theCommandLine, baseUrl, theBasicAuthOptionName, theBearerTokenOptionName);
	}

	protected IGenericClient newClientWithBaseUrl(CommandLine theCommandLine, String theBaseUrl, String theBasicAuthOptionName, String theBearerTokenOptionName) throws ParseException {
		myFhirCtx.getRestfulClientFactory().setSocketTimeout((int) DateUtils.MILLIS_PER_HOUR);
		IGenericClient retVal = myFhirCtx.newRestfulGenericClient(theBaseUrl);

		String basicAuthHeaderValue = getAndParseOptionBasicAuthHeader(theCommandLine, theBasicAuthOptionName);
		if (isNotBlank(basicAuthHeaderValue)) {
			retVal.registerInterceptor(new SimpleRequestHeaderInterceptor(Constants.HEADER_AUTHORIZATION, basicAuthHeaderValue));
		}

		if (isNotBlank(theBearerTokenOptionName)) {
			String bearerToken = getAndParseBearerTokenAuthHeader(theCommandLine, theBearerTokenOptionName);
			if (isNotBlank(bearerToken)) {
				retVal.registerInterceptor(new SimpleRequestHeaderInterceptor(Constants.HEADER_AUTHORIZATION, Constants.HEADER_AUTHORIZATION_VALPREFIX_BEARER + bearerToken));
			}
		}

		return retVal;
	}

	private String getAndParseBearerTokenAuthHeader(CommandLine theCommandLine, String theBearerTokenOptionName) throws ParseException {
		String value = theCommandLine.getOptionValue(theBearerTokenOptionName);
		if (PROMPT.equals(value)) {
			return promptUser("Enter Bearer Token: ");
		}
		return value;
	}

	protected void parseFhirContext(CommandLine theCommandLine) throws ParseException {
		String version = theCommandLine.getOptionValue(FHIR_VERSION_PARAM);
		if (isBlank(version)) {
			throw new ParseException(Msg.code(1581) + "Missing required option: -" + FHIR_VERSION_PARAM);
		}

		try {
			FhirVersionEnum versionEnum = FhirVersionEnum.valueOf(version.toUpperCase());
			myFhirCtx = versionEnum.newContext();
		} catch (Exception e) {
			throw new ParseException(Msg.code(1582) + "Invalid FHIR version string: " + version);
		}
	}


	public abstract void run(CommandLine theCommandLine) throws ParseException, ExecutionException;

	public List<String> provideUsageNotes() {
		return Collections.emptyList();
	}
}
