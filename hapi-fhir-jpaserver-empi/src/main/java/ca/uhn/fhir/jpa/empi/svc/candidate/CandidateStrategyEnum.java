package ca.uhn.fhir.jpa.empi.svc.candidate;

public enum CandidateStrategyEnum {
	/** Find Person candidates based on matching EID */
	EID,
	/** Find Person candidates based on a link already existing for the target resource */
	LINK,
	/** Find Person candidates based on other targets that match the incoming target using the EMPI Matching rules */
	SCORE
}
