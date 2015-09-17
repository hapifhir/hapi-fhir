package ca.uhn.fhir.cli;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.fusesource.jansi.Ansi.*;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.fusesource.jansi.Ansi;

import com.phloc.commons.io.file.FileUtils;

import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.validation.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirInstanceValidator;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import ca.uhn.fhir.validation.ValidationSupportChain;
import net.sf.saxon.om.Chain;

public class ValidateCommand extends BaseCommand {

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

		OptionGroup source = new OptionGroup();
		source.addOption(new Option("f", "file", true, "The name of the file to validate"));
		source.addOption(new Option("d", "data", true, "The text to validate"));
		retVal.addOptionGroup(source);

		retVal.addOption("x", "xsd", false, "Validate using Schemas");
		retVal.addOption("s", "sch", false, "Validate using Schematrons");
		retVal.addOption("p", "profile", false, "Validate using Profiles (StructureDefinition / ValueSet)");
		retVal.addOption("r", "fetch-remote", false, "Allow fetching remote resources (in other words, if a resource being validated refers to an external StructureDefinition, Questionnaire, etc. this flag allows the validator to access the internet to try and fetch this resource)");

		retVal.addOption("e", "encoding", false, "File encoding (default is UTF-8)");

		return retVal;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException, Exception {
		String fileName = theCommandLine.getOptionValue("f");
		String contents = theCommandLine.getOptionValue("c");
		if (isNotBlank(fileName) && isNotBlank(contents)) {
			throw new ParseException("Can not supply both a file (-f) and data (-d)");
		}
		if (isBlank(fileName) && isBlank(contents)) {
			throw new ParseException("Must supply either a file (-f) or data (-d)");
		}

		if (isNotBlank(fileName)) {
			String encoding = theCommandLine.getOptionValue("e", "UTF-8");
			ourLog.info("Reading file '{}' using encoding {}", fileName, encoding);

			contents = IOUtils.toString(new InputStreamReader(new FileInputStream(fileName), encoding));
			ourLog.info("Fully read - Size is {}", FileUtils.getFileSizeDisplay(contents.length()));
		}

		EncodingEnum enc = MethodUtil.detectEncodingNoDefault(defaultString(contents));
		if (enc == null) {
			throw new ParseException("Could not detect encoding (json/xml) of contents");
		}

		FhirValidator val = getFhirCtx().newValidator();
		if (theCommandLine.hasOption("p")) {
			FhirInstanceValidator instanceValidator = new FhirInstanceValidator();
			val.registerValidatorModule(instanceValidator);
			if (theCommandLine.hasOption("r")) {
				instanceValidator.setValidationSupport(new ValidationSupportChain(new DefaultProfileValidationSupport(), new LoadingValidationSupport()));
			}
		}

		val.setValidateAgainstStandardSchema(theCommandLine.hasOption("x"));
		val.setValidateAgainstStandardSchematron(theCommandLine.hasOption("s"));

		ValidationResult results = val.validateWithResult(contents);

		StringBuilder b = new StringBuilder("Validation results:" + ansi().boldOff());
		int count = 0;
		for (SingleValidationMessage next : results.getMessages()) {
			count++;
			b.append(App.LINESEP);
			String leftString = "Issue "+count+": ";
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
			ourLog.warn("Validation FAILED");
		}
	}
}
