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
				.filter(sql -> sql.contains("EXISTS") && sql.contains("HFJ_RES_LINK"))
				.findFirst()
				.orElseThrow();
			assertThat(searchSql)
				// The link EXISTS is correlated to the outer query. An INNER JOIN inside it would compare
				// TARGET_RES_PARTITION_ID to HFJ_SPIDX_TOKEN.PARTITION_ID, and Citus rejects that because
				// TARGET_RES_PARTITION_ID isn't the column HFJ_RES_LINK is distributed on. The IN subquery
				// is neither correlated nor joined to the link table, which lets Citus plan/execute it independently.
				.contains("(s0.TARGET_RES_PARTITION_ID,s0.TARGET_RESOURCE_ID) IN (SELECT s0.PARTITION_ID,s0.RES_ID FROM HFJ_SPIDX_TOKEN s0")
				.doesNotContain("INNER JOIN HFJ_SPIDX_TOKEN");
		} finally {
			myStorageSettings.setIndexOnContainedResources(previousIndexContained);
		}
	}
}
