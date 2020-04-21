package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.test.BaseTest;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
abstract class BaseLoaderTest extends BaseTest {

	@Mock
	protected RequestDetails mySrd;
	@Captor
	protected ArgumentCaptor<List<ConceptMap>> myConceptMapCaptor;
	@Captor
	protected ArgumentCaptor<TermCodeSystemVersion> myCsvCaptor;
	@Captor
	protected ArgumentCaptor<List<ValueSet>> myValueSetsCaptor;
	@Captor
	protected ArgumentCaptor<CodeSystem> mySystemCaptor;

	Map<String, ConceptMap> extractConceptMaps() {
		Map<String, ConceptMap> conceptMaps = new HashMap<>();
		for (ConceptMap next : myConceptMapCaptor.getAllValues().get(0)) {
			conceptMaps.put(next.getId(), next);
		}
		return conceptMaps;
	}

	Map<String, TermConcept> extractConcepts() {
		Map<String, TermConcept> concepts = new HashMap<>();
		for (TermConcept next : myCsvCaptor.getValue().getConcepts()) {
			concepts.put(next.getCode(), next);
		}
		return concepts;
	}

	Map<String, ValueSet> extractValueSets() {
		Map<String, ValueSet> valueSets = new HashMap<>();
		for (ValueSet next : myValueSetsCaptor.getValue()) {
			valueSets.put(next.getId(), next);
		}
		return valueSets;
	}


}
