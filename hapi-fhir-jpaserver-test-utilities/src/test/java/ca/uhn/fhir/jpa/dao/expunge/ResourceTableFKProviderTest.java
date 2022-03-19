package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
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
		Set<ResourceForeignKey> expected = new HashSet<>();

		try (Connection connection = myDataSource.getConnection()) {
			DatabaseMetaData metadata = connection.getMetaData();

			Set<String> tableNames = new HashSet<>();
			ResultSet tables = metadata.getTables(null, null, null, null);
			while (tables.next()) {
				tableNames.add(tables.getString("TABLE_NAME"));
			}
			tableNames.remove("HFJ_RESOURCE");

			for (String nextTargetTable : tableNames) {
				String sourceTable = "HFJ_RESOURCE";
				ResultSet crossRefs = metadata.getCrossReference(null, null, sourceTable, null, null, nextTargetTable);
				while (crossRefs.next()) {
					String fkTableName = crossRefs.getString("FKTABLE_NAME");
					String fkColumnName = crossRefs.getString("FKCOLUMN_NAME");
					ResourceForeignKey foreignKey = new ResourceForeignKey(fkTableName, fkColumnName);
					ourLog.info("Found FK to HFJ_RESOURCE: {}", foreignKey);
					expected.add(foreignKey);
				}

			}

		}

		// Add the extra FKs that are not available in the CROSS_REFERENCES table
		expected.add(new ResourceForeignKey("HFJ_HISTORY_TAG", "RES_ID"));

		// If this assertion fails, it means hapi-fhir has added a new foreign-key dependency to HFJ_RESOURCE.  To fix
		// the test, add the missing key to myResourceTableFKProvider.getResourceForeignKeys()
		List<ResourceForeignKey> actual = myResourceTableFKProvider.getResourceForeignKeys();
		assertThat(actual, containsInAnyOrder(expected.toArray()));
	}
}
