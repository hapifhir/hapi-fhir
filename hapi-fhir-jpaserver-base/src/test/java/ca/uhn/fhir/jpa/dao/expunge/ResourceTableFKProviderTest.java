package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;


class ResourceTableFKProviderTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceTableFKProviderTest.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	ResourceTableFKProvider myResourceTableFKProvider;
	@Autowired
	private DataSource myDataSource;


	@Test
	public void testWeHaveAllForeignKeys() throws SQLException {
		List<ResourceForeignKey> expected = myResourceTableFKProvider.getResourceForeignKeys();
		Set<ResourceForeignKey> actual = new HashSet<>();

		try (Connection connection = myDataSource.getConnection()) {
			DatabaseMetaData metadata = connection.getMetaData();

			for (ResourceForeignKey nextExpected : expected) {
				String sourceTable = nextExpected.table;
				String targetTable = "HFJ_RESOURCE";
				ResultSet crossRefs = metadata.getCrossReference(null, null, sourceTable, null, null, targetTable);
				while (crossRefs.next()) {
					String fkTableName = crossRefs.getString("FKTABLE_NAME");
					String fkColumnName = crossRefs.getString("FKCOLUMN_NAME");
					String fkName = crossRefs.getString("FK_NAME");
					actual.add(new ResourceForeignKey(fkTableName, fkName));
				}

			}

		}

		// Add the extra FKs that are not available in the CROSS_REFERENCES table
//		actual.add(new ResourceForeignKey("HFJ_HISTORY_TAG", "RES_ID"));

		//actual.add(new ResourceForeignKey("TRM_CODESYSTEM_VER", "RES_ID"));
		//actual.add(new ResourceForeignKey("HFJ_RES_VER_PROV", "RES_PID"));
		// If this assertion fails, it means hapi-fhir has added a new foreign-key dependency to HFJ_RESOURCE.  To fix
		// the test, add the missing key to myResourceTableFKProvider.getResourceForeignKeys()
		assertThat(expected, containsInAnyOrder(actual.toArray()));
	}
}
