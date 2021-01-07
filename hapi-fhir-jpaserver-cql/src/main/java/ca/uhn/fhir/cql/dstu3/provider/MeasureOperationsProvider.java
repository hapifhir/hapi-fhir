package ca.uhn.fhir.cql.dstu3.provider;

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
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Library;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MeasureOperationsProvider {
	private static final Logger logger = LoggerFactory.getLogger(MeasureOperationsProvider.class);

	@Autowired
	private LibraryResolutionProvider<Library> libraryResolutionProvider;
	@Autowired
	private DaoRegistry registry;
	@Autowired
	private IFhirResourceDao<Measure> myMeasureDao;
	@Autowired
	private EvaluationProviderFactory factory;

	/*
	 *
	 * NOTE that the source, user, and pass parameters are not standard parameters
	 * for the FHIR $evaluate-measure operation
	 *
	 */
	@Operation(name = "$evaluate-measure", idempotent = true, type = Measure.class)
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
													 @OperationParam(name = "pass") String pass) throws InternalErrorException, FHIRException {
		LibraryLoader libraryLoader = LibraryHelper.createLibraryLoader(this.libraryResolutionProvider);
		MeasureEvaluationSeed seed = new MeasureEvaluationSeed(this.factory, libraryLoader,
			this.libraryResolutionProvider);
		Measure measure = myMeasureDao.read(theId);

		if (measure == null) {
			throw new RuntimeException("Could not find Measure/" + theId.getIdPart());
		}

		seed.setup(measure, periodStart, periodEnd, productLine, source, user, pass);

		// resolve report type
		MeasureEvaluation evaluator = new MeasureEvaluation(this.registry,
			seed.getMeasurementPeriod());
		if (reportType != null) {
			switch (reportType) {
				case "patient":
					return evaluator.evaluatePatientMeasure(seed.getMeasure(), seed.getContext(), patientRef);
				case "patient-list":
					return evaluator.evaluatePatientListMeasure(seed.getMeasure(), seed.getContext(), practitionerRef);
				case "population":
					return evaluator.evaluatePopulationMeasure(seed.getMeasure(), seed.getContext());
				default:
					throw new IllegalArgumentException("Invalid report type: " + reportType);
			}
		}

		// default report type is patient
		MeasureReport report = evaluator.evaluatePatientMeasure(seed.getMeasure(), seed.getContext(), patientRef);
		if (productLine != null) {
			Extension ext = new Extension();
			ext.setUrl("http://hl7.org/fhir/us/cqframework/cqfmeasures/StructureDefinition/cqfm-productLine");
			ext.setValue(new StringType(productLine));
			report.addExtension(ext);
		}

		return report;
	}
}
