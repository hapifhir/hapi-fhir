package ca.uhn.fhir.batch2.model;

/**
 * A class to be extended so that data can be accumulated across
 * all "consumption" steps in a reduction job.
 * Named generically so it can be used in StepExecutionDetails
 * (a concrete class).
 *
 * Not a serializable class, so any data can be set and used for consumption.
 * But none will be persisted to the DB.
 */
public class AdditionalData {
}
