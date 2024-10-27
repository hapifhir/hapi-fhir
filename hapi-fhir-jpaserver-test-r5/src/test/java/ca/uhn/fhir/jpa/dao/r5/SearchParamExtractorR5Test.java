package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR5;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import org.hl7.fhir.r5.model.Appointment;
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
		SearchParamExtractorR5 extractor = new SearchParamExtractorR5(new StorageSettings(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> dates = extractor.extractSearchParamDates(appointment);

		//We find one, and the lexer doesn't explode.
		assertThat(dates).hasSize(1);
	}
}
