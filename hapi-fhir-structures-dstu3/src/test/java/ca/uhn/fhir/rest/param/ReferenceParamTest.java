package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class ReferenceParamTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ReferenceParamTest.class);
	private FhirContext ourCtx = FhirContext.forDstu3();

	@Test
	public void testValueWithSlashPersistsAcrossSerialization() {
		ReferenceParam param = new ReferenceParam();
		param.setValueAsQueryToken(ourCtx, "derived-from", ":DocumentReference.contenttype", "application/vnd.mfer");

		assertThat(param.getValueAsQueryToken(ourCtx)).isEqualTo("application/vnd.mfer");
		assertThat(param.getQueryParameterQualifier()).isEqualTo(":DocumentReference.contenttype");

		byte[] serialized = SerializationUtils.serialize(param);
		ourLog.info("Serialized: {}", new String(serialized, Charsets.US_ASCII));
		param = SerializationUtils.deserialize(serialized);

		assertThat(param.getValueAsQueryToken(ourCtx)).isEqualTo("application/vnd.mfer");
		assertThat(param.getQueryParameterQualifier()).isEqualTo(":DocumentReference.contenttype");
	}

	@Test
	public void testWithResourceType() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, null, "Location/123");
		assertThat(rp.getResourceType()).isEqualTo("Location");
		assertThat(rp.getIdPart()).isEqualTo("123");
		assertThat(rp.getValue()).isEqualTo("Location/123");
		assertThat(rp.getQueryParameterQualifier()).isNull();

	}

	@Test
	public void testWithResourceType_AbsoluteUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, null, "http://a.b/c/d/e");
		assertThat(rp.getResourceType()).isEqualTo("d");
		assertThat(rp.getIdPart()).isEqualTo("e");
		assertThat(rp.getValue()).isEqualTo("http://a.b/c/d/e");
		assertThat(rp.getQueryParameterQualifier()).isNull();

	}

	@Test
	public void testWithNoResourceTypeAsQualifierAndChain() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ".name", "FOO");
		assertThat(rp.getResourceType()).isNull();
		assertThat(rp.getIdPart()).isEqualTo("FOO");
		assertThat(rp.getValue()).isEqualTo("FOO");
		assertThat(rp.getQueryParameterQualifier()).isEqualTo(".name");
		assertThat(rp.getChain()).isEqualTo("name");

	}

	@Test
	public void testWithNoResourceTypeAsQualifierAndChain_RelativeUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ".name", "Patient/1233");
		assertThat(rp.getResourceType()).isNull();
		assertThat(rp.getIdPart()).isEqualTo("Patient/1233");
		assertThat(rp.getValue()).isEqualTo("Patient/1233");
		assertThat(rp.getQueryParameterQualifier()).isEqualTo(".name");
		assertThat(rp.getChain()).isEqualTo("name");

	}

	@Test
	public void testWithNoResourceTypeAsQualifierAndChain_AbsoluteUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ".name", "http://something.strange/a/b/c");
		assertThat(rp.getResourceType()).isNull();
		assertThat(rp.getIdPart()).isEqualTo("http://something.strange/a/b/c");
		assertThat(rp.getValue()).isEqualTo("http://something.strange/a/b/c");
		assertThat(rp.getQueryParameterQualifier()).isEqualTo(".name");
		assertThat(rp.getChain()).isEqualTo("name");

	}

	@Test
	public void testWithResourceTypeAsQualifier() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Location", "123");
		assertThat(rp.getResourceType()).isEqualTo("Location");
		assertThat(rp.getIdPart()).isEqualTo("123");
		assertThat(rp.getValue()).isEqualTo("123");
		assertThat(rp.getQueryParameterQualifier()).isNull();

	}

	/**
	 * TODO: is this an error?
	 */
	@Test
	@Disabled
	public void testMismatchedTypeAndValueType() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Location", "Patient/123");
		assertThat(rp.getResourceType()).isEqualTo("Patient");
		assertThat(rp.getIdPart()).isEqualTo("123");
		assertThat(rp.getValue()).isEqualTo("Patient/123");
		assertThat(rp.getQueryParameterQualifier()).isNull();

	}

	@Test
	public void testDuplicatedTypeAndValueType() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Patient", "Patient/123");
		assertThat(rp.getResourceType()).isEqualTo("Patient");
		assertThat(rp.getIdPart()).isEqualTo("123");
		assertThat(rp.getValue()).isEqualTo("Patient/123");
		assertThat(rp.getQueryParameterQualifier()).isNull();

	}

	// TODO: verify this behavior is correct. Same case as testWithResourceTypeAsQualifier_RelativeUrl()
	@Test
	public void testWithResourceTypeAsQualifier_AbsoluteUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Location", "http://a.b/c/d/e");
		assertThat(rp.getResourceType()).isEqualTo("Location");
		assertThat(rp.getIdPart()).isEqualTo("http://a.b/c/d/e");
		assertThat(rp.getValue()).isEqualTo("http://a.b/c/d/e");
		assertThat(rp.getQueryParameterQualifier()).isNull();

	}


	@Test
	public void testWithResourceTypeAsQualifierAndChain() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Location.name", "FOO");
		assertThat(rp.getResourceType()).isEqualTo("Location");
		assertThat(rp.getIdPart()).isEqualTo("FOO");
		assertThat(rp.getValue()).isEqualTo("FOO");
		assertThat(rp.getQueryParameterQualifier()).isEqualTo(":Location.name");
		assertThat(rp.getChain()).isEqualTo("name");

	}

	@Test
	public void testWithResourceTypeAsQualifierAndChain_IdentifierUrlAndValue() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Patient.identifier", "http://hey.there/a/b|123");
		assertThat(rp.getResourceType()).isEqualTo("Patient");
		assertThat(rp.getIdPart()).isEqualTo("http://hey.there/a/b|123");
		assertThat(rp.getValue()).isEqualTo("http://hey.there/a/b|123");
		assertThat(rp.getQueryParameterQualifier()).isEqualTo(":Patient.identifier");
		assertThat(rp.getChain()).isEqualTo("identifier");

	}

	@Test
	public void testWithResourceTypeAsQualifierAndChain_IdentifierUrlOnly() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Patient.identifier", "http://hey.there/a/b|");
		assertThat(rp.getResourceType()).isEqualTo("Patient");
		assertThat(rp.getValue()).isEqualTo("http://hey.there/a/b|");
		assertThat(rp.getIdPart()).isEqualTo("http://hey.there/a/b|");
		assertThat(rp.getQueryParameterQualifier()).isEqualTo(":Patient.identifier");
		assertThat(rp.getChain()).isEqualTo("identifier");

	}

	@Test
	public void testWithResourceTypeAsQualifierAndChain_ValueOnlyNoUrl() {

		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Patient.identifier", "|abc");
		assertThat(rp.getResourceType()).isEqualTo("Patient");
		assertThat(rp.getIdPart()).isEqualTo("|abc");
		assertThat(rp.getValue()).isEqualTo("|abc");
		assertThat(rp.getQueryParameterQualifier()).isEqualTo(":Patient.identifier");
		assertThat(rp.getChain()).isEqualTo("identifier");

	}

	@Test
	public void testGetIdPartAsBigDecimal() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, null, "123");

		assertThat(rp.getIdPartAsBigDecimal().toPlainString()).isEqualTo("123");
	}

	@Test
	public void testGetIdPart() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, null, "123");

		assertThat(rp.isIdPartValidLong()).isTrue();
		assertThat(rp.getIdPart()).isEqualTo("123");
		assertThat(rp.getResourceType(ourCtx)).isNull();
	}

	@Test
	public void testGetIdPartWithType() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, ":Patient", "123");

		assertThat(rp.getIdPart()).isEqualTo("123");
		assertThat(rp.getResourceType(ourCtx).getSimpleName()).isEqualTo("Patient");
	}

	@Test
	public void testSetValueWithType() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValue("Patient/123");

		assertThat(rp.getIdPart()).isEqualTo("123");
		assertThat(rp.getResourceType(ourCtx).getSimpleName()).isEqualTo("Patient");
	}

	@Test
	public void testSetValueWithoutType() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValue("123");

		assertThat(rp.getIdPart()).isEqualTo("123");
		assertThat(rp.getResourceType(ourCtx)).isNull();
	}

	@Test
	public void testGetIdPartAsLong() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, null, "123");

		assertThat(rp.getIdPartAsLong().longValue()).isEqualTo(123L);
	}

	@Test
	public void testToStringParam() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, null, "123");

		assertThat(rp.toStringParam(ourCtx).getValue()).isEqualTo("123");
	}

	@Test
	public void testToTokenParam() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, null, "123");

		assertThat(rp.toTokenParam(ourCtx).getValue()).isEqualTo("123");
	}

	@Test
	public void testToDateParam() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, null, "2020-10-01");

		assertThat(rp.toDateParam(ourCtx).getValueAsString()).isEqualTo("2020-10-01");
	}

	@Test
	public void testToNumberParam() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, null, "1.23");

		assertThat(rp.toNumberParam(ourCtx).getValue().toPlainString()).isEqualTo("1.23");
	}

	@Test
	public void testToQuantityParam() {
		ReferenceParam rp = new ReferenceParam();
		rp.setValueAsQueryToken(ourCtx, null, null, "1.23|http://unitsofmeasure.org|cm");

		assertThat(rp.toQuantityParam(ourCtx).getValue().toPlainString()).isEqualTo("1.23");
		assertThat(rp.toQuantityParam(ourCtx).getSystem()).isEqualTo("http://unitsofmeasure.org");
		assertThat(rp.toQuantityParam(ourCtx).getUnits()).isEqualTo("cm");
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
