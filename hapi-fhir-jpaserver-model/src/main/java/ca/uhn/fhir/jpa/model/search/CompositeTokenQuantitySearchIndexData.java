package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.TokenParam;

import javax.annotation.Nullable;

//fixme jm: extend a generic CompositeSearchIndexData
public class CompositeTokenQuantitySearchIndexData {

	private final TokenSearchIndexData myTokenSearchIndexData;
	private final QuantitySearchIndexData myQtySearchIndexData;

	public CompositeTokenQuantitySearchIndexData(@Nullable String theTokenSystem, String theTokenCode,
																@Nullable String theQtySystem, @Nullable String theQtyCode, double theQtyValue) {

		myTokenSearchIndexData = buildTokenSearchIndexData(theTokenSystem, theTokenCode);
		myQtySearchIndexData = buildQuantitySearchIndexData(theQtyCode, theQtySystem, theQtyValue);
	}

	private TokenSearchIndexData buildTokenSearchIndexData(@Nullable String theTokenSystem, String theTokenCode) {
		return new TokenSearchIndexData(theTokenSystem, theTokenCode);
	}

	private QuantitySearchIndexData buildQuantitySearchIndexData(
			@Nullable String theQtySystem, @Nullable String theQtyCode, double theQtyValue) {
		return new QuantitySearchIndexData(theQtyCode, theQtySystem, theQtyValue);
	}


	public TokenSearchIndexData getTokenSearchIndexData() {
		return myTokenSearchIndexData;
	}

	public QuantitySearchIndexData getQtySearchIndexData() {
		return myQtySearchIndexData;
	}


}
