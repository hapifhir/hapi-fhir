package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PreExpandValueSetParametersValidatorTest {

	@Spy
	FhirContext myFhirContext = FhirContext.forR4Cached();

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IValidationSupport myValidationSupport;

	@Mock
	private IFhirResourceDao<ValueSet> myValueSetDao;

	@InjectMocks
	private PreExpandValueSetParametersValidator mySvc;

	@Test
	void testValidate_ValueSetWithNoUrl() {

		// Setup

		PreExpandValueSetParameters parameters = new PreExpandValueSetParameters();
		parameters.setId("ValueSet/1");

		when(myDaoRegistry.getResourceDao(eq("ValueSet"))).thenReturn(myValueSetDao);
		when(myValueSetDao.read(eq(new IdType("ValueSet/1")), any())).thenReturn(new ValueSet());

		// Test

		List<String> errors = mySvc.validate(new SystemRequestDetails(), parameters);

		// Validate

		assertThat(errors).containsExactly(
			"ValueSet does not have a URL and can not be pre-expanded: ValueSet/1"
		);

	}

	@Test
	void testValidate_ValueSetIdNotFound() {

		// Setup

		PreExpandValueSetParameters parameters = new PreExpandValueSetParameters();
		parameters.setId("ValueSet/1");

		when(myDaoRegistry.getResourceDao(eq("ValueSet"))).thenReturn(myValueSetDao);
		when(myValueSetDao.read(eq(new IdType("ValueSet/1")), any())).thenThrow(new ResourceNotFoundException("ValueSet/1"));

		// Test

		List<String> errors = mySvc.validate(new SystemRequestDetails(), parameters);

		// Validate

		assertThat(errors).containsExactly(
			"ValueSet does not exist: ValueSet/1"
		);

	}

	// Created by Claude Opus 5.5
	@Test
	void testValidate_ValueSetUrlAndVersionNotFound() {

		// Setup

		PreExpandValueSetParameters parameters = new PreExpandValueSetParameters();
		parameters.setUrl("http://foo");
		parameters.setVersion("1.0");

		// Test

		List<String> errors = mySvc.validate(new SystemRequestDetails(), parameters);

		// Validate

		assertThat(errors).containsExactly(
			"ValueSet not found: http://foo|1.0"
		);
		verify(myValidationSupport).fetchValueSet("http://foo", "1.0");

	}

	@Test
	void testValidate_TooManyParameters() {

		// Setup

		PreExpandValueSetParameters parameters = new PreExpandValueSetParameters();
		parameters.setUrl("http://foo");
		parameters.setId("ValueSet/1");

		// Test

		List<String> errors = mySvc.validate(new SystemRequestDetails(), parameters);

		// Validate

		assertThat(errors).containsExactly(
			"Can not combine ValueSet ID with URL or version parameters"
		);

	}

	@Test
	void testValidate_EmptyParameters() {

		// Setup

		PreExpandValueSetParameters parameters = new PreExpandValueSetParameters();

		// Test

		List<String> errors = mySvc.validate(new SystemRequestDetails(), parameters);

		// Validate

		assertThat(errors).containsExactly(
			"Either a ValueSet URL or a ValueSet ID must be provided"
		);

	}

}
