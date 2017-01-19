package ca.uhn.fhir.cli;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.instance.model.api.IBaseParameters;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

public class UploadTerminologyCommand extends BaseCommand {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UploadTerminologyCommand.class);

	@Override
	public String getCommandDescription() {
		//@formatter:off
		return "Uploads a terminology package (e.g. a SNOMED CT ZIP file) to a HAPI JPA server. "
				+ "Note that this command uses a custom operation that is only implemented on HAPI "
				+ "JPA servers that have been configured to accept it.";
		//@formatter:on
	}

	@Override
	public String getCommandName() {
		return "upload-terminology";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		Option opt;

		addFhirVersionOption(options);

		opt = new Option("t", "target", true, "Base URL for the target server (e.g. \"http://example.com/fhir\")");
		opt.setRequired(true);
		options.addOption(opt);

		opt = new Option("u", "url", true, "The code system URL associated with this upload (e.g. " + IHapiTerminologyLoaderSvc.SCT_URL + ")");
		opt.setRequired(false);
		options.addOption(opt);

		opt = new Option("d", "data", true, "Local *.zip containing file to use to upload");
		opt.setRequired(false);
		options.addOption(opt);

		opt = new Option("b", "bearer-token", true, "Bearer token to add to the request");
		opt.setRequired(false);
		options.addOption(opt);

		opt = new Option("v", "verbose", false, "Verbose output");
		opt.setRequired(false);
		options.addOption(opt);
		
		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws Exception {
		FhirContext ctx = getSpecVersionContext(theCommandLine);

		String targetServer = theCommandLine.getOptionValue("t");
		if (isBlank(targetServer)) {
			throw new ParseException("No target server (-t) specified");
		} else if (targetServer.startsWith("http") == false && targetServer.startsWith("file") == false) {
			throw new ParseException("Invalid target server specified, must begin with 'http' or 'file'");
		}

		String termUrl = theCommandLine.getOptionValue("u");
		if (isBlank(termUrl)) {
			throw new ParseException("No URL provided");
		}
		
		String[] datafile = theCommandLine.getOptionValues("d");
		if (datafile == null || datafile.length == 0) {
			throw new ParseException("No data file provided");
		}
		
		String bearerToken = theCommandLine.getOptionValue("b");

		IGenericClient client = super.newClient(ctx, targetServer);
		IBaseParameters inputParameters;
		if (ctx.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			Parameters p = new Parameters();
			p.addParameter().setName("url").setValue(new UriType(termUrl));
			for (String next : datafile) {
				p.addParameter().setName("localfile").setValue(new StringType(next));
			}
			inputParameters = p;
		} else {
			throw new ParseException("This command does not support FHIR version " + ctx.getVersion().getVersion());
		}

		if (isNotBlank(bearerToken)) {
			client.registerInterceptor(new BearerTokenAuthInterceptor(bearerToken));
		}

		if (theCommandLine.hasOption('v')) {
			client.registerInterceptor(new LoggingInterceptor(true));
		}
		
		ourLog.info("Beginning upload - This may take a while...");
		IBaseParameters response = client
			.operation()
			.onServer()
			.named("upload-external-code-system")
			.withParameters(inputParameters)
			.execute();

		ourLog.info("Upload complete!");
		ourLog.info("Response:\n{}", ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
	}

}
