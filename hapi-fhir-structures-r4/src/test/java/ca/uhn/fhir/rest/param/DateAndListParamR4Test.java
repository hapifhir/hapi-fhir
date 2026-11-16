package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DateAndListParamR4Test {

	private static DateAndListParam ourLastActual;

	@RegisterExtension
	private RestfulServerExtension myServer = new RestfulServerExtension(FhirContext.forR4Cached())
		 .registerProvider(new ObservationProvider());


	@Test
	void testOr_NoQualifier() {
		// Test
		myServer.fhirRequest("/Observation?date=2020,2023").get().assertStatus(200);

		// Verify
		DateAndListParam actual = ourLastActual;
		assertEquals(1, actual.size());
		DateOrListParam actualOrList0 = actual.getValuesAsQueryTokens().get(0);
		assertEquals(2, actualOrList0.size());
		DateParam actualParam0 = actualOrList0.getValuesAsQueryTokens().get(0);
		DateParam actualParam1 = actualOrList0.getValuesAsQueryTokens().get(1);
		assertEquals("2020", actualParam0.getValueAsString());
		assertNull(actualParam0.getPrefix());
		assertEquals(TemporalPrecisionEnum.YEAR, actualParam0.getPrecision());
		assertEquals("2023", actualParam1.getValueAsString());
		assertNull(actualParam1.getPrefix());
		assertEquals(TemporalPrecisionEnum.YEAR, actualParam1.getPrecision());
	}

	@Test
	void testOr_QualifierOnFirst() {
		// Test
		myServer.fhirRequest("/Observation?date=lt2020,2023").get().assertStatus(200);

		// Verify
		DateAndListParam actual = ourLastActual;
		assertEquals(1, actual.size());
		DateOrListParam actualOrList0 = actual.getValuesAsQueryTokens().get(0);
		assertEquals(2, actualOrList0.size());
		DateParam actualParam0 = actualOrList0.getValuesAsQueryTokens().get(0);
		DateParam actualParam1 = actualOrList0.getValuesAsQueryTokens().get(1);
		assertEquals("2020", actualParam0.getValueAsString());
		assertEquals(ParamPrefixEnum.LESSTHAN, actualParam0.getPrefix());
		assertEquals(TemporalPrecisionEnum.YEAR, actualParam0.getPrecision());
		assertEquals("2023", actualParam1.getValueAsString());
		assertNull(actualParam1.getPrefix());
		assertEquals(TemporalPrecisionEnum.YEAR, actualParam1.getPrecision());
	}

	@Test
	void testAndOr_QualifiersOnAllParams() {
		// Test
		myServer.fhirRequest("/Observation?date=ge2026-01-01,lt2025&date=ap2017,ge2018-01-01T12:12:12.001Z").get().assertStatus(200);

		// Verify
		DateAndListParam actual = ourLastActual;
		assertEquals(2, actual.size());
		DateOrListParam actualOrList0 = actual.getValuesAsQueryTokens().get(0);
		assertEquals(2, actualOrList0.size());
		DateParam actualParam0 = actualOrList0.getValuesAsQueryTokens().get(0);
		DateParam actualParam1 = actualOrList0.getValuesAsQueryTokens().get(1);
		assertEquals("2026-01-01", actualParam0.getValueAsString());
		assertEquals(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, actualParam0.getPrefix());
		assertEquals(TemporalPrecisionEnum.DAY, actualParam0.getPrecision());
		assertEquals("2025", actualParam1.getValueAsString());
		assertEquals(ParamPrefixEnum.LESSTHAN, actualParam1.getPrefix());
		assertEquals(TemporalPrecisionEnum.YEAR, actualParam1.getPrecision());

		DateOrListParam actualOrList1 = actual.getValuesAsQueryTokens().get(1);
		assertEquals(2, actualOrList1.size());
		actualParam0 = actualOrList1.getValuesAsQueryTokens().get(0);
		actualParam1 = actualOrList1.getValuesAsQueryTokens().get(1);
		assertEquals("2017", actualParam0.getValueAsString());
		assertEquals(ParamPrefixEnum.APPROXIMATE, actualParam0.getPrefix());
		assertEquals(TemporalPrecisionEnum.YEAR, actualParam0.getPrecision());
		assertEquals("2018-01-01T12:12:12.001Z", actualParam1.getValueAsString());
		assertEquals(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, actualParam1.getPrefix());
		assertEquals(TemporalPrecisionEnum.MILLI, actualParam1.getPrecision());
	}


	public static class ObservationProvider implements IResourceProvider {

		@Search
		public List<Observation> search(@RequiredParam(name = "date") DateAndListParam theDateAndListParam) {
			ourLastActual = theDateAndListParam;
			return List.of();
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}
	}

}
