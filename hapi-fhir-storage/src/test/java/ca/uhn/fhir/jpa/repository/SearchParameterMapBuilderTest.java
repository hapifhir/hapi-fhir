package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SearchParameterMapBuilderTest {
	FhirContext myFhirContext = FhirContext.forR4Cached();
	SearchParameterMapBuilder myBuilder = new SearchParameterMapBuilder();
	SearchParameterMap myResult;

	@Test
	void  testIdParameter() {
	    // given
		myBuilder.addOrList("_id", new ReferenceParam("123"), new ReferenceParam("345"));

	    // when
		myResult = myBuilder.build();

	    // then
	    assertThat(myResult.toNormalizedQueryString(myFhirContext)).isEqualTo("?_id=123,345");
	}

	@Test
	void testCountParameter() {
		// given
		myBuilder.addNumericParameter("_count", 123);

		// when
		myResult = myBuilder.build();

		// then
		assertThat(myResult.toNormalizedQueryString(myFhirContext)).isEqualTo("?_count=123");
	}

	@Test
	void testMultipleCountUsesLast() {
		// given
		myBuilder.addOrList("_count", new NumberParam("123"), new NumberParam("50"));
		myBuilder.addOrList("_count", new NumberParam("23"), new NumberParam("42"));

		// when
		// when
		myResult = myBuilder.build();

		// then
		assertThat(myResult.toNormalizedQueryString(myFhirContext)).isEqualTo("?_count=42");
	}
}
