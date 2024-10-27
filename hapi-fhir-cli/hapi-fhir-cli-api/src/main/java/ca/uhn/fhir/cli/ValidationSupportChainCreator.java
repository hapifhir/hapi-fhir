/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
				new DefaultProfileValidationSupport(ctx), new InMemoryTerminologyServerValidationSupport(ctx));

		if (commandLine.hasOption("l")) {
			try {
				String localProfile = commandLine.getOptionValue("l");
				LocalFileValidationSupport localFileValidationSupport = new LocalFileValidationSupport(ctx);

				localFileValidationSupport.loadFile(localProfile);

				chain.addValidationSupport(localFileValidationSupport);
				chain.addValidationSupport(new SnapshotGeneratingValidationSupport(ctx));
			} catch (IOException e) {
				throw new RuntimeException(Msg.code(2254) + "Failed to load local profile.", e);
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
