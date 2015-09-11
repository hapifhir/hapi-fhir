package ca.uhn.fhir.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;

public class BaseCommand {

	public BaseCommand() {
		super();
	}

	protected IGenericClient newClient(FhirContext ctx) {
		IGenericClient fhirClient = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
		return fhirClient;
	}

	public Options getOptions() {
		return null;
	}

	public String getCommandName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void run(CommandLine theCommandLine) throws ParseException {
		// TODO Auto-generated method stub
		
	}

}