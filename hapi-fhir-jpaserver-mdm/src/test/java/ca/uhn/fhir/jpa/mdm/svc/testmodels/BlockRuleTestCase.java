package ca.uhn.fhir.jpa.mdm.svc.testmodels;

import ca.uhn.fhir.mdm.blocklist.json.BlockListJson;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class BlockRuleTestCase {

	private final String myId;

	/**
	 * Block rule being tested
	 */
	private final BlockListJson myBlockRule;

	/**
	 * Resource being tested (we use only patients for now)
	 */
	private final IBaseResource myPatientResource;

	/**
	 * Expected block result; true if blocked, false if not blocked
	 */
	private final boolean myExpectedBlockResult;

	public BlockRuleTestCase(
		String theId,
		BlockListJson theJson,
		IBaseResource theResource,
		boolean theExpectedResult
	) {
		myId = theId;
		myBlockRule = theJson;
		myPatientResource = theResource;
		myExpectedBlockResult = theExpectedResult;
	}

	public String getId() {
		return myId;
	}

	public BlockListJson getBlockRule() {
		return myBlockRule;
	}

	public IBaseResource getPatientResource() {
		return myPatientResource;
	}

	public boolean isExpectedBlockResult() {
		return myExpectedBlockResult;
	}
}
