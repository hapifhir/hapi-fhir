package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.io.IOUtils;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import java.io.FileReader;

public class ValidateSimple {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidateSimple.class);

	public static void main(String[] args) throws Exception {
		FhirContext ctx = FhirContext.forR4();

		// Create a validator module
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator(ctx);

		// We'll create a validation chain with only the DefaultProfileValidationupport registered
		ValidationSupportChain validationSupportChain = new ValidationSupportChain();
		validationSupportChain.addValidationSupport((ca.uhn.fhir.context.support.IValidationSupport) new DefaultProfileValidationSupport(ctx));
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
