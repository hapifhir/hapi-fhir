package ca.uhn.fhir.cr.dstu3.provider;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.cr.dstu3.service.MeasureService;
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
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class MeasureOperationsProvider {

	private static final Logger log = LoggerFactory.getLogger(MeasureOperationsProvider.class);

	@Autowired
	Function<RequestDetails, MeasureService> dstu3MeasureServiceFactory;

	/**
	 * Implements the <a href=
	 * "https://www.hl7.org/fhir/operation-measure-evaluate-measure.html">$evaluate-measure</a>
	 * operation found in the
	 * <a href="http://www.hl7.org/fhir/clinicalreasoning-module.html">FHIR Clinical
	 * Reasoning Module</a>. This implementation aims to be compatible with the CQF
	 * IG.
	 *
	 * @param requestDetails The details (such as tenant) of this request. Usually
	 *                       auto-populated HAPI.
	 * @param theId          the Id of the Measure to evaluate
	 * @param periodStart    The start of the reporting period
	 * @param periodEnd      The end of the reporting period
	 * @param reportType     The type of MeasureReport to generate
	 * @param patient        the patient to use as the subject to use for the
	 *                       evaluation
	 * @param practitioner   the practitioner to use for the evaluation
	 * @param lastReceivedOn the date the results of this measure were last
	 *                       received.
	 * @param productLine    the productLine (e.g. Medicare, Medicaid, etc) to use
	 *                       for the evaluation. This is a non-standard parameter.
	 * @param additionalData the data bundle containing additional data
	 * @return the calculated MeasureReport
	 */
	@Operation(name = ProviderConstants.CQL_EVALUATE_MEASURE, idempotent = true, type = Measure.class)
	public MeasureReport evaluateMeasure(RequestDetails requestDetails, @IdParam IdType theId,
													 @OperationParam(name = "periodStart") String periodStart,
													 @OperationParam(name = "periodEnd") String periodEnd,
													 @OperationParam(name = "reportType") String reportType,
													 @OperationParam(name = "patient") String patient,
													 @OperationParam(name = "practitioner") String practitioner,
													 @OperationParam(name = "lastReceivedOn") String lastReceivedOn,
													 @OperationParam(name = "productLine") String productLine,
													 @OperationParam(name = "additionalData") Bundle additionalData,
													 @OperationParam(name = "terminologyEndpoint") Endpoint terminologyEndpoint) throws InternalErrorException, FHIRException {
		return this.dstu3MeasureServiceFactory
			.apply(requestDetails)
			.evaluateMeasure(
				theId,
				periodStart,
				periodEnd,
				reportType,
				patient,
				practitioner,
				lastReceivedOn,
				productLine,
				additionalData,
				terminologyEndpoint);
	}
}
