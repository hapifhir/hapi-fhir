package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.model.util.JpaConstants;

/**
 * @deprecated TerminologyUploaderProvider
 */
@Deprecated
public class BaseTerminologyUploaderProvider {

	// FIXME: remove these before 4.0.0
	public static final String UPLOAD_EXTERNAL_CODE_SYSTEM = JpaConstants.OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM;
	public static final String CONCEPT_COUNT = "conceptCount";
	public static final String TARGET = "target";
	public static final String SYSTEM = "system";
	public static final String PARENT_CODE = "parentCode";
	public static final String VALUE = "value";

}
