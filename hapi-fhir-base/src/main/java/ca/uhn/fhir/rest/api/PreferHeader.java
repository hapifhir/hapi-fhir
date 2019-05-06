package ca.uhn.fhir.rest.api;

import javax.annotation.Nullable;
import java.util.HashMap;

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

	/**
	 * Represents values for "return" value as provided in the the <a href="https://tools.ietf.org/html/rfc7240#section-4.2">HTTP Prefer header</a>.
	 */
	public enum PreferReturnEnum {

		REPRESENTATION("representation"), MINIMAL("minimal"), OPERATION_OUTCOME("OperationOutcome");

		private static HashMap<String, PreferReturnEnum> ourValues;
		private String myHeaderValue;

		PreferReturnEnum(String theHeaderValue) {
			myHeaderValue = theHeaderValue;
		}

		public String getHeaderValue() {
			return myHeaderValue;
		}

		public static PreferReturnEnum fromHeaderValue(String theHeaderValue) {
			if (ourValues == null) {
				HashMap<String, PreferReturnEnum> values = new HashMap<>();
				for (PreferReturnEnum next : PreferReturnEnum.values()) {
					values.put(next.getHeaderValue(), next);
				}
				ourValues = values;
			}
			return ourValues.get(theHeaderValue);
		}

	}

}
