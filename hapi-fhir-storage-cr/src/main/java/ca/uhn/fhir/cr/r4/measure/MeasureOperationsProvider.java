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

import ca.uhn.fhir.cr.common.StringTimePeriodHandler;
import ca.uhn.fhir.cr.r4.R4MeasureEvaluatorSingleFactory;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.MeasureReport;
import org.opencds.cqf.fhir.utility.monad.Eithers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeasureOperationsProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(MeasureOperationsProvider.class);

	private final R4MeasureEvaluatorSingleFactory myR4MeasureServiceFactory;
	private final StringTimePeriodHandler myStringTimePeriodHandler;

	public MeasureOperationsProvider(
			R4MeasureEvaluatorSingleFactory theR4MeasureServiceFactory,
			StringTimePeriodHandler theStringTimePeriodHandler) {
		myR4MeasureServiceFactory = theR4MeasureServiceFactory;
		myStringTimePeriodHandler = theStringTimePeriodHandler;
	}

	// LUKETODO:  fix javadoc
	/**
	 * Implements the <a href=
	 * "https://www.hl7.org/fhir/operation-measure-evaluate-measure.html">$evaluate-measure</a>
	 * operation found in the
	 * <a href="http://www.hl7.org/fhir/clinicalreasoning-module.html">FHIR Clinical
	 * Reasoning Module</a>. This implementation aims to be compatible with the CQF
	 * IG.
	 *
	 * @param theId             the id of the Measure to evaluate
	 * @param thePeriodStart    The start of the reporting period
	 * @param thePeriodEnd      The end of the reporting period
	 * @param theReportType     The type of MeasureReport to generate
	 * @param theSubject        the subject to use for the evaluation
	 * @param thePractitioner   the practitioner to use for the evaluation
	 * @param theLastReceivedOn the date the results of this measure were last
	 *                          received.
	 * @param theProductLine    the productLine (e.g. Medicare, Medicaid, etc) to use
	 *                          for the evaluation. This is a non-standard parameter.
	 * @param theAdditionalData the data bundle containing additional data
	 * @param theRequestDetails The details (such as tenant) of this request. Usually
	 *                          autopopulated HAPI.
	 * @return the calculated MeasureReport
	 */
	@Operation(name = ProviderConstants.CR_OPERATION_EVALUATE_MEASURE, idempotent = true, type = Measure.class)
	public MeasureReport evaluateMeasure(EvaluateMeasureSingleParams theParams, RequestDetails theRequestDetails)
			throws InternalErrorException, FHIRException {
		return myR4MeasureServiceFactory
				.create(theRequestDetails)
				.evaluate(
						// LUKETODO:  1. can we support the concept of Either in hapi-fhir annotations?
						// LUKETODO:  2. can we modify OperationParam to support the concept of mututally exclusive
						// params
						// LUKETODO:  3. code gen from operation definition
						Eithers.forMiddle3(theParams.getId()),
						// LUKETODO:  push this into the hapi-fhir REST framework code
						myStringTimePeriodHandler.getStartZonedDateTime(theParams.getPeriodStart(), theRequestDetails),
						// LUKETODO:  push this into the hapi-fhir REST framework code
						myStringTimePeriodHandler.getEndZonedDateTime(theParams.getPeriodEnd(), theRequestDetails),
						theParams.getReportType(),
						theParams.getSubject(),
						theParams.getLastReceivedOn(),
						null,
						theParams.getTerminologyEndpoint(),
						null,
						theParams.getAdditionalData(),
						theParams.getParameters(),
						theParams.getProductLine(),
						theParams.getPractitioner());
	}
}
