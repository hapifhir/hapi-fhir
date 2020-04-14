package ca.uhn.fhir.empi.api;

public class Constants {
	/**
	 * TAG system for Person resources which are managed by HAPI EMPI.
	 * FIXME EMPI move these into the HAPI (non-jpa) module
	 */

	public static final String SYSTEM_EMPI_MANAGED = "https://hapifhir.org/NamingSystem/managing-empi-system";
	public static final String CODE_HAPI_EMPI_MANAGED = "HAPI-EMPI";
	public static final String CODE_NO_EMPI_MANAGED = "NO-EMPI";
	public static final String HAPI_ENTERPRISE_IDENTIFIER_SYSTEM = "http://hapifhir.io/fhir/NamingSystem/empi-person-enterprise-id";
	public static final String ALL_RESOURCE_SEARCH_PARAM_TYPE = "All";


}
