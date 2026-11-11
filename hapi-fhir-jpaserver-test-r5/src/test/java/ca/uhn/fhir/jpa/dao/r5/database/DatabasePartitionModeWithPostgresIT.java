package ca.uhn.fhir.jpa.dao.r5.database;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {
	EmbeddedDatabaseConfigurations.Postgres.class
})
public class DatabasePartitionModeWithPostgresIT extends BaseDatabasePartitionModeIT {

	/**
	 * Distributed Postgres variants reject a correlated subquery containing a join on
	 * TARGET_RES_PARTITION_ID, which only a real multi-node cluster detects.
	 */
	@Test
	void testChainedSearch_whenContainedResourceIndexingEnabled_usesUncorrelatedSubqueryForReferenceTarget() {
		boolean previousIndexContained = myStorageSettings.isIndexOnContainedResources();
		// With this setting off, the chain only follows the reference to the Patient. With it on, the Patient
		// may also be contained in the ServiceRequest, where its identifier is indexed on the ServiceRequest
		// itself. The search must cover both cases, which generates WHERE clause:
		// EXISTS (token row on the ServiceRequest) OR EXISTS (link row to a Patient with that token)
		myStorageSettings.setIndexOnContainedResources(true);
		try {
			IIdType patientId = createPatient(withIdentifier("http://acme.org/mrn", "PT12494"));
			createResource("ServiceRequest", withSubject(patientId));
			myCaptureQueriesListener.clear();

			myServer.getFhirClient()
				.search()
				.byUrl("ServiceRequest?subject:Patient.identifier=http://acme.org/mrn|PT12494")
				.returnBundle(Bundle.class)
				.execute();

			String searchSql = myCaptureQueriesListener.getSelectQueries().stream()
				.map(query -> query.getSql(false, false))
				.filter(sql -> sql.contains("HFJ_RES_LINK"))
				.findFirst()
				.orElseThrow();
			assertThat(searchSql)
				// The link EXISTS is correlated to the outer query. An INNER JOIN inside it would compare
				// TARGET_RES_PARTITION_ID to HFJ_SPIDX_TOKEN.PARTITION_ID, and Citus rejects that because
				// TARGET_RES_PARTITION_ID isn't the column HFJ_RES_LINK is distributed on. The IN subquery
				// is neither correlated nor joined to the link table, which lets Citus plan/execute it independently.
				.contains("(s0.TARGET_RES_PARTITION_ID,s0.TARGET_RESOURCE_ID) IN (SELECT s1.PARTITION_ID,s1.RES_ID FROM HFJ_SPIDX_TOKEN s1")
				.doesNotContain("INNER JOIN HFJ_SPIDX_TOKEN");
		} finally {
			myStorageSettings.setIndexOnContainedResources(previousIndexContained);
		}
	}

	/**
	 * Each branch of the chain (the contained Patient and the referenced Patient) is matched with an uncorrelated
	 * tuple IN on the searched resource's key rather than an EXISTS correlated to it. The branch subquery no longer
	 * depends on the row being filtered: PostgreSQL evaluates it once per search, and Citus runs it as a semi-join
	 * within each worker instead of re-scanning the target rows for every resource it filters.
	 */
	@Test
	void testChainedSearch_whenContainedResourceIndexingEnabled_matchesEachBranchWithUncorrelatedSubquery() {
		boolean previousIndexContained = myStorageSettings.isIndexOnContainedResources();
		myStorageSettings.setIndexOnContainedResources(true);
		try {
			IIdType patientId = createPatient(withIdentifier("http://acme.org/mrn", "PT12494"));
			IIdType serviceRequestId = createResource("ServiceRequest", withSubject(patientId));
			myCaptureQueriesListener.clear();

			Bundle result = myServer.getFhirClient()
				.search()
				.byUrl("ServiceRequest?subject:Patient.identifier=http://acme.org/mrn|PT12494")
				.returnBundle(Bundle.class)
				.execute();

			assertThat(result.getEntry())
				.extracting(entry -> entry.getResource().getIdElement().getIdPart())
				.containsExactly(serviceRequestId.getIdPart());
			String searchSql = myCaptureQueriesListener.getSelectQueries().stream()
				.map(query -> query.getSql(false, false))
				.filter(sql -> sql.contains("HFJ_RES_LINK"))
				.findFirst()
				.orElseThrow();
			assertThat(searchSql)
				// contained Patient: the identifier is indexed on the ServiceRequest itself
				.contains("(t0.PARTITION_ID,t0.RES_ID) IN (SELECT s0.PARTITION_ID,s0.RES_ID FROM HFJ_SPIDX_TOKEN s0")
				// referenced Patient: the ServiceRequest's link to a Patient with that identifier
				.contains("(t0.PARTITION_ID,t0.RES_ID) IN (SELECT s0.PARTITION_ID,s0.SRC_RESOURCE_ID FROM HFJ_RES_LINK s0")
				.doesNotContain("EXISTS")
				.doesNotContain("= t0.");
		} finally {
			myStorageSettings.setIndexOnContainedResources(previousIndexContained);
		}
	}
}
