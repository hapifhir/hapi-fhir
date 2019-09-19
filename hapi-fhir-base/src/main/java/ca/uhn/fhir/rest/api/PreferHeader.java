package ca.uhn.fhir.rest.api;

import javax.annotation.Nullable;

public class PreferHeader {

	private PreferReturnEnum myReturn;
	private boolean myRespondAsync;

	public @Nullable
	PreferReturnEnum getReturn() {
		return myReturn;
	}

	public PreferHeader setReturn(PreferReturnEnum theReturn) {
		myReturn = theReturn;
		return this;
	}

	public boolean getRespondAsync() {
		return myRespondAsync;
	}

	public PreferHeader setRespondAsync(boolean theRespondAsync) {
		myRespondAsync = theRespondAsync;
		return this;
	}

}
