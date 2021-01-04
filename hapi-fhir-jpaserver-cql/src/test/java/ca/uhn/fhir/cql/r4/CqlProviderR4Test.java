package ca.uhn.fhir.cql.r4;

import ca.uhn.fhir.cql.BaseCqlR4Test;
import ca.uhn.fhir.cql.common.provider.CqlProviderTestBase;
import ca.uhn.fhir.cql.r4.provider.MeasureOperationsProvider;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.MeasureReport;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CqlProviderR4Test extends BaseCqlR4Test implements CqlProviderTestBase {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderR4Test.class);

	@Autowired
	IFhirResourceDao<Measure> myMeasureDao;
	@Autowired
	IFhirResourceDao<Library> myLibraryDao;
	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;

	// Fails with:
	//	org.opencds.cqf.cql.engine.exception.CqlException: Unexpected exception caught during execution: java.lang.NullPointerException
	//	at org.opencds.cqf.cql.engine.elm.execution.Executable.evaluate(Executable.java:37)
	//	at org.opencds.cqf.cql.engine.elm.execution.QueryEvaluator.internalEvaluate(QueryEvaluator.java:165)
	//	at org.opencds.cqf.cql.engine.elm.execution.Executable.evaluate(Executable.java:18)
	//	at org.opencds.cqf.cql.engine.elm.execution.ExpressionDefEvaluator.internalEvaluate(ExpressionDefEvaluator.java:19)
	//	at org.opencds.cqf.cql.engine.elm.execution.Executable.evaluate(Executable.java:18)
	//	at org.opencds.cqf.cql.engine.elm.execution.ExpressionRefEvaluator.internalEvaluate(ExpressionRefEvaluator.java:11)
	//	at org.opencds.cqf.cql.engine.elm.execution.Executable.evaluate(Executable.java:18)
	//	at org.opencds.cqf.cql.engine.elm.execution.QueryEvaluator.internalEvaluate(QueryEvaluator.java:165)
	//	at org.opencds.cqf.cql.engine.elm.execution.Executable.evaluate(Executable.java:18)
	//	at org.opencds.cqf.cql.engine.elm.execution.ExpressionDefEvaluator.internalEvaluate(ExpressionDefEvaluator.java:19)
	//	at org.opencds.cqf.cql.engine.elm.execution.Executable.evaluate(Executable.java:18)
	//	at org.opencds.cqf.cql.engine.elm.execution.ExpressionRefEvaluator.internalEvaluate(ExpressionRefEvaluator.java:11)
	//	at org.opencds.cqf.cql.engine.elm.execution.Executable.evaluate(Executable.java:18)
	//	at org.opencds.cqf.cql.engine.elm.execution.QueryEvaluator.internalEvaluate(QueryEvaluator.java:165)
	//	at org.opencds.cqf.cql.engine.elm.execution.Executable.evaluate(Executable.java:18)
	//	at org.opencds.cqf.cql.engine.elm.execution.ExpressionDefEvaluator.internalEvaluate(ExpressionDefEvaluator.java:19)
	//	at org.opencds.cqf.cql.engine.elm.execution.Executable.evaluate(Executable.java:18)
	//	at org.opencds.cqf.cql.engine.elm.execution.ExpressionRefEvaluator.internalEvaluate(ExpressionRefEvaluator.java:11)
	//	at org.opencds.cqf.cql.engine.elm.execution.Executable.evaluate(Executable.java:18)
	//	at org.opencds.cqf.cql.engine.elm.execution.ExpressionDefEvaluator.internalEvaluate(ExpressionDefEvaluator.java:19)
	//	at org.opencds.cqf.cql.engine.elm.execution.Executable.evaluate(Executable.java:18)
	//	at ca.uhn.fhir.cql.r4.evaluation.MeasureEvaluation.evaluateCriteria(MeasureEvaluation.java:190)
	//	at ca.uhn.fhir.cql.r4.evaluation.MeasureEvaluation.evaluatePopulationCriteria(MeasureEvaluation.java:212)
	//	at ca.uhn.fhir.cql.r4.evaluation.MeasureEvaluation.evaluate(MeasureEvaluation.java:408)
	//	at ca.uhn.fhir.cql.r4.evaluation.MeasureEvaluation.evaluatePopulationMeasure(MeasureEvaluation.java:113)
	//	at ca.uhn.fhir.cql.r4.evaluation.MeasureEvaluation.evaluatePatientMeasure(MeasureEvaluation.java:65)
	//	at ca.uhn.fhir.cql.r4.provider.MeasureOperationsProvider.evaluateMeasure(MeasureOperationsProvider.java:192)
	//	at ca.uhn.fhir.cql.r4.CqlProviderR4Test.testEXM104EvaluateMeasure(CqlProviderR4Test.java:55)
	@Test
	public void testEXM104EvaluateMeasure() throws IOException {
		IdType measureId = new IdType("Measure", "measure-EXM104-8.2.000");
		loadBundle("r4/EXM104/EXM104-8.2.000-bundle.json");
		String measure = "Measure/measure-EXM104-8.2.000";
		String patient = "Patient/numer-EXM104";
		String periodStart = "2003-01-01";
		String periodEnd = "2003-12-31";

		MeasureReport report = myMeasureOperationsProvider.evaluateMeasure(measureId, periodStart, periodEnd, measure, "patient",
			patient, null, null, null, null, null, null);
		// Assert it worked
		assertThat(report.getGroup(), hasSize(1));
		assertThat(report.getGroup().get(0).getPopulation(), hasSize(3));
		for (MeasureReport.MeasureReportGroupComponent group : report.getGroup()) {
			for (MeasureReport.MeasureReportGroupPopulationComponent population : group.getPopulation()) {
				assertTrue(population.getCount() > 0);
			}
		}
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(report));
	}
}
