package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.igpacks.parser.IgPackParserDstu3;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

public class IgPackUploader extends BaseCommand {
	// TODO: Don't use qualified names for loggers in HAPI CLI.
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
	public void run(CommandLine theCommandLine) throws ParseException{
		parseFhirContext(theCommandLine);

		IGenericClient client = newClient(theCommandLine);

		String url = theCommandLine.getOptionValue("u");

		Collection<File> files = null;
		try {
			files = loadFile(url, null, false);
		} catch (IOException e) {
			throw new CommandFailureException(e);
		}

		for (File nextFile : files) {
			FhirContext ctx = getFhirContext();
			switch (ctx.getVersion().getVersion()) {
				case DSTU3:
					IgPackParserDstu3 packParser = new IgPackParserDstu3(ctx);
					IValidationSupport ig = null;
					try {
						ig = packParser.parseIg(new FileInputStream(nextFile), nextFile.getName());
					} catch (FileNotFoundException e) {
						throw new CommandFailureException(e);
					}
					Iterable<IBaseResource> conformanceResources = ig.fetchAllConformanceResources();
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
