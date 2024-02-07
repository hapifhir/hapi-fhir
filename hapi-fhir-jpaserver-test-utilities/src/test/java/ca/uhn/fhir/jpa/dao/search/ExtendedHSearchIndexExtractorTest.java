package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.search.CompositeSearchIndexData;
import ca.uhn.fhir.jpa.model.search.DateSearchIndexData;
import ca.uhn.fhir.jpa.model.search.ExtendedHSearchIndexData;
import ca.uhn.fhir.jpa.model.search.QuantitySearchIndexData;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParamComposite;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ExtendedHSearchIndexExtractorTest implements ITestDataBuilder.WithSupport {
	FhirContext myFhirContext = FhirContext.forR4Cached();
	JpaStorageSettings myJpaStorageSettings = new JpaStorageSettings();
	FhirContextSearchParamRegistry mySearchParamRegistry = new FhirContextSearchParamRegistry(myFhirContext);
	SearchParamExtractorR4 mySearchParamExtractor = new SearchParamExtractorR4(myJpaStorageSettings, new PartitionSettings(), myFhirContext, mySearchParamRegistry);


	@Test
	void testExtract_composite_producesValues() {
		// setup
		ResourceIndexedSearchParamComposite composite = new ResourceIndexedSearchParamComposite("component-code-value-concept", "Observation.component");

		ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> codeParams = new ISearchParamExtractor.SearchParamSet<>();
		codeParams.add(new ResourceIndexedSearchParamToken(new PartitionSettings(), "Observation", "component-code", "https://example.com", "8480-6"));
		composite.addComponentIndexedSearchParams("component-code", RestSearchParameterTypeEnum.TOKEN, codeParams);

		ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> valueParams = new ISearchParamExtractor.SearchParamSet<>();
		valueParams.add(new ResourceIndexedSearchParamToken(new PartitionSettings(), "Observation", "component-value-concept", "https://example.com", "some_other_value"));
		composite.addComponentIndexedSearchParams("component-value-concept", RestSearchParameterTypeEnum.TOKEN, valueParams);

		ResourceIndexedSearchParams extractedParams = ResourceIndexedSearchParams.withSets();
		extractedParams.myCompositeParams.add(composite);

		// run: now translate to HSearch
		ResourceSearchParams activeSearchParams = mySearchParamRegistry.getActiveSearchParams("Observation");
		ExtendedHSearchIndexExtractor extractor = new ExtendedHSearchIndexExtractor(
			myJpaStorageSettings, myFhirContext, activeSearchParams, mySearchParamExtractor);
		ExtendedHSearchIndexData indexData = extractor.extract(new Observation(), extractedParams);

		// validate
		Set<CompositeSearchIndexData> spIndexData = indexData.getSearchParamComposites().get("component-code-value-concept");
		assertThat(spIndexData).hasSize(1);
	}

	@Test
	void testExtract_withParamMarkedAsMissing_willBeIgnored() {
		//setup
		ResourceIndexedSearchParams searchParams = ResourceIndexedSearchParams.withSets();
		ResourceIndexedSearchParamDate searchParamDate = new ResourceIndexedSearchParamDate(new PartitionSettings(), "SearchParameter", "Date", null, null, null, null, null);
		searchParamDate.setMissing(true);
		searchParams.myDateParams.add(searchParamDate);

		ResourceIndexedSearchParamQuantity searchParamQuantity = new ResourceIndexedSearchParamQuantity(new PartitionSettings(), "SearchParameter", "Quantity", null, null, null);
		searchParamQuantity.setMissing(true);
		searchParams.myQuantityParams.add(searchParamQuantity);

		// run: now translate to HSearch
		ResourceSearchParams activeSearchParams = mySearchParamRegistry.getActiveSearchParams("Patient");
		ExtendedHSearchIndexExtractor extractor = new ExtendedHSearchIndexExtractor(
			myJpaStorageSettings, myFhirContext, activeSearchParams, mySearchParamExtractor);
		ExtendedHSearchIndexData indexData = extractor.extract(new SearchParameter(), searchParams);

		// validate
		Set<DateSearchIndexData> dIndexData = indexData.getDateIndexData().get("Date");
		assertThat(dIndexData).hasSize(0);
		Set<QuantitySearchIndexData> qIndexData = indexData.getQuantityIndexData().get("Quantity");
		assertThat(qIndexData).hasSize(0);

	}

	@Override
	public Support getTestDataBuilderSupport() {
		return new SupportNoDao(myFhirContext);
	}
}
