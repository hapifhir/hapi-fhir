package ca.uhn.fhir.jpa.migrate.dao;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.VersionEnum;
import org.apache.commons.lang3.Validate;
import org.flywaydb.core.api.MigrationVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class HapiMigrationDao {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigrationDao.class);
	private final JdbcTemplate myJdbcTemplate;
	private final String myMigrationTablename;
	private final MigrationQueryBuilder myMigrationQueryBuilder;
	private final DataSource myDataSource;

	public HapiMigrationDao(DataSource theDataSource, String theMigrationTablename) {
		myDataSource = theDataSource;
		myJdbcTemplate = new JdbcTemplate(theDataSource);
		myMigrationTablename = theMigrationTablename;
		myMigrationQueryBuilder = new MigrationQueryBuilder(theMigrationTablename);
	}

	public Set<MigrationVersion> fetchMigrationVersions() {
		String query = myMigrationQueryBuilder.findVersionQuery();
		List<String> result = myJdbcTemplate.queryForList(query, String.class);

		return result.stream()
			.map(MigrationVersion::fromVersion)
			.collect(Collectors.toSet());
	}

	public void deleteAll() {
		myJdbcTemplate.execute(myMigrationQueryBuilder.deleteAll());
	}

	public HapiMigrationEntity save(HapiMigrationEntity theEntity) {
		Validate.notNull(theEntity.getDescription(), "Description may not be null");
		Validate.notNull(theEntity.getExecutionTime(), "Execution time may not be null");
		Validate.notNull(theEntity.getSuccess(), "Success may not be null");

		Integer highestKey = getHighestKey();
		if (highestKey == null || highestKey < 0) {
			highestKey = 0;
		}
		Integer nextAvailableKey = highestKey + 1;
		theEntity.setPid(nextAvailableKey);
		theEntity.setType("JDBC");
		theEntity.setScript("HAPI FHIR");
		theEntity.setInstalledBy(VersionEnum.latestVersion().name());
		theEntity.setInstalledOn(new Date());
		String insertRecordStatement = myMigrationQueryBuilder.insertStatement(theEntity);
		int result = myJdbcTemplate.update(insertRecordStatement);
		return theEntity;
	}

	private Integer getHighestKey() {
		String highestKeyQuery = myMigrationQueryBuilder.getHighestKeyQuery();
		return myJdbcTemplate.queryForObject(highestKeyQuery, Integer.class);
	}

	public void createMigrationTableIfRequired() {
		if (getTableNames().contains(myMigrationTablename)) {
			return;
		}
		ourLog.info("Creating table {}", myMigrationTablename);
		String createTableStatement = myMigrationQueryBuilder.createTableStatement();
		ourLog.info(createTableStatement);
		myJdbcTemplate.execute(createTableStatement);
	}

	private Set<String> getTableNames() {
		try {
			try (Connection connection = myDataSource.getConnection()) {
				DatabaseMetaData metadata;
				metadata = connection.getMetaData();
				ResultSet tables = metadata.getTables(connection.getCatalog(), connection.getSchema(), null, null);

				Set<String> columnNames = new HashSet<>();
				while (tables.next()) {
					String tableName = tables.getString("TABLE_NAME");
					tableName = tableName.toUpperCase(Locale.US);

					String tableType = tables.getString("TABLE_TYPE");
					if ("SYSTEM TABLE".equalsIgnoreCase(tableType)) {
						continue;
					}
					if (SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME.equalsIgnoreCase(tableName)) {
						continue;
					}

					columnNames.add(tableName);
				}

				return columnNames;
			}
		} catch (SQLException e) {
			throw new InternalErrorException(Msg.code(40) + e);
		}
	}
}
