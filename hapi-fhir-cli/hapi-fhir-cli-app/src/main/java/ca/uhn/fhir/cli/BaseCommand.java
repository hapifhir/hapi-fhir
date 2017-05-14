package ca.uhn.fhir.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.IGenericClient;

public abstract class BaseCommand implements Comparable<BaseCommand> {

	private static final String SPEC_DEFAULT_VERSION = "dstu3";

	private FhirContext myFhirCtx;

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

//	public FhirContext getFhirCtx() {
//		if (myFhirCtx == null) {
//			myFhirCtx = FhirContext.forDstu2();
//		}
//		return myFhirCtx;
//	}

	protected void addFhirVersionOption(Options theOptions) {
		Option opt = new Option("f", "fhirversion", true, "Spec version to upload (default is '" + SPEC_DEFAULT_VERSION + "')");
		opt.setRequired(false);
		theOptions.addOption(opt);
	}

	protected FhirContext getSpecVersionContext(CommandLine theCommandLine) throws ParseException {
		if (myFhirCtx == null) {
			String specVersion = theCommandLine.getOptionValue("f", SPEC_DEFAULT_VERSION);
			specVersion = specVersion.toLowerCase();
			FhirVersionEnum version;
			if ("dstu2".equals(specVersion)) {
				version = FhirVersionEnum.DSTU2;
			} else if ("dstu3".equals(specVersion)) {
				version = FhirVersionEnum.DSTU3;
			} else {
				throw new ParseException("Unknown spec version: " + specVersion);
			}

			myFhirCtx = new FhirContext(version);
		}
		return myFhirCtx;
	}

}