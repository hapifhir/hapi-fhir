package ca.uhn.fhir.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;

public abstract class BaseCommand implements Comparable<BaseCommand> {

	public BaseCommand() {
		super();
	}

	@Override
	public int compareTo(BaseCommand theO) {
		return getCommandName().compareTo(theO.getCommandName());
	}

	public abstract String getCommandDescription();

	public abstract String getCommandName();

	public abstract Options getOptions();

	protected IGenericClient newClient(FhirContext ctx, String theBaseUrl) {
		ctx.getRestfulClientFactory().setSocketTimeout(10 * 60 * 1000);
		IGenericClient fhirClient = ctx.newRestfulGenericClient(theBaseUrl);
		return fhirClient;
	}

	public abstract void run(CommandLine theCommandLine) throws ParseException, Exception;

}