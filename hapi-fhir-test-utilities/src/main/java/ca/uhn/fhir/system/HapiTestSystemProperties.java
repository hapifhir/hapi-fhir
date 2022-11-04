package ca.uhn.fhir.system;

public final class HapiTestSystemProperties {
	private static final String MASS_INGESTION_MODE = "mass_ingestion_mode";
	private static final String SINGLE_DB_CONNECTION = "single_db_connection";
	private static final String UNLIMITED_DB_CONNECTION = "unlimited_db_connection";

	private HapiTestSystemProperties() {}

	public static void enableUnlimitedDbConnections() {
		System.setProperty(UNLIMITED_DB_CONNECTION, "true");
	}

	public static boolean isUnlimitedDbConnectionsEnabled() {
		return "true".equals(System.getProperty(UNLIMITED_DB_CONNECTION));
	}

	public static void disableUnlimitedDbConnections() {
		System.clearProperty(UNLIMITED_DB_CONNECTION);
	}

	public static void enableMassIngestionMode() {
		System.setProperty(MASS_INGESTION_MODE, "true");
	}

	public static boolean isMassIngestionModeEnabled() {
		return "true".equals(System.getProperty(MASS_INGESTION_MODE));
	}

	public static void disableMassIngestionMode() {
		System.clearProperty(MASS_INGESTION_MODE);
	}

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
