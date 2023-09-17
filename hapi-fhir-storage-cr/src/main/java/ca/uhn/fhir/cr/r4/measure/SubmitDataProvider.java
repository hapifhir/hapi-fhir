/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.MeasureReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;

public class SubmitDataProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(SubmitDataProvider.class);

	Function<RequestDetails, SubmitDataService> mySubmitDataServiceFunction;

	public SubmitDataProvider(Function<RequestDetails, SubmitDataService> submitDataServiceFunction) {
		this.mySubmitDataServiceFunction = submitDataServiceFunction;
	}
	/**
	 * Implements the <a href=
	 * "http://hl7.org/fhir/R4/measure-operation-submit-data.html">$submit-data</a>
	 * operation found in the
	 * <a href="http://hl7.org/fhir/R4/clinicalreasoning-module.html">FHIR Clinical
	 * Reasoning Module</a> per the
	 * <a href="http://build.fhir.org/ig/HL7/davinci-deqm/datax.html#submit-data">Da
	 * Vinci DEQM FHIR Implementation Guide</a>.
	 *
	 *
	 * The submitted MeasureReport and Resources will be saved to the local server.
	 * A Bundle reporting the result of the transaction will be returned.
	 *
	 * Usage:
	 * URL: [base]/Measure/$submit-data
	 * URL: [base]/Measure/[id]/$submit-data
	 *
	 * @param theRequestDetails generally auto-populated by the HAPI server
	 *                          framework.
	 * @param theId             the Id of the Measure to submit data for
	 * @param theReport         the MeasureReport to be submitted
	 * @param theResources      the resources to be submitted
	 * @return Bundle the transaction result
	 */
	@Description(
			shortDefinition = "$submit-data",
			value =
					"Implements the <a href=\"http://hl7.org/fhir/R4/measure-operation-submit-data.html\">$submit-data</a> operation found in the <a href=\"http://hl7.org/fhir/R4/clinicalreasoning-module.html\">FHIR Clinical Reasoning Module</a> per the <a href=\"http://build.fhir.org/ig/HL7/davinci-deqm/datax.html#submit-data\">Da Vinci DEQM FHIR Implementation Guide</a>.")
	@Operation(name = "$submit-data", type = Measure.class)
	public Bundle submitData(
			RequestDetails theRequestDetails,
			@IdParam IdType theId,
			@OperationParam(name = "measureReport", min = 1, max = 1) MeasureReport theReport,
			@OperationParam(name = "resource") List<IBaseResource> theResources) {
		return mySubmitDataServiceFunction.apply(theRequestDetails).submitData(theId, theReport, theResources);
	}
}
