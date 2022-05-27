package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;

public class MetadataSource {
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

	private String getEdition(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
		final String result;
		if (theConnectionProperties.getDriverType() == DriverTypeEnum.MSSQL_2012) {
			result = theConnectionProperties.newJdbcTemplate().queryForObject("SELECT SERVERPROPERTY ('edition')", String.class);
		} else {
			 result = "unimplemented";
		}
		return result;
	}
}
