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
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.igpacks.parser.IgPackParserDstu2;
import ca.uhn.fhir.igpacks.parser.IgPackParserDstu3;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.base.Charsets;
import com.helger.commons.io.file.FileHelper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.fusesource.jansi.Ansi.Color;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.fusesource.jansi.Ansi.ansi;

public class ValidateCommand extends BaseCommand {
	// TODO: Don't use qualified names for loggers in HAPI CLI.
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidateCommand.class);

	@Override
	public String getCommandDescription() {
		return "Validate a resource using the FHIR validation tools";
	}

	@Override
	public String getCommandName() {
		return "validate";
	}

	@Override
	public Options getOptions() {
		Options retVal = new Options();
		addFhirVersionOption(retVal);

		OptionGroup source = new OptionGroup();
		addOptionalOption(retVal, source, "n", "file", "filename", "The name of the file to validate");
		addOptionalOption(retVal, source, "d", "data", "text", "The text to validate");
		retVal.addOptionGroup(source);

		retVal.addOption("p", "profile", false, "Validate using Profiles (StructureDefinition / ValueSet)");
		retVal.addOption("r", "fetch-remote", false,
			"Allow fetching remote resources (in other words, if a resource being validated refers to an external StructureDefinition, Questionnaire, etc. this flag allows the validator to access the internet to try and fetch this resource)");
		addOptionalOption(retVal, "l", "fetch-local", "filename", "Fetch a profile locally and use it if referenced");
		addOptionalOption(retVal, null, "igpack", true, "If specified, provides the filename of an IGPack file to include in validation");
		addOptionalOption(retVal, "x", "xsd", false, "Validate using Schemas");
		addOptionalOption(retVal, "s", "sch", false, "Validate using Schematrons");
		addOptionalOption(retVal, "e", "encoding","encoding", "File encoding (default is UTF-8)");

    return retVal;
	}

	private String loadFile(String theFileName) throws ParseException {
		return new String(loadFileAsByteArray(theFileName), Charsets.UTF_8);
	}

	private byte[] loadFileAsByteArray(String theFileName) throws ParseException {
		byte[] input;
		try {
			input = IOUtils.toByteArray(new FileInputStream(new File(theFileName)));
		} catch (IOException e) {
			throw new ParseException("Failed to load file '" + theFileName + "' - Error: " + e.toString());
		}
		return input;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		parseFhirContext(theCommandLine);

		String fileName = theCommandLine.getOptionValue("n");
		String contents = theCommandLine.getOptionValue("d");
		if (isNotBlank(fileName) && isNotBlank(contents)) {
			throw new ParseException("Can not supply both a file (-n) and data (-d)");
		}
		if (isBlank(fileName) && isBlank(contents)) {
			throw new ParseException("Must supply either a file (-n) or data (-d)");
		}

		if (isNotBlank(fileName)) {
			String encoding = theCommandLine.getOptionValue("e", "UTF-8");
			ourLog.info("Reading file '{}' using encoding {}", fileName, encoding);

			try {
				contents = IOUtils.toString(new InputStreamReader(new FileInputStream(fileName), encoding));
			} catch (IOException e) {
				throw new CommandFailureException(e);
			}
			ourLog.info("Fully read - Size is {}", FileHelper.getFileSizeDisplay(contents.length()));
		}

		ca.uhn.fhir.rest.api.EncodingEnum enc = ca.uhn.fhir.rest.api.EncodingEnum.detectEncodingNoDefault(defaultString(contents));
		if (enc == null) {
			throw new ParseException("Could not detect encoding (json/xml) of contents");
		}

		FhirContext ctx = getFhirContext();
		FhirValidator val = ctx.newValidator();

		IBaseResource localProfileResource = null;
		if (theCommandLine.hasOption("l")) {
			String localProfile = theCommandLine.getOptionValue("l");
			ourLog.info("Loading profile: {}", localProfile);
			String input = loadFile(localProfile);

			localProfileResource = ca.uhn.fhir.rest.api.EncodingEnum.detectEncodingNoDefault(input).newParser(ctx).parseResource(input);
		}

		byte[] igPack = null;
		String igpackFilename = null;
		if (theCommandLine.hasOption("igpack")) {
			igpackFilename = theCommandLine.getOptionValue("igpack");
			igPack = loadFileAsByteArray(igpackFilename);
		}

		if (theCommandLine.hasOption("p")) {
			switch (ctx.getVersion().getVersion()) {
				case DSTU2: {
					ValidationSupportChain validationSupport = new ValidationSupportChain(
						new DefaultProfileValidationSupport(ctx), new InMemoryTerminologyServerValidationSupport(ctx));
					if (igPack != null) {
						FhirContext hl7orgCtx = FhirContext.forDstu2Hl7Org();
						hl7orgCtx.setParserErrorHandler(new LenientErrorHandler(false));
						IgPackParserDstu2 parser = new IgPackParserDstu2(hl7orgCtx);
						IValidationSupport igValidationSupport = parser.parseIg(igPack, igpackFilename);
						validationSupport.addValidationSupport(igValidationSupport);
					}

					if (theCommandLine.hasOption("r")) {
						validationSupport.addValidationSupport((IValidationSupport) new LoadingValidationSupportDstu2());
					}
					FhirInstanceValidator instanceValidator;
					instanceValidator = new FhirInstanceValidator(validationSupport);
					val.registerValidatorModule(instanceValidator);

					break;
				}
				case DSTU3: {
					FhirInstanceValidator instanceValidator = new FhirInstanceValidator(ctx);
					val.registerValidatorModule(instanceValidator);
					ValidationSupportChain validationSupport = new ValidationSupportChain(new DefaultProfileValidationSupport(ctx), new InMemoryTerminologyServerValidationSupport(ctx));
					if (igPack != null) {
						IgPackParserDstu3 parser = new IgPackParserDstu3(getFhirContext());
						IValidationSupport igValidationSupport = parser.parseIg(igPack, igpackFilename);
						validationSupport.addValidationSupport(igValidationSupport);
					}

					if (theCommandLine.hasOption("r")) {
						validationSupport.addValidationSupport((IValidationSupport) new LoadingValidationSupportDstu3());
					}
					instanceValidator.setValidationSupport(validationSupport);
					break;
				}
				default:
					throw new ParseException("Profile validation (-p) is not supported for this FHIR version");
			}
		}

		val.setValidateAgainstStandardSchema(theCommandLine.hasOption("x"));
		val.setValidateAgainstStandardSchematron(theCommandLine.hasOption("s"));

		ValidationResult results;
		try {
			results = val.validateWithResult(contents);
		} catch (DataFormatException e) {
			throw new CommandFailureException(e.getMessage());
		}

		StringBuilder b = new StringBuilder("Validation results:" + ansi().boldOff());
		int count = 0;
		for (SingleValidationMessage next : results.getMessages()) {
			count++;
			b.append(App.LINESEP);
			String leftString = "Issue " + count + ": ";
			int leftWidth = leftString.length();
			b.append(ansi().fg(Color.GREEN)).append(leftString);
			if (next.getSeverity() != null) {
				b.append(next.getSeverity()).append(ansi().fg(Color.WHITE)).append(" - ");
			}
			if (isNotBlank(next.getLocationString())) {
				b.append(ansi().fg(Color.WHITE)).append(next.getLocationString());
			}
			String[] message = WordUtils.wrap(next.getMessage(), 80 - leftWidth, "\n", true).split("\\n");
			for (String line : message) {
				b.append(App.LINESEP);
				b.append(ansi().fg(Color.WHITE));
				b.append(leftPad("", leftWidth)).append(line);
			}

		}
		b.append(App.LINESEP);

		if (count > 0) {
			ourLog.info(b.toString());
		}

		if (results.isSuccessful()) {
			ourLog.info("Validation successful!");
		} else {
			throw new CommandFailureException("Validation failed");
		}
	}
}
