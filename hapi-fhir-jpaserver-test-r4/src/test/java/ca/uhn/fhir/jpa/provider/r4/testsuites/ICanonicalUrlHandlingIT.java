package ca.uhn.fhir.jpa.provider.r4.testsuites;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import jakarta.persistence.EntityManager;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MeasureReport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface ICanonicalUrlHandlingIT {

	String MEASURE_URL = "http://ecqi.healthit.gov/ecqms/Measure/CervicalCancerScreeningFHIR";
	String URL_VERSION = "0.0.005";

	FhirContext getFhirContext();

	DaoRegistry getDaoRegistry();

	IGenericClient getClient();

	EntityManager getEntityManager();

	<T> T runInTx(Callable<T> theCallable);

	@Test
	default void createMeasureReport_withCanonicalMeasureWithVersion_savesBothVersionAndUrl() {
		//setup
		MeasureReport mr = new MeasureReport();
		mr.setMeasure(MEASURE_URL + "|" + URL_VERSION);

		// create
		IFhirResourceDao<MeasureReport> mrDao = getDaoRegistry().getResourceDao(MeasureReport.class);
		mrDao.create(mr, new SystemRequestDetails());

		// verify
		List<ResourceLink> results = runInTx(() -> {
			return getEntityManager().createQuery("SELECT t from ResourceLink t", ResourceLink.class).getResultList();
		});
		assertEquals(1, results.size());
		ResourceLink link = results.get(0);
		assertNotNull(link.getTargetResourceUrl());
		assertNotNull(link.getTargetResourceUrlVersion());
	}

	@Test
	default void searchMeasureReport_withInvalidVersion_returnsNothing() {
		// setup
		MeasureReport mr = new MeasureReport();
		mr.setMeasure(MEASURE_URL + "|" + URL_VERSION);

		// create
		IFhirResourceDao<MeasureReport> mrDao = getDaoRegistry().getResourceDao(MeasureReport.class);
		DaoMethodOutcome outcome = mrDao.create(mr, new SystemRequestDetails());
		assertNotNull(outcome);
		assertNotNull(outcome.getId());

		SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true);
		IBundleProvider provider = mrDao.search(map, new SystemRequestDetails());
		assertEquals(1, provider.getAllResources().size());

		// test
		IGenericClient client = getClient();
		Bundle response = client.search()
			.byUrl("MeasureReport?measure=" + MEASURE_URL + "|" + "1.1.1")
			.returnBundle(Bundle.class)
			.execute();

		// verify
		assertTrue(response.getEntry().isEmpty());
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"?measure=http://ecqi.healthit.gov/ecqms/Measure/CervicalCancerScreeningFHIR",
		"?measure=http://ecqi.healthit.gov/ecqms/Measure/CervicalCancerScreeningFHIR|0.0.005"
	})
	default void searchMeasureReport_byMeasureCanonicalURLwithOrWithoutVersion_worksAsExpected(String theQueryString) {
		// setup
		MeasureReport mr = new MeasureReport();
		mr.setMeasure(MEASURE_URL + "|" + URL_VERSION);

		IFhirResourceDao<MeasureReport> mrDao = getDaoRegistry().getResourceDao(MeasureReport.class);
		DaoMethodOutcome outcome = mrDao.create(mr, new SystemRequestDetails());
		assertNotNull(outcome);
		assertNotNull(outcome.getId());

		SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true);
		IBundleProvider provider = mrDao.search(map, new SystemRequestDetails());
		assertEquals(1, provider.getAllResources().size());

		// test
		IGenericClient client = getClient();
		Bundle response = client.search()
			.byUrl("MeasureReport" + theQueryString)
			.returnBundle(Bundle.class)
			.execute();

		// verify
		assertEquals(1, response.getTotal());
		assertEquals(outcome.getId().getIdPart(), response.getEntry().get(0).getResource().getIdPart());
	}
}
