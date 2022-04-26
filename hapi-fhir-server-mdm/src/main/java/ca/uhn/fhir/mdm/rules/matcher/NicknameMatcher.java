package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.searchparam.nickname.NicknameSvc;

import java.io.IOException;
import java.util.List;

public class NicknameMatcher implements IMdmStringMatcher {
	private final NicknameSvc myNicknameSvc;

	public NicknameMatcher() {
		try {
			myNicknameSvc = new NicknameSvc();
		} catch (IOException e) {
			throw new ConfigurationException("Unable to load nicknames", e);
		}
	}

	@Override
	public boolean matches(String theLeftString, String theRightString) {
		List<String> leftNames = myNicknameSvc.getEquivalentNames(theLeftString);
		return leftNames.contains(theRightString);
	}
}
