package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheRefresherImpl;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerRegistryImpl;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opencds.cqf.fhir.cql.EvaluationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class CrResourceListenerTests extends BaseCrR4TestServer {
	@Autowired
	EvaluationSettings myEvaluationSettings;
	@Autowired
	ResourceChangeListenerRegistryImpl myResourceChangeListenerRegistry;
	@Autowired
	ResourceChangeListenerCacheRefresherImpl myResourceChangeListenerCacheRefresher;


	public MeasureReport runEvaluateMeasure(String periodStart, String periodEnd, String subject, String measureId, String reportType, String practitioner){

		var parametersEval = new Parameters();
		parametersEval.addParameter("periodStart", new DateType(periodStart));
		parametersEval.addParameter("periodEnd", new DateType(periodEnd));
		parametersEval.addParameter("practitioner", practitioner);
		parametersEval.addParameter("reportType", reportType);
		parametersEval.addParameter("subject", subject);

		var report = ourClient.operation().onInstance("Measure/" + measureId)
			.named("$evaluate-measure")
			.withParameters(parametersEval)
			.returnResourceType(MeasureReport.class)
			.execute();

		assertNotNull(report);

		return report;
	}

	@Test
	void testCodeCacheInvalidation() throws InterruptedException {

		assertTrue(myResourceChangeListenerRegistry.getWatchedResourceNames().contains("ValueSet"));

		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		runEvaluateMeasure("2019-01-01", "2019-12-31", "Patient/numer-EXM130", "ColorectalCancerScreeningsFHIR", "Individual", null);

		// This is a manual init
		myResourceChangeListenerCacheRefresher.refreshExpiredCachesAndNotifyListeners();

		//cached valueSets
		assertEquals(11, myEvaluationSettings.getValueSetCache().size());

		//remove valueset from server
		var id = new IdType("ValueSet/2.16.840.1.113883.3.464.1003.101.12.1001");
		ourClient.delete().resourceById(id).execute();

		// This is a manual refresh
		myResourceChangeListenerCacheRefresher.refreshExpiredCachesAndNotifyListeners();

		//_ALL_ valuesets should be removed from cache
		assertEquals(0, myEvaluationSettings.getValueSetCache().size());
	}

	@Test
	void testElmCacheInvalidation() throws InterruptedException {

		assertTrue(myResourceChangeListenerRegistry.getWatchedResourceNames().contains("Library"));

		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		// evaluate-measure adds library to repository cache
		runEvaluateMeasure("2019-01-01", "2019-12-31", "Patient/numer-EXM130", "ColorectalCancerScreeningsFHIR", "Individual", null);

		// This is a manual init
		myResourceChangeListenerCacheRefresher.refreshExpiredCachesAndNotifyListeners();

		//cached libraries
		assertEquals(7, myEvaluationSettings.getLibraryCache().size());

		//remove Library from server
		var id = new IdType("Library/ColorectalCancerScreeningsFHIR");
		ourClient.delete().resourceById(id).execute();

		// This is a manual refresh
		myResourceChangeListenerCacheRefresher.refreshExpiredCachesAndNotifyListeners();

		//_ALL_ Libraries should be removed from cache
		assertEquals(0, myEvaluationSettings.getLibraryCache().size());
	}

}
