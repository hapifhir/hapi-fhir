/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.cr.r4.ICareGapsServiceFactory;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.EmbeddedOperationParams;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CareGapsOperationProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(CareGapsOperationProvider.class);

	private final ICareGapsServiceFactory myR4CareGapsProcessorFactory;

	public CareGapsOperationProvider(ICareGapsServiceFactory theR4CareGapsProcessorFactory) {
		myR4CareGapsProcessorFactory = theR4CareGapsProcessorFactory;
	}

	/**
	 * Implements the <a href=
	 * "http://build.fhir.org/ig/HL7/davinci-deqm/OperationDefinition-care-gaps.html">$care-gaps</a>
	 * operation found in the
	 * <a href="http://build.fhir.org/ig/HL7/davinci-deqm/index.html">Da Vinci DEQM
	 * FHIR Implementation Guide</a> that overrides the <a href=
	 * "http://build.fhir.org/operation-measure-care-gaps.html">$care-gaps</a>
	 * operation found in the
	 * <a href="http://hl7.org/fhir/R4/clinicalreasoning-module.html">FHIR Clinical
	 * Reasoning Module</a>.
	 *
	 * The operation calculates measures describing gaps in care. For more details,
	 * reference the <a href=
	 * "http://build.fhir.org/ig/HL7/davinci-deqm/gaps-in-care-reporting.html">Gaps
	 * in Care Reporting</a> section of the
	 * <a href="http://build.fhir.org/ig/HL7/davinci-deqm/index.html">Da Vinci DEQM
	 * FHIR Implementation Guide</a>.
	 *
	 * A Parameters resource that includes zero to many document bundles that
	 * include Care Gap Measure Reports will be returned.
	 *
	 * Usage:
	 * URL: [base]/Measure/$care-gaps
	 *
	 * @param theRequestDetails generally auto-populated by the HAPI server
	 *                          framework.
	 * @param theParams          Please refer to the javadoc for {@link CareGapsParams} for more information on the parameters.
	 *   If 'true', this will return summarized subject bundle with only detectedIssue resource.
	 * @return Parameters of bundles of Care Gap Measure Reports
	 */
	@Description(
			shortDefinition = "$care-gaps operation",
			value =
					"Implements the <a href=\"http://build.fhir.org/ig/HL7/davinci-deqm/OperationDefinition-care-gaps.html\">$care-gaps</a> operation found in the <a href=\"http://build.fhir.org/ig/HL7/davinci-deqm/index.html\">Da Vinci DEQM FHIR Implementation Guide</a> which is an extension of the <a href=\"http://build.fhir.org/operation-measure-care-gaps.html\">$care-gaps</a> operation found in the <a href=\"http://hl7.org/fhir/R4/clinicalreasoning-module.html\">FHIR Clinical Reasoning Module</a>.")
	@Operation(name = ProviderConstants.CR_OPERATION_CARE_GAPS, idempotent = true, type = Measure.class)
	public Parameters careGapsReport(
			RequestDetails theRequestDetails, @EmbeddedOperationParams CareGapsParams theParams) {

		return myR4CareGapsProcessorFactory
				.create(theRequestDetails)
				.getCareGapsReport(
						theParams.getPeriodStart(),
						theParams.getPeriodEnd(),
						theParams.getSubject(),
						theParams.getStatus(),
						// LUKETODO:  why can't we have a List<IdType> @OperationParam?
						theParams.getMeasureId(),
						theParams.getMeasureIdentifier(),
						theParams.getMeasureUrl(),
						Optional.ofNullable(theParams.getNonDocument())
								.map(BooleanType::getValue)
								.orElse(false));
	}
}
