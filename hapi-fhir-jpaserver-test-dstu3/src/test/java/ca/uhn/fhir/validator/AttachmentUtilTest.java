package ca.uhn.fhir.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.AttachmentUtil;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttachmentUtilTest {

	@Test
	public void testCreateAttachmentDstu3() {
		FhirContext ctx = FhirContext.forDstu3Cached();
		ICompositeType attachment = AttachmentUtil.newInstance(ctx);
		AttachmentUtil.setData(ctx, attachment, new byte[]{0, 1, 2, 3});
		AttachmentUtil.setUrl(ctx, attachment, "http://foo");
		AttachmentUtil.setContentType(ctx, attachment, "text/plain");
		AttachmentUtil.setSize(ctx, attachment, 123);

		org.hl7.fhir.dstu3.model.Observation obs = new org.hl7.fhir.dstu3.model.Observation();
		obs.setValue((org.hl7.fhir.dstu3.model.Type) attachment);
		String encoded = ctx.newJsonParser().encodeResourceToString(obs);
		assertEquals("{\"resourceType\":\"Observation\",\"valueAttachment\":{\"contentType\":\"text/plain\",\"data\":\"AAECAw==\",\"url\":\"http://foo\",\"size\":123}}", encoded);
	}

	@Test
	public void testCreateAttachmentR4() {
		FhirContext ctx = FhirContext.forR4Cached();
		ICompositeType attachment = AttachmentUtil.newInstance(ctx);
		AttachmentUtil.setData(ctx, attachment, new byte[]{0, 1, 2, 3});
		AttachmentUtil.setUrl(ctx, attachment, "http://foo");
		AttachmentUtil.setContentType(ctx, attachment, "text/plain");
		AttachmentUtil.setSize(ctx, attachment, 123);

		org.hl7.fhir.r4.model.Communication communication = new org.hl7.fhir.r4.model.Communication();
		communication.addPayload().setContent((org.hl7.fhir.r4.model.Type) attachment);
		String encoded = ctx.newJsonParser().encodeResourceToString(communication);
		assertEquals("{\"resourceType\":\"Communication\",\"payload\":[{\"contentAttachment\":{\"contentType\":\"text/plain\",\"data\":\"AAECAw==\",\"url\":\"http://foo\",\"size\":123}}]}", encoded);
	}

	@Test
	public void testCreateAttachmentR5() {
		FhirContext ctx = FhirContext.forR5Cached();
		ICompositeType attachment = AttachmentUtil.newInstance(ctx);
		AttachmentUtil.setData(ctx, attachment, new byte[]{0, 1, 2, 3});
		AttachmentUtil.setUrl(ctx, attachment, "http://foo");
		AttachmentUtil.setContentType(ctx, attachment, "text/plain");
		AttachmentUtil.setSize(ctx, attachment, 123);

		org.hl7.fhir.r5.model.Communication communication = new org.hl7.fhir.r5.model.Communication();
		communication.addPayload().setContent((org.hl7.fhir.r5.model.DataType) attachment);
		String encoded = ctx.newJsonParser().encodeResourceToString(communication);
		assertEquals("{\"resourceType\":\"Communication\",\"payload\":[{\"contentAttachment\":{\"contentType\":\"text/plain\",\"data\":\"AAECAw==\",\"url\":\"http://foo\",\"size\":123}}]}", encoded);
	}
}
