package ca.uhn.fhir.jpa.mdm.helper.testmodels;

import static org.junit.jupiter.api.Assertions.fail;

public class MdmTestLinkExpression {

	private final String myLinkExpression;

	private final String myLeftSideResourceIdentifier;
	private final String myMdmLinkSource;
	private final String myMdmMatchResult;
	private final String myRightSideResourceIdentifier;

	public MdmTestLinkExpression(
		String theInputState
	) {
		myLinkExpression = theInputState;
		String[] params = parseState(theInputState);

		myLeftSideResourceIdentifier = params[0].trim();
		myMdmLinkSource = params[1].trim();
		myMdmMatchResult = params[2].trim();
		myRightSideResourceIdentifier = params[3].trim();
	}

	private String[] parseState(String theStateString) {
		String[] state = theStateString.split(",");
		if (state.length != 4) {
			// we're using this exclusively in test area
			fail(String.format("%s must contain 4 arguments; found %d", theStateString, state.length));
		}

		return state;
	}

	public String getLinkExpression() {
		return myLinkExpression;
	}

	public String getLeftSideResourceIdentifier() {
		return myLeftSideResourceIdentifier;
	}

	public String getMdmLinkSource() {
		return myMdmLinkSource;
	}

	public String getMdmMatchResult() {
		return myMdmMatchResult;
	}

	public String getRightSideResourceIdentifier() {
		return myRightSideResourceIdentifier;
	}
}
