package ca.uhn.fhir.system;

public final class HapiTestSystemProperties {
	private static final String MASS_INGESTION_MODE = "mass_ingestion_mode";
	private static final String SINGLE_DB_CONNECTION = "single_db_connection";
	private static final String UNLIMITED_DB_CONNECTION = "unlimited_db_connection";

	private HapiTestSystemProperties() {}

	/**
	 * Set the database connection pool size to 100
	 */
	public static void enableUnlimitedDbConnections() {
		System.setProperty(UNLIMITED_DB_CONNECTION, "true");
	}

	public static boolean isUnlimitedDbConnectionsEnabled() {
		return "true".equals(System.getProperty(UNLIMITED_DB_CONNECTION));
	}

	public static void disableUnlimitedDbConnections() {
		System.clearProperty(UNLIMITED_DB_CONNECTION);
	}

	/**
	 * Creates a DaoConfig with setMassIngestionMode(true) at test app context startup time
	 */
	public static void enableMassIngestionMode() {
		System.setProperty(MASS_INGESTION_MODE, "true");
	}

	public static boolean isMassIngestionModeEnabled() {
		return "true".equals(System.getProperty(MASS_INGESTION_MODE));
	}

	public static void disableMassIngestionMode() {
		System.clearProperty(MASS_INGESTION_MODE);
	}

	/**
	 * Set the database connection pool size to 1
	 */
	public static boolean isSingleDbConnectionEnabled() {
		return "true".equals(System.getProperty(SINGLE_DB_CONNECTION));
	}

	public static void enableSingleDbConnection() {
		System.setProperty(SINGLE_DB_CONNECTION, "true");
	}

	public static void disableSingleDbConnection() {
		System.clearProperty(SINGLE_DB_CONNECTION);
	}
}
