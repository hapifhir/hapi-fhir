package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR5;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.util.DateUtils;
import org.hl7.fhir.r5.model.Appointment;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Period;
import org.hl7.fhir.r5.model.ServiceRequest;
import org.hl7.fhir.r5.model.Timing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchParamExtractorR5Test {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamExtractorR5Test.class);
	private static final FhirContext ourCtx = FhirContext.forR5Cached();
	private static final StorageSettings ourStorageSettings = new StorageSettings();
	private FhirContextSearchParamRegistry mySearchParamRegistry;

	@BeforeEach
	public void before() {
		mySearchParamRegistry = new FhirContextSearchParamRegistry(ourCtx);
	}

	@Test
	public void testParamWithOrInPath() {
		//Given a basic appointment
		Appointment appointment = new Appointment();
		appointment.setStatus(Appointment.AppointmentStatus.ARRIVED);
		appointment.setStart(new Date());
		appointment.setEnd(new Date());
		Appointment.AppointmentParticipantComponent participant = new Appointment.AppointmentParticipantComponent();
		participant.setStatus(Appointment.ParticipationStatus.ACCEPTED);
		appointment.setParticipant(Collections.singletonList(participant));


		//When we extract the Date SPs
		SearchParamExtractorR5 extractor = new SearchParamExtractorR5(ourStorageSettings, new PartitionSettings(), ourCtx, mySearchParamRegistry);
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> dates = extractor.extractSearchParamDates(appointment);

		//We find one, and the lexer doesn't explode.
		assertThat(dates).hasSize(1);
	}

	@Test
	void testBoundsPeriodEndOnlyIndexesStartOfTimeAsLowValue() {
		// FHIR spec: a missing period.start is "less than" any actual date, so sp_value_low must be the
		// start-of-time sentinel that addDate_Period() uses, not a copy of period.end
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setOccurrence(new Timing()
			.setRepeat(new Timing.TimingRepeatComponent()
				.setBounds(new Period().setEndElement(new DateTimeType("2024-09-16T16:00:00.000-06:00")))));

		SearchParamExtractorR5 extractor = new SearchParamExtractorR5(ourStorageSettings, new PartitionSettings(), ourCtx, mySearchParamRegistry);
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

		SearchParamExtractorR5 extractor = new SearchParamExtractorR5(storageSettings, new PartitionSettings(), ourCtx, new FhirContextSearchParamRegistry(ourCtx));
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
