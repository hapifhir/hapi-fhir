package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.searchparam.nickname.NicknameSvc;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
		String leftString = theLeftString.toLowerCase(Locale.ROOT);
		String rightString = theRightString.toLowerCase(Locale.ROOT);

		List<String> leftNames = myNicknameSvc.getEquivalentNames(leftString);
		return leftNames.contains(rightString);
	}
}
