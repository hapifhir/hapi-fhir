package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class AbstractImportExportCsvConceptMapCommand extends BaseCommand {
	protected static final String DEFAULT_PATH = "./";
	protected static final String CONCEPTMAP_URL_PARAM = "u";
	protected static final String FILENAME_PARAM = "f";
	protected static final String PATH_PARAM = "p";

	protected IGenericClient client;
	protected String conceptMapUrl;
	protected FhirVersionEnum fhirVersion;
	protected String filename;
	protected String path;

	@Override
	protected void addFhirVersionOption(Options theOptions) {
		String versions = Arrays.stream(FhirVersionEnum.values())
			.filter(t -> t != FhirVersionEnum.DSTU2_1 && t != FhirVersionEnum.DSTU2_HL7ORG && t != FhirVersionEnum.DSTU2)
			.map(t -> t.name().toLowerCase())
			.sorted()
			.collect(Collectors.joining(", "));
		addRequiredOption(theOptions, FHIR_VERSION_OPTION, "fhir-version", "version", "The FHIR version being used. Valid values: " + versions);
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException, ExecutionException {
		parseFhirContext(theCommandLine);
		FhirContext ctx = getFhirContext();

		String targetServer = theCommandLine.getOptionValue(BASE_URL_PARAM);
		if (isBlank(targetServer)) {
			throw new ParseException("No target server (-" + BASE_URL_PARAM + ") specified");
		} else if (!targetServer.startsWith("http") && !targetServer.startsWith("file")) {
			throw new ParseException("Invalid target server specified, must begin with 'http' or 'file'");
		}

		conceptMapUrl = theCommandLine.getOptionValue(CONCEPTMAP_URL_PARAM);
		if (isBlank(conceptMapUrl)) {
			throw new ParseException("No ConceptMap URL (" + CONCEPTMAP_URL_PARAM + ") specified");
		}

		filename = theCommandLine.getOptionValue(FILENAME_PARAM);
		if (isBlank(filename)) {
			throw new ParseException("No filename (" + FILENAME_PARAM + ") specified");
		}
		if (!filename.endsWith(".csv")) {
			filename = filename.concat(".csv");
		}

		path = theCommandLine.getOptionValue(PATH_PARAM);
		if (isBlank(path)) {
			path = DEFAULT_PATH;
		}

		client = super.newClient(theCommandLine);
		fhirVersion = ctx.getVersion().getVersion();
		if (fhirVersion != FhirVersionEnum.DSTU3
			&& fhirVersion != FhirVersionEnum.R4) {
			throw new ParseException("This command does not support FHIR version " + fhirVersion);
		}

		if (theCommandLine.hasOption('v')) {
			client.registerInterceptor(new LoggingInterceptor(true));
		}

		process();
	}

	protected abstract void process() throws ParseException;

	protected enum Header {
		CONCEPTMAP_URL,
		SOURCE_VALUE_SET,
		TARGET_VALUE_SET,
		SOURCE_CODE_SYSTEM,
		SOURCE_CODE_SYSTEM_VERSION,
		TARGET_CODE_SYSTEM,
		TARGET_CODE_SYSTEM_VERSION,
		SOURCE_CODE,
		SOURCE_DISPLAY,
		TARGET_CODE,
		TARGET_DISPLAY,
		EQUIVALENCE,
		COMMENT
	}
}
