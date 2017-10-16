package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.igpacks.parser.IgPackParserDstu3;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

public class IgPackUploader extends BaseCommand {
	private static final Logger ourLog = LoggerFactory.getLogger(IgPackUploader.class);

	@Override
	public String getCommandDescription() {
		return "Uploads an Implementation Guide Validation Pack";
	}

	@Override
	public String getCommandName() {
		return "upload-igpack";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		addFhirVersionOption(options);

		Option opt = new Option("t", "target", true, "Base URL for the target server (e.g. \"http://example.com/fhir\")");
		opt.setRequired(false);
		options.addOption(opt);

		opt = new Option("u", "url", true, "The URL to the validation.pack file, e.g. http://hl7.org/fhir/us/core/validator.pack");
		opt.setRequired(true);
		options.addOption(opt);

		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException, Exception {
		FhirContext ctx = getSpecVersionContext(theCommandLine);

		String targetServer = theCommandLine.getOptionValue("t");
		IGenericClient client = ctx.newRestfulGenericClient(targetServer);

		String url = theCommandLine.getOptionValue("u");

		Collection<File> files = loadFile(ctx, url, null, false);
		for (File nextFile : files) {
			switch (ctx.getVersion().getVersion()) {
				case DSTU3:
					IgPackParserDstu3 packParser = new IgPackParserDstu3(ctx);
					IValidationSupport ig = packParser.parseIg(new FileInputStream(nextFile), nextFile.getName());
					Iterable<IBaseResource> conformanceResources = ig.fetchAllConformanceResources(ctx);
					for (IBaseResource nextResource : conformanceResources) {
						String nextResourceUrl = ((IPrimitiveType<?>)ctx.newTerser().getSingleValueOrNull(nextResource, "url")).getValueAsString();
						ourLog.info("Uploading resource: {}", nextResourceUrl);
						client
							.update()
							.resource(nextResource)
							.conditional()
							.and(StructureDefinition.URL.matches().value(nextResourceUrl))
							.execute();
					}
					break;
				default:
					throw new ParseException("This command does not support FHIR version " + ctx.getVersion().getVersion());
			}
		}
	}
}
