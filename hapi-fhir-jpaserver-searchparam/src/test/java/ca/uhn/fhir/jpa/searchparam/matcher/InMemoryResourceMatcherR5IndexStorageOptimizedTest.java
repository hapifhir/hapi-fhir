package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import org.hl7.fhir.r5.model.Observation;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;


public class InMemoryResourceMatcherR5IndexStorageOptimizedTest extends InMemoryResourceMatcherR5Test {

	@BeforeEach
	void setUp() {
		myStorageSettings.setIndexStorageOptimized(true);
	}

	@NotNull
	@Override
	protected ResourceIndexedSearchParamDate extractEffectiveDateParam(Observation theObservation) {
		ResourceIndexedSearchParamDate searchParamDate = super.extractEffectiveDateParam(theObservation);
		searchParamDate.optimizeIndexStorage();
		return searchParamDate;
	}

	@Override
	protected ResourceIndexedSearchParamToken extractCodeTokenParam(Observation theObservation) {
		ResourceIndexedSearchParamToken searchParamToken = super.extractCodeTokenParam(theObservation);
		searchParamToken.optimizeIndexStorage();
		return searchParamToken;
	}

	@Override
	protected ResourceIndexedSearchParamUri extractSourceUriParam(Observation theObservation) {
		ResourceIndexedSearchParamUri searchParamUri = super.extractSourceUriParam(theObservation);
		searchParamUri.optimizeIndexStorage();
		return searchParamUri;
	}
}
