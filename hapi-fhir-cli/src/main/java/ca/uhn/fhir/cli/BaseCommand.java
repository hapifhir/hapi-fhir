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

	protected IGenericClient newClient(FhirContext ctx) {
		IGenericClient fhirClient = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
		return fhirClient;
	}

	public abstract Options getOptions();

	public abstract String getCommandName();

	public abstract void run(CommandLine theCommandLine) throws ParseException;

	@Override
	public int compareTo(BaseCommand theO) {
		return getCommandName().compareTo(theO.getCommandName());
	}

}