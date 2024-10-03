package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ca.uhn.fhir.jpa.dao.expunge.ResourceForeignKey.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


@TestPropertySource(properties = {
	JpaConstants.HAPI_INCLUDE_PARTITION_IDS_IN_PKS + "=true"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ResourceTableFKProviderIT extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceTableFKProviderIT.class);

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

			Map<String, ResourceForeignKeyBuilder> builders = new HashMap<>();
			for (String nextTargetTable : tableNames) {
				String sourceTable = "HFJ_RESOURCE";
				ResultSet crossRefs = metadata.getCrossReference(null, null, sourceTable, null, null, nextTargetTable);
				while (crossRefs.next()) {
					String fkName = crossRefs.getString("FK_NAME");
					ResourceForeignKeyBuilder builder = builders.computeIfAbsent(fkName, k -> new ResourceForeignKeyBuilder());

					String fkTableName = crossRefs.getString("FKTABLE_NAME");
					builder.withTable(fkTableName);

					String fkColumnName = crossRefs.getString("FKCOLUMN_NAME");
					ourLog.info("Key {} Table {} Column {}", fkName, fkTableName, fkColumnName);

					if (fkColumnName.contains("PART")) {
						builder.withPartitionIdColumn(fkColumnName);
					} else if (fkColumnName.contains("RES") || fkColumnName.contains("PID")) {
						builder.withResourceIdColumn(fkColumnName);
					} else {
						fail("Unexpected column: " + fkColumnName);
					}

				}

			}

			for (var builder : builders.values()) {
				ResourceForeignKey foreignKey = builder.build();
				ourLog.info("Found FK to HFJ_RESOURCE: {}", foreignKey);
				expected.add(foreignKey);
			}

		}

		// Add the extra FKs that are not available in the CROSS_REFERENCES table
		expected.add(new ResourceForeignKey("HFJ_HISTORY_TAG", "PARTITION_ID", "RES_ID"));

		// If this assertion fails, it means hapi-fhir has added a new foreign-key dependency to HFJ_RESOURCE.  To fix
		// the test, add the missing key to myResourceTableFKProvider.getResourceForeignKeys()
		List<ResourceForeignKey> actual = myResourceTableFKProvider.getResourceForeignKeys();
		assertThat(actual.toArray()).containsExactlyInAnyOrder(expected.toArray());
	}
}
