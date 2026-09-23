package ca.uhn.fhir.jpa.dao.r4b;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4B;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.util.DateUtils;
import org.hl7.fhir.r4b.model.DateTimeType;
import org.hl7.fhir.r4b.model.Period;
import org.hl7.fhir.r4b.model.ServiceRequest;
import org.hl7.fhir.r4b.model.Timing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SearchParamExtractorR4BTest {

	private static final FhirContext ourCtx = FhirContext.forR4BCached();
	private static final StorageSettings ourStorageSettings = new StorageSettings();
	private FhirContextSearchParamRegistry mySearchParamRegistry;

	@BeforeEach
	void before() {
		mySearchParamRegistry = new FhirContextSearchParamRegistry(ourCtx);
	}

	@Test
	void testBoundsPeriodEndOnlyIndexesStartOfTimeAsLowValue() {
		// FHIR spec: a missing period.start is "less than" any actual date, so sp_value_low must be the
		// start-of-time sentinel that addDate_Period() uses
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setOccurrence(new Timing()
			.setRepeat(new Timing.TimingRepeatComponent()
				.setBounds(new Period().setEndElement(new DateTimeType("2024-09-16T16:00:00.000-06:00")))));

		SearchParamExtractorR4B extractor = new SearchParamExtractorR4B(ourStorageSettings, new PartitionSettings(), ourCtx, mySearchParamRegistry);
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> dates = extractor.extractSearchParamDates(serviceRequest);

		ResourceIndexedSearchParamDate occurrence = dates.stream()
			.filter(p -> "occurrence".equals(p.getParamName()))
			.findFirst()
			.orElse(null);

		assertThat(occurrence).isNotNull();
		assertThat(occurrence.getValueHigh()).isEqualTo("2024-09-16T16:00:00.000-06:00");
		assertThat(occurrence.getValueLow()).isEqualTo(ourStorageSettings.getPeriodIndexStartOfTime().getValue());
	}

	@Test
	void testBoundsPeriodStartOnlyIndexesEndOfTimeAsHighValue() {
		// FHIR spec: a missing period.end is "greater than" any actual date, so sp_value_high must be the
		// end-of-time sentinel that addDate_Period() uses
		StorageSettings storageSettings = new StorageSettings();
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setOccurrence(new Timing()
				.setRepeat(new Timing.TimingRepeatComponent()
						.setBounds(new Period().setStartElement(new DateTimeType("2024-09-16T16:00:00.000-06:00")))));

		SearchParamExtractorR4B extractor = new SearchParamExtractorR4B(storageSettings, new PartitionSettings(), ourCtx, new FhirContextSearchParamRegistry(ourCtx));
		extractor.start();
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> dates = extractor.extractSearchParamDates(serviceRequest);

		ResourceIndexedSearchParamDate occurrence = dates.stream()
				.filter(p -> "occurrence".equals(p.getParamName()))
				.findFirst()
				.orElse(null);

		assertThat(occurrence).isNotNull();
		assertThat(occurrence.getValueLow()).isEqualTo("2024-09-16T16:00:00.000-06:00");
		assertThat(occurrence.getValueHigh()).isEqualTo(DateUtils.getEndOfDay(storageSettings.getPeriodIndexEndOfTime().getValue()));
	}
}
