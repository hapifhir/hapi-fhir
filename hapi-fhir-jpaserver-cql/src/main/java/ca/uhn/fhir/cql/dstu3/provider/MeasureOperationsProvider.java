package ca.uhn.fhir.cql.dstu3.provider;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.cql.common.provider.EvaluationProviderFactory;
import ca.uhn.fhir.cql.common.provider.LibraryResolutionProvider;
import ca.uhn.fhir.cql.dstu3.evaluation.MeasureEvaluation;
import ca.uhn.fhir.cql.dstu3.evaluation.MeasureEvaluationSeed;
import ca.uhn.fhir.cql.dstu3.helper.LibraryHelper;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Library;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class implements the dstu3 $evaluate-measure operation defined in the FHIR Clinical Reasoning module.
 * Changes should comply with the specification in as far as is possible, and questions about Measure or CQL evaluation can be directed to the original authors.
 * @author Jonathan Percival
 * @author Bryn Rhodes
 * @see <a href="https://hl7.org/fhir/STU3/measure-operations.html#evaluate-measure">https://hl7.org/fhir/STU3/measure-operations.html#evaluate-measure</a>
 */
@Component
public class MeasureOperationsProvider {
	@Autowired
	private LibraryResolutionProvider<Library> libraryResolutionProvider;
	@Autowired
	private DaoRegistry registry;
	@Autowired
	private IFhirResourceDao<Measure> myMeasureDao;
	@Autowired
	private EvaluationProviderFactory factory;
	@Autowired
	private LibraryHelper libraryHelper;

	/*
	 *
	 * NOTE that the source, user, and pass parameters are not standard parameters
	 * for the FHIR $evaluate-measure operation
	 *
	 */
	@Operation(name = ProviderConstants.CQL_EVALUATE_MEASURE, idempotent = true, type = Measure.class)
	public MeasureReport evaluateMeasure(@IdParam IdType theId,
													 @OperationParam(name = "periodStart") String periodStart,
													 @OperationParam(name = "periodEnd") String periodEnd,
													 @OperationParam(name = "measure") String measureRef,
													 @OperationParam(name = "reportType") String reportType,
													 @OperationParam(name = "patient") String patientRef,
													 @OperationParam(name = "productLine") String productLine,
													 @OperationParam(name = "practitioner") String practitionerRef,
													 @OperationParam(name = "lastReceivedOn") String lastReceivedOn,
													 @OperationParam(name = "source") String source,
													 @OperationParam(name = "user") String user,
													 @OperationParam(name = "pass") String pass,
													 RequestDetails theRequestDetails) throws InternalErrorException, FHIRException {
		LibraryLoader libraryLoader = this.libraryHelper.createLibraryLoader(this.libraryResolutionProvider);
		MeasureEvaluationSeed seed = new MeasureEvaluationSeed(this.factory, libraryLoader,
			this.libraryResolutionProvider, this.libraryHelper);
		Measure measure = myMeasureDao.read(theId, theRequestDetails);

		if (measure == null) {
			throw new RuntimeException(Msg.code(1639) + "Could not find Measure/" + theId.getIdPart());
		}

		seed.setup(measure, periodStart, periodEnd, productLine, source, user, pass, theRequestDetails);

		// resolve report type
		MeasureEvaluation evaluator = new MeasureEvaluation(this.registry,
			seed.getMeasurementPeriod());
		if (reportType != null) {
			switch (reportType) {
				case "patient":
					return evaluator.evaluatePatientMeasure(seed.getMeasure(), seed.getContext(), patientRef, theRequestDetails);
				case "patient-list":
					return evaluator.evaluatePatientListMeasure(seed.getMeasure(), seed.getContext(), practitionerRef, theRequestDetails);
				case "population":
					return evaluator.evaluatePopulationMeasure(seed.getMeasure(), seed.getContext(), theRequestDetails);
				default:
					throw new IllegalArgumentException(Msg.code(1640) + "Invalid report type: " + reportType);
			}
		}

		// default report type is patient
		MeasureReport report = evaluator.evaluatePatientMeasure(seed.getMeasure(), seed.getContext(), patientRef, theRequestDetails);
		if (productLine != null) {
			Extension ext = new Extension();
			ext.setUrl("http://hl7.org/fhir/us/cqframework/cqfmeasures/StructureDefinition/cqfm-productLine");
			ext.setValue(new StringType(productLine));
			report.addExtension(ext);
		}

		return report;
	}
}
