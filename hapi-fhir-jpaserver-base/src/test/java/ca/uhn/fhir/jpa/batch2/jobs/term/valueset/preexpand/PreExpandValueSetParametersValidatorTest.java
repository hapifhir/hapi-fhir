package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.persistence.Id;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

}
