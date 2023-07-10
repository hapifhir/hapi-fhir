package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a CDS Hooks Service Feedback Request
 *
 * @see <a href="https://cds-hooks.hl7.org/ballots/2020Sep/">Version 1.1 of the CDS Hooks Specification</a>
 */
public class CdsServiceFeedbackJson implements IModelJson {
	@JsonProperty(value = "card", required = true)
	String myCard;

	@JsonProperty(value = "outcome", required = true)
	CdsServiceFeebackOutcomeEnum myOutcome;

	@JsonProperty(value = "acceptedSuggestions")
	List<CdsServiceAcceptedSuggestionJson> myAcceptedSuggestions;

	@JsonProperty(value = "overrideReason")
	CdsServiceOverrideReasonJson myOverrideReason;

	@JsonProperty(value = "outcomeTimestamp", required = true)
	String myOutcomeTimestamp;

	public String getCard() {
		return myCard;
	}

	public CdsServiceFeedbackJson setCard(String theCard) {
		myCard = theCard;
		return this;
	}

	public CdsServiceFeebackOutcomeEnum getOutcome() {
		return myOutcome;
	}

	public CdsServiceFeedbackJson setOutcome(CdsServiceFeebackOutcomeEnum theOutcome) {
		myOutcome = theOutcome;
		return this;
	}

	public List<CdsServiceAcceptedSuggestionJson> getAcceptedSuggestions() {
		return myAcceptedSuggestions;
	}

	public CdsServiceFeedbackJson setAcceptedSuggestions(List<CdsServiceAcceptedSuggestionJson> theAcceptedSuggestions) {
		myAcceptedSuggestions = theAcceptedSuggestions;
		return this;
	}

	public CdsServiceOverrideReasonJson getOverrideReason() {
		return myOverrideReason;
	}

	public CdsServiceFeedbackJson setOverrideReason(CdsServiceOverrideReasonJson theOverrideReason) {
		myOverrideReason = theOverrideReason;
		return this;
	}

	public String getOutcomeTimestamp() {
		return myOutcomeTimestamp;
	}

	public CdsServiceFeedbackJson setOutcomeTimestamp(String theOutcomeTimestamp) {
		myOutcomeTimestamp = theOutcomeTimestamp;
		return this;
	}
}
