package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.StringUtil;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CanonicalType;
import org.hl7.fhir.r5.model.MessageHeader;
import org.hl7.fhir.r5.model.Narrative;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.UrlType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirResourceDaoR5ValidationTest extends BaseJpaR5Test {

	/**
	 * When validating message and document bundles, the validator tries to resolve
	 * any URLs (such as the "example.com" ones in this test) from the
	 * {@link ca.uhn.fhir.context.support.IValidationSupport} infrastructure.
	 * Make sure we don't throw any errors when this happens.
	 */
	@Test
	public void testValidate_withInterlinkedReference_returnsNoErrors() {
		// Setup
		BundleBuilder bb = new BundleBuilder(myFhirContext);

		MessageHeader mh = new MessageHeader();
		mh.setId("http://example.com/MessageHeader/123");
		mh.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		mh.getText().getDiv().setValue("<div>Hello</div>");
		// The validator will try to resolve these URLs by fetching them
		// using the IWorkerContext/IValidationSupport
		mh.setEvent(new CanonicalType("http://example.com/event123"));
		mh.getSource().setEndpoint(new UrlType("http://example.com/Endpoint/12345"));
		bb.addCollectionEntry(mh);

		Bundle bundle = bb.getBundleTyped();
		String serialized = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);

		// Test
		MethodOutcome outcome = myBundleDao.validate(bundle, null, serialized, EncodingEnum.JSON, ValidationModeEnum.CREATE, null, mySrd);

		// Verify
		ourLog.info(StringUtil.prependLineNumbers(serialized));
		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));

		assertEquals("No issues detected during validation", oo.getIssueFirstRep().getDiagnostics());
	}

}


