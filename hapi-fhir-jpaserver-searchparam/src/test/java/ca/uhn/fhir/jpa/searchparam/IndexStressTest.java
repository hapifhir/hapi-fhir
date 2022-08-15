package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorDstu3;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndexStressTest {

	private static final Logger ourLog = LoggerFactory.getLogger(IndexStressTest.class);

	@Test
	public void testExtractSearchParams() {
		Patient p = new Patient();
		p.addName().setFamily("FOO").addGiven("BAR").addGiven("BAR");
		p.getMaritalStatus().setText("DDDDD");
		p.addAddress().addLine("A").addLine("B").addLine("C");

		FhirContext ctx = FhirContext.forDstu3();
		IValidationSupport mockValidationSupport = mock(IValidationSupport.class);
		when(mockValidationSupport.getFhirContext()).thenReturn(ctx);
		ISearchParamRegistry searchParamRegistry = mock(ISearchParamRegistry.class);
		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(new ModelConfig(), new PartitionSettings(), ctx, searchParamRegistry);
		extractor.start();

		ResourceSearchParams resourceSearchParams = new ResourceSearchParams("Patient");
		ctx.getResourceDefinition("Patient")
			.getSearchParams()
			.forEach(t -> resourceSearchParams.put(t.getName(), t));
		when(searchParamRegistry.getActiveSearchParams(eq("Patient"))).thenReturn(resourceSearchParams);

		Set<ResourceIndexedSearchParamString> params = extractor.extractSearchParamStrings(p);

		StopWatch sw = new StopWatch();
		int loops = 100;
		for (int i = 0; i < loops; i++) {
			params = extractor.extractSearchParamStrings(p);
		}

		ourLog.info("Indexed {} times in {}ms/time", loops, sw.getMillisPerOperation(loops));

		assertEquals(9, params.size());
		verify(mockValidationSupport, times(1)).fetchAllStructureDefinitions();
	}
}
