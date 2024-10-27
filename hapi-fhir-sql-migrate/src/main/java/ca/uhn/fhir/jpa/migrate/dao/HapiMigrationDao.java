/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.migrate.dao;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.VersionEnum;
import org.apache.commons.lang3.Validate;
import org.flywaydb.core.api.MigrationVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;

public class HapiMigrationDao {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigrationDao.class);

	private final JdbcTemplate myJdbcTemplate;
	private final String myMigrationTablename;
	private final MigrationQueryBuilder myMigrationQueryBuilder;
	private final DataSource myDataSource;

	public HapiMigrationDao(DataSource theDataSource, DriverTypeEnum theDriverType, String theMigrationTablename) {
		myDataSource = theDataSource;
		myJdbcTemplate = new JdbcTemplate(theDataSource);
		myMigrationTablename = theMigrationTablename;
		myMigrationQueryBuilder = new MigrationQueryBuilder(theDriverType, theMigrationTablename);
	}

	public String getMigrationTablename() {
		return myMigrationTablename;
	}

	public Set<MigrationVersion> fetchSuccessfulMigrationVersions() {
		List<HapiMigrationEntity> allEntries = findAll();
		return allEntries.stream()
				.filter(HapiMigrationEntity::getSuccess)
				.map(HapiMigrationEntity::getVersion)
				.map(MigrationVersion::fromVersion)
				.collect(Collectors.toSet());
	}

	public void deleteAll() {
		myJdbcTemplate.execute(myMigrationQueryBuilder.deleteAll());
	}

	/**
	 *
	 * @param theEntity to save.  If the pid is null, the next available pid will be set
	 * @return true if any database records were changed
	 */
	public boolean save(HapiMigrationEntity theEntity) {
		Validate.notNull(theEntity.getDescription(), "Description may not be null");
		Validate.notNull(theEntity.getExecutionTime(), "Execution time may not be null");
		Validate.notNull(theEntity.getSuccess(), "Success may not be null");

		if (theEntity.getPid() == null) {
			Integer highestKey = getHighestKey();
			if (highestKey == null || highestKey < 0) {
				highestKey = 0;
			}
			Integer nextAvailableKey = highestKey + 1;
			theEntity.setPid(nextAvailableKey);
		}
		theEntity.setType("JDBC");
		theEntity.setScript("HAPI FHIR");
		theEntity.setInstalledBy(VersionEnum.latestVersion().name());
		theEntity.setInstalledOn(new Date());
		String insertRecordStatement = myMigrationQueryBuilder.insertPreparedStatement();
		int changedRecordCount = myJdbcTemplate.update(insertRecordStatement, theEntity.asPreparedStatementSetter());
		return changedRecordCount > 0;
	}

	private Integer getHighestKey() {
		String highestKeyQuery = myMigrationQueryBuilder.getHighestKeyQuery();
		return myJdbcTemplate.queryForObject(highestKeyQuery, Integer.class);
	}

	public boolean createMigrationTableIfRequired() {
		if (migrationTableExists()) {
			return false;
		}
		ourLog.info("Creating table {}", myMigrationTablename);

		String createTableStatement = myMigrationQueryBuilder.createTableStatement();
		ourLog.info(createTableStatement);
		myJdbcTemplate.execute(createTableStatement);

		String createIndexStatement = myMigrationQueryBuilder.createIndexStatement();
		ourLog.info(createIndexStatement);
		myJdbcTemplate.execute(createIndexStatement);

		HapiMigrationEntity entity = HapiMigrationEntity.tableCreatedRecord();
		myJdbcTemplate.update(myMigrationQueryBuilder.insertPreparedStatement(), entity.asPreparedStatementSetter());

		return true;
	}

	private boolean migrationTableExists() {
		try {
			try (Connection connection = myDataSource.getConnection()) {
				ResultSet tables =
						connection.getMetaData().getTables(connection.getCatalog(), connection.getSchema(), null, null);

				while (tables.next()) {
					String tableName = tables.getString("TABLE_NAME");

					if (myMigrationTablename.equalsIgnoreCase(tableName)) {
						return true;
					}
				}
				return false;
			}
		} catch (SQLException e) {
			throw new InternalErrorException(Msg.code(2141) + e);
		}
	}

	public List<HapiMigrationEntity> findAll() {
		String allQuery = myMigrationQueryBuilder.findAllQuery();
		ourLog.debug("Executing query: [{}]", allQuery);
		return myJdbcTemplate.query(allQuery, HapiMigrationEntity.rowMapper());
	}

	/**
	 * @return true if the record was successfully deleted
	 */
	public boolean deleteLockRecord(Integer theLockPid, String theLockDescription) {
		int recordsChanged = myJdbcTemplate.update(
				myMigrationQueryBuilder.deleteLockRecordStatement(theLockPid, theLockDescription));
		return recordsChanged > 0;
	}

	public Optional<HapiMigrationEntity> findFirstByPidAndNotDescription(
			Integer theLockPid, String theLockDescription) {
		String query = myMigrationQueryBuilder.findByPidAndNotDescriptionQuery(theLockPid, theLockDescription);

		return myJdbcTemplate.query(query, HapiMigrationEntity.rowMapper()).stream()
				.findFirst();
	}
}
