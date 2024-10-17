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

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.*;
import org.opencds.cqf.fhir.api.Repository;
import org.opencds.cqf.fhir.cr.measure.MeasureEvaluationOptions;
import org.opencds.cqf.fhir.cr.measure.r4.R4MeasureProcessor;
import org.opencds.cqf.fhir.cr.measure.r4.R4RepositorySubjectProvider;
import org.opencds.cqf.fhir.cr.measure.r4.utils.R4MeasureServiceUtils;
import org.opencds.cqf.fhir.utility.monad.Either3;
import org.opencds.cqf.fhir.utility.repository.Repositories;

import java.util.Collections;

public class R4MeasureService implements R4MeasureEvaluatorSingle {
	private final Repository repository;
	private final MeasureEvaluationOptions measureEvaluationOptions;

	public R4MeasureService(Repository repository, MeasureEvaluationOptions measureEvaluationOptions) {
		this.repository = repository;
		this.measureEvaluationOptions = measureEvaluationOptions;
	}

	public MeasureReport evaluate(
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
			String practitioner) {

		var repo = Repositories.proxy(repository, true, dataEndpoint, contentEndpoint, terminologyEndpoint);
		var processor = new R4MeasureProcessor(repo, this.measureEvaluationOptions, new R4RepositorySubjectProvider());

		R4MeasureServiceUtils r4MeasureServiceUtils = new R4MeasureServiceUtils(repository);
		r4MeasureServiceUtils.ensureSupplementalDataElementSearchParameter();

		MeasureReport measureReport = null;

		if (StringUtils.isNotBlank(practitioner)) {
			if (!practitioner.contains("/")) {
				practitioner = "Practitioner/".concat(practitioner);
			}
			subjectId = practitioner;
		}

		measureReport = processor.evaluateMeasure(
				measure,
				periodStart,
				periodEnd,
				reportType,
				Collections.singletonList(subjectId),
				additionalData,
				parameters);

		// add ProductLine after report is generated
		measureReport = r4MeasureServiceUtils.addProductLineExtension(measureReport, productLine);

		// add subject reference for non-individual reportTypes
		return r4MeasureServiceUtils.addSubjectReference(measureReport, practitioner, subjectId);
	}
}
