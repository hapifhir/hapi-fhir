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
package ca.uhn.fhir.cr.clinical_reasoning_copy;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Parameters;
import org.opencds.cqf.fhir.cr.measure.r4.R4MeasureService;
import org.opencds.cqf.fhir.utility.monad.Either3;

/**
 * Interface for {@link R4MeasureService} and any other concrete classes that implement the same
 * signature.
 */
public interface R4MeasureEvaluatorSingle {

	MeasureReport evaluate(
			Either3<CanonicalType, IdType, Measure> measure,
			String periodStart,
			String periodEnd,
			String reportType,
			String subjectId,
			String lastReceivedOn,
			Endpoint contentEndpoint,
			Endpoint terminologyEndpoint,
			Endpoint dataEndpoint,
			Bundle additionalData,
			Parameters parameters,
			String productLine,
			String practitioner);
}
