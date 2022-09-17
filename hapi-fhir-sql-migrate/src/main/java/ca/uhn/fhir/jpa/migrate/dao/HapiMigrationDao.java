package ca.uhn.fhir.jpa.migrate.dao;

import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.util.VersionEnum;
import org.apache.commons.lang3.Validate;
import org.flywaydb.core.api.MigrationVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
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
		// FIXME KHS
		RowMapper<HapiMigrationEntity> mapper = HapiMigrationEntity.newRowMapper();
		List<HapiMigrationEntity> list = myJdbcTemplate.query("SELECT * FROM TEST_MIGRATION_TABLE", mapper);
		return myJdbcTemplate.queryForObject(highestKeyQuery, Integer.class);
	}

	// FIXME KHS use this in migration
	public void createMigrationTableIfRequired() {
		try {
			fetchMigrationVersions();
		} catch (BadSqlGrammarException e) {
			ourLog.info("Creating table {}", myMigrationTablename);
			myJdbcTemplate.execute(myMigrationQueryBuilder.createTableStatement());
		}
	}
}
