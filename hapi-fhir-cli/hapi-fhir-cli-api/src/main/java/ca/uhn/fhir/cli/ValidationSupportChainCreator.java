package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.cli.CommandLine;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.LocalFileValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;

import java.io.IOException;

public class ValidationSupportChainCreator {

	public static ValidationSupportChain getValidationSupportChainR4(FhirContext ctx, CommandLine commandLine) {
		ValidationSupportChain chain = new ValidationSupportChain(
			new DefaultProfileValidationSupport(ctx),
			new InMemoryTerminologyServerValidationSupport(ctx));

		if (commandLine.hasOption("l")) {
			try {
				String localProfile = commandLine.getOptionValue("l");
				LocalFileValidationSupport localFileValidationSupport = new LocalFileValidationSupport(ctx);

				localFileValidationSupport.loadFile(localProfile);

				chain.addValidationSupport(localFileValidationSupport);
				chain.addValidationSupport(new SnapshotGeneratingValidationSupport(ctx));
			} catch (IOException e) {
				throw new RuntimeException(Msg.code(2141) + "Failed to load local profile.", e);
			}
		}
		if (commandLine.hasOption("r")) {
			chain.addValidationSupport(new LoadingValidationSupportDstu3());
		}

		return chain;
	}

	public static ValidationSupportChain getValidationSupportChainDstu2(FhirContext ctx, CommandLine commandLine) {
		ValidationSupportChain chain = new ValidationSupportChain(
			new DefaultProfileValidationSupport(ctx), new InMemoryTerminologyServerValidationSupport(ctx));

		if (commandLine.hasOption("r")) {
			chain.addValidationSupport(new LoadingValidationSupportDstu2());
		}

		return chain;
	}
}
