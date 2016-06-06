package ca.uhn.fhir.context;

/**
 * This enum contains options to be used for {@link FhirContext#setPerformanceOptions(PerformanceOptionsEnum...)}
 */
public enum PerformanceOptionsEnum {

	/**
	 * When this option is set, model classes will not be scanned for children until the 
	 * child list for the given type is actually accessed.
	 * <p>
	 * The effect of this option is that reflection operations to scan children will be
	 * deferred, and some may never happen if specific model types aren't actually used.
	 * This option is useful on environments where reflection is particularly slow, e.g.
	 * Android or low powered devices.
	 * </p> 
	 */
	DEFERRED_MODEL_SCANNING
	
}
