package ca.uhn.fhir;

/**
 * Spring Boot order constants.
 */
public interface IHapiBootOrder {
	int ADD_JOB_DEFINITIONS = 100;
	int REGISTER_INTERCEPTORS = 200;

	int SUBSCRIPTION_MATCHING_CHANNEL_HANDLER = 300;
	int AFTER_SUBSCRIPTION_INITIALIZED = 310;

}
