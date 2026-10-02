package ca.uhn.fhir.rest.param;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class ReferenceParamTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ReferenceParamTest.class);

	private final FhirContext myCtx = FhirContext.forDstu3Cached();

	@Test
	public void testValueWithSlashPersistsAcrossSerialization() {
		ReferenceParam param = new ReferenceParam();
		param.setValueAsQueryToken(myCtx, "derived-from", ":DocumentReference.contenttype", "application/vnd.mfer");

		assertEquals("application/vnd.mfer", param.getValueAsQueryToken());
		assertEquals(":DocumentReference.contenttype", param.getQueryParameterQualifier());

		byte[] serialized = SerializationUtils.serialize(param);
		ourLog.info("Serialized: {}", new String(serialized, Charsets.US_ASCII));
		param = SerializationUtils.deserialize(serialized);

		assertEquals("application/vnd.mfer", param.getValueAsQueryToken());
		assertEquals(":DocumentReference.contenttype", param.getQueryParameterQualifier());
	}

	@Test
	public void testWithResourceType() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, null, "Location/123");
		assertEquals("Location", rp.getResourceType());
		assertEquals("123", rp.getIdPart());
		assertEquals("Location/123", rp.getValue());
		assertNull(rp.getQueryParameterQualifier());

	}

	@Test
	public void testWithResourceType_AbsoluteUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, null, "http://a.b/c/d/e");
		assertEquals("d", rp.getResourceType());
		assertEquals("e", rp.getIdPart());
		assertEquals("http://a.b/c/d/e", rp.getValue());
		assertNull(rp.getQueryParameterQualifier());

	}

	@Test
	public void testWithNoResourceTypeAsQualifierAndChain() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, ".name", "FOO");
		assertNull(rp.getResourceType());
		assertEquals("FOO", rp.getIdPart());
		assertEquals("FOO", rp.getValue());
		assertEquals(".name", rp.getQueryParameterQualifier());
		assertTrue(rp.hasChain());
		assertEquals("name", rp.getChain());

	}

	@Test
	public void testWithNoResourceTypeAsQualifierAndChain_RelativeUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, ".name", "Patient/1233");
		assertNull(rp.getResourceType());
		assertEquals("Patient/1233", rp.getIdPart());
		assertEquals("Patient/1233", rp.getValue());
		assertEquals(".name", rp.getQueryParameterQualifier());
		assertEquals("name", rp.getChain());

	}

	@Test
	public void testWithNoResourceTypeAsQualifierAndChain_AbsoluteUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, ".name", "http://something.strange/a/b/c");
		assertNull(rp.getResourceType());
		assertEquals("http://something.strange/a/b/c", rp.getIdPart());
		assertEquals("http://something.strange/a/b/c", rp.getValue());
		assertEquals(".name", rp.getQueryParameterQualifier());
		assertEquals("name", rp.getChain());

	}

	@Test
	public void testWithResourceTypeAsQualifier() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, ":Location", "123");
		assertEquals("Location", rp.getResourceType());
		assertEquals("123", rp.getIdPart());
		assertEquals("123", rp.getValue());
		assertNull(rp.getQueryParameterQualifier());

	}

	/**
	 * TODO: is this an error?
	 */
	@Test
	@Disabled
	public void testMismatchedTypeAndValueType() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, ":Location", "Patient/123");
		assertEquals("Patient", rp.getResourceType());
		assertEquals("123", rp.getIdPart());
		assertEquals("Patient/123", rp.getValue());
		assertNull(rp.getQueryParameterQualifier());

	}

	@Test
	public void testDuplicatedTypeAndValueType() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, ":Patient", "Patient/123");
		assertEquals("Patient", rp.getResourceType());
		assertEquals("123", rp.getIdPart());
		assertEquals("Patient/123", rp.getValue());
		assertNull(rp.getQueryParameterQualifier());

	}

	// TODO: verify this behavior is correct. Same case as testWithResourceTypeAsQualifier_RelativeUrl()
	@Test
	public void testWithResourceTypeAsQualifier_AbsoluteUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, ":Location", "http://a.b/c/d/e");
		assertEquals("Location", rp.getResourceType());
		assertEquals("http://a.b/c/d/e", rp.getIdPart());
		assertEquals("http://a.b/c/d/e", rp.getValue());
		assertNull(rp.getQueryParameterQualifier());

	}


	@Test
	public void testWithResourceTypeAsQualifierAndChain() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, ":Location.name", "FOO");
		assertEquals("Location", rp.getResourceType());
		assertEquals("FOO", rp.getIdPart());
		assertEquals("FOO", rp.getValue());
		assertEquals(":Location.name", rp.getQueryParameterQualifier());
		assertEquals("name", rp.getChain());

	}

	@Test
	public void testWithResourceTypeAsQualifierAndChain_IdentifierUrlAndValue() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, ":Patient.identifier", "http://hey.there/a/b|123");
		assertEquals("Patient", rp.getResourceType());
		assertEquals("http://hey.there/a/b|123", rp.getIdPart());
		assertEquals("http://hey.there/a/b|123", rp.getValue());
		assertEquals(":Patient.identifier", rp.getQueryParameterQualifier());
		assertEquals("identifier", rp.getChain());

	}

	@Test
	public void testWithResourceTypeAsQualifierAndChain_IdentifierUrlOnly() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, ":Patient.identifier", "http://hey.there/a/b|");
		assertEquals("Patient", rp.getResourceType());
		assertEquals("http://hey.there/a/b|", rp.getValue());
		assertEquals("http://hey.there/a/b|", rp.getIdPart());
		assertEquals(":Patient.identifier", rp.getQueryParameterQualifier());
		assertEquals("identifier", rp.getChain());

	}

	@Test
	public void testWithResourceTypeAsQualifierAndChain_ValueOnlyNoUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, ":Patient.identifier", "|abc");
		assertEquals("Patient", rp.getResourceType());
		assertEquals("|abc", rp.getIdPart());
		assertEquals("|abc", rp.getValue());
		assertEquals(":Patient.identifier", rp.getQueryParameterQualifier());
		assertEquals("identifier", rp.getChain());

	}

	@Test
	public void testGetIdPartAsBigDecimal() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, null, "123");

		assertEquals("123", rp.getIdPartAsBigDecimal().toPlainString());
	}

	@Test
	public void testGetIdPart() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, null, "123");

		assertTrue(rp.isIdPartValidLong());
		assertEquals("123", rp.getIdPart());
		assertNull(rp.getResourceType(myCtx));
	}

	@Test
	public void testGetIdPartWithType() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, ":Patient", "123");

		assertEquals("123", rp.getIdPart());
		assertEquals("Patient", rp.getResourceType(myCtx).getSimpleName());
	}

	@Test
	public void testSetValueWithType() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValue("Patient/123");

		assertEquals("123", rp.getIdPart());
		assertEquals("Patient", rp.getResourceType(myCtx).getSimpleName());
	}

	@Test
	public void testSetValueWithoutType() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValue("123");

		assertEquals("123", rp.getIdPart());
		assertNull(rp.getResourceType(myCtx));
	}

	@Test
	public void testGetIdPartAsLong() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, null, "123");

		assertEquals(123L, rp.getIdPartAsLong().longValue());
	}

	@Test
	public void testToStringParam() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, null, "123");

		assertEquals("123", rp.toStringParam(myCtx).getValue());
	}

	@Test
	public void testToTokenParam() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, null, "123");

		assertEquals("123", rp.toTokenParam(myCtx).getValue());
	}

	@Test
	public void testToDateParam() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, null, "2020-10-01");

		assertEquals("2020-10-01", rp.toDateParam(myCtx).getValueAsString());
	}

	@Test
	public void testToNumberParam() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, null, "1.23");

		assertEquals("1.23", rp.toNumberParam(myCtx).getValue().toPlainString());
	}

	@Test
	public void testToQuantityParam() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(myCtx, null, null, "1.23|http://unitsofmeasure.org|cm");

		assertEquals("1.23", rp.toQuantityParam(myCtx).getValue().toPlainString());
		assertEquals("http://unitsofmeasure.org", rp.toQuantityParam(myCtx).getSystem());
		assertEquals("cm", rp.toQuantityParam(myCtx).getUnits());
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		            , Patient/123 ,             , Patient/123 , true
		.identifier , Patient/123 , .identifier , Patient/123 , true
		:missing    , true        , :missing    , true        , true
		:missing    , false       , :missing    , true        , false
		.blah       , Patient/123 , .identifier , Patient/123 , false
		:mdm        , Patient/123 ,             , Patient/123 , false
		:mdm        , Patient/123 , :mdm        , Patient/123 , true
		""")
	void testEqualsAndHashCode(String theToken0Qualifier, String theToken0Value, String theToken1Qualifier, String theToken1Value, boolean theExpectMatch) {

		ReferenceParam p0 = new ReferenceParam();
		p0.setValueAsQueryToken(null, null, theToken0Qualifier, theToken0Value);

		ReferenceParam p1 = new ReferenceParam();
		p1.setValueAsQueryToken(null, null, theToken1Qualifier, theToken1Value);

		if (theExpectMatch) {
			assertEquals(p0, p1);
			assertEquals(p0.hashCode(), p1.hashCode());
		} else {
			assertNotEquals(p0, p1);
			assertNotEquals(p0.hashCode(), p1.hashCode());
		}
	}

	@Test
	void testChainedParamWithMdmExpand() {
		ReferenceParam param = new ReferenceParam();
		param.setValueAsQueryToken(null, "patient", ".name:mdm", "Smith");

		assertThat(param.isMdmExpand()).isTrue();
		assertThat(param.getChain()).isEqualTo("name");
		assertThat(param.getValue()).isEqualTo("Smith");
	}

	@Test
	void testSetValueToReference_PreviousValueWasChainedToken() {
		ReferenceParam param = new ReferenceParam("identifier", "http://patient|1");
		assertEquals("identifier", param.getChain());
		assertEquals("http://patient|1", param.getValue());
		assertNull(param.getResourceType());
		assertNull(param.getBaseUrl());
		assertEquals("http://patient|1", param.getIdPart());

		// Test
		param.setValue("Patient/123");

		// Verify
		assertNull(param.getBaseUrl());
		assertNull(param.getChain());
		assertEquals("Patient", param.getResourceType());
		assertEquals("123", param.getValue());
		assertEquals("123", param.getIdPart());
	}

	@Test
	void testSetValueToReference_PreviousValueWasQualifiedReference() {
		ReferenceParam param = new ReferenceParam("http://example.com/fhir/Patient/A");
		assertNull(param.getChain());
		assertEquals("http://example.com/fhir", param.getBaseUrl());
		assertEquals("Patient", param.getResourceType());
		assertEquals("http://example.com/fhir/Patient/A", param.getValue());
		assertEquals("A", param.getIdPart());

		// Test
		param.setValue("Patient/123");

		// Verify
		assertNull(param.getBaseUrl());
		assertEquals("Patient", param.getResourceType());
		assertEquals("123", param.getValue());
		assertEquals("123", param.getIdPart());
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
