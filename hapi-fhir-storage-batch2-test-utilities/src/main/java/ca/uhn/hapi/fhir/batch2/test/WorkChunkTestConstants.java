package ca.uhn.hapi.fhir.batch2.test;

public interface WorkChunkTestConstants {
	public static final String JOB_DEFINITION_ID = "definition-id";
	public static final String TARGET_STEP_ID = "step-id";
	public static final String DEF_CHUNK_ID = "definition-chunkId";
	public static final int JOB_DEF_VER = 1;
	public static final int SEQUENCE_NUMBER = 1;
	public static final String CHUNK_DATA = "{\"key\":\"value\"}";
	public static final String ERROR_MESSAGE_A = "This is an error message: A";
	public static final String ERROR_MESSAGE_B = "This is a different error message: B";
	public static final String ERROR_MESSAGE_C = "This is a different error message: C";

	String FIRST_ERROR_MESSAGE = ERROR_MESSAGE_A;
}
