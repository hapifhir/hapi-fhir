package ca.uhn.fhir.jpa.stresstest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.ISearchParamRegistry;
import ca.uhn.fhir.jpa.dao.dstu3.SearchParamExtractorDstu3;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.CachingValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class IndexStressTest {

	private static final Logger ourLog = LoggerFactory.getLogger(IndexStressTest.class);

	@Test
	public void testExtractSearchParams() {
		Patient p = new Patient();
		p.addName().setFamily("FOO").addGiven("BAR").addGiven("BAR");
		p.getMaritalStatus().setText("DDDDD");
		p.addAddress().addLine("A").addLine("B").addLine("C");

		DaoConfig daoConfig = new DaoConfig();
		FhirContext ctx = FhirContext.forDstu3();
		IValidationSupport mockValidationSupport = mock(IValidationSupport.class);
		IValidationSupport validationSupport = new CachingValidationSupport(new ValidationSupportChain(new DefaultProfileValidationSupport(), mockValidationSupport));
		ISearchParamRegistry searchParamRegistry = mock(ISearchParamRegistry.class);
		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(daoConfig, ctx, validationSupport, searchParamRegistry);
		extractor.start();

		Map<String, RuntimeSearchParam> spMap = ctx
			.getResourceDefinition("Patient")
			.getSearchParams()
			.stream()
			.collect(Collectors.toMap(RuntimeSearchParam::getName, t -> t));
		when(searchParamRegistry.getActiveSearchParams(eq("Patient"))).thenReturn(spMap);

		ResourceTable entity = new ResourceTable();
		Set<ResourceIndexedSearchParamString> params = extractor.extractSearchParamStrings(entity, p);

		StopWatch sw = new StopWatch();
		int loops = 100;
		for (int i = 0; i < loops; i++) {
			entity = new ResourceTable();
			params = extractor.extractSearchParamStrings(entity, p);
		}

		ourLog.info("Indexed {} times in {}ms/time", loops, sw.getMillisPerOperation(loops));

		assertEquals(9, params.size());
		verify(mockValidationSupport, times(1)).fetchAllStructureDefinitions((any(FhirContext.class)));
	}
}
