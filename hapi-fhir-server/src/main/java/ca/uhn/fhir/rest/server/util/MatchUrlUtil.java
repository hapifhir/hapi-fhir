package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;

public class MatchUrlUtil {

	/**
	 * Non-instantiable
	 */
	private MatchUrlUtil() {
		// nothing
	}

	/**
	 * Parses a FHIR-style Match URL (Patient?identifier=http://foo|bar) into
	 * a parsed set of parameters.
	 */
	public static List<NameValuePair> translateMatchUrl(String theMatchUrl) {
		List<NameValuePair> parameters;
		String matchUrl = theMatchUrl;
		int questionMarkIndex = matchUrl.indexOf('?');
		if (questionMarkIndex != -1) {
			matchUrl = matchUrl.substring(questionMarkIndex + 1);
		}

		final String[] searchList = new String[] {"|", "=>=", "=<=", "=>", "=<"};
		final String[] replacementList = new String[] {"%7C", "=%3E%3D", "=%3C%3D", "=%3E", "=%3C"};
		matchUrl = StringUtils.replaceEach(matchUrl, searchList, replacementList);
		if (matchUrl.contains(" ")) {
			throw new InvalidRequestException(Msg.code(1744) + "Failed to parse match URL[" + theMatchUrl
					+ "] - URL is invalid (must not contain spaces)");
		}

		parameters = URLEncodedUtils.parse((matchUrl), Constants.CHARSET_UTF8, '&');

		// One issue that has happened before is people putting a "+" sign into an email address in a match URL
		// and having that turn into a " ". Since spaces are never appropriate for email addresses, let's just
		// assume they really meant "+".
		for (int i = 0; i < parameters.size(); i++) {
			NameValuePair next = parameters.get(i);
			if (next.getName().equals("email") && next.getValue().contains(" ")) {
				BasicNameValuePair newPair =
						new BasicNameValuePair(next.getName(), next.getValue().replace(' ', '+'));
				parameters.set(i, newPair);
			}
		}

		return parameters;
	}
}
