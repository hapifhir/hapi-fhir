package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;

/**
 * Helper to extract database information about supported migrations.
 */
public class MetadataSource {

	/**
	 * Does this database support index operations without write-locking the table?
	 */
	public boolean isOnlineIndexSupported(DriverTypeEnum.ConnectionProperties theConnectionProperties) {

		switch (theConnectionProperties.getDriverType()) {
			case ORACLE_12C:
			case POSTGRES_9_4:
			case COCKROACHDB_21_1:
				return true;
			case MSSQL_2012:
				String edition = getEdition(theConnectionProperties);
				return edition.startsWith("Enterprise");
			default:
				return false;
		}
	}

	/**
	 * Get the MS Sql Server edition.  Other databases are not supported yet.
	 *
	 * @param theConnectionProperties the database to inspect
	 * @return the edition string (e.g. Standard, Enterprise, Developer, etc.)
	 */
	private String getEdition(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
		final String result;
		if (theConnectionProperties.getDriverType() == DriverTypeEnum.MSSQL_2012) {
			result = theConnectionProperties.newJdbcTemplate().queryForObject("SELECT SERVERPROPERTY ('edition')", String.class);
		} else {
			throw new UnsupportedOperationException(Msg.code(2084) + "We only know about MSSQL editions.");
		}
		return result;
	}
}
