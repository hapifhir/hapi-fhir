package ca.uhn.fhir.jpa.dao.r4b;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
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
	private final StorageSettings myStorageSettings = new StorageSettings();
	private SearchParamExtractorR4B myExtractor;

	// Created by Claude Opus 5.5
	@BeforeEach
	void before() {
		myExtractor = new SearchParamExtractorR4B(myStorageSettings, new PartitionSettings(), ourCtx, new FhirContextSearchParamRegistry(ourCtx));
	}

	// Created by Claude Opus 5.5
	private ResourceIndexedSearchParamDate extractOccurrenceParam(ServiceRequest theServiceRequest) {
		return myExtractor.extractSearchParamDates(theServiceRequest).stream()
			.filter(p -> "occurrence".equals(p.getParamName()))
			.findFirst()
			.orElse(null);
	}

	@Test
	void testBoundsPeriod_endOnly_indexesStartOfTimeAsLowValue() {
		// FHIR spec: a missing period.start is "less than" any actual date, so sp_value_low must be the
		// start-of-time sentinel that addDate_Period() uses
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setOccurrence(new Timing()
			.setRepeat(new Timing.TimingRepeatComponent()
				.setBounds(new Period().setEndElement(new DateTimeType("2024-09-16T16:00:00.000-06:00")))));

		ResourceIndexedSearchParamDate occurrence = extractOccurrenceParam(serviceRequest);

		assertThat(occurrence).isNotNull();
		assertThat(occurrence.getValueLow()).isEqualTo(myStorageSettings.getPeriodIndexStartOfTime().getValue());
		assertThat(occurrence.getValueHigh()).isEqualTo("2024-09-16T16:00:00.000-06:00");
	}

	@Test
	void testBoundsPeriod_startOnly_indexesEndOfTimeAsHighValue() {
		// FHIR spec: a missing period.end is "greater than" any actual date, so sp_value_high must be the
		// end-of-time sentinel that addDate_Period() uses
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setOccurrence(new Timing()
			.setRepeat(new Timing.TimingRepeatComponent()
				.setBounds(new Period().setStartElement(new DateTimeType("2024-09-16T16:00:00.000-06:00")))));

		ResourceIndexedSearchParamDate occurrence = extractOccurrenceParam(serviceRequest);

		assertThat(occurrence).isNotNull();
		assertThat(occurrence.getValueLow()).isEqualTo("2024-09-16T16:00:00.000-06:00");
		assertThat(occurrence.getValueHigh()).isEqualTo(DateUtils.getEndOfDay(myStorageSettings.getPeriodIndexEndOfTime().getValue()));
	}
}
