package ca.uhn.fhir.cli;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.StructureDefinition;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.rest.client.IGenericClient;

public class ValidationDataUploader extends BaseCommand {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidationDataUploader.class);

	@Override
	public String getCommandDescription() {
		return "Uploads the conformance resources (StructureDefinition and ValueSet) from the official FHIR definitions.";
	}

	@Override
	public String getCommandName() {
		return "upload-definitions";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		Option opt;

		opt = new Option("t", "target", true, "Base URL for the target server (e.g. \"http://example.com/fhir\")");
		opt.setRequired(true);
		options.addOption(opt);

		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		String targetServer = theCommandLine.getOptionValue("t");
		if (isBlank(targetServer)) {
			throw new ParseException("No target server (-t) specified");
		} else if (targetServer.startsWith("http") == false) {
			throw new ParseException("Invalid target server specified, must begin with 'http'");
		}

		FhirContext ctx = FhirContext.forDstu2();
		ourLog.info("Uploading definitions to server: " + targetServer);

		IGenericClient client = newClient(ctx, targetServer);
		long start = System.currentTimeMillis();

		String vsContents;
		try {
			ctx.getVersion().getPathToSchemaDefinitions();
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/instance/model/valueset/"+"valuesets.xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(e.toString());
		}
		Bundle bundle = ctx.newXmlParser().parseResource(Bundle.class, vsContents);

		int total = bundle.getEntry().size();
		int count = 1;
		for (Entry i : bundle.getEntry()) {
			ValueSet next = (ValueSet) i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading ValueSet {}/{} : {}", new Object[] { count, total, next.getIdElement().getValue() });
			client.update().resource(next).execute();

			count++;
		}

		try {
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/instance/model/valueset/"+"v3-codesystems.xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(e.toString());
		}

		bundle = ctx.newXmlParser().parseResource(Bundle.class, vsContents);
		total = bundle.getEntry().size();
		count = 1;
		for (Entry i : bundle.getEntry()) {
			ValueSet next = (ValueSet) i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading v3-codesystems ValueSet {}/{} : {}", new Object[] { count, total, next.getIdElement().getValue() });
			client.update().resource(next).execute();

			count++;
		}

		try {
			vsContents = IOUtils.toString(ValidationDataUploader.class.getResourceAsStream("/org/hl7/fhir/instance/model/valueset/"+"v2-tables.xml"), "UTF-8");
		} catch (IOException e) {
			throw new CommandFailureException(e.toString());
		}
		bundle = ctx.newXmlParser().parseResource(Bundle.class, vsContents);
		total = bundle.getEntry().size();
		count = 1;
		for (Entry i : bundle.getEntry()) {
			ValueSet next = (ValueSet) i.getResource();
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading v2-tables ValueSet {}/{} : {}", new Object[] { count, total, next.getIdElement().getValue() });
			client.update().resource(next).execute();
			count++;
		}

		ourLog.info("Finished uploading ValueSets");

		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		Resource[] mappingLocations;
		try {
			mappingLocations = patternResolver.getResources("classpath*:org/hl7/fhir/instance/model/profile/"+"*.profile.xml");
		} catch (IOException e) {
			throw new CommandFailureException(e.toString());
		}
		total = mappingLocations.length;
		count = 1;
		for (Resource i : mappingLocations) {
			StructureDefinition next;
			try {
				next = ctx.newXmlParser().parseResource(StructureDefinition.class, IOUtils.toString(i.getInputStream(), "UTF-8"));
			} catch (Exception e) {
				throw new CommandFailureException(e.toString());
			}
			next.setId(next.getIdElement().toUnqualifiedVersionless());

			ourLog.info("Uploading StructureDefinition {}/{} : {}", new Object[] { count, total, next.getIdElement().getValue() });
			try {
				client.update().resource(next).execute();
			} catch (Exception e) {
				ourLog.warn("Failed to upload {} - {}", next.getIdElement().getValue(), e.getMessage());
			}
			count++;
		}

		ourLog.info("Finished uploading ValueSets");

		long delay = System.currentTimeMillis() - start;

		ourLog.info("Finished uploading definitions to server (took {} ms)", delay);
	}

}
