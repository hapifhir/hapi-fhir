package ca.uhn.fhir.cli;

import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.concurrent.ExecutionException;

public class ToggleSearchParametersCommand extends BaseCommand {

	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public String getCommandName() {
		return null;
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		addFhirVersionOption(options);
		addBaseUrlOption(options);
		addRequiredOption(options, "u", "url", true, "The code system URL associated with this upload (e.g. " + IHapiTerminologyLoaderSvc.SCT_URI + ")");
		addBasicAuthOption(options);
		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException, ExecutionException {

	}

}
