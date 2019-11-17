package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu3.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import java.io.FileReader;

public class ValidateSimple {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidateSimple.class);

	public static void main(String[] args) throws Exception {
		FhirContext ctx = FhirContext.forR4();

		// Create a validator module
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator();

		// We'll create a validation chain with only the DefaultProfileValidationupport registered
		ValidationSupportChain validationSupportChain = new ValidationSupportChain();
		validationSupportChain.addValidationSupport(new DefaultProfileValidationSupport());
		instanceValidator.setValidationSupport(validationSupportChain);

		// Create a validator and register the InstanceValidator module
		FhirValidator val = ctx.newValidator();
		val.registerValidatorModule(instanceValidator);

		// Read in the file and validate it
		String nextFile = args[0];
		try (FileReader fileReader = new FileReader(nextFile)) {
			String input = IOUtils.toString(fileReader);
			ValidationResult result = val.validateWithResult(input);
			IBaseOperationOutcome oo = result.toOperationOutcome();
			ourLog.info("Result:\n{}", ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(oo));
		}

	}

}
