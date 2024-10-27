package ca.uhn.fhir.model.valueset;

import org.apache.jena.riot.web.HttpMethod;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BundleEntryTransactionMethodEnumTest {

	/**
	 * HEAD is not supported either; but won't show up in transaction bundles
	 */
	@ParameterizedTest
	@EnumSource(value = HttpMethod.class,
		mode = EnumSource.Mode.EXCLUDE,
		names = {"METHOD_TRACE", "METHOD_OPTIONS", "METHOD_QUERY", "METHOD_HEAD"}
	)
	public void parseAllHTTPVerbs(HttpMethod theMethod) {
		// test
		BundleEntryTransactionMethodEnum val = BundleEntryTransactionMethodEnum.valueOf(theMethod.method());

		// verify
		assertEquals(theMethod.method(), val.getCode());
	}
}
