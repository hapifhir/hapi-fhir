/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.dstu3.measure;

import ca.uhn.fhir.cr.dstu3.IMeasureServiceFactory;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Endpoint;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MeasureOperationsProvider {
	@Autowired
	IMeasureServiceFactory myDstu3MeasureProcessorFactory;

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
	 * @param thePatient        the patient to use as the subject to use for the
	 *                          evaluation
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
	public MeasureReport evaluateMeasure(
			@IdParam IdType theId,
			@OperationParam(name = "periodStart") String thePeriodStart,
			@OperationParam(name = "periodEnd") String thePeriodEnd,
			@OperationParam(name = "reportType") String theReportType,
			@OperationParam(name = "patient") String thePatient,
			@OperationParam(name = "practitioner") String thePractitioner,
			@OperationParam(name = "lastReceivedOn") String theLastReceivedOn,
			@OperationParam(name = "productLine") String theProductLine,
			@OperationParam(name = "additionalData") Bundle theAdditionalData,
			@OperationParam(name = "terminologyEndpoint") Endpoint theTerminologyEndpoint,
			@OperationParam(name = "parameters") Parameters theParameters,
			RequestDetails theRequestDetails)
			throws InternalErrorException, FHIRException {
		return myDstu3MeasureProcessorFactory
				.create(theRequestDetails)
				.evaluateMeasure(
						theId,
						thePeriodStart,
						thePeriodEnd,
						theReportType,
						thePatient,
						thePractitioner,
						theLastReceivedOn,
						theProductLine,
						theAdditionalData,
						theParameters,
						theTerminologyEndpoint);
	}
}
