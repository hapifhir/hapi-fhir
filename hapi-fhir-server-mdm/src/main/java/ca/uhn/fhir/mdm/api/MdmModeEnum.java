package ca.uhn.fhir.mdm.api;

public enum MdmModeEnum {
	/**
	 * Normal MDM processing that creates Golden Resources and maintains links
	 */
	LINK_PROCESSING,
	/**
	 * in RULES mode, MDM operations are disabled and no MDM processing occurs. This is useful if, for example,
	 * you want to use the Patient/$match operation without having the overhead of creating links and golden resources.
	 */
	RULES

	// TODO KHS possible future mode DEDUPLICATION where incoming matched resources have their data merged into the golden
	// resource and then deleted with all references updated to point to the golden resource
}
