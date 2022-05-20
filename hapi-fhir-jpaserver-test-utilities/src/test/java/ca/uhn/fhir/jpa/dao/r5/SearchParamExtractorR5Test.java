package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.PathAndRef;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR5;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.collect.Sets;
import org.hl7.fhir.r5.model.Appointment;
import org.hl7.fhir.r5.model.Enumerations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
		participant.setStatus(Enumerations.ParticipationStatus.ACCEPTED);
		appointment.setParticipant(Collections.singletonList(participant));


		//When we extract the Date SPs
		SearchParamExtractorR5 extractor = new SearchParamExtractorR5(new ModelConfig(), new PartitionSettings(), ourCtx, mySearchParamRegistry);
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> dates = extractor.extractSearchParamDates(appointment);

		//We find one, and the lexer doesn't explode.
		assertEquals(1, dates.size());
	}
}
